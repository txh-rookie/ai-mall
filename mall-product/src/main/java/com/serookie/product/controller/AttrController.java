package com.serookie.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.serookie.product.vo.AttrInfoVo;
import com.serookie.product.vo.AttrResponseVo;
import com.serookie.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.serookie.product.entity.AttrEntity;
import com.serookie.product.service.AttrService;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.R;



/**
 * 商品属性
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;


    @GetMapping("/{attrType}/list/{catelogId}")
    public R attrSaleList(@RequestParam Map<String,Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType") String attrType){
        PageUtils  page= attrService.queryBaseAttr(params,catelogId);
        return R.ok().put("data",page);
    }

    @GetMapping("/base/list/{catelogId}")
    public R attrBaseList(@RequestParam Map<String,Object> params,@PathVariable("catelogId") Long catelogId){
          PageUtils  page= attrService.queryBaseAttr(params,catelogId);
          return R.ok().put("data",page);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
//    @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);
        AttrInfoVo attr=attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attr);
    }
    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attrVo){
		attrService.AttrSave(attrVo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrInfoVo attrInfoVo){
		attrService.attrUpdate(attrInfoVo);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
