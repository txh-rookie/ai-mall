package com.serookie.product.service.impl;

import com.kevintam.common.utils.Query;
import com.serookie.product.dao.AttrAttrgroupRelationDao;
import com.serookie.product.dao.AttrDao;
import com.serookie.product.entity.AttrAttrgroupRelationEntity;
import com.serookie.product.entity.AttrEntity;
import com.serookie.product.service.AttrService;
import com.serookie.product.service.ProductAttrValueService;
import com.serookie.product.vo.AttrRelationVo;
import com.serookie.product.vo.SkuInfoVo;
import com.serookie.product.vo.SpuGroupByAttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevintam.common.utils.PageUtils;

import com.serookie.product.dao.AttrGroupDao;
import com.serookie.product.entity.AttrGroupEntity;
import com.serookie.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrDao attrDao;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
            if(catelogId==0){
                IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<AttrGroupEntity>());
                return new PageUtils(page);
            }else{
                QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("catelog_id",catelogId);
//               List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(queryWrapper);//查询出来所有的数据
                String key=(String)params.get("key");
                if(!StringUtils.isEmpty(key)){
                    queryWrapper.and((obj)->{
                        obj.eq("attr_group_id",key).or().like("attr_group_name",key);
                    });
                }
                IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), queryWrapper);
                return new PageUtils(page);
            }
    }

    @Override
    public List<AttrEntity> getAttrRelation(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> attrGroupIdEntity = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
//        List<AttrEntity> attrEntities = new ArrayList<>();
//        attrGroupIdEntity.forEach(elem->{
//            AttrEntity attrEntity = attrDao.selectById(elem.getAttrId());
//            attrEntities.add(attrEntity);
//        });
        List<Long> listIds = attrGroupIdEntity.stream().map(elem -> {
            return elem.getAttrId();
        }).collect(Collectors.toList());
        if(listIds==null || listIds.size()==0){
            return null;
        }
        List<AttrEntity> attrEntities = attrDao.selectBatchIds(listIds);
        return attrEntities;
    }

    @Override
    public void deleteAttrRelation(AttrRelationVo[] attrRelationVo) {
        //1、删除attr
        List<AttrAttrgroupRelationEntity> entityList = Arrays.stream(attrRelationVo).map(elem -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(elem, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        //2、删除及联关系
        attrAttrgroupRelationDao.deleteBatchRelation(entityList);
    }

    @Override
    public PageUtils notAttrRelation(Map<String,Object> params,Long attrGroupId) {
        AttrGroupEntity attrGroupEntity = this.getById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //先查询出所有在分类下的分组属性，过滤掉当前的使用的分组属性
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId).ne("attr_group_id", attrGroupId));
        List<Long> attrGroup = attrGroupEntities.stream().map(elem -> {
            return elem.getAttrGroupId();
        }).collect(Collectors.toList());
        //然后根据当前信息去查询所有查询的属性信息
        List<AttrAttrgroupRelationEntity> entityList = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroup));
        List<Long> attrIds = entityList.stream().map(elem -> {
            return elem.getAttrId();
        }).collect(Collectors.toList());
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId).notIn("attr_id", attrIds);
        IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    @Override
    public List<SpuGroupByAttrVo> getSpuGroupByAttrVos(Long spuId, Long catalogId) {
        //从pms_product_attr_value表中拿到所有的attrId
        System.out.println(catalogId);
        System.out.println(spuId);
       List<SpuGroupByAttrVo> spuGroupByAttrVos= this.baseMapper.getSpuGroupWithAttrs(spuId,catalogId);
        System.out.println(spuGroupByAttrVos);
        return spuGroupByAttrVos;
    }
}