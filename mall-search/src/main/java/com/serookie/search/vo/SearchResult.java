package com.serookie.search.vo;

import com.serookie.search.entity.SpuESModel;
import lombok.Data;

import java.util.List;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/28
 * 根据检索查询到的数据进行返回
 */
@Data
public class SearchResult {

   private List<SpuESModel> spuList;
    /**
     * 分页信息
     */
    private Long pageNum;//页码
    private Integer pageTotal;//总页吗
    private Long total;//总记录数

    private List<BrandVo> brandVos;//返回的品牌
    private List<AttrVo> attrVos;//封装的属性
   private List<CatalogVo> catalogVos;//封装的分类属性
    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;
//        private String brandImg;
    }
    /**
     * 品牌数
     */
    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    /**
     * attr属性
     */
    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;//属性
        private List<String> attrValue;//属性值
    }
}
