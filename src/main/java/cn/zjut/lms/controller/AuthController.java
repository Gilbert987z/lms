package cn.zjut.lms.controller;

import cn.zjut.lms.util.ResultJson;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class AuthController {
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

//        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);

        Map<String, Object> map = new HashMap<>();
        map.put("token",key);
        map.put("captchaImg",base64Img);
        return ResultJson.ok().data(map);
    }
}
