package com.cloud.fish.identification.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.cloud.fish.identification.model.entity.FishList;
import com.cloud.fish.identification.model.vo.AlgorithmVo;
import com.cloud.fish.identification.service.UploadImg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.List;

@Service("UploadImg")
@Slf4j
public class UploadImgImpl implements UploadImg {

    private static final String ALGORITHM_PATH = "";

    private static final String IMG_PATH = ALGORITHM_PATH + "\\detect\\images";
    private static final String ALGORITHM_COMMAND = "python " + ALGORITHM_PATH + "\\detect\\detect.py --save-txt --save-conf";
    private static final String EXCEL_PATH = ALGORITHM_PATH + "";
    private static final String NGINX_PATH = "";
    private static final String NGINX_IMG = "";
    private static final String FISH_EXCEL = "fish-list";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将小程序传过来的文件存放在指定目录中
     *
     * @param file
     * @return
     */
    @Override
    public String imageUpload(MultipartFile file) {
        //得到文件名称
        String filename = file.getOriginalFilename();

        //待检测的图片存放的位置
        String realFile = IMG_PATH;

        File file1 = new File(realFile);
        //如果存在，则必须清除里面的图片后才能放进新的图片
        deleteFile(file1);

        File file2 = new File(realFile + File.separator + filename);
        //将上传的图片传到file1中
        try {
            file.transferTo(file2);
            log.info("图片上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnImg(realFile);
    }

    /**
     * 删除文件下的所有文件
     *
     * @param file
     */
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
            //打印文件名
            String name = file.getName();
            System.out.println(name);
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();
            }
        }
    }


    /**
     * 算法识别功能
     *
     * @return
     */
    public AlgorithmVo runAlgorithm() {
        Process process;
        List<String> list = null;
        AlgorithmVo vo = new AlgorithmVo();
        try {
            //运行算法
            process = Runtime.getRuntime().exec(ALGORITHM_COMMAND);

            //得到运行后结果返回的目录
            String exp = getExp(process.getErrorStream(), System.err);

            //得到鱼类目录,先判断是否在缓存中，如果在缓存，直接获得列表
            List<FishList> fishList;
            if (stringRedisTemplate.hasKey(FISH_EXCEL)) {
                String s = stringRedisTemplate.opsForValue().get(FISH_EXCEL);
                fishList = JSON.parseArray(s, FishList.class);
            } else {
                String excelPath = EXCEL_PATH;
                fishList = getFishList(excelPath);
                String jsonString = JSON.toJSONString(fishList);
                stringRedisTemplate.opsForValue().set(FISH_EXCEL, jsonString);
            }

            //算法结果路径
            String expPath = ALGORITHM_PATH + "\\detect\\runs\\detect\\" + exp + "\\labels";
            String imgPath = ALGORITHM_PATH + "\\detect\\runs\\detect\\" + exp;

            File dir = new File(expPath);

            //这个用来存放返回的结果，一个txt对应一个String
            list = new ArrayList<>();

            //labels下的txt文件
            File[] files = dir.listFiles();

            //如果识别失败就直接返回
            if (files.length == 0) {
                return vo;
            }


            for (File file : files) {
                String txt = getMaxConfidenceDegree(FileUtil.readUtf8Lines(file));
                String info = getInfo(txt, fishList);
                list.add(info);
            }


            String s = list.get(0);
            String[] s1 = s.split(" ");

            vo.setChineseName(s1[0]);
            vo.setEnglishName(s1[1]);
            vo.setConfidenceDegree(s1[2]);
            vo.setNewImgPath(returnImg(imgPath));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

    /**
     * 得到运行完算法后，返回结果所在的目录
     *
     * @param inputStream
     * @param out
     * @return
     */
    private String getExp(InputStream inputStream, PrintStream out) {
        String s = "";
        String res = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                s += line;

            }

            int index1 = s.lastIndexOf("detect");
            int index2 = s.lastIndexOf("labels");

            res = s.substring(index1 + 7, index2 - 1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    /**
     * 得到excel下的鱼类目录
     *
     * @param path
     * @return
     */
    private List<FishList> getFishList(String path) {
        List<FishList> list = new ArrayList<>();
        EasyExcel.read(path, FishList.class, new AnalysisEventListener<FishList>() {
            @Override
            public void invoke(FishList fishList, AnalysisContext analysisContext) {
                fishList.setId(fishList.getId() + 1);
                list.add(fishList);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            }
        }).sheet().doRead();


        return list;
    }

    /**
     * 得到置信度最高的一条信息
     *
     * @param strings
     * @return
     */
    private String getMaxConfidenceDegree(List<String> strings) {
        Collections.sort(strings, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] s1 = o1.split(" ");
                String[] s2 = o2.split(" ");

                int n1 = (int) (Double.valueOf(s1[5]) * 1000);
                int n2 = (int) (Double.valueOf(s2[5]) * 1000);
                return n2 - n1;
            }
        });

        return strings.get(0);
    }

    /**
     * 通过txt，返回鱼的中文名，拉丁名，置信度
     *
     * @param s
     * @param fishLists
     * @return
     */
    private String getInfo(String s, List<FishList> fishLists) {
        String[] split = s.split(" ");
        int id = Integer.valueOf(split[0]);

        FishList fishList = fishLists.get(id);

        String fishKinds = fishList.getFishKinds();
        String fishLatinName = fishList.getFishLatinName();
        String confidenceDegree = split[5];

        return fishKinds + " " + fishLatinName + " " + confidenceDegree;
    }

    /**
     * 在nginx中创建图片，并且返回图片的地址
     *
     * @param path
     * @return
     */
    private String returnImg(String path) {
        File dir = new File(path);

        String fileName = "";
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().contains(".")) {
                fileName = file.getName();
            }
        }


        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        String newPath = uuid + "." + fileName.substring(fileName.lastIndexOf(".") + 1);

        try {
            FileInputStream fis = new FileInputStream(path + "\\" + fileName);
            FileOutputStream fos = new FileOutputStream(NGINX_PATH + "\\" + newPath);

            byte[] bytes = new byte[100];
            int temp = 0;
            while ((temp = fis.read(bytes)) > 0) {
                //将byte数组中内容直接写入
                fos.write(bytes, 0, temp);
            }
            //刷新
            fos.flush();
            //关闭
            fis.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        String res = NGINX_IMG + newPath;
        return res;
    }


}
