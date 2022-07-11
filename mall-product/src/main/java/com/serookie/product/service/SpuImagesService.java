package com.serookie.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.product.entity.SpuImagesEntity;

import java.util.Map;

/**
 * spu图片
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-25 14:21:52
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

