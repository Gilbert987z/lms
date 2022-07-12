package cn.zjut.lms.config.security.handler;

import cn.zjut.lms.util.ResultCode;
import cn.zjut.lms.util.ResultJson;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义无权访问处理类
 *
 * 当登录后,访问接口没有权限的时候,该处理类的方法被调用
 * 用来解决认证过的用户访问无权限资源时的异常
 *
 * 认证过的用户
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.info("权限不足");
//        ResultJson result = ResultJson.error().message("权限不足");

        ResultJson result = ResultJson.error().code(ResultCode.Forbidden).message("权限不足");

        response.setContentType("text/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); //设置响应码
        response.getWriter().write(JSON.toJSONString(result));

//        ResultJson result = ResultJson.error_unauthorized();
//
//        response.setContentType("text/json;charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN); //设置响应码
//        response.getWriter().write(JSON.toJSONString(result));
    }
}
