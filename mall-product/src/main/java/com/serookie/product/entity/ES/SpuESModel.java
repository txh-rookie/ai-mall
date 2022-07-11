package com.serookie.product.entity.ES;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/25
 */
@Data
public class SpuESModel {
    /**
     * sku的主键id
     */
    private Long skuId;
    /**
     * spuId
     */
    private Long spuId;
    /**
     * 商品名称
     * 做全文检索
     */
    private String skuTitle;
    /**
     * 商品价格
     * 在数据库中，使用varchar存储，拿出来转为price
     */
    private BigDecimal price;
    /**
     * sku的默认图片
     */
    private String skuImg;
    /**
     * 销量
     */
    private Long sales; // 销量
    /**
     * 是否含有库存
     */
    private Boolean hasStore;
    /**
     * 热度评分
     */
    private Long hotScope;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 品牌图片弟子
     */
    private String brandImg;
    /**
     * 分类id
     */
    private Long catelogId;
    /**
     * 分类名
     */
    private String categoryName;

    private List<SearchAttrValue> attrs;
}
