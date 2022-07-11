package com.serookie.order.vo;

import com.serookie.order.intercptor.LoginUserInterceptor;
import com.serookie.order.to.MemberAddressTo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/10
 */

public class OrderConfirmVo {
    //收货地址
    @Getter @Setter
    private List<MemberAddressTo> address;
    //购物车选中到商品信息
    @Getter @Setter
    private List<OrderItemVo> orderItems;
    //积分信息
    @Getter @Setter
    private Integer integration;
    /**
     * 防重令牌
     */
    @Getter @Setter
    private String orderToken;
    //商品到总价格
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice() {
        if(orderItems!=null&&orderItems.size()>0){
            BigDecimal decimal = new BigDecimal("0");
            orderItems.forEach(item->{
                BigDecimal multiply = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));//加上购物车的价格
                decimal.add(multiply);
            });
            return totalPrice;
        }
        return null;
    }

    //应付到价格
    private BigDecimal playPrice;

    public BigDecimal getPlayPrice() {
        return getTotalPrice();
    }
}
