package com.serookie.cart.interceptor;

import com.alibaba.fastjson.JSON;
import com.kevintam.common.constant.CartConstant;
import com.serookie.cart.to.UserInfoTo;
import com.serookie.cart.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/6
 */
@Slf4j
@Component
public class CartInterceptor implements HandlerInterceptor {


    public static ThreadLocal<UserInfoTo> threadLocal=new ThreadLocal<UserInfoTo>();//共享数据

    /**
     * 执行之前会执行.....
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("user");
        String user = JSON.toJSONString(obj);
        MemberVo memberVo = JSON.parseObject(user, MemberVo.class);
        if(!StringUtils.isEmpty(memberVo)){
           //为空，没有进行登录过
           userInfoTo.setUserId(memberVo.getId());
        }
            //表示没有登录过
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0){
            for (Cookie cookie:cookies) {
                String name = cookie.getName();
                if(name.equals(CartConstant.TIME_USER_COOKIE_NAME)){
                    //表示已经有临时身份
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }
        //新建一个临时用户
        if(StringUtils.isEmpty(userInfoTo.getUserKey())){
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }
        threadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 执行完成之后，会执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();
        if(!userInfoTo.getTempUser()){
            Cookie cookie = new Cookie(CartConstant.TIME_USER_COOKIE_NAME,userInfoTo.getUserKey());
            cookie.setDomain("serookie.com");
            cookie.setMaxAge(CartConstant.TIME_USER_COOKIE_TIMEOUT);//1个月到过期时间
            response.addCookie(cookie);//填入一个cookie
        }
        threadLocal.remove();//用完清空
    }
}
