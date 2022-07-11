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
public class WxUser {
    private String nickname;
    private String openId;
    private Integer sex;
    private String headImgUrl;
}
