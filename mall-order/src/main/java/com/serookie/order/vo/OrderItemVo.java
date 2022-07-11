package com.serookie.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/10
 */
@Data
public class OrderItemVo {
    private Long skuId;
    private String title;
    private String image;
    private BigDecimal price;
    private List<String> skuAttr;
    private Integer count;
    private BigDecimal totalPrice;
}
