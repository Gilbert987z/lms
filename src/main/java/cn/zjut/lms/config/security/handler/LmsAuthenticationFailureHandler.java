package cn.zjut.lms.config.security.handler;

import cn.zjut.lms.util.ResultJson;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 王杨帅
 * 处理登录验证失败
 *
 * @create 2018-05-27 21:55
 * @desc
 **/
@Slf4j
@Component
public class LmsAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("登录验证失败");

        ResultJson result = ResultJson.error().message("登录验证失败");

        response.setContentType("application/json;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.write( JSON.toJSONString(result));

        out.flush();
        out.close();  //解决了登录失败还会执行登录成功的handler问题
    }
}
