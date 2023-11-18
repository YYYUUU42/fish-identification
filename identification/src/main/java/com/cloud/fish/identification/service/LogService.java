package com.cloud.fish.identification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.fish.identification.model.entity.Log;

/**
* @author yu
* @description 针对表【log】的数据库操作Service
* @createDate 2023-08-22 22:07:32
*/
public interface LogService extends IService<Log> {


    /**
     * 记录操作日志
     * @param openid
     * @param algorithmVoJson
     */
    void saveLog(String openid, String algorithmVoJson);

}
