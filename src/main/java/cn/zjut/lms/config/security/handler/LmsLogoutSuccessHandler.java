package cn.zjut.lms.config.security.handler;

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
    RedisUtil redisUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("登出成功处理逻辑");

        String token = httpServletRequest.getHeader(JwtUtil.HEADER_TOKEN);  //获取到token了

        String userId = JwtUtil.getClaimsFromToken(token, "userId");  //从token获取userId

        redisUtil.del(RedisUtil.USER_TOKEN + userId);  //登出删除缓存

        ResultJson result = ResultJson.ok().message("登出成功");

        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
