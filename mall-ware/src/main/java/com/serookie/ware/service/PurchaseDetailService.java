package com.serookie.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 10:56:31
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

