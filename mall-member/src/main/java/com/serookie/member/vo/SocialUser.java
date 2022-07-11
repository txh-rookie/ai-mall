package com.serookie.member.vo;

import lombok.Data;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/4
 */
@Data
public class SocialUser {
    private Long id;
    private String avatarUrl;
    private String name;
    private String bio;
    private String accessToken;
}
