package com.serookie.product.dao;

import com.serookie.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.serookie.product.vo.SkuItemSaleAttrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-25 14:21:52
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVo> getSelectAttrSaleBySkuId(Long spuId);

    List<String> selectSkuIdAttr(@Param("skuId") Long skuId);
}
