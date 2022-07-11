//package com.serookie.auth.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
///**
// * @author kevintam
// * @version 1.0
// * @title
// * @description
// * @createDate 2022/7/2
// * springsecurity最重要的两个功能为：授权、认证
// */
//@Configuration
//public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    /**
//     *
//     * @param web
//     * @throws Exception
//     */
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/static/**","/login.html","/register","/");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.formLogin().loginPage("/login.html").loginProcessingUrl("/login");
//                //登录失败
//    }
//}
