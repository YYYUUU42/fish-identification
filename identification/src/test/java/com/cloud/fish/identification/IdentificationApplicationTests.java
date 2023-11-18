package com.cloud.fish.identification;

import com.cloud.fish.identification.service.FishCategoryService;
import com.cloud.fish.identification.service.FishImgService;
import com.cloud.fish.identification.service.FishInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@SpringBootTest
class IdentificationApplicationTests {

    @Autowired
    private FishInfoService fishInfoService;

    @Autowired
    private FishCategoryService fishCategoryService;

    @Autowired
    private FishImgService fishImgService;

    @Test
    void contextLoads1() {
        File file = new File("D:\\code");
        // 所有空间
        long totalSpace = file.getFreeSpace()/1024/1024/1024;
        System.out.println(file.getTotalSpace());
        // 可用剩余空间
        System.out.println(file.getUsableSpace());
        // 剩余空间
        System.out.println(file.getFreeSpace());

        System.out.println(totalSpace);

    }

    @Test
    void contextLoads2() {
        String path="D:\\code\\鱼类识别\\鱼类识别算法0530\\detect\\runs\\detect\\exp2";
        deleteFile(new File(path));
    }


    public static void deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            System.out.println("文件删除失败,请检查文件路径是否正确");
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();
            }
            f.delete();
        }
    }





}
