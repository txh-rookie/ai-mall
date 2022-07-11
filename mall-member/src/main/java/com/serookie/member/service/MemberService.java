package com.serookie.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevintam.common.utils.PageUtils;
import com.serookie.member.entity.MemberEntity;
import com.serookie.member.entity.UserParamVo;
import com.serookie.member.entity.WxUser;
import com.serookie.member.exception.CheckPhoneException;
import com.serookie.member.exception.CheckUsernameException;
import com.serookie.member.vo.LoginParamVo;
import com.serookie.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 10:27:35
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(UserParamVo userParamVo);
    //校验用户名
    void checkUsername(String username) throws CheckUsernameException;
    //校验手机号,确保唯一性
    void checkPhone(String phone) throws CheckPhoneException;

    MemberEntity login(LoginParamVo loginParamVo);

    MemberEntity login(SocialUser socialUser);
    MemberEntity login(WxUser wxUser);
}

