package com.serookie.cart.to;

import lombok.Data;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/6
 */
@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    //做一个标志位 false表示没有user-key true表示有临时用户到
    private Boolean tempUser=false;
}
