package com.cloud.fish.thirdparty.controller;
import com.cloud.fish.thirdparty.constant.UploadConstant;
import com.cloud.fish.thirdparty.dto.PicUploadResult;
import com.cloud.fish.thirdparty.enume.UploadStatusEnum;
import com.cloud.fish.thirdparty.utils.CodeUtils;
import com.cloud.fish.thirdparty.utils.R;
import com.cloud.fish.thirdparty.utils.UploadUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.cloud.fish.thirdparty.service.CountService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/thirdparty/local")
public class LocalUploadController {
    @Value("${wx.mp.app-id}")
    private String appId;
    @Value("${wx.mp.secret}")
    private String secret;

    @Autowired
    private CountService countService;


    @RequestMapping("/upload")
    public R localUploadFiles(@RequestParam("fishName") String fishName,
                              @RequestParam("direct") String direct,
                              @RequestParam("code") String code,
                              String type,
                              HttpServletRequest request){

        String fileName;
        if(StringUtils.isNotEmpty(type)&&type.equals("1")) {
            if(StringUtils.isNotEmpty(fishName)) {
                fileName = "D:\\鱼类采集\\其他\\" + fishName + "\\" + CodeUtils.getOpenId(appId, secret, code);
            }else{
                fileName = "D:\\鱼类采集\\其他\\未知" +countService.getCount() +"\\"+direct +"\\" + CodeUtils.getOpenId(appId, secret, code);
                countService.UpdateCount();
            }
        }else {
            fileName = "D:\\鱼类采集\\" + direct + "\\" + fishName + "\\" + CodeUtils.getOpenId(appId, secret, code);
        }
        MultipartHttpServletRequest req =(MultipartHttpServletRequest)request;
        List<MultipartFile> files = req.getFiles("files");

        boolean isFlag = false;
        for (MultipartFile uploadFile : files) {
            for (String types : UploadConstant.IMAGE_TYPE) {
                if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(), types)) {
                    isFlag = true;
                    break;
                }
            }
        }

        if (isFlag) {
            PicUploadResult picUploadResult = UploadUtils.uploadManyImg(files,fileName);
            boolean isLegal = picUploadResult.isLegal();

            if (isLegal) {
                Map<String, List<String>> resMap = new HashMap<>();
                resMap.put("imgPaths", picUploadResult.getImgPahts());
                return R.ok().put("data",resMap);
            } else {
                return R.error(UploadStatusEnum.UPLOAD_FAIL.getCode(),UploadStatusEnum.UPLOAD_FAIL.getMsg());
            }
        } else {
            return R.error(UploadStatusEnum.FORMAT_ERROR.getCode(), UploadStatusEnum.FORMAT_ERROR.getMsg());
        }
    }

}
