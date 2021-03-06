package cn.zjut.lms.controller;

import cn.zjut.lms.model.AccessToken;
import cn.zjut.lms.model.User;
import cn.zjut.lms.service.LoginService;
import cn.zjut.lms.service.UserService;
import cn.zjut.lms.util.IpUtil;
import cn.zjut.lms.util.JwtUtil;
import cn.zjut.lms.util.RedisUtil;
import cn.zjut.lms.util.ResultJson;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;



/**
 * 登录校验
 */
@Slf4j
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 登录
     *
     * @param loginDTO
     * @return token登录凭证
     */
    @PostMapping("/login")
    public ResultJson login(@Valid @RequestBody User loginDTO, HttpServletRequest request, BindingResult bindingResult) {
        System.out.println("数据校验");

        //todo 需要修改，改成 message[报错1，报错2，报错3]
        if (bindingResult.hasErrors()) { //获取校验失败情况下的反馈信息
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        }

        String username = loginDTO.getUsername(); //用户输入的用户名
        String password = loginDTO.getPassword();
        //用户信息
        User user = loginService.findByUsername(username); //1.先用用户名查询用户信息
        //账号不存在、密码错误
        if (user == null) {
            return ResultJson.error().message("无此账号");
        } else {
            if (!user.getPassword().equals(password)) {
                return ResultJson.error().message("密码错误");
            } else {

                int userId = user.getId();//获取到userId
                //生成token，并保存到数据库
//                String token = JwtUtil.generateToken(username);
                String token = JwtUtil.generateToken(user);
                //获取IP地址
                String ipAddress = IpUtil.getIpAddr(request);
                //登录时间
                java.util.Date date = new java.util.Date();
                java.sql.Date currentTime = new java.sql.Date(date.getTime());

                AccessToken accessToken = new AccessToken();
                accessToken.setUserId(userId); //写入userid
                accessToken.setAccessToken(token); //写入token
                accessToken.setLoginIp(ipAddress); //写入IP地址
                accessToken.setLoginTime(currentTime); //写入登录时间


                AccessToken accessToken_get = loginService.tokenFindByUserId(userId);
                System.out.println(accessToken_get);
                try {   //空指针的判断，accessToken_get可能为null
                    int accessToken_id = accessToken_get.getId();
                    accessToken.setId(accessToken_id); //写入access_token的id
                    accessToken.setUpdatedAt(currentTime); //写入修改时间
                    loginService.update(accessToken); //修改数据
                } catch (Exception e) {
                    accessToken.setCreatedAt(currentTime); //写入创建时间
                    loginService.add(accessToken); //添加数据
                }

                return ResultJson.ok().data("accessToken", token);
            }
        }


    }

//    @GetMapping(value = "/info")
//    public ResultJson info(String token){
//        User user =
//        return ResultJson.ok().data(user);
//    }

    /**
     * 注册
     *
     * @param user
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "register", consumes = "application/json")
    public ResultJson add(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //数据校验
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        }

        String username = user.getUsername();
        String mobile = user.getMobile();

        int countUsername = userService.countUsername(username);
        int countMobile = userService.countMobile(mobile);

        if (countUsername > 0) { //查重校验
            return ResultJson.error().message("用户名已被占用");
        } else if (countMobile > 0) {
            return ResultJson.error().message("电话号码已被占用");
        } else {
            //密码加密
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);

            //todo 默认头像
//            user.setAvatar(Const.DEFULT_AVATAR);

            boolean result = userService.add(user);
            if (result) {
                return ResultJson.ok().message("注册成功");
            } else {
                return ResultJson.error().message("数据不存在");
            }
        }
    }


    @GetMapping("/checkToken")
    public Boolean checkToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        return JwtUtil.checkToken(token);
    }

    /**
     * 登出
     *
     * @param
     * @return
     */
    @GetMapping("/logout")
    public ResultJson logout(@RequestHeader("token") String token) {
        log.info("登出");

        //从request中取出token
//        String token = TokenUtil.getRequestToken(request);


        AccessToken accessToken = new AccessToken();

        //修改时间
        java.util.Date date = new java.util.Date();
        java.sql.Date currentTime = new java.sql.Date(date.getTime());

        accessToken.setAccessToken(token);
        accessToken.setUpdatedAt(currentTime);

        loginService.logout(accessToken);

        RedisUtil redisUtil = new RedisUtil();
        Integer userId = accessToken.getUserId();
        redisUtil.del(RedisUtil.USER_TOKEN + userId);  //登出删除缓存


        return ResultJson.ok().message("登出成功");
    }

    /**
     * 获取用户信息接口
     *
     * @param principal
     * @return
     */
    @GetMapping("/info")
    public ResultJson userInfo(Principal principal){
//        System.out.println(Integer.parseInt(principal.getName()));

//        User user = loginService.findByUsername(principal.getName());
        User user = loginService.findByUserId(Integer.parseInt(principal.getName()));
//        System.out.println(user);

//        Map<String, Object> map = new HashMap<>();
//        map.put("id", user.getId());
//        map.put("username", user.getUsername());
//        map.put("mobile", user.getMobile());
//        map.put("image", user.getImages());
//        map.put("desc", user.getDesc());
//        map.put("createdAt", user.getCreatedAt());

        return ResultJson.ok().data("detail", user);

    }

}
