package com.cloud.fish.thirdparty.utils;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cloud.fish.thirdparty.constant.UploadConstant;
import java.util.HashMap;

public class CodeUtils {
    public static String getOpenId(String appId,String secret,String code) {

        //微信小程序官方接口
        String requestUrl = UploadConstant.REQUEST_URL;
        //接口所需参数
        HashMap<String, Object> requestUrlParam = new HashMap<>();
        //小程序appId
        requestUrlParam.put("appid", appId);
        //小程序secret
        requestUrlParam.put("secret", secret);
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数，固定写死即可
        requestUrlParam.put("grant_type",UploadConstant.GRANT_TYPE);
        //发送post请求读取调用微信接口获取openid用户唯一标识
        String result = HttpUtil.get(requestUrl, requestUrlParam);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        String openid = jsonObject.get("openid", String.class);
        return openid;

    }
}
