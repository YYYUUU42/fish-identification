package com.cloud.fish.identification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.fish.identification.model.entity.FishImgEntity;


/**
 * 鱼类图片路径
 *
 * @author ${author}
 * @email ${email}
 * @date 2023-03-08 00:11:41
 */
public interface FishImgService extends IService<FishImgEntity> {
    FishImgEntity getFishImg(Long fishId);
}

