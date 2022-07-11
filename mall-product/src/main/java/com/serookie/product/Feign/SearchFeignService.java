package com.serookie.product.Feign;

import com.kevintam.common.utils.R;
import com.serookie.product.entity.ES.SpuESModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("elasticsearch-service")
public interface SearchFeignService {

    @PostMapping("/search/save/product/up")
    public R productStatusUp(@RequestBody List<SpuESModel> spuESModels);
}
