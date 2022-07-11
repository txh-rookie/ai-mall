package com.serookie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Repository;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/16
 */
@EnableFeignClients(basePackages = {"com.serookie.member.fegin"})
@EnableDiscoveryClient
@MapperScan("com.serookie.member.dao")
@SpringBootApplication
public class MallMember {
    public static void main(String[] args) {
        SpringApplication.run(MallMember.class,args);
    }
}
