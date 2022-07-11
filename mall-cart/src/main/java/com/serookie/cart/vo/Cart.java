package com.serookie.cart.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/6
 * 购物项
 */
public class Cart {
    private List<CartItem> items;
    private Integer countNum;//商品数量
    private Integer countType;//商品类型数量
    private BigDecimal totalAmount;//商品总价
    private BigDecimal reduce=new BigDecimal("0.00");//减免价格

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNum() {
        AtomicInteger count= new AtomicInteger();
        //商品数量
        if(items!=null&&items.size()>0){//有商品的
             items.forEach(item->{
                 count.addAndGet(item.getCount());
             });
        }
        return count.get();
    }

    public Integer getCountType() {
        int size=0;
        if(items!=null&&items.size()>0){
             size = items.size();
        }
        return size;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0.00");
        //遍历所有的商品,得到总的价位
        if(items!=null&&items.size()>0){
            items.forEach(item->{
                if(item.isCheck()){
                    BigDecimal totalPrice = item.getTotalPrice();
                    amount.add(totalPrice);
                }
            });
        }
        //减去优惠

        return  amount.subtract(reduce);
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
