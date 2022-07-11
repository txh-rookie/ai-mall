package com.serookie.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/1
 * 注册参数的封装
 */
@Data
public class RegParamVo {
    @NotNull(message = "用户名必须提交")
    @Length(min=6,max = 18) //密码的长度必须为6到18位才可以
    private String username;
    @NotNull(message = "用户名必须提交")
    @Length(min = 6,max = 18)
    private String password;
    @NotNull(message = "数据不能为null")
    private String phone;
    @NotNull(message = "验证码不能为空")
    private String code;
}
