package com.serookie.product.service.impl;

import com.serookie.product.entity.*;
import com.serookie.product.service.*;
import com.serookie.product.vo.SkuInfoVo;
import com.serookie.product.vo.SkuItemSaleAttrVo;
import com.serookie.product.vo.SpuGroupByAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.product.dao.SkuInfoDao;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> findBySpuId(Long spuId) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<SkuInfoEntity>();
        queryWrapper.eq("spu_id",spuId);
        List<SkuInfoEntity> infoEntities = this.list(queryWrapper);
        return infoEntities;
    }

    @Override
    public SkuInfoVo skuItem(Long skuId) {
        SkuInfoVo skuInfoVo = new SkuInfoVo();
        //1、获取sku基本信息
        //使用异步编排的方式进行优化
        CompletableFuture<SkuInfoEntity> skuInfoSupplyAsync = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);
            skuInfoVo.setSkuInfoEntity(skuInfoEntity);
            return skuInfoEntity;
        }, threadPoolExecutor);
        //2、sku的图片信息
        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            List<SkuImagesEntity> skuImagesEntityList = skuImagesService.getBaseImagesBySkuId(skuId);
            skuInfoVo.setImagesEntities(skuImagesEntityList);
        }, threadPoolExecutor);
        //3、获取spu的销售属性组合
        CompletableFuture<Void> skuSaleFuture = skuInfoSupplyAsync.thenAcceptAsync((res) -> {
            List<SkuItemSaleAttrVo> skuSaleAttrValueEntities = skuSaleAttrValueService.getBaseSaleAttrValue(res.getSpuId());
            skuInfoVo.setSkuItemVo(skuSaleAttrValueEntities);
        }, threadPoolExecutor);
        //4、获取spu的介绍
        CompletableFuture<Void> spuInfoDescFuture = skuInfoSupplyAsync.thenAcceptAsync((res) -> {
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            skuInfoVo.setDesp(spuInfoDescEntity);
        }, threadPoolExecutor);
        //5、获取spu的规格参数信息
        CompletableFuture<Void> spuGroupByAttrFuture = skuInfoSupplyAsync.thenAcceptAsync((res) -> {
            List<SpuGroupByAttrVo> attrVos = attrGroupService.getSpuGroupByAttrVos(res.getSpuId(), res.getCatalogId());
            skuInfoVo.setSpuGroupByAttrVos(attrVos);
        }, threadPoolExecutor);
        CompletableFuture<Void> allOf = CompletableFuture.allOf(skuSaleFuture, spuGroupByAttrFuture, imageFuture, spuInfoDescFuture, skuInfoSupplyAsync);
        try {
            allOf.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return skuInfoVo;
    }







}