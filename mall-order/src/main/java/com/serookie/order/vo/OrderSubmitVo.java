package com.serookie.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/11
 */
@Data
public class OrderSubmitVo {
    private Long addressId;//地址id
    private String playType;//支付方式
    private String orderToken;//防重令牌
    private BigDecimal playPrice;//应付金额
    private String note;//备注
}
