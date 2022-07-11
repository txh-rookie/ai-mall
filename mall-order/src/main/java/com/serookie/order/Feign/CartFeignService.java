package com.serookie.order.Feign;

import com.kevintam.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient("mall-cart-service")
public interface CartFeignService {
    @GetMapping("/getUserCart")
    public R getUserCart();
}
