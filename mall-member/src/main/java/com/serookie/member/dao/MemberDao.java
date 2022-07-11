package com.serookie.member.dao;

import com.serookie.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 10:27:35
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
