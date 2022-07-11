package com.serookie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Stack;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/17
 */
@Configuration
public class CorsConfig {

    /**
     * 跨域的解决
     * @return
     */
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource corsConfiguration = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedMethod("*");//允许跨域的端口
        cors.addAllowedOrigin("*");//允许哪些来源的请求跨域
        cors.addAllowedHeader("*");//允许跨域的一些字段
        cors.setAllowCredentials(true);//对cookie进行放域
        corsConfiguration.registerCorsConfiguration("/**",cors);
        return new CorsWebFilter(corsConfiguration);
    }
}
