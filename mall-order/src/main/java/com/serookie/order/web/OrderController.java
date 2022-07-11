package com.serookie.order.web;

import com.serookie.order.service.OmsOrderService;
import com.serookie.order.vo.OrderConfirmVo;
import com.serookie.order.vo.OrderSubmitResponse;
import com.serookie.order.vo.OrderSubmitVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/10
 */
@Slf4j
@Controller
public class OrderController {

    @Autowired
    private OmsOrderService orderService;

    /**
     * 订单准备的页面数据
     * @param model
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        //将数据回显出去
       OrderConfirmVo confirm=orderService.confirmOrder();
       model.addAttribute("orderConfirm",confirm);
        return "confirm";
    }

    /**
     * 下单接口
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo orderSubmit){
        /**
         * 1、接口的防重
         * 2、创建订单
         * 3、锁定库存
         * 4、验证一下价格
         * 5、跳转到支付页面
         */
        OrderSubmitResponse response=orderService.submitOrder(orderSubmit);
        log.info("下单成功:{}",response);
        if(response.getCode()==0){
            //表示成功
            return "redirect:http://order.serookie.com/play.html";
        }else{
            //表示订单生成失败
            return "redirect:http://order.serookie.com";
        }
    }
}
