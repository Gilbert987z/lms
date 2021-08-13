package cn.zjut.lms.controller;

import cn.zjut.lms.model.AccessToken;
import cn.zjut.lms.model.SysUser;
import cn.zjut.lms.service.Login1Service;
//import cn.zjut.lms.service.LoginService;
import cn.zjut.lms.service.UserService;
import cn.zjut.lms.util.IpUtil;
import cn.zjut.lms.util.JwtUtil;
import cn.zjut.lms.util.ResultJson;
//import cn.zjut.lms.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


/**
 * 登录校验
 */
@RestController
@RequestMapping("/")
public class Login1Controller {

    @Autowired
    private Login1Service login1Service;
    @Autowired
    private UserService userService;
//    @Autowired
//    private JwtUtil loginService;

    /**
     * 登录
     *
     * @param loginDTO
     * @return token登录凭证
     */
    @PostMapping("/login")
    public ResultJson login(@Valid @RequestBody SysUser loginDTO, HttpServletRequest request, BindingResult bindingResult) {
        System.out.println("数据校验");

        //todo 需要修改，改成 message[报错1，报错2，报错3]
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        }

        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        //用户信息
        SysUser user = login1Service.findByUsername(username); //1.先用用户名查询用户信息
        //账号不存在、密码错误
        if (user == null) {
            return ResultJson.error().message("无此账号");
        } else {
            if (!user.getPassword().equals(password)) {
                return ResultJson.error().message("密码错误");
            } else {

                int userId = user.getId();//获取到userId
                //生成token，并保存到数据库
                String token = JwtUtil.createToken(username);
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


                AccessToken accessToken_get = login1Service.findByUserId(userId);
                System.out.println(accessToken_get);
                try {   //空指针的判断，accessToken_get可能为null
                    int accessToken_id = accessToken_get.getId();
                    accessToken.setId(accessToken_id); //写入access_token的id
                    accessToken.setUpdatedAt(currentTime); //写入修改时间
                    login1Service.update(accessToken); //修改数据
                } catch (Exception e) {
                    accessToken.setCreatedAt(currentTime); //写入创建时间
                    login1Service.add(accessToken); //添加数据
                }

//                if(accessToken_id != 0){ //如果有数据   据说int类型默认为0
//                    accessToken.setId(accessToken_id); //写入access_token的id
//                    accessToken.setUpdatedAt(currentTime); //写入修改时间
//                    login1Service.update(accessToken); //修改数据
//
//                    System.out.println();
//                }else{ //如果没有数据
//                    accessToken.setCreatedAt(currentTime); //写入创建时间
//                    login1Service.add(accessToken); //添加数据
//                }


                return ResultJson.ok().data("accessToken", token);
            }
        }


//        if (user == null || !user.getPassword().equals(password)) { //2.如果用户不存在，或者密码错误
//            return ResultJson.login_error();
//        } else {
//            //生成token，并保存到数据库
//            String token = JwtUtil.createToken(username);
//            //todo
//            user.setToken();
//            return ResultJson.ok().data("accessToken",token);
//        }
    }

    //注册
    @PostMapping(value = "register", consumes = "application/json")
    public ResultJson add(@Valid @RequestBody SysUser user, BindingResult bindingResult) {
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
            boolean result = userService.add(user);
            if (result) {
                return ResultJson.ok().message("增加成功");
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
        //从request中取出token
//        String token = TokenUtil.getRequestToken(request);



        AccessToken accessToken = new AccessToken();

        //修改时间
        java.util.Date date = new java.util.Date();
        java.sql.Date currentTime = new java.sql.Date(date.getTime());

        accessToken.setAccessToken(token);
        accessToken.setUpdatedAt(currentTime);

        login1Service.logout(accessToken);
        return ResultJson.ok().message("登出成功");
    }
//
//    /**
//     * 测试
//     *
//     * @param
//     * @return
//     */
//    @PostMapping("/test")
//    public ResultJson test() {
//
//        return ResultJson.ok("恭喜你，验证成功啦，我可以返回数据给你");
//    }

}
