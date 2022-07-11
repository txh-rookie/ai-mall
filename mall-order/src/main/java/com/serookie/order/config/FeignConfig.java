package com.serookie.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/10
 */
@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestTemplate(){
        //通过配置拦截器，来将丢失的请求头,进行补齐
        return new RequestInterceptor(){
            @Override
            public void apply(RequestTemplate requestTemplate) {
                ServletRequestAttributes requestAttributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = requestAttributes.getRequest();//拿到了老请求
                if(request!=null){
                    String cookie = request.getHeader("Cookie");
                    requestTemplate.header("Cookie",cookie);//将请求放到原请求上
                }
            }
        };
    }
}
