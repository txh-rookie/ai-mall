package com.serookie.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.product.entity.SkuSaleAttrValueEntity;
import com.serookie.product.vo.SkuItemSaleAttrVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-25 14:21:52
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuItemSaleAttrVo> getBaseSaleAttrValue(Long spuId);

    List<String> listSkuIdAttr(Long skuId);
}

