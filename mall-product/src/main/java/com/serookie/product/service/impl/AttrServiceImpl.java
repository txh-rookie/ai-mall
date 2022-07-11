package com.serookie.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.serookie.product.dao.AttrAttrgroupRelationDao;
import com.serookie.product.dao.AttrGroupDao;
import com.serookie.product.dao.CategoryDao;
import com.serookie.product.entity.AttrAttrgroupRelationEntity;
import com.serookie.product.entity.AttrGroupEntity;
import com.serookie.product.entity.CategoryEntity;
import com.serookie.product.service.CategoryService;
import com.serookie.product.vo.AttrInfoVo;
import com.serookie.product.vo.AttrResponseVo;
import com.serookie.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.Query;

import com.serookie.product.dao.AttrDao;
import com.serookie.product.entity.AttrEntity;
import com.serookie.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {


    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 修改属性
     * @param attrVo
     */
    @Transactional
    @Override
    public void AttrSave(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        /**
         * 第一个参数为属性的来源，第二个参数是要封装到那个参数里
         */
        BeanUtils.copyProperties(attrVo,attrEntity);
        //1、保存基本数据
        this.save(attrEntity);
        //2、保存关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        attrAttrgroupRelationDao.insert(relationEntity);
    }

    @Override
    public PageUtils queryBaseAttr(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        if(catelogId!=0){
           queryWrapper.eq("catelog_id",catelogId);
        }
        String key=(String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((item)->{
                 item.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        //分页插件封装
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        List<AttrEntity> records = page.getRecords();
        List<AttrResponseVo> attrList = records.stream().map((item) -> {
            AttrResponseVo attrResponseVo = new AttrResponseVo();
            BeanUtils.copyProperties(item, attrResponseVo);
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", item.getAttrId()));
            if (!StringUtils.isEmpty(relationEntity)) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                attrResponseVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            CategoryEntity categoryEntity = categoryDao.selectById(item.getCatelogId());
            if (!StringUtils.isEmpty(categoryEntity)) {
                attrResponseVo.setCatelogName(categoryEntity.getName());
            }
            return attrResponseVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(attrList);
        return pageUtils;
    }

    @Override
    public AttrInfoVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = attrDao.selectById(attrId);
        AttrInfoVo attrInfoVo = new AttrInfoVo();
        BeanUtils.copyProperties(attrEntity,attrInfoVo);
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
        attrInfoVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
        Long[] catelogPath = categoryService.findByCatelogPath(attrEntity.getCatelogId());
        attrInfoVo.setCatelogPath(catelogPath);
        return attrInfoVo;
    }

    @Override
    public void attrUpdate(AttrInfoVo attrInfoVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrInfoVo,attrEntity);
        attrDao.updateById(attrEntity);//自己进行更新操作
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
        attrAttrgroupRelationEntity.setAttrGroupId(attrInfoVo.getAttrGroupId());
        //修改及联属性
        Long count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrInfoVo.getAttrId()));
        if(count>0){
            //修改
            attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity,new UpdateWrapper<AttrAttrgroupRelationEntity>());
        }else{
            //新增操作
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public List<Long> selectByTypeIds(List<Long> attrIds) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>();
        queryWrapper.in("attr_id",attrIds).and((elem)->{
            elem.eq("search_type",1);
        });
        List<AttrEntity> attrEntities = attrDao.selectList(queryWrapper);
        List<Long> attrId = attrEntities.stream().map(elem -> {
            return elem.getAttrId();
        }).collect(Collectors.toList());
        return attrId;
    }
}