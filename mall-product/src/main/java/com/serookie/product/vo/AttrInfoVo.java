package com.serookie.product.vo;

import com.serookie.product.entity.AttrEntity;
import lombok.Data;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/24
 * 返回查询属性详情的封装vo
 */
@Data
public class AttrInfoVo extends AttrEntity {
   private Long attrGroupId;
   private Long catelogId;
   private Long[] catelogPath;
}
