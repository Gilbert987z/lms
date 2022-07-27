package cn.zjut.lms.controller;

import cn.hutool.core.map.MapUtil;
import cn.zjut.lms.common.Const;
import cn.zjut.lms.controller.BaseController;
import cn.zjut.lms.entity.User;
import cn.zjut.lms.util.JwtUtil;
import cn.zjut.lms.util.RedisUtil;
import cn.zjut.lms.util.ResultCode;
import cn.zjut.lms.util.ResultJson;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class AuthController extends BaseController {

    @Autowired
    Producer producer;

    @GetMapping("/captcha")
    public ResultJson captcha() throws IOException {

        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        // 为了测试
//        key = "aaaaa";
//        code = "11111";
        System.out.println(key);
        System.out.println(code);

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(outputStream.toByteArray());

        //存储key和code到redis中；  todo 验证码时间120秒？？？
        boolean flag = redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120000); //时间是毫秒

		//传输base64验证码图片和token
        Map<String, Object> map = new HashMap<>();
        map.put("token", key);
        map.put("captchaImg", base64Img);

        return ResultJson.ok().data(map);
    }

    @GetMapping("/api-token-refresh")
    public ResultJson refresh_token(Principal principal, HttpServletResponse response){
        log.info("api-token-refresh");

//        try{
//            log.info("api-token-refresh   401");

            User user = userService.getByUsername(principal.getName());

            //生成jwt
            String token = JwtUtil.generateToken(user, JwtUtil.EXPIRE_TIME);
            //refresh_token
            String refreshToken = JwtUtil.generateToken(user, JwtUtil.EXPIRE_TIME + JwtUtil.REFEASH_TIME_PLUS);

            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("refreshToken", refreshToken);
            return ResultJson.ok().data(map);
//        }catch (Exception e){
//            log.info("api-token-refresh   418");
//
//            response.setStatus(ResultCode.REFRESH_TOKEN_EXPIRED); //设置响应码
//            return ResultJson.error().code(ResultCode.REFRESH_TOKEN_EXPIRED).message("token失效，请重新登录！");
//        }

//        String refreshTokenBefore = (String)params.get("refreshToken"); //前端请求的refresh_token
//
//        //有效
//        if(JwtUtil.validateToken(refreshTokenBefore, user)){
//            //生成jwt
//            String token = JwtUtil.generateToken(user,JwtUtil.EXPIRE_TIME);
//            //refresh_token
//            String refreshToken = JwtUtil.generateToken(user,JwtUtil.EXPIRE_TIME+JwtUtil.REFEASH_TIME_PLUS);
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("token", token);
//            map.put("refreshToken", refreshToken);
//            return ResultJson.ok().data(map);
//        }else{
//            return  ResultJson.error().code(ResultCode.Unauthorized).message("用户无权访问，需重新登录");
//        }
    }
}
