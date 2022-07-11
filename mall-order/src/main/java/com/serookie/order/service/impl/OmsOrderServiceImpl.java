package com.serookie.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.kevintam.common.utils.Constant;
import com.kevintam.common.utils.R;
import com.rabbitmq.client.Channel;
import com.serookie.order.Feign.AddressFeignService;
import com.serookie.order.Feign.CartFeignService;
import com.serookie.order.config.MyThreadPoolConfig;
import com.serookie.order.constant.OrderConstant;
import com.serookie.order.entity.OmsOrderReturnReasonEntity;
import com.serookie.order.intercptor.LoginUserInterceptor;
import com.serookie.order.to.MemberAddressTo;
import com.serookie.order.to.OrderCreateTo;
import com.serookie.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.order.dao.OmsOrderDao;
import com.serookie.order.entity.OmsOrderEntity;
import com.serookie.order.service.OmsOrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.constraints.Size;

@RabbitListener(queues = {"java.queue"})
@Slf4j
@Service("omsOrderService")
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderDao, OmsOrderEntity> implements OmsOrderService {

    @Autowired
    private AddressFeignService addressFeignService;

    @Autowired
    private CartFeignService cartFeignService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OmsOrderEntity> page = this.page(
                new Query<OmsOrderEntity>().getPage(params),
                new QueryWrapper<OmsOrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        //进行封装
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberVo memberVo = LoginUserInterceptor.threadLocal.get();
        //通过request的上下文拿到共享数据
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //远程查询用户服务的用户地址
        /**
         * feign在远程调用之前要构造请求，调用很多的拦截器
         * 优化：CompletableFuture异步编排来实现异步的方式进行调用
         */
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            //防止在异步的场景下,feign的远程调用会丢失上下文。在数据进行共享
            RequestContextHolder.setRequestAttributes(requestAttributes);
            R result = addressFeignService.infoAddress(memberVo.getId());
            String data = JSON.toJSONString(result.get("data"));
            List<MemberAddressTo> memberAddressTos = JSON.parseArray(data, MemberAddressTo.class);
            orderConfirmVo.setAddress(memberAddressTos);
        }, executor);
        //远程调用购物车选中到商品信息,调用远程接口时,openFeign丢失了消息头,所以在cart中
        //无法拿到session
        CompletableFuture<Void> cartFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            R userCart = cartFeignService.getUserCart();
            List<OrderItemVo> data =(List<OrderItemVo>) userCart.get("data");
//            String cartResult = (String) userCart.get("data");
//            List<OrderItemVo> orderItemVos = JSON.parseArray(cartResult, OrderItemVo.class);
            orderConfirmVo.setOrderItems(data);
        }, executor);
        //拿到用户积分
        MemberVo memberVo1 = LoginUserInterceptor.threadLocal.get();
        Integer integration = memberVo1.getIntegration();
        orderConfirmVo.setIntegration(integration);
        // TODO: 2022/7/11 防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        //将token放到redis中
        redisTemplate.opsForValue().set(OrderConstant.ORDER_TOKEN_PREFIX+memberVo.getId(),token,30, TimeUnit.MINUTES);
        CompletableFuture.allOf(addressFuture,cartFuture).get();
        return orderConfirmVo;
    }

    @Override
    public OrderSubmitResponse submitOrder(OrderSubmitVo orderSubmit) {
        MemberVo memberVo = LoginUserInterceptor.threadLocal.get();
        OrderSubmitResponse response = new OrderSubmitResponse();
        //1、验证一下令牌
        String orderToken = orderSubmit.getOrderToken();
        //验证令排的操作，必须是原子性的。获取redis的值，比较，删除必须是原子的，不然在多线程环境中，会出现线程安全问题。
        /**
         * 1、lua脚本中的参数，第一个参数KEYS[1] 也就是需要获取的参数
         * 2、ARGV[1] 也就是传入的参数
         * 3、KEYS[1]==ARGV[1] 比较的是这二个参数是否相等
         * 4、相等，会去删除这个keys
         * 5、不想等，会去返回0
         */
        String luaScript="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        /**
         * 三个参数：
         * 1、DefaultRedisScript执行脚本的类，传入lua脚本，返回的参数的类型。
         * 2、脚本中的Keys，要去redis中去获得的值。
         * 3、前端传来的防重令牌。
         */
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(luaScript, Long.class),
                Arrays.asList(OrderConstant.ORDER_TOKEN_PREFIX + memberVo.getId()),
                orderToken);
        if(result==0L){
            //表示防重令牌校验失败
        }else{
            //表示防重令排校验成功
            Long addressId = orderSubmit.getAddressId();
            createOrder(addressId);
        }
//        String redisToken = redisTemplate.opsForValue().get(OrderConstant.ORDER_TOKEN_PREFIX + memberVo.getId());
//        //比较一下token是否相等，如果相等就表示可以进行下单，如果不想等，就表示不能下单
//        if(orderToken.equals(redisToken)){
//            //相等，可以下单
//
//        }else{
//            //不能下单
//        }
        return response;
    }

    /**
     * 创建订单的方法
     * @return
     */
    private OrderCreateTo createOrder(Long addressId){
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        //生成一个订单号
        String timeId = IdWorker.getTimeId();
         OmsOrderEntity order = new OmsOrderEntity();
        //设置订单号
        order.setOrderSn(timeId);
        //获取地址信息
        R info = addressFeignService.info(addressId);
        String memberReceiveAddress = JSON.toJSONString(info.get("memberReceiveAddress"));
        MemberAddressTo memberAddressTo = JSON.parseObject(memberReceiveAddress, MemberAddressTo.class);
        //收货的详细地址
        order.setReceiverDetailAddress(memberAddressTo.getDetailAddress());
        //收货城市
        order.setReceiverCity(memberAddressTo.getCity());
        //设置收货人信息
        order.setReceiverName(memberAddressTo.getName());
        order.setReceiverPhone(memberAddressTo.getPhone());
        order.setReceiverPostCode(memberAddressTo.getPostCode());
        order.setReceiverProvince(memberAddressTo.getProvince());
        order.setReceiverRegion(memberAddressTo.getRegion());
        //获取到所有到订单项
        R userCart = cartFeignService.getUserCart();
        return orderCreateTo;
    }

    /**
     * 类型为Message
     *
     *
     */
    @RabbitHandler
    public void rabbitListener(Message message,OmsOrderReturnReasonEntity order, Channel channel) throws IOException {
        //手动应答: 应答的标签 信道的消息按顺序自增的 是否进行批量删除
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        log.info("监听到到信息:{}",order);
    }
}