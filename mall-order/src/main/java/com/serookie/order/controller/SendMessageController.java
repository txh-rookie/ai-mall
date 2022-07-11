package com.serookie.order.controller;

import com.serookie.order.entity.OmsOrderEntity;
import com.serookie.order.entity.OmsOrderReturnReasonEntity;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/9
 */
@RestController
public class SendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String sendMessage(@RequestParam(value = "num",defaultValue = "10") Integer num){
        for (int i = 0; i <num ; i++) {
            OmsOrderReturnReasonEntity omsOrderReturnReasonEntity = new OmsOrderReturnReasonEntity();
            omsOrderReturnReasonEntity.setId(1L);
            omsOrderReturnReasonEntity.setName("haha"+i);
            omsOrderReturnReasonEntity.setCreateTime(new Date());
            omsOrderReturnReasonEntity.setStatus(1);
            rabbitTemplate.convertAndSend("java.exchange","java1",omsOrderReturnReasonEntity,new  CorrelationData(UUID.randomUUID().toString()));
        }
        return "ok";
    }
}
