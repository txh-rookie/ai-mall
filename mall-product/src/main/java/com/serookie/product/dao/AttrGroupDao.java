package com.serookie.product.dao;

import com.serookie.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.serookie.product.vo.SkuInfoVo;
import com.serookie.product.vo.SpuGroupByAttrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {
    List<SpuGroupByAttrVo> getSpuGroupWithAttrs(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);
//	 public List<AttrGroupEntity> ConditionQueryList(Long key,Long catelogId);
}
