package cn.zjut.lms.config;

import cn.zjut.lms.service.BookBorrowLogService;
import cn.zjut.lms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @author zhangzhe
 * @create 2022-07-12
 * @Description
 */
@Slf4j
@Configuration    // 1. 代表当前类是一个配置类
@EnableScheduling // 2.开启定时任务
public class ScheduleTask {

    @Autowired
    BookBorrowLogService bookBorrowLogService;

    //3.添加定时任务
    /**
     * 每天凌晨执行一次
     */
    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTask() {
        System.out.println("执行静态定时任务时间" + LocalDateTime.now());

        boolean isSuccess = bookBorrowLogService.task();
        if (isSuccess){
            log.info("定时任务执行成功");
        }else{
            log.warn("定时任务失败");
        }

    }
}
