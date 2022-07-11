package com.serookie.order.intercptor;

import com.alibaba.fastjson.JSON;
import com.serookie.order.vo.MemberVo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/10
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberVo> threadLocal=new ThreadLocal<>();
    /**
     * 前置通知,去判断一下是否需要登录
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("user");
        String user = JSON.toJSONString(obj);
        MemberVo memberVo = JSON.parseObject(user, MemberVo.class);
        if(StringUtils.isEmpty(memberVo)){
            //去重定向到登录页面
            response.sendRedirect("http://auth.serookie.com");
            return false;//用户没有进行登录
        }
        threadLocal.set(memberVo);//放入到ThreadLocal中，来共享变量
        return true;//用户进行过登录
    }
}
