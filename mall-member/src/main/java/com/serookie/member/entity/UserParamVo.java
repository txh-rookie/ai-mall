package com.serookie.member.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
public class UserParamVo {
    private String username;
    private String password;
    private String phone;
}
