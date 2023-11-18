package com.cloud.fish.identification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.fish.common.utils.ResponseResult;
import com.cloud.fish.identification.exception.CustomException;
import com.cloud.fish.identification.model.entity.UserInfo;
import com.cloud.fish.identification.mapper.UserInfoMapper;
import com.cloud.fish.identification.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yu
 * @description 针对表【user_info】的数据库操作Service实现
 * @createDate 2023-08-22 22:07:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    /**
     * 修改用户信息
     *
     * @param userInfo
     * @return
     */
    @Override
    public ResponseResult updateUser(UserInfo userInfo) {
        String username = userInfo.getUsername();
        String account = userInfo.getAccount();
        String password = userInfo.getPassword();
        if (StringUtils.isAnyBlank(username, account, password)) {
            throw new CustomException(1001, "用户名、账号、密码不能为空");
        }

        //对密码进行校验，校验规则：包含数字以及大小写字母，不可有特殊字符
        /*
        (?=.*[0-9])：至少包含一个数字。
        (?=.*[a-z])：至少包含一个小写字母。
        (?=.*[A-Z])：至少包含一个大写字母。
        (?!.*[^a-zA-Z0-9])：不包含特殊字符。
        .{8,}：密码长度至少为 6。
         */
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.*[^a-zA-Z0-9]).{6,}$";
        if (!password.matches(passwordPattern)) {
            throw new CustomException(1002, "密码规则:包含数字以及大小写字母，不可有特殊字符,密码长度至少为6");
        }

        //查找数据库中，之前用户是否有添加过数据
        UserInfo info = getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getOpenid, userInfo.getOpenid()));
        int count = count(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, username));
        int count1 = count(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getAccount, account));

        if (info != null) {
            if (count > 0 && !info.getUsername().equals(username)) {
                throw new CustomException(1003, "用户名已存在");
            }

            if (count1 > 0 && !info.getAccount().equals(account)) {
                throw new CustomException(1004, "账号已存在");
            }

            userInfo.setId(info.getId());

            //判断是否修改成功
            boolean update = updateById(userInfo);
            if (!update) {
                throw new CustomException(1006, "修改人物信息失败");
            }
        } else {
            if (count > 0) {
                throw new CustomException(1003, "用户名已存在");
            }

            if (count1 > 0) {
                throw new CustomException(1004, "账号已存在");
            }

            //判断是否添加成功
            boolean save = save(userInfo);
            if (!save) {
                throw new CustomException(1006, "添加人物信息失败");
            }
        }

        return ResponseResult.okResult();
    }
}




