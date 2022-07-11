package com.serookie.product.entity.ES;

import lombok.Data;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/25
 */
@Data
public class SearchAttrValue {
    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性名称
     */
    private String attrName;
    /**
     * 属性值
     */
    private String attrValue;
}
