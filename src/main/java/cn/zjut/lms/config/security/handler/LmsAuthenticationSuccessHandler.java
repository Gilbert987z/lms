package cn.zjut.lms.config.security.handler;

import cn.zjut.lms.common.dto.AccessToken;
import cn.zjut.lms.common.exception.CaptchaException;
import cn.zjut.lms.entity.User;
import cn.zjut.lms.service.UserService;
import cn.zjut.lms.util.IpUtil;
import cn.zjut.lms.util.JwtUtil;
import cn.zjut.lms.util.RedisUtil;
import cn.zjut.lms.util.ResultJson;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 王杨帅
 * 处理登录验证成功
 * @create 2018-05-27 21:48
 * @desc
 **/
@Slf4j
@Component
public class LmsAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    UserService userService;

    @Autowired
    LmsAuthenticationFailure2Handler lmsAuthenticationFailure2andler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登录验证成功");


        String username = authentication.getName();
        User user = userService.getByUsername(username);


        if (user.getStatus() == 0) { //被封禁
            // 交给认证失败处理器
            lmsAuthenticationFailure2andler.onAuthenticationFailure(request, response);
        }

        //生成jwt
        String token = JwtUtil.generateToken(user, JwtUtil.EXPIRE_TIME);
        //生成refresh_token
        String refreshToken = JwtUtil.generateToken(user, JwtUtil.EXPIRE_TIME + JwtUtil.REFEASH_TIME_PLUS);

        long userId = user.getId();//获取到userId
        //获取IP地址
        String ipAddress = IpUtil.getIpAddr(request);

        AccessToken accessToken = new AccessToken();
        accessToken.setUserId(userId); //写入userid
        accessToken.setAccessToken(token); //写入token
        accessToken.setLoginIp(ipAddress); //写入IP地址
        accessToken.setLoginTime(LocalDateTime.now()); //写入登录时间

        //生成一个token对象,保存在redis中
        redisUtil.set(RedisUtil.USER_TOKEN + userId, accessToken, JwtUtil.EXPIRE_TIME);//毫秒
        //refresh_token
        redisUtil.set(RedisUtil.USER_TOKEN + userId, accessToken, JwtUtil.EXPIRE_TIME + JwtUtil.REFEASH_TIME_PLUS);//毫秒

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("token", token);
        map.put("refreshToken", refreshToken);

        ResultJson result = ResultJson.ok().data(map).message("登录成功");

        response.setContentType("application/json;charset=UTF-8"); // 响应类型
        PrintWriter out = response.getWriter();
        out.write(JSON.toJSONString(result));

        out.flush();
        out.close();
    }
}
