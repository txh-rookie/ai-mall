package com.serookie.member.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.member.dao.MemberReceiveAddressDao;
import com.serookie.member.entity.MemberReceiveAddressEntity;
import com.serookie.member.service.MemberReceiveAddressService;


@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<MemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据用户id查询所有到信息
     * @param memberId
     * @return
     */
    @Override
    public List<MemberReceiveAddressEntity> getAddressByMemberId(Long memberId) {
        QueryWrapper<MemberReceiveAddressEntity> addressEntityQueryWrapper = new QueryWrapper<>();
        addressEntityQueryWrapper.eq("member_id",memberId);
        List<MemberReceiveAddressEntity> list = this.list(addressEntityQueryWrapper);
        return list;
    }

}