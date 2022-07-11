package com.serookie.auth.message;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import com.serookie.auth.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;
import java.util.concurrent.TimeUnit;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/1
 */
@Slf4j
@Component
public class SendCode {

    public static final String CODE_PHONE="sms:code:";

    @Autowired
    private Client client; //拿到客户端

    @Autowired
    private StringRedisTemplate redisTemplate; //拿到redis
    //发送邮件
    public void sendCode(String phone){
        String numberCode = CodeUtils.getNumberCode().toString();//6位的随机数
        System.out.println(numberCode);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers(phone)
                .setTemplateParam("{\"code\":"+numberCode.toString()+"}");
        String code=CODE_PHONE+phone;
//        加上一个时间戳来防刷
        RuntimeOptions runtime = new RuntimeOptions();//接受短信服务
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            log.info("发送成功的的编码是:{}",sendSmsResponse.getStatusCode());
            if(sendSmsResponse.statusCode==200){
                long timeMillis = System.currentTimeMillis();
                String time = numberCode+"_" + timeMillis;
                redisTemplate.opsForValue().set(code,time.toString(),5, TimeUnit.MINUTES);
            }
        } catch (TeaException error) {
            // 如有需要，请打印 error
            String errorLog = Common.assertAsString(error.message);
            log.error("发送消息错误:{}",errorLog);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            String s = Common.assertAsString(error.message);
            log.info("发送消息错误:{}",s);
        }
    }
}
