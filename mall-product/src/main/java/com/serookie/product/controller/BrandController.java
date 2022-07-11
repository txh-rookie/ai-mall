package com.serookie.product.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.kevintam.common.valid.AddGroup;
import com.kevintam.common.valid.UpdateGroup;
import com.kevintam.common.valid.UpdateStatus;
import com.serookie.product.utils.DateFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.serookie.product.entity.BrandEntity;
import com.serookie.product.service.BrandService;
import com.kevintam.common.utils.PageUtils;
import com.kevintam.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * 品牌
 *
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
@Slf4j
@RestController
@RequestMapping("product/brand")
public class  BrandController {
    @Autowired
    private BrandService brandService;

    @Autowired
    private OSS client;//注入一个oss

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;
    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endpoint;
    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 文件上传
     * 这是第一种方式，用户将文件上传到后端服务器，后端服务器再将文件上传到oss里面，这种方式会加大的服务器的压力。
     */
    @PostMapping("/upload")
    public R uploadBrandFile(@RequestParam("file") MultipartFile file)  {

        if(StringUtils.isEmpty(file)){
            return R.error();
        }
        String name = file.getOriginalFilename();
        if(name.lastIndexOf(".") <0){
            return R.error("文件格式异常");
        }
        //判断文件的后缀
        String prefix = name.substring(name.lastIndexOf("."));
        if(!prefix.equalsIgnoreCase(".jpg")&&!prefix.equalsIgnoreCase(".jpeg")
                && !prefix.equalsIgnoreCase(".svg")&&!prefix.equalsIgnoreCase(".png")){
             return R.error("文件格式异常");
        }
        String newName= DateFormatUtils.DateFormat(new Date())+"-"+file.getOriginalFilename();
        log.info(newName);
        try {
            client.putObject("serookie",newName,new ByteArrayInputStream(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.shutdown();
        }
        return R.ok("文件上传成功");
    }
    /**
     * 第二种方式就是前端请求后端服务器要发送文件，
     * 后端给它签发一个签名,它拿到这个签名之后，就可以独立发送文件到oss里面
     */
    @GetMapping("/oss/policy")
    @ResponseBody
    public R policy(){
        String host="http://"+"serookie"+"."+endpoint;
        String dir="mall";
        Map<String, String> respMap=null;
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);
            respMap= new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            client.shutdown();
        }
        return R.ok().put("data",respMap);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
//    @RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     * 使用valid开发校验规则
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
//    @RequiresPermissions("product:brand:save")
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand /*BindingResult result*/){
//        HashMap<String,String> map=new HashMap<>();
//        if(result.hasErrors()){
//            result.getFieldErrors().forEach((item)->{
//                String defaultMessage = item.getDefaultMessage();//返回的信息
//                String field = item.getField();
//                map.put(field,defaultMessage);
//            });
//            return R.error(400,"输入的数据不合法").put("data",map);
//        }
		brandService.save(brand);
        return R.ok();
    }
    /**
     * 根据一个status来进行修改成功的
     */
     @PostMapping("/save/status")
     public R updateStatus(@Validated(UpdateStatus.class) @RequestBody BrandEntity brandEntity){
         boolean save = brandService.updateById(brandEntity);
         return R.ok();
     }
    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("product:brand:update")
    public R update(@RequestBody BrandEntity brand){
        // TODO: 2022/6/23 进行修改必须保证冗余字段的修改
		brandService.updateDetail(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
