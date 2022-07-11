package com.serookie.auth.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kevintam.common.utils.HttpUtils;
import com.kevintam.common.utils.R;
import com.serookie.auth.Feign.MemberFeignService;
import com.serookie.auth.entity.ConstantWxUtils;
import com.serookie.auth.vo.SocialUser;
import com.serookie.auth.vo.WXParamVo;
import com.serookie.auth.vo.WxUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/5
 */
@Slf4j
@Controller
@RequestMapping("/api/ucenter/wx")
public class WXController {

    @Autowired
    private MemberFeignService memberFeignService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, HttpSession session) throws Exception {
        log.info("code:{}",code);
        HashMap<String,String> query=new HashMap<>();
        query.put("appid",ConstantWxUtils.WX_APPID);
        query.put("secret",ConstantWxUtils.WX_APP_SECRET);
        query.put("code",code);
        query.put("grant_type","authorization_code");
        HashMap<String,String> header=new HashMap<>();
        HttpResponse response = HttpUtils.doGet("https://api.weixin.qq.com", "/sns/oauth2/access_token", "GET", header, query);
        String entity = EntityUtils.toString(response.getEntity());
        WXParamVo wxParamVo = JSON.parseObject(entity, WXParamVo.class);
        log.info("请求微信的服务器的信息:{}",wxParamVo);
        //去资源服务器拿到用户信息
        HashMap<String,String> params=new HashMap<>();
        params.put("access_token",wxParamVo.getAccessToken());
        params.put("openid",wxParamVo.getOpenId());
        HttpResponse wxResult = HttpUtils.doGet("https://api.weixin.qq.com", "/sns/userinfo", "GET", header, params);
        String wxUser = EntityUtils.toString(wxResult.getEntity());
        WxUser wxUser1 = JSON.parseObject(wxUser, WxUser.class);
        log.info("用户信息:{}",wxUser1);
        R wxLogin = memberFeignService.wxLogin(wxUser1);
        Integer wxCode = (Integer) wxLogin.get("code");
        if(wxCode==0){
            session.setAttribute("user",wxUser1);
            return  "redirect:http://127.0.0.1:9001";
        }
        return "redirect:http://127.0.0.1:8160";
    }
    @GetMapping("/login")
    public String getWxCode(){
        String baseUrl="https://open.weixin.qq.com/connect/qrconnect"+
                "?appid=%s"+
                "&redirect_uri=%s"+
                "&response_type=code"+
                "&scope=snsapi_login"+
                "&state=%s"+
                "#wechat_redirect";
        String redirectUrl = ConstantWxUtils.WX_APP_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(baseUrl, ConstantWxUtils.WX_APPID, redirectUrl, "atguigu");
        //1、请求wx的地址，通过重定向
        return "redirect:"+url;
    }
}
