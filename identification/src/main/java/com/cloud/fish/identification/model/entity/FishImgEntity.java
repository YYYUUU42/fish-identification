package com.cloud.fish.identification.model.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Data;

/**
 * 鱼类图片路径
 * 
 * @author ${author}
 * @email ${email}
 * @date 2023-03-08 00:11:41
 */
@Data
@TableName("fish_img")
public class FishImgEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 鱼类名称id
	 */
	@TableId
	private Long fishId;
	/**
	 * 鱼类图片路径
	 */
	private String imgPath;

}
