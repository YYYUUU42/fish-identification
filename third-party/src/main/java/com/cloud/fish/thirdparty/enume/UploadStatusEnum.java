package com.cloud.fish.thirdparty.enume;

public enum UploadStatusEnum {
    FORMAT_ERROR("上传的图片格式必须为:bmp,jpg,jpeg,png",20001),
    UPLOAD_FAIL("图片上传失败",40001);
    private String msg;
    private Integer code;

    UploadStatusEnum(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }
}
