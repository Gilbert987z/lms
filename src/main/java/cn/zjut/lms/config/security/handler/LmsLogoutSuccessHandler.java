package cn.zjut.lms.config.security.handler;

import cn.zjut.lms.model.AccessToken;
import cn.zjut.lms.model.User;
import cn.zjut.lms.service.LoginService;
import cn.zjut.lms.util.JwtUtil;
import cn.zjut.lms.util.RedisUtil;
import cn.zjut.lms.util.ResultJson;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

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

    @Autowired
    LoginService loginService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("登出成功处理逻辑");


        String token = httpServletRequest.getHeader("token");  //获取到token了
        log.info("token的值："+token);

        AccessToken accessToken = new AccessToken();

        //修改时间
        java.util.Date date = new java.util.Date();
        java.sql.Date currentTime = new java.sql.Date(date.getTime());

        accessToken.setAccessToken(token);
        accessToken.setUpdatedAt(currentTime);

        loginService.logout(accessToken);


        String userId = JwtUtil.getClaimsFromToken(token,"userId");  //从token获取userId
        String redis_token = RedisUtil.USER_TOKEN + userId;
        log.info(redis_token.getClass().toString());
        log.info(RedisUtil.USER_TOKEN + userId);

        redisUtil.del(RedisUtil.USER_TOKEN + userId);  //登出删除缓存

        ResultJson result = ResultJson.ok().message("登出成功");

        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
