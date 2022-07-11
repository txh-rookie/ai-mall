package com.serookie.product.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.serookie.product.entity.CategoryEntity;
import com.serookie.product.service.CategoryService;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.R;



/**
 * 商品三级分类
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
@Slf4j
@RestController
@RequestMapping("product/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;


    /**
     * 将数据封装称为tree类型
     */
    @GetMapping("/list/tree")
    public R categoryWIthTree() throws InterruptedException {
        List<CategoryEntity> categoryTree= categoryService.queryWithTree();
        //将结果集进行封装返回给前端
        return R.ok().put("categoryTree",categoryTree);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
//    @RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);
        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryEntity category){
        if(StringUtils.isEmpty(category)){
            log.error("参数为空",new NullPointerException());
            return R.error("参数不能为空");
        }
		categoryService.save(category);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
//    @RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
        //要进行及联更新
		categoryService.updateDetail(category);
        return R.ok();
    }
    /**
     * 删除
     */
    @PostMapping("/delete")
//    @RequiresPermissions("product:category:delete")
    public R deletes(@RequestBody Long[] catIds){
        //需要检索一下该数据有没有被其他对象所引用
		//去检索一下
        Boolean flag = categoryService.removeMenuIds(catIds);
        return R.ok();
    }
}
