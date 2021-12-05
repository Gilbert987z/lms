package cn.zjut.lms.config.security.handler;

import cn.zjut.lms.util.ResultCode;
import cn.zjut.lms.util.ResultJson;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 认证失败处理类
 *
 * 当匿名请求需要登录的接口时,拦截处理
 * 用来解决匿名用户访问无权限资源时的异常
 */
@Slf4j
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("用户无权访问，需重新登录");
        ResultJson result = ResultJson.error().code(ResultCode.Unauthorized).message("用户无权访问，需重新登录");


        response.setContentType("text/json;charset=utf-8"); //返回乱码问题 https://blog.csdn.net/u014424628/article/details/50589966
        response.getWriter().write(JSON.toJSONString(result));
    }
}
