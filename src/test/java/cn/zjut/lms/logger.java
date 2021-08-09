package cn.zjut.lms;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.mybatis.logging.LoggerFactory;

//import java.util.logging.Logger;

public class logger {

    /**
     * LoggerFactory:记录器工厂
     * logger:日志记录器
     */
    Logger logger = LoggerFactory.getLogger(getClass());//获取当前类的日志
    @Test
    void contextLoads() {
        //跟踪运行信息
        logger.trace("这是 trace 日志信息！");
        //调试信息
        logger.debug("这是 debug 日志信息！");
        //自定义信息
        logger.info("这是 info 日志信息");
        //警告信息：如果运行结果是不预期的值，则可以进行警告
        logger.warn("这是 warn 日志信息");
        //错误信息：出现异常捕获时
        logger.error("这是 error 日志信息");

    }

}
