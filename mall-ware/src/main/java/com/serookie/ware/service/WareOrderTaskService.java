package com.serookie.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 10:56:31
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

