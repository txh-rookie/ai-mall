package com.serookie.product.service.impl;

import com.serookie.product.vo.SkuItemSaleAttrVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.product.dao.SkuSaleAttrValueDao;
import com.serookie.product.entity.SkuSaleAttrValueEntity;
import com.serookie.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getBaseSaleAttrValue(Long spuId) {
        List<SkuItemSaleAttrVo> skuItemSaleAttrVos=this.baseMapper.getSelectAttrSaleBySkuId(spuId);
        return skuItemSaleAttrVos;
    }

    @Override
    public List<String> listSkuIdAttr(Long skuId) {
        List<String> skuIdAttrs=this.baseMapper.selectSkuIdAttr(skuId);
        return null;
    }

}