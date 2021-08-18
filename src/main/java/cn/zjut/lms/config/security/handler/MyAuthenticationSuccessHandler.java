package cn.zjut.lms.config.security.handler;

import cn.zjut.lms.util.ResultJson;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Galen
 * @Date: 2019/3/28-9:17
 * @Description: 认证成功的处理
 **/
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
//        ResultJson respBean = ResultJson.ok().message("登录成功!", SecurityUserUtil.getCurrentUser());
        ResultJson respBean = ResultJson.ok().message("登录成功!");
        new GalenWebMvcWrite().writeToWeb(response, respBean);
        System.out.println("登录成功!");
    }
}
