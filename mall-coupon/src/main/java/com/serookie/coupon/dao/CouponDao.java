package com.serookie.coupon.dao;

import com.serookie.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:28:25
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
