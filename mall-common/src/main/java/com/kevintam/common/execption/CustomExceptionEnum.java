package com.kevintam.common.execption;

import lombok.Data;

/**
 * 自定义异常处理的枚举类
 */
public enum CustomExceptionEnum {
     UNKNOWN_EXCEPTION(10000,"系统未知错误"),
     CHECK_EXCEPTION(10001,"数据校验错误"),
     CODE_EXCEPTION(10002,"验证码获取频率太高,请稍后在式"),
     PRODUCT_UP_EXCEPTION(10003,"商品上架异常"),
     CHECK_USERNAME_EXCEPTION(10004,"用户名已存在"),
    LOGIN_ERROR_EXCEPTION(10006,"账号密码错误"),
    CHECK_PHONE_EXCEPTION(10005,"手机号已存在");

     private Integer code;
     private String message;
     private CustomExceptionEnum(Integer code,String message){
         this.code=code;
         this.message=message;
     }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
