package com.serookie.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.order.entity.OmsOrderEntity;
import com.serookie.order.vo.OrderConfirmVo;
import com.serookie.order.vo.OrderSubmitResponse;
import com.serookie.order.vo.OrderSubmitVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 10:43:27
 */
public interface OmsOrderService extends IService<OmsOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    OrderSubmitResponse submitOrder(OrderSubmitVo orderSubmit);
}

