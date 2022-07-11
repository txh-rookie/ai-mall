package com.serookie.order.to;

import com.serookie.order.entity.OmsOrderEntity;
import com.serookie.order.entity.OmsOrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/11
 */
@Data
public class OrderCreateTo {
    private OmsOrderEntity order;//订单
    private List<OmsOrderItemEntity> orderItems;//订单项
    private BigDecimal payPrice;//订单计算的应付价格
    private BigDecimal freePrice;//运费
}
