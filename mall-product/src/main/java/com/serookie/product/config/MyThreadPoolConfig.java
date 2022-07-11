package com.serookie.product.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/30
 * 自定义一个线程池，将线程池注入ioc容器里面
 */
//@EnableConfigurationProperties(ThreadPoolConfigurationProperties.class) 不写component也可以进行注入
@Configuration
public class MyThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigurationProperties properties){
        /**
         * 跟配置文件中的属性进行动态的绑定
         * corePoolSize:线程池的核心线程数
         * maxMumPoolSize：最大线程池的数量
         * keepAliveTime：存活时间
         * timeUnit:时间单元
         * BlockedQueue阻塞队列
         * 线程工厂，一般使用默认的即可
         * 拒绝策略
         */
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(properties.getCoreSize(),
                properties.getMaxMumSize(), properties.getKeepAliveTime(), TimeUnit.SECONDS,new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),new ThreadPoolExecutor.DiscardPolicy());
        return poolExecutor;//将线程注入到ioc容器中
    }
}
