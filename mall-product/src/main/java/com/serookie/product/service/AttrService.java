package com.serookie.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.product.entity.AttrEntity;
import com.serookie.product.vo.AttrInfoVo;
import com.serookie.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void AttrSave(AttrVo attrVo);

    PageUtils queryBaseAttr(Map<String, Object> params, Long catelogId);

    AttrInfoVo getAttrInfo(Long attrId);

    void attrUpdate(AttrInfoVo attrInfoVo);

    List<Long> selectByTypeIds(List<Long> attrIds);
}

