package com.cloud.fish.thirdparty.utils;

import com.cloud.fish.thirdparty.dto.PicUploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class UploadUtils {

    public static PicUploadResult uploadManyImg(List<MultipartFile> uploadFile, String fileName) {
        List<String> imgPaths = new ArrayList<>();
        for (MultipartFile multipartFile : uploadFile) {
            PicUploadResult picUploadResult = uploadImgMethod(multipartFile,fileName);
            if(picUploadResult.isLegal()){
                imgPaths.add(picUploadResult.getImgPath());
            }else{
                return picUploadResult;
            }

        }

        PicUploadResult picUploadResult = new PicUploadResult();
        picUploadResult.setLegal(true);
        picUploadResult.setImgPahts(imgPaths);
        return picUploadResult;
    }

    /**
     * 上传图片方法类
     * @param multipartFile
     * @return
     */
    public static PicUploadResult uploadImgMethod(MultipartFile multipartFile,String fileName) {
        PicUploadResult picUploadResult = new PicUploadResult();

        if (multipartFile.isEmpty()) {
            //返回选择文件提示
            picUploadResult.setLegal(false);
            return picUploadResult;
        }

        //存放上传文件的文件夹
        File file = new File(fileName);
        if (!file.exists()) {
            //递归生成文件夹
            file.mkdirs();
        }
        //获取原始的名字  original:最初的，起始的  方法是得到原来的文件名在客户机的文件系统名称
        String oldName = multipartFile.getOriginalFilename();
        String newName = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."));
        try {
            //构建真实的文件路径
            File newFile = new File(file.getAbsolutePath() + File.separator + newName);
            //转存文件到指定路径，如果文件名重复的话，将会覆盖掉之前的文件,这里是把文件上传到 “绝对路径”
            multipartFile.transferTo(newFile);

            String filePath =file.getAbsolutePath() + File.separator + newName;
            log.info("-----------【" + filePath + "】-----------");
            picUploadResult.setLegal(true);
            picUploadResult.setImgPath(filePath);
            return picUploadResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        picUploadResult.setLegal(false);
        return picUploadResult;
    }
}
