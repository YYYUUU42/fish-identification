package com.cloud.fish.identification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.fish.identification.model.entity.FishInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 鱼类信息表
 *
 * @author ${author}
 * @email ${email}
 * @date 2023-02-13 17:38:20
 */
@Mapper
public interface FishInfoDao extends BaseMapper<FishInfoEntity> {
	
}
