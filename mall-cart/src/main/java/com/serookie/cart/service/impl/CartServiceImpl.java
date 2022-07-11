package com.serookie.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kevintam.common.utils.R;
import com.serookie.cart.config.MyThreadPoolConfig;
import com.serookie.cart.feign.SkuInfoFeignService;
import com.serookie.cart.interceptor.CartInterceptor;
import com.serookie.cart.service.CartService;
import com.serookie.cart.to.SkuInfoEntity;
import com.serookie.cart.to.UserInfoTo;
import com.serookie.cart.vo.Cart;
import com.serookie.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/6
 */
@Service("CartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SkuInfoFeignService skuInfoFeignService;
    @Autowired
    private ThreadPoolExecutor executor;

    public static final String CART_PREFIX = "cart:";//拼接上用户的参数，登录后的cart:1 没有登录的话cart:uuid

    @Override
    public CartItem addCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        //操作这个Hash对象来操作数据
        BoundHashOperations<String, Object, Object> cart = getCart();
        //判断商品中是否有数据
        String item =(String) cart.get(skuId.toString());
        if(StringUtils.isEmpty(item)){
            //通过远程连接去查询
            CartItem cartItem = new CartItem();
            CompletableFuture<Void> completableFuture1 = CompletableFuture.runAsync(() -> {
                R info = skuInfoFeignService.info(skuId);
                Integer code = (Integer) info.get("code");
                //表示查询成功
                String skuInfo = JSON.toJSONString(info.get("skuInfo"));
                SkuInfoEntity skuInfoEntity = JSON.parseObject(skuInfo, SkuInfoEntity.class);
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setPrice(skuInfoEntity.getPrice());
                cartItem.setSkuId(skuId);
                cartItem.setTitle(skuInfoEntity.getSkuTitle());
                cartItem.setImage(skuInfoEntity.getSkuDefaultImg());
            }, executor);
            //远程查询sku组合的信息
//        cartItem.setSkuAttr();
            CompletableFuture<Void> completableFuture2 = CompletableFuture.runAsync(() -> {
                List<String> strings = skuInfoFeignService.listSkuIdAttr(skuId);
                cartItem.setSkuAttr(strings);
            }, executor);
            CompletableFuture<Void> allOf = CompletableFuture.allOf(completableFuture1, completableFuture2);
            allOf.get();
            String jsonCart = JSON.toJSONString(cartItem);
            cart.put(skuId.toString(),jsonCart);
            return cartItem;
        }else{
            //表示redis中有数据，拿到商品数进行叠加
            CartItem cartItem = JSON.parseObject(item, CartItem.class);
            cartItem.setCount(cartItem.getCount()+num);
            cart.put(skuId.toString(),JSON.toJSONString(cartItem));
            return cartItem;
        }
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        //先添加到购物车后，进行查询到
        BoundHashOperations<String, Object, Object> cart = getCart();
        String carts = (String) cart.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(carts, CartItem.class);
        return cartItem;
    }

    @Override
    public Cart getCarts() {
        //需要进行判断是否登录
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String userKey = userInfoTo.getUserKey();
        String tempCartKey=CART_PREFIX+userKey;
        Cart cart = new Cart();
        if(userInfoTo.getUserId()!=null){
            //表示登录成功,合并购物车
            Long userId = userInfoTo.getUserId();
            String userCartKey=CART_PREFIX+userId.toString();//拿到购物车到key
            //拿到所有的临时用户的用户信息
            List<CartItem> tempCartItems = getCartItems(tempCartKey);
            if(tempCartItems!=null && tempCartItems.size()>0){
                //表示临时购物车是有数据的,需要合并
                tempCartItems.forEach(item->{
                    try {
                        //将未登录的购物车里面的数据，添加到购物车里面
                        addCart(item.getSkuId(),item.getCount());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                //合并完购物车之后，将临时购物车进行清除
                deleteCart(tempCartKey);
                List<CartItem> cartItems = getCartItems(userCartKey);
                cart.setItems(cartItems);
            }
        }else{
            //表示没有进行登录
            //以这个cartKey去查询redis，拿到所有到临时数据
            List<CartItem> cartItems = getCartItems(tempCartKey);
            cart.setItems(cartItems);
        }
        return cart;
    }

    @Override
    public void checkItem(Long skuId, Integer checked) {
        BoundHashOperations<String, Object, Object> cart = getCart();
        String cartItem = (String)cart.get(skuId.toString());
        if(cartItem!=null){
            CartItem cartItem1 = JSON.parseObject(cartItem, CartItem.class);
            cartItem1.setCheck(checked==1?true:false);
            //进行更新操作
            cart.put(skuId,cartItem1);
        }
    }

    @Override
    public void updateNum(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> cart = getCart();
        String cartItem =(String) cart.get(skuId.toString());
        CartItem cartItem1 = JSON.parseObject(cartItem, CartItem.class);
        cartItem1.setCount(num);
        //修改
        cart.put(skuId,cartItem1);
    }

    @Override
    public void delCart(Long skuId) {
        BoundHashOperations<String, Object, Object> cart = getCart();
        cart.delete(skuId.toString());
    }

    /**
     * 根据用户id查询购物车
//     * @param memberId
     * @return
     */
    @Override
    public List<CartItem> getMemberIdCart() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if(userInfoTo.getUserId()!=null){
            String cartKey=CART_PREFIX+userInfoTo.getUserId();
            List<CartItem> cartItems = getCartItems(cartKey);
            //去查询一下商品的价格，进行一下更新
            if(!StringUtils.isEmpty(cartItems)){
                cartItems.forEach(item->{
                    R newPrice = skuInfoFeignService.getNewPrice(item.getSkuId());
                    String data= (String)newPrice.get("data");
                    item.setPrice(new BigDecimal(data));
                });
                return cartItems;
            }
            return null;
        }else{
          //没有登录
            return null;
        }
    }

    private List<CartItem> getCartItems(String carKey){
        BoundHashOperations<String, Object, Object> boundHashOps= redisTemplate.boundHashOps(carKey);
        //遍历
        List<Object> cartList = boundHashOps.values();
        if(cartList!=null&&cartList.size()>0) {
            //表示集合中有值
            List<CartItem> collect = cartList.stream().map((item) -> {
                String itemStr = (String) item;
                CartItem cartItem = JSON.parseObject(itemStr, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }
    private BoundHashOperations<String, Object, Object> getCart() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        //拿到共享数据,判断是临时身份，还是登录的身份
        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            //则表示登录成功,有用户的id
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            //否则，表示只有临时用户
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(cartKey);
        return hashOperations;
    }

    @Override
    public void deleteCart(String cartKey){
        redisTemplate.delete(cartKey);
    }
}
