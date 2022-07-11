package com.serookie.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.product.entity.CommentReplayEntity;

import java.util.Map;

/**
 * 商品评价回复关系
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

