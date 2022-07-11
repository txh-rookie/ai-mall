package com.serookie.cart.service;

import com.serookie.cart.vo.Cart;
import com.serookie.cart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CartService {
    /**
     * 添加商品信息到购物车中
     * @param skuId
     * @param num
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    CartItem addCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 根据skuId获取购物车属性
     */
    CartItem getCartItem(Long skuId);

    /**
     * 获取所有的购物车属性
     */
    Cart getCarts();

    /**
     * 清空临时购物车
     * @param cartKey
     */
    void deleteCart(String cartKey);

    /**
     * 修改购物车是否被选中
     * @param skuId
     * @param checked
     */
    void checkItem(Long skuId, Integer checked);
    /**
     * 修改购物车商品到数量
     */
    void updateNum(Long skuId, Integer num);

    void delCart(Long skuId);

    List<CartItem> getMemberIdCart();
}
