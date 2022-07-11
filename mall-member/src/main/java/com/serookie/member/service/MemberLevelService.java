package com.serookie.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 10:27:35
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

