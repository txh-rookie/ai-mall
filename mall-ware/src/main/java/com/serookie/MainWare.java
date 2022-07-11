package com.serookie;

import org.apache.ibatis.annotations.Mapper;
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
@MapperScan("com.serookie.ware.dao")
@SpringBootApplication
public class MainWare {
    public static void main(String[] args) {
        SpringApplication.run(MainWare.class,args);
    }
}
