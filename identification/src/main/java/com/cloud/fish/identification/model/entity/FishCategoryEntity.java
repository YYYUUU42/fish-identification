package com.cloud.fish.identification.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;

/**
 * 鱼类信息分类
 * 
 * @author ${author}
 * @email ${email}
 * @date 2023-03-06 01:56:28
 */
@Data
@TableName("fish_category")
public class FishCategoryEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId
	private Long id;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 父分类id
	 */
	private Long parentId;
	/**
	 * 分类级别：0->1级；1->2级
	 */
	private Integer level;
	/**
	 * 分类排序
	 */
	private Integer sort;

}
