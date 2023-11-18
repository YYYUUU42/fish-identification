package com.cloud.fish.identification.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {
    /**
     * 用户名
     */
    private String username;

    /**
     * 账户
     */
    private String account;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 身份
     */
    private String standing;

}
