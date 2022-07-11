package com.serookie.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.kevintam.common.annoaction.ListValue;
import com.kevintam.common.valid.AddGroup;
import com.kevintam.common.valid.UpdateGroup;
import com.kevintam.common.valid.UpdateStatus;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author kevintam
 * @email 843808107@qq.com
 * @date 2022-06-16 09:12:05
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@Null(message = "新增时，不能指名id",groups = {AddGroup.class})
	@NotNull(message = "修改时,id不能为空的",groups = {UpdateGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 * @BotNull表示该字段不能为空
	 * @NotBlank
	 */
	@NotBlank(message = "品牌名称不能为空",groups = {AddGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotNull(message = "该字段不能为空")
	@URL(message = "必须是一个网络地址")
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@ListValue(value={0,1},groups = {UpdateStatus.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 * 写正则表达式
	 */
	@NotNull(message = "检索字母不能为空")
	@Pattern(regexp = "^[a-zA-Z]$",message = "检索状态必须是一个字母")
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序字段不能为空")
	@Min(message = "排序必须大于等于0",value = 0)
	private Integer sort;

}
