package com.cloud.fish.identification.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.fish.identification.mapper.FishImgDao;
import com.cloud.fish.identification.model.entity.FishImgEntity;
import com.cloud.fish.identification.service.FishImgService;
import org.springframework.stereotype.Service;

@Service("fishImgService")
public class FishImgServiceImpl extends ServiceImpl<FishImgDao, FishImgEntity> implements FishImgService {
    @Override
    public FishImgEntity getFishImg(Long fishId) {
        return this.baseMapper.selectOne(new QueryWrapper<FishImgEntity>().eq("fish_id", fishId));
    }
}