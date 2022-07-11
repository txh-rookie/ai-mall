package com.serookie.member.service.impl;

import com.kevintam.common.utils.R;
import com.serookie.member.dao.MemberLevelDao;
import com.serookie.member.entity.MemberLevelEntity;
import com.serookie.member.entity.UserParamVo;
import com.serookie.member.entity.WxUser;
import com.serookie.member.exception.CheckPhoneException;
import com.serookie.member.exception.CheckUsernameException;
import com.serookie.member.vo.LoginParamVo;
import com.serookie.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.member.dao.MemberDao;
import com.serookie.member.entity.MemberEntity;
import com.serookie.member.service.MemberService;
import org.springframework.util.StringUtils;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private MemberLevelDao memberLevelDao;
    @Autowired
    private MemberService memberService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(UserParamVo userParamVo) {
        MemberEntity memberEntity = new MemberEntity();
        //将账号进行封装
        memberEntity.setCreateTime(new Date());
        //保证用户的唯一
        checkUsername(userParamVo.getUsername());
        checkPhone(userParamVo.getPhone());
        memberEntity.setUsername(userParamVo.getUsername());
        memberEntity.setMobile(userParamVo.getPhone());
        //查询默认的用户会员
        MemberLevelEntity entity = memberLevelDao.selectDefaultLaven();
        memberEntity.setLevelId(entity.getId());
        //密码进行加密  使用加密编码器进行加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(userParamVo.getPassword());
        memberEntity.setPassword(encode);
        this.baseMapper.insert(memberEntity);
    }

    @Override
    public void checkUsername(String username) throws CheckUsernameException {
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        Long count = this.baseMapper.selectCount(queryWrapper);
        if(count>0){
            throw new CheckUsernameException("用户名以存在");
        }
    }

    @Override
    public void checkPhone(String phone)throws CheckPhoneException {
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",phone);
        Long count = this.baseMapper.selectCount(queryWrapper);
        if(count>0){
            throw new CheckPhoneException("手机号已存在");
        }
    }

    @Override
    public MemberEntity login(LoginParamVo loginParamVo) {
        //账号密码
        String username = loginParamVo.getUsername();
        String password = loginParamVo.getPassword();
        //对账号进行校验
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username).or().eq("mobile",username);
        MemberEntity memberEntity = this.getOne(queryWrapper);
        if(!StringUtils.isEmpty(memberEntity)){
            //正确对拿到用户，校验密码
            //加密后密码
            String passwordDb = memberEntity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean matches = passwordEncoder.matches(password, passwordDb);
            return matches?memberEntity:null;
        }
        return null;
    }

    /**
     * 接入github的oauth登录
     * @param socialUser
     * @return
     */
    @Override
    public MemberEntity login(SocialUser socialUser) {
        //通过唯一表示符id来进行区分是通过社交登录的，还是账号密码登录
        //登录和注册和并逻辑
        Long sId = socialUser.getId();
        //先判断是否注册过数据库
        QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("social_id",socialUser.getId());
        MemberEntity memberEntity = this.getOne(queryWrapper);
        if(StringUtils.isEmpty(memberEntity)){
            //表示没有进行注册过,给它进行注册
            MemberEntity member = new MemberEntity();
            member.setSocialId(socialUser.getId());
            member.setUsername(socialUser.getName());
            member.setHeader(socialUser.getAvatarUrl());
            member.setAccessToken(socialUser.getAccessToken());
            member.setSign(socialUser.getBio());
            QueryWrapper<MemberLevelEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("default_status",1);
            MemberLevelEntity memberLevelEntity = memberLevelDao.selectOne(wrapper);
            member.setLevelId(memberLevelEntity.getId());//设置会员等级
            memberService.save(member);//注册成功
            //注册成功后，进行登录
            return member;
        }else{
            //表示已经注册过,更新一下token
            memberEntity.setAccessToken(socialUser.getAccessToken());
            memberService.updateById(memberEntity);//进行更新
            return memberEntity;
        }
    }

    /**
     * 接入oauth2.0的wx登录
     * @param wxUser
     * @return
     */
    @Override
    public MemberEntity login(WxUser wxUser) {
        if(!StringUtils.isEmpty(wxUser)){
            QueryWrapper<MemberEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("openid",wxUser.getOpenId());
            MemberEntity entity = this.getOne(wrapper);
            if(StringUtils.isEmpty(entity)){
                //表示没有进行注册过,给他进行注册
                MemberEntity memberEntity = new MemberEntity();
                memberEntity.setHeader(wxUser.getHeadImgUrl());
                memberEntity.setOpenid(wxUser.getOpenId());
                memberEntity.setNickname(wxUser.getNickname());
                memberEntity.setGender(wxUser.getSex());
//                memberEntity.setUsername(wxUser.getNickname());
                memberService.save(memberEntity);//注册
                return memberEntity;
            }else {
                //已经注册过，给他登录
                return entity;
            }
        }
        return null;
    }

}