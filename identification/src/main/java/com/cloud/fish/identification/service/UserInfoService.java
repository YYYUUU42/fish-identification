package com.cloud.fish.identification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.fish.common.utils.ResponseResult;
import com.cloud.fish.identification.model.entity.UserInfo;

/**
* @author yu
* @description 针对表【user_info】的数据库操作Service
* @createDate 2023-08-22 22:07:36
*/
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 修改用户信息
     * @param userInfo
     * @return
     */
    ResponseResult updateUser(UserInfo userInfo);
}
