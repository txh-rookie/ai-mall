package com.serookie;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/16
 */
//@EnableScheduling//开启定时任务
@EnableFeignClients
@EnableRedisHttpSession
@EnableRabbit //开启mq队列
@EnableDiscoveryClient
@MapperScan("com.serookie.order.dao")
@SpringBootApplication
public class MainOrder {
    public static void main(String[] args) {
        SpringApplication.run(MainOrder.class,args);
    }
}
