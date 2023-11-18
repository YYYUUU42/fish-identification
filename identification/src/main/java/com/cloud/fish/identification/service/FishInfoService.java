package com.cloud.fish.identification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.fish.identification.model.entity.FishInfoEntity;


import java.util.List;


/**
 * 鱼类信息表
 *
 * @author ${author}
 * @email ${email}
 * @date 2023-02-13 17:38:20
 */
public interface FishInfoService extends IService<FishInfoEntity> {
    List<FishInfoEntity> queryList();
}

