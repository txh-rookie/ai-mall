package com.serookie.product.dao;

import com.serookie.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
