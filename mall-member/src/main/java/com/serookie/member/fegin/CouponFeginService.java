package com.serookie.member.fegin;


import com.kevintam.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//调用mall-coupon服务的接口
//填入你要调用服务名
@FeignClient("mall-coupon")
public interface CouponFeginService {
    @GetMapping("/coupon/coupon/member/list")
    public R couponList();
}
