package com.serookie.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/30
 */
@Component
@ConfigurationProperties(prefix = "mall.thread")
@Data
public class ThreadPoolConfigurationProperties {
    private Integer coreSize;//核心线程数
    private Integer maxMumSize;//最大核心线程数
    private Integer keepAliveTime;//线程的最大存活时间
}
