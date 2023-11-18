package com.cloud.fish.identification.service;


import com.cloud.fish.identification.model.vo.AlgorithmVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UploadImg {

    /**
     * 将前端传来的图片存放到要运行算法的文件夹中
     * @param file
     * @return
     * @throws IOException
     */
    String imageUpload(MultipartFile file) throws IOException;

    /**
     * 运行算法，返回运行结果
     * @return
     */
    AlgorithmVo runAlgorithm();

}
