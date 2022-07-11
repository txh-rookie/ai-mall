package com.serookie.search.vo;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/28
 * 检索的参数
 * categoryId=255&keyword=小米&sort=saleCount_desc/asc(销量排序,升序还是降序)&
 */
public class SearchParam {

    private String keyword;//全文检索,去es进行全文匹配
    private Long catelogId;//三级分类的id

    /**
     * 1、salCount_desc/asc 根据销量进行排序
     * 2、skuPrice_desc/asc 根据价格进行排序
     * 3、hostScore_desc/asc 根据热度进行排序
     */
    private String sort;//排序的字段

    /**
     * 过滤条件
     * 过滤:hasStock（是否有货）、skuPrice区间、brandId、catalog3Id、attrs
     * 是否有货就是1/0、
     *
     */
     private Integer HasStock;
    /**
     * 价格区间
     * 价格区间[0~500]、[500~]、[~、500]的
     * skuPrice=0_500、500_、_500
     */
    private String skuPrice;
    /**
     * 品牌id选中多个
     */
    private List<Long> BrandId;//品牌id,有可能会选择多个

    /**
     * 按照属性进行筛选
     * 1、内存
     * 2、屏幕
     * 3、尺寸
     * 参数的话:attr=1_8g:16g&attr=2_6.5寸:7寸
     */
    private List<String> attrs;

    /**
     * 分页属性
     */
    private Long pageNum;//页码
}
