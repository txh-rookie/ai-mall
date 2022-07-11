package com.serookie.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/29
 */
@Data
public class SpuItemGroupAttrVo {
    private String groupName;
    private List<SpuGroupByAttrVo> spuGroupByAttrVos;
}
