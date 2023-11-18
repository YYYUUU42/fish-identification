package com.cloud.fish.identification.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloud.fish.common.utils.ResponseResult;
import com.cloud.fish.identification.model.entity.UserInfo;
import com.cloud.fish.identification.model.vo.UserInfoVo;
import com.cloud.fish.identification.service.UserInfoService;
import com.cloud.fish.identification.utils.BeanCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户信息
 */
@RestController
@RequestMapping("/fish/user")
public class UserController {

    @Resource
    private UserInfoService userInfoService;


    /**
     * 修改用户信息
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/update")
    public ResponseResult updateUser(@RequestBody UserInfo userInfo) {
        return userInfoService.updateUser(userInfo);
    }

    /**
     * 获得用户信息
     *
     * @param openid
     * @return
     */
    @GetMapping("/getUserInfo")
    public ResponseResult getUserInfo(String openid) {
        UserInfo one = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getOpenid, openid));
        UserInfoVo userInfoVo = null;
        if (one != null) {
            userInfoVo = BeanCopyUtils.copyBean(one, UserInfoVo.class);
        }
        return ResponseResult.okResult(userInfoVo);
    }
}
