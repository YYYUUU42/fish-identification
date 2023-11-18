package com.cloud.fish.identification.service.impl;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.fish.identification.mapper.FishInfoDao;
import com.cloud.fish.identification.model.entity.FishInfoEntity;
import com.cloud.fish.identification.service.FishInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service("fishInfoService")
public class FishInfoServiceImpl extends ServiceImpl<FishInfoDao, FishInfoEntity> implements FishInfoService {
    private final String FISH_KINDS ="fish_kinds";
    private final Long TTL =12L;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<FishInfoEntity> queryList() {
        String fishKinds = stringRedisTemplate.opsForValue().get(FISH_KINDS);

        if(StringUtils.isNotEmpty(fishKinds)){
            //反序列化
            List<FishInfoEntity> list = JSONObject.parseArray(fishKinds, FishInfoEntity.class);
            return list;
        }

        List<FishInfoEntity> fishInfoEntities = this.baseMapper.selectList(new QueryWrapper<FishInfoEntity>());

        //序列化
        String toJsonStr = JSONUtil.toJsonStr(fishInfoEntities);

        //redis缓存
        stringRedisTemplate.opsForValue().setIfAbsent(FISH_KINDS,toJsonStr,TTL, TimeUnit.HOURS);

        return fishInfoEntities;
    }
}