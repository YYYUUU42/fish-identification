package com.cloud.fish.identification.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.fish.identification.service.LogService;
import com.cloud.fish.identification.model.vo.AlgorithmVo;
import com.cloud.fish.identification.service.UploadImg;
import com.cloud.fish.identification.utils.MD5;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 算法调用
 */
@RestController
@RequestMapping("/fish")
public class UploadImgController {

    @Autowired
    private UploadImg uploadImg;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private LogService logService;

    private static final String REDISSON_LOCK = "fish-lock";


    @RequestMapping("/upload")
    public JSONObject upload(HttpServletRequest request) throws Exception {
        //设置编码
        request.setCharacterEncoding("utf-8");

        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;

        //对应前端的upload的name参数"image"
        MultipartFile file = req.getFile("image");
        String openid = request.getParameter("openid");

        //得到文件的唯一MD5
        String MD5key = MD5.encrypt(file);

        String res = null;

        //判断md5是否存在于缓存中，是，直接得到返回
        if (stringRedisTemplate.hasKey(MD5key)) {
            res = stringRedisTemplate.opsForValue().get(MD5key);

            //记录操作
            logService.saveLog(openid, res);

            //转换成JSONObject类型
            JSONObject json = JSON.parseObject(res);

            return json;
        } else {
            //因为在高并发中，可能会出现，执行完上传图片后，执行算法的时候，又上传完另一个图片，导致和需要运行算法的图片不一致，返回结果不符，故加上分布式锁

            //获取锁
            RLock lock = redissonClient.getLock(REDISSON_LOCK);
            try {
                //尝试加锁,加锁成功的操作
                //第一个参数为等待的时间，如果再这个时间内取到锁将返回true，如果超过这个时间还没取到锁将返回false
                //第二个参数，取到锁之后锁过期时间，当超过这个时间还没执行完业务锁将被释放。
                if (lock.tryLock(100,100, TimeUnit.SECONDS)) {
                    try {
                        //业务功能

                        //存放图片到指定的路径中，返回旧照片的访问地址
                        String oldImgPath = uploadImg.imageUpload(file);

                        //运行算法并返回json格式的结果给前端
                        AlgorithmVo vo = uploadImg.runAlgorithm();

                        //无论是否识别成功都返回原来的照片
                        vo.setOldImgPath(oldImgPath);
                        res = JSONObject.toJSONString(vo);

                        //将res存入Redis缓存中
                        stringRedisTemplate.opsForValue().set(MD5key, res, 12, TimeUnit.HOURS);

                        //记录操作
                        logService.saveLog(openid, res);

                        //转换成JSONObject类型
                        JSONObject json = JSON.parseObject(res);

                        return json;
                    } catch (Exception e1) {
                        System.out.println("加锁成功,但是操作出现异常");
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (Exception e) {
                System.out.println("加锁失败!");
                e.printStackTrace();
            }
        }

        return null;
    }

}
