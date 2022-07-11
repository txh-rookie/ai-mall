package com.serookie.cart.feign;

import com.kevintam.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("mall-product")
public interface SkuInfoFeignService {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
    @GetMapping("/product/skusaleattrvalue/list/{skuId}")
    public List<String> listSkuIdAttr(@PathVariable("skuId") Long skuId);

    @GetMapping("/product/skuinfo/getNewPrice")
    public R getNewPrice(@RequestParam("skuId") Long skuId);
}
