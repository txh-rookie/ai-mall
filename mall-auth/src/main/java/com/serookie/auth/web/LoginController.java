package com.serookie.auth.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kevintam.common.execption.CustomExceptionEnum;
import com.kevintam.common.utils.HttpUtils;
import com.kevintam.common.utils.R;
import com.serookie.auth.Feign.MemberFeignService;
import com.serookie.auth.message.SendCode;
import com.serookie.auth.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/30
 */
@Slf4j
@Controller
public class LoginController {

    @Autowired
    private SendCode sendCode;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MemberFeignService memberFeignService;

    @GetMapping({"/","login.html"})
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @GetMapping("/sms/sendCode")
    @ResponseBody
    public R sendSms(@RequestParam("phone") String phone){
        String codeVal = redisTemplate.opsForValue().get(SendCode.CODE_PHONE + phone);
        if(!StringUtils.isEmpty(codeVal)){
            long l = Long.parseLong(codeVal.split("_")[1]);
            if(System.currentTimeMillis()-l<60000){
                return R.error(CustomExceptionEnum.CODE_EXCEPTION.getCode(), CustomExceptionEnum.CODE_EXCEPTION.getMessage());
            }
        }
        //防止
        sendCode.sendCode(phone);//表示是否发送成功
        return R.ok();
    }

    @PostMapping("/reg")
    public String register(@Valid RegParamVo regParamVo, BindingResult result, Model model){
        if(result.hasErrors()){
            Map<String,String> errors=new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(fieldError -> {
                String field = fieldError.getField();
                String defaultMessage = fieldError.getDefaultMessage();
                errors.put(field,defaultMessage);
            });
            model.addAttribute("errors",errors);
            log.error("错误信息:{}",errors);
            return "register";//转发到注册页
        }
        //真正注册的服务
        String code = regParamVo.getCode();
        String phone = regParamVo.getPhone();
        String val = redisTemplate.opsForValue().get(SendCode.CODE_PHONE + phone);
        if(!StringUtils.isEmpty(val)){
            String codeVal = val.split("_")[0];
           if(code.equals(codeVal)){
               //相等，表示注册成功,令牌删除
               redisTemplate.delete(SendCode.CODE_PHONE + phone);
               //调用feign的服务接口
               UserParamVo userParamVo = new UserParamVo();
               BeanUtils.copyProperties(regParamVo,userParamVo);
               R res = memberFeignService.register(userParamVo);
               Integer resCode=(Integer)res.get("code");
               if(resCode==0){
                   //表示成功
                   return "login";
               }else{
                   model.addAttribute("error", res.get("msg"));
                   return "register";
               }
           }else{
               model.addAttribute("error","验证码出错");
               return "register";
           }
        }
        // TODO: 2022/7/1 审核账号密码是否已经存在，如果存在则注册失败
        return "login";
    }
    @PostMapping("/login")
    public String loginVo(LoginParamVo loginParamVo,HttpSession session){
        log.info("用户名和密码:{},{}",loginParamVo.getUsername(),loginParamVo.getPassword());
        R login = memberFeignService.login(loginParamVo);
        Integer code = (Integer) login.get("code");
        log.info("编码格式:{}",code);
        if(code==0){

            session.setAttribute("user",login.get("data"));
            return "redirect:http://serookie.com";
        }
        return "login";
    }
    @GetMapping("/oauth/github/success")
    public String success(@RequestParam("code") String code, HttpSession session) throws Exception {
        log.info("拿到的code的授权吗:{}",code);
        //换取token,登录成功就跳转到首页
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String,String> map=new HashMap<>();
        map.put("client_id","8d8b0e66d172698fb3b3");
        map.put("client_secret","68e55772ae02279db614557bbbcf4f26f88dbeb8");
        map.put("code",code);
        map.put("redirect_uri","http://127.0.0.1:9002/oauth/github/success");
        HttpResponse response = HttpUtils.doPost("https://github.com",
                "/login/oauth/access_token", "POST", headers, querys,map);
        if(response.getStatusLine().getStatusCode()==200){
          //成功,获取到access_token
            String result = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSON.parseObject(result);
            String access_token = jsonObject.getString("access_token");
            String accessToken="token "+access_token;
            log.info("access_token:{}",accessToken);
            headers.put("Authorization",accessToken);
            HttpResponse getResult = HttpUtils.doGet("https://api.github.com", "/user", "GET", headers, null);
            String social = EntityUtils.toString(getResult.getEntity());
            log.info("social:{}",social);
            SocialUser socialUser = JSON.parseObject(social, SocialUser.class);
            socialUser.setAccessToken(accessToken);
            //哪都socialUser的id表示用户登录登录成功
            log.info("SocialUser:{}",socialUser);
            R ok = memberFeignService.OauthLogin(socialUser);
            Integer code1 = (Integer) ok.get("code");
            if(code1==0){
                //登录成功
                Object resultData = ok.get("data");
                String data = JSON.toJSONString(resultData);
                MemberVo memberVo = JSON.parseObject(data, MemberVo.class);
                session.setAttribute("user",memberVo);
                return  "redirect:http://serookie.com";
            }else{
                //登录失败
                return "redirect:http://auth.serookie.com";
            }
        }else{
          //失败
            return "redirect:http://auth.serookie.com";
        }
    }
}
