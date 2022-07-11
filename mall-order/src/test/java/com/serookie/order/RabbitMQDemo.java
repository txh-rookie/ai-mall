package com.serookie.order;

import com.serookie.order.entity.OmsOrderEntity;
import com.serookie.order.entity.OmsOrderReturnReasonEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/9
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQDemo {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;


    /**
     * 创建交换机
     */
    @Test
    public void createExchange(){
        /**
         * 1、交换机的名称
         * 2、是否持久化
         * 3、是否自动删除
         */
//        TopicExchange 主题交换机 根据router-key来进行模糊匹配，多个单词，所以可以使用* #来进行匹配
//        FanoutExchange 扇出交换机 广播模式，给所有绑定来该交换机的队列发送信息。
        DirectExchange directExchange = new DirectExchange("java.exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
        log.info("创建交换机成功:{}",directExchange);
    }

    /**
     * 创建队列
     */
    @Test
    public void createQueue(){
        /**
         * 1、队列的名称
         * 2、是否持久化
         * 3、是否排它性,就是其他的队列没有办法进行连接
         * 4、是否自动删除
         */
        Queue queue = new Queue("java.queue",true,false,false);
        String name = amqpAdmin.declareQueue(queue);
        log.info("队列名称:{}",name);
    }

    /**
     * 将交换机与队列进行绑定
     *     public Binding(String destination, Binding.DestinationType destinationType, String exchange,
     *     String routingKey, @Nullable Map<String, Object> arguments
     *     1、队列名 目的地
     *     2、绑定的类型，是交换机还是队列 目的到的类型  QUEUE,EXCHANGE;
     *     3、交换机的名称
     *     4、路由键
     *     5、其他参数
     */
    @Test
    public void binding(){
        Binding binding = new Binding("java.queue", Binding.DestinationType.QUEUE,"java.exchange","java",null);
        amqpAdmin.declareBinding(binding);
        log.info("绑定交换机与队列成功");
    }
    /**
     * 怎么发送信息
     */
    @Test
    public void sendMessage() throws UnsupportedEncodingException {
        OmsOrderReturnReasonEntity omsOrderReturnReasonEntity = new OmsOrderReturnReasonEntity();
        omsOrderReturnReasonEntity.setId(1L);
        omsOrderReturnReasonEntity.setName("haha");
        omsOrderReturnReasonEntity.setCreateTime(new Date());
        omsOrderReturnReasonEntity.setStatus(1);
        String msg="hello world";
        rabbitTemplate.convertAndSend("java.exchange","java",omsOrderReturnReasonEntity);
        log.info("发送的消息为:{}",msg);
    }
//    @Test
//    public void rabbitListener(){
//
//    }
}
