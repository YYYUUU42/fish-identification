package com.cloud.fish.thirdparty.service.impl;

import com.cloud.fish.thirdparty.service.CountService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("CountService")
public class CountServiceImpl implements CountService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String getCount() {
        String count=stringRedisTemplate.opsForValue().get("count");
        if(StringUtils.isNotEmpty(count)){
            return count;
        }else {
            stringRedisTemplate.opsForValue().set("count", "1");
        }
        return "1";
    }

    @Override
    public void UpdateCount() {
        stringRedisTemplate.opsForValue().increment("count",1);
    }
}
