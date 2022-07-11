package com.serookie.auth.Feign;

import com.kevintam.common.utils.R;
import com.serookie.auth.vo.*;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/1
 */
@FeignClient("mall-member")
public interface MemberFeignService {
    @PostMapping("/member/member/register")
    public R register(@RequestBody UserParamVo userParamVo);
    @PostMapping("/member/member/login")
    public R login(@RequestBody LoginParamVo loginParamVo);
    @PostMapping("/member/member/oauth/login")
    public R OauthLogin(@RequestBody SocialUser socialUser);
    @PostMapping("/member/member/oauth/wx/login")
    public R wxLogin(@RequestBody WxUser wxUser);
}
