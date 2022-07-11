package com.serookie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/16
 */
@EnableDiscoveryClient
@MapperScan("com.serookie.coupon.dao")
@SpringBootApplication
public class MainCoupon {
    public static void main(String[] args) {
        SpringApplication.run(MainCoupon.class,args);
    }
}
