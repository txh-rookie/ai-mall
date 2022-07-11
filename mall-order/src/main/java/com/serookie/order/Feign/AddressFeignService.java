package com.serookie.order.Feign;

import com.kevintam.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("mall-member")
public interface AddressFeignService {
    @GetMapping("/member/memberreceiveaddress/info")
    public R infoAddress(@RequestParam("memberId") Long memberId);
    @RequestMapping("/member/memberreceiveaddress/info/{id}")
    public R info(@PathVariable("id") Long id);
}
