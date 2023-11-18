package com.cloud.fish.identification.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("fish_info")
public class FishInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 鱼类id
	 */
	@TableId
	@TableField("fishId")
	private Integer fishId;

	/**
	 * 鱼类中文名称
	 */
	@TableField("chineseName")
	private String chineseName;

	/**
	 * 鱼类英文名称
	 */
	@TableField("englishName")
	private String englishName;


}
