package com.cloud.fish.identification.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.fish.identification.model.entity.FishCategoryEntity;
import com.cloud.fish.identification.model.vo.CategoryVo;

import java.util.List;
import java.util.Map;

/**
 * 鱼类信息分类
 *
 * @author ${author}
 * @email ${email}
 * @date 2023-03-06 01:56:28
 */
public interface FishCategoryService extends IService<FishCategoryEntity> {

    Map<String, List<CategoryVo>> getCategoriesFromRedis();

    Map<String, List<CategoryVo>> getCategories();




}

