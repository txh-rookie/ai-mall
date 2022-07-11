package com.serookie.product.service.impl;

import com.serookie.product.entity.SkuInfoEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.product.dao.SkuImagesDao;
import com.serookie.product.entity.SkuImagesEntity;
import com.serookie.product.service.SkuImagesService;
import org.springframework.util.StringUtils;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuImagesEntity> page = this.page(
                new Query<SkuImagesEntity>().getPage(params),
                new QueryWrapper<SkuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuImagesEntity> getBaseImagesBySkuId(Long skuId) {
        List<SkuImagesEntity> skuImagesEntities=null;
        if(!StringUtils.isEmpty(skuId)){
            QueryWrapper<SkuImagesEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sku_id",skuId);
            skuImagesEntities = this.baseMapper.selectList(queryWrapper);
        }else {
            throw new NullPointerException("参数不能为null");
        }
        return skuImagesEntities;
    }

}