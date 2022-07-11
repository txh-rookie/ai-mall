package com.serookie.cart.web;

import com.kevintam.common.utils.R;
import com.serookie.cart.interceptor.CartInterceptor;
import com.serookie.cart.service.CartService;
import com.serookie.cart.to.UserInfoTo;
import com.serookie.cart.vo.Cart;
import com.serookie.cart.vo.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/7/6
 */
@Slf4j
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 1、浏览器有一个cooker user-key 表示用户身份，一个月后过期
     * 如果是第一次登录，会有一个临时身份，浏览器进行保存，每次访问带着这个cookie
     *
     * 2、需要检查是否进行登录
     * 如果有登录，则session有数据
     * 如果没有进行登录，则将cookie -user key来做，
     * 如果第一次没有临时身份，则新建一个临时身份
     * @return
     */
    @GetMapping({"/","cart.html"})
    public String cart(Model model){
        Cart cart= cartService.getCarts();
        model.addAttribute("cart",cart);
        return "cartList";
    }
    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,@RequestParam("checked") Integer checked){
         cartService.checkItem(skuId,checked);
         return "redirect:http://cart.serookie.com/cartList";
    }

   @GetMapping("/updateNum")
   public String updateNum(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num){
        cartService.updateNum(skuId,num);
        return "redirect:http://cart.serookie.com/cartList";
   }
   @GetMapping("/delete")
   public String deleteCarts(@RequestParam("skuId") Long skuId){
        cartService.delCart(skuId);
        return "redirect:http://cart.serookie.com/cartList";
   }
    //防止用户重负提交
    @GetMapping("/addCart")
    public String addCart(@RequestParam("skuId")Long skuId, @RequestParam("num") Integer num, RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
         log.info("skuId:{},num:{}",skuId,num);
         cartService.addCart(skuId,num);
         redirectAttributes.addAttribute("skuId",skuId);
         return "redirect:http://cart.serookie.com/cartList";
    }
    //这个才是真正查询的接口
    @GetMapping("/addCartSuccess.html")
    public String addCartSuccessPage(@RequestParam("skuId") Long skuId, Model model){
        CartItem item= cartService.getCartItem(skuId);
        model.addAttribute("item",item);
        return "success";
    }
    /**
     * 根据用户id去查询购物车中的商品
     */
    @GetMapping("/getUserCart")
    @ResponseBody
    public R getUserCart(){
         List<CartItem> cartItemList=cartService.getMemberIdCart();
         return R.ok().put("data",cartItemList);
    }
}
