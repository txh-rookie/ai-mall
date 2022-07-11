package com.serookie.product.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/18
 */
@Configuration
public class OssConfig {

    /**
     * 往ioc中注入一个容器
     * @return
     */
    @Bean
    public OSS ossClientBuilder(){
        String endpoint="https://oss-cn-hangzhou.aliyuncs.com";
        String accessKeyId="LTAI5tDpjsbnHhqUqoXJAZpS";
        String secretKey="GOFw6HQL7R7ymJkOyjalS0Xijkgwtl";
        return new OSSClientBuilder().build(endpoint,accessKeyId,secretKey);
    }
}
