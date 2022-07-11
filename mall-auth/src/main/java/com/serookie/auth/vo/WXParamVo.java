package com.serookie.auth.vo;

import lombok.Data;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/5
 */
@Data
public class WXParamVo {
    private String accessToken;
    private String openId;
    private Long expiresIn;
    private String refreshToken;
    private String unionId;
}
