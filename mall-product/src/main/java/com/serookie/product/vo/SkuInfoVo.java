package com.serookie.product.vo;

import com.serookie.product.entity.SkuImagesEntity;
import com.serookie.product.entity.SkuInfoEntity;
import com.serookie.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/29
 * 检索的详情商品
 */
@Data
public class SkuInfoVo {
    //1、sku基本信息获取 pms_sku_info
    SkuInfoEntity skuInfoEntity;
    //sku的图片信息 pms_sku_images
    List<SkuImagesEntity> imagesEntities;
    //3、获取spu的销售属性组合
    List<SkuItemSaleAttrVo> skuItemVo;
    //4、获取spu的介绍
    SpuInfoDescEntity desp;
    //5、获取spu的规格参数信息
    private List<SpuGroupByAttrVo> spuGroupByAttrVos;
}
