package cn.zjut.lms.security_s.handler;

import cn.zjut.lms.model.AccessToken;
import cn.zjut.lms.util.IpUtil;
import cn.zjut.lms.util.JwtUtil;
import cn.zjut.lms.util.ResultJson;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Hutengfei
 * @Description: 登出成功处理逻辑
 * @Date Create in 2019/9/4 10:17
 */
@Slf4j
@Component
public class LmsLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("登出成功处理逻辑");

        ResultJson result = ResultJson.ok();

        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
