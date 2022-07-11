package com.serookie.product.service.impl;

import com.serookie.product.dao.CategoryBrandRelationDao;
import com.serookie.product.entity.CategoryBrandRelationEntity;
import com.serookie.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.product.dao.BrandDao;
import com.serookie.product.entity.BrandEntity;
import com.serookie.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {


    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 开启事务来保证它修改状态的一致性
     * @param brand
     */
    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {
        // TODO: 2022/6/23 必须关注冗余字段的修改
        //1、先更新自己表里的数据
        this.updateById(brand);
        //2、判断该字段里面是否有要更新的字段
        if(!StringUtils.isEmpty(brand)){
            //要连其他表的字段一起更新
            categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());
            // TODO: 2022/6/23 其他的字段也有可能会更新
        }
    }

}