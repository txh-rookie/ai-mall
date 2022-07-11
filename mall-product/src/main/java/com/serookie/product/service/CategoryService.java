package com.serookie.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> queryWithTree() throws InterruptedException;

    Boolean  removeMenuIds(Long[] catIds);

    Long[] findByCatelogPath(Long attrGroupId);

    void updateDetail(CategoryEntity category);

    List<CategoryEntity> getLevel1Category();
}

