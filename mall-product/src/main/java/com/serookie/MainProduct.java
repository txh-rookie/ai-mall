package com.serookie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/16
 */
@EnableRedisHttpSession
@MapperScan("com.serookie.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MainProduct {
    public static void main(String[] args) {
        SpringApplication.run(MainProduct.class,args);
    }
}
