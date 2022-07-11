package com.serookie.member.controller;

import java.util.Arrays;
import java.util.Map;
import com.kevintam.common.execption.CustomExceptionEnum;
import com.serookie.member.entity.UserParamVo;
import com.serookie.member.entity.WxUser;
import com.serookie.member.exception.CheckPhoneException;
import com.serookie.member.exception.CheckUsernameException;
import com.serookie.member.fegin.CouponFeginService;
import com.serookie.member.vo.LoginParamVo;
import com.serookie.member.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.serookie.member.entity.MemberEntity;
import com.serookie.member.service.MemberService;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.R;



/**
 * 会员
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 10:27:35
 */
@Slf4j
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponFeginService couponFeginService;

    @GetMapping("/coupon")
    public R coupons(){
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUsername("李四");
        R r = couponFeginService.couponList();
        return R.ok().put("member",memberEntity).put("coupon",r);
    }
    @PostMapping("/register")
    public R register(@RequestBody UserParamVo userParamVo){
        //注册成数据库
        try{
            memberService.register(userParamVo);
        }catch(CheckUsernameException usernameException){
            return R.error(CustomExceptionEnum.CHECK_USERNAME_EXCEPTION.getCode(), CustomExceptionEnum.CHECK_USERNAME_EXCEPTION.getMessage());
        }catch(CheckPhoneException phoneException){
            return R.error(CustomExceptionEnum.CHECK_PHONE_EXCEPTION.getCode(),CustomExceptionEnum.CHECK_PHONE_EXCEPTION.getMessage());
        }
        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
    /**
     * 登录
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginParamVo loginParamVo){
        if(!StringUtils.isEmpty(loginParamVo)){
            MemberEntity memberEntity= memberService.login(loginParamVo);
            if(!StringUtils.isEmpty(memberEntity)){
                return R.ok().put("data",memberEntity);//登录成功
            }else{
                return R.error(CustomExceptionEnum.LOGIN_ERROR_EXCEPTION.getCode(), CustomExceptionEnum.LOGIN_ERROR_EXCEPTION.getMessage());
            }
        }else{
          return R.error(CustomExceptionEnum.LOGIN_ERROR_EXCEPTION.getCode(), CustomExceptionEnum.LOGIN_ERROR_EXCEPTION.getMessage());
        }
    }
    //github
    @PostMapping("/oauth/login")
    public R OauthLogin(@RequestBody SocialUser socialUser){
        MemberEntity login = memberService.login(socialUser);
        return R.ok().put("data",login);//表示登录成功
    }
    @PostMapping("/oauth/wx/login")
    public R wxLogin(@RequestBody WxUser wxUser){
        log.info("wxUser:{}",wxUser);
        MemberEntity member=memberService.login(wxUser);
        return R.ok().put("data",member);
    }
}
