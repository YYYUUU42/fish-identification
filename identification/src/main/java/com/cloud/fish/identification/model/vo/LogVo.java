package com.cloud.fish.identification.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogVo {

    /**
     * 微信小程序的openId
     */
    private String openid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户的操作
     */
    private String operation;

    /**
     * 操作的时间
     */
    private Date time;

    /**
     * 操作成功与否
     */
    private String detail;

}
