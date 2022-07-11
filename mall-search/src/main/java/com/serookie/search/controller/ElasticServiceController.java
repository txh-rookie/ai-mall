package com.serookie.search.controller;

import com.kevintam.common.execption.CustomExceptionEnum;
import com.kevintam.common.utils.R;
import com.serookie.search.entity.SearchAttrValue;
import com.serookie.search.entity.SpuESModel;
import com.serookie.search.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/25
 */
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticServiceController {

    @Autowired
    private ProductService productService;

    /**
     * 保存上架的商品信息在es服务中
     */
    @PostMapping("/product/up")
    public R productStatusUp(@RequestBody List<SpuESModel> spuESModels) throws IOException {
        Boolean flag=null;
        try{
            flag = productService.productStatusUp(spuESModels);
        }catch(Exception exception){
          log.error("商品上架错误:",exception);
          return R.error(CustomExceptionEnum.CHECK_EXCEPTION.getCode(), CustomExceptionEnum.CHECK_EXCEPTION.getMessage());
        }
        return R.ok().put("data",flag);
    }

}
