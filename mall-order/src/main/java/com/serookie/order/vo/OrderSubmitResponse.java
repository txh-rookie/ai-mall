package com.serookie.order.vo;

import com.serookie.order.entity.OmsOrderEntity;
import lombok.Data;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/11
 */
@Data
public class OrderSubmitResponse {
    private OmsOrderEntity orderEntity;
    private Integer code;//0表示成功
}
