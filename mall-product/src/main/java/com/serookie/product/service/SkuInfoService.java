package com.serookie.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.product.entity.SkuInfoEntity;
import com.serookie.product.vo.SkuInfoVo;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuInfoEntity> findBySpuId(Long spuId);

    SkuInfoVo skuItem(Long skuId);
}

