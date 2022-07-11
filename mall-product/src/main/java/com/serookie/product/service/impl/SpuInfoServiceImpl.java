package com.serookie.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.kevintam.common.utils.R;
import com.serookie.product.Feign.SearchFeignService;
import com.serookie.product.entity.*;
import com.serookie.product.entity.ES.SearchAttrValue;
import com.serookie.product.entity.ES.SpuESModel;
import com.serookie.product.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.product.dao.SpuInfoDao;


@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService attrValueService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SearchFeignService feignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        List<SpuESModel> spuList=new ArrayList<>();
        //根据spuId查询找到所有的sku属性
       List<SkuInfoEntity> skus=skuInfoService.findBySpuId(spuId);
       //attr属性
        QueryWrapper<ProductAttrValueEntity> entityQueryWrapper = new QueryWrapper<>();
        entityQueryWrapper.eq("spu_id",spuId);
        //根据spuId查询到所有到属性
        List<ProductAttrValueEntity> attrValueEntityList = attrValueService.list(entityQueryWrapper);
        List<Long> attrIds = attrValueEntityList.stream().map((elem) -> {
            return elem.getAttrId();
        }).collect(Collectors.toList());
        //然后根据id去查询所有能显示的属性数据
       List<Long> attrEntities= attrService.selectByTypeIds(attrIds);
       Set<Long> set=new HashSet<>(attrEntities);
        ArrayList<SearchAttrValue> searchAttrValues = new ArrayList<>();
        List<SearchAttrValue> attrValues = attrValueEntityList.stream().filter(elem -> {
            return set.contains(elem.getAttrId());
        }).map(item -> {
            SearchAttrValue attrValue = new SearchAttrValue();
            BeanUtils.copyProperties(item, searchAttrValues);
            return attrValue;
        }).collect(Collectors.toList());
        //2、分装信息
        List<SpuESModel> spuESModels = skus.stream().map(item -> {
            SpuESModel spuESModel = new SpuESModel();
            BeanUtils.copyProperties(item, spuESModel);
            //图片不一致
            spuESModel.setSkuImg(item.getSkuDefaultImg());
            // TODO: 2022/6/25 要使用远程连接去访问库存系统是否有库存
//            spuESModel.setHasStore();
            // TODO: 2022/6/25 热度评分默认为0
            // TODO: 2022/6/25 查询品牌信息和图片
            BrandEntity brandEntity = brandService.getById(item.getBrandId());
            spuESModel.setBrandName(brandEntity.getName());
            spuESModel.setBrandImg(brandEntity.getLogo());
            CategoryEntity categoryEntity = categoryService.getById(spuESModel.getCatelogId());
            spuESModel.setCategoryName(categoryEntity.getName());
            spuESModel.setAttrs(attrValues);
            //查询一些属性
            return spuESModel;
        }).collect(Collectors.toList());
        // TODO: 2022/6/25 将这些索引信息发送到es服务器上 ，将检索信息发送给search服务
        R statusUp = feignService.productStatusUp(spuESModels);
//        JSON.parseObject(statusUp.get("data"),Boolean.class)
        log.info("是否上传成功:{}",statusUp);
    }

}