package com.serookie.product.web;

import com.serookie.product.service.SkuInfoService;
import com.serookie.product.vo.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.constraints.Size;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/29
 */
@Slf4j
@Controller
public class ItemController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String item(@PathVariable("skuId") Long skuId, Model model){
        System.out.println("skuId为:"+skuId);
        SkuInfoVo skuInfoVo=skuInfoService.skuItem(skuId);
        model.addAttribute("item",skuInfoVo);
        log.info("skuInfoVo:{}",skuInfoVo);
        return "item";
    }
}
