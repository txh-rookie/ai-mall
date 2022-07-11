package com.serookie.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/30
 */
@EnableRedisHttpSession
@EnableFeignClients //开启feign的使用
@EnableDiscoveryClient //开启服务的注册
@SpringBootApplication
public class MainAuth9002 {
    public static void main(String[] args) {
        SpringApplication.run(MainAuth9002.class,args);
    }
}
