package com.serookie.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.member.entity.MemberReceiveAddressEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 10:27:35
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<MemberReceiveAddressEntity> getAddressByMemberId(Long memberId);
}

