package com.serookie.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.product.entity.AttrEntity;
import com.serookie.product.entity.AttrGroupEntity;
import com.serookie.product.vo.AttrRelationVo;
import com.serookie.product.vo.SkuInfoVo;
import com.serookie.product.vo.SpuGroupByAttrVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrEntity> getAttrRelation(Long attrGroupId);

    void deleteAttrRelation(AttrRelationVo[] attrRelationVo);

    PageUtils notAttrRelation(Map<String,Object> params,Long attrGroupId);

    List<SpuGroupByAttrVo> getSpuGroupByAttrVos(Long spuId,Long catelogId);
}

