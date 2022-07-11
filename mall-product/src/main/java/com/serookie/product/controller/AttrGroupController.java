package com.serookie.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.serookie.product.entity.AttrEntity;
import com.serookie.product.service.AttrAttrgroupRelationService;
import com.serookie.product.service.CategoryService;
import com.serookie.product.vo.AttrRelationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.serookie.product.entity.AttrGroupEntity;
import com.serookie.product.service.AttrGroupService;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.R;



/**
 * 属性分组
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
@Slf4j
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;


    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrGroupId){
        List<AttrEntity> entityList=attrGroupService.getAttrRelation(attrGroupId);
        return R.ok().put("data",entityList);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);
        return R.ok().put("page", page);
    }

    @PostMapping("/attr/relation")
    public R addAttrRelation(@RequestBody List<AttrRelationVo> attrRelationVo){
        //添加功能
         attrAttrgroupRelationService.saveBatch(attrRelationVo);
         return R.ok();
    }
    /**
     *
     */
    @GetMapping("/list/{catelogId}")
    public R catelogIdList(@RequestParam Map<String,Object> params,@PathVariable("catelogId") Long catelogId){
        log.info("分类id的值为：{}",catelogId);
        PageUtils pageUtils = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page",pageUtils);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
//    @RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] catelogPath=categoryService.findByCatelogPath(catelogId);
//        log.info("递归查询的数据为:{}",catelogPath);
		attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);
        return R.ok();
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R noAttrRelation(@RequestParam Map<String,Object> params,@PathVariable("attrgroupId") Long attrGroupId){
          PageUtils page=attrGroupService.notAttrRelation(params,attrGroupId);
          return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    @PostMapping("/attr/relation/delete")
    public R deleteAttrRelation(@RequestBody AttrRelationVo[] attrRelationVo){
         attrGroupService.deleteAttrRelation(attrRelationVo);
         return R.ok();
    }
    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
