package com.serookie.product.service.impl;

import com.serookie.product.vo.AttrRelationVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.product.dao.AttrAttrgroupRelationDao;
import com.serookie.product.entity.AttrAttrgroupRelationEntity;
import com.serookie.product.service.AttrAttrgroupRelationService;
import org.springframework.util.StringUtils;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 添加属性与分组关联关系
     * @param attrRelationVo
     */
    @Override
    public void saveBatch(List<AttrRelationVo> attrRelationVo) {
         if(!StringUtils.isEmpty(attrRelationVo)){
             List<AttrAttrgroupRelationEntity> collect= attrRelationVo.stream().map(elem -> {
                 AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
                 BeanUtils.copyProperties(elem, attrAttrgroupRelationEntity);
                 return attrAttrgroupRelationEntity;
             }).collect(Collectors.toList());
             this.saveBatch(collect);
         }
    }

}