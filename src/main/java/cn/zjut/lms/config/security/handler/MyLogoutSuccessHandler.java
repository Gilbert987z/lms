package cn.zjut.lms.config.security.handler;

import cn.zjut.lms.util.ResultJson;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Galen
 * @Date: 2019/3/28-9:21
 * @Description: 注销登录处理
 **/
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        ResultJson respBean = ResultJson.ok().message("注销成功!");
//        new com.galen.security.interceptor.handler.GalenWebMvcWrite().writeToWeb(response, respBean);
        System.out.println("注销成功!");
    }
}
