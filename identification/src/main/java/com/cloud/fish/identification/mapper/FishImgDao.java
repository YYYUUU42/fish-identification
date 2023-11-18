package com.cloud.fish.identification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.fish.identification.model.entity.FishImgEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 鱼类图片路径
 * 
 * @author ${author}
 * @email ${email}
 * @date 2023-03-08 00:11:41
 */
@Mapper
public interface FishImgDao extends BaseMapper<FishImgEntity> {
	
}
