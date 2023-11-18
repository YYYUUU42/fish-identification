package com.cloud.fish.identification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.fish.identification.model.entity.FishCategoryEntity;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface FishCategoryDao extends BaseMapper<FishCategoryEntity> {
	
}
