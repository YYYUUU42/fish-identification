package com.cloud.fish.identification.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.fish.identification.model.entity.Log;
import com.cloud.fish.identification.model.entity.UserInfo;
import com.cloud.fish.identification.mapper.LogMapper;
import com.cloud.fish.identification.service.LogService;
import com.cloud.fish.identification.service.UserInfoService;
import com.cloud.fish.identification.model.vo.AlgorithmVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author yu
 * @description 针对表【log】的数据库操作Service实现
 * @createDate 2023-08-22 22:07:32
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 记录操作日志
     *
     * @param openid
     * @param algorithmVoJson
     */
    @Override
    public void saveLog(String openid, String algorithmVoJson) {
        AlgorithmVo algorithmVo = JSON.parseObject(algorithmVoJson, AlgorithmVo.class);

        Log log = new Log();
        log.setOpenid(openid);

        UserInfo userInfo = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getOpenid, openid));
        String username = "";

        if (userInfo != null) {
            username = userInfo.getUsername();
        }
        log.setUsername(username);

        log.setImg(algorithmVo.getOldImgPath());

        String detail = "识别失败";
        //识别算法成功
        if (StringUtils.hasText(algorithmVo.getNewImgPath())) {
            log.setImg(algorithmVo.getNewImgPath());
            detail = "识别成功";
        }
        log.setDetail(detail);

        log.setTime(new Date(System.currentTimeMillis()));
        log.setOperation("鱼类识别");

        this.save(log);
    }
}




