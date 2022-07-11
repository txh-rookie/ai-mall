package com.serookie.order.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

import javax.annotation.PostConstruct;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/9
 */
@Slf4j
@Configuration
public class RabbitmqConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct
    public void initRabbitTemplate(){
        //发布确认的模式
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                if(!b){
                    log.info("出现问题的消息:{},是否消息应答:{}",correlationData,b);
                }
            }
        });
    }
//    @PostConstruct
//    public void initRabbitRouter(){
//        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
//            /**
//             * 只要消息没有发送到指定到队列，就会触发该函数
//             * 1、message，投递失败到消息
//             * 2、回复状态码
//             * 3、回复到文本内容
//             * 4、当时发送给到那个交换机
//             * 5、router-key：当时所发送到路由键
//             * @param message
//             * @param i
//             * @param s
//             * @param s1
//             * @param s2
//             */
//            @Override
//            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
//                 log.info("消息发送异常:{},状态吗:{},文本内容为:{},交换机为:{},路由键到:{}",message,i,s,s1,s2);
//            }
//        });
//    }
}
