package com.cloud.fish.identification.job;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 执行定时任务
 *
 * @author yu
 */
@Component
@Slf4j
public class DeleteFileJob {

    @Resource
    private RedissonClient redissonClient;

    private static final String REDISSON_LOCK = "fish-lock";

    private static final String ALGORITHM_RESULT = "C:\\fish\\鱼类识别算法0530\\detect\\runs\\detect";

    private static final String NGINX_PATH = "C:\\nginx-1.24.0\\html\\static";

    /**
     * 用来判断文件是否删除成功
     */
    private static int flag = 1;


    /**
     * 每个星期天凌晨三点执行，删除算法运行结果下的文件，和超过30天的照片
     */
    @Scheduled(cron = "0 0 1 ? * 7")
    public void deleteFileByTime() {
        //加个锁保险起见
        RLock lock = redissonClient.getLock(REDISSON_LOCK);
        try {
            //尝试加锁,加锁成功的操作
            if (lock.tryLock(100, TimeUnit.SECONDS)) {
                try {
                    //业务功能
                    //删除一个文件夹下的所有文件(包括子目录内的文件)
                    //算法运行结果文件
                    File file1 = new File(ALGORITHM_RESULT);
                    deleteFile(file1);

                    Integer count = moveFileByDate(NGINX_PATH, 30);

                    log.info("共删除30天以前的文件{}个", count);

                    if (flag == 1) {
                        log.info("运行结果文件删除成功！");
                    }

                } catch (Exception e1) {
                    log.info("加锁成功,但是操作出现异常");
                } finally {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            log.info("加锁失败!");
            e.printStackTrace();
        }
    }

    /**
     * 每小时执行，当内存小于等于10G的时候就删除3天前的所有照片
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void deleteFileByMemory() {
        File file = new File(NGINX_PATH);
        file.getUsableSpace();
        // 可用剩余空间
        long usableSpace = file.getUsableSpace() / 1024 / 1024 / 1024;
        if (usableSpace <= 10) {
            Integer count = moveFileByDate(NGINX_PATH, 3);
            log.info("内存过小，删除3天前的文件共{}个", count);
        }
    }


    /**
     * 删除文件夹下的文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            flag = 0;
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

    /**
     * 删除指定目录下n天前的文件
     *
     * @param fromDir 目录
     * @param date    需要删除n天前的时间
     * @return
     */
    public static Integer moveFileByDate(String fromDir, Integer date) {
        File srcDir = new File(fromDir);
        if (!srcDir.exists()) {
            return 0;
        }
        File[] files = srcDir.listFiles();
        if (files == null || files.length <= 0) {
            return 0;
        }
        int l = 0;
        Date today = new Date();
        for (File file : files) {
            if (file.isFile()) {
                try {
                    File ff = file;
                    long time = ff.lastModified();
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(time);
                    Date lastModified = cal.getTime();
                    long days = getDistDates(today, lastModified);
                    if (days >= date) {
                        file.delete();
                        l++;
                    }
                } catch (Exception e) {
                    log.info("删除nginx文件失败");
                }
            }
        }

        return l;
    }

    /**
     * 得到两个时间段时间差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDistDates(Date startDate, Date endDate) {
        long totalDate = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        long timestart = calendar.getTimeInMillis();
        calendar.setTime(endDate);
        long timeend = calendar.getTimeInMillis();
        totalDate = Math.abs((timeend - timestart)) / (1000 * 60 * 60 * 24);
        return totalDate;
    }

}
