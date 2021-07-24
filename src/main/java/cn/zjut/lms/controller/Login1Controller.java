package cn.zjut.lms.controller;

import cn.zjut.lms.model.AccessToken;
import cn.zjut.lms.model.User;
import cn.zjut.lms.service.Login1Service;
//import cn.zjut.lms.service.LoginService;
import cn.zjut.lms.util.IpUtil;
import cn.zjut.lms.util.JwtUtil;
import cn.zjut.lms.util.ResultJson;
//import cn.zjut.lms.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;


/**
 * 登录校验
 */
@RestController
@RequestMapping("/")
public class Login1Controller {

    @Autowired
    private Login1Service login1Service;
//    @Autowired
//    private JwtUtil loginService;

    /**
     * 登录
     *
     * @param loginDTO
     * @return token登录凭证
     */
    @PostMapping("/login")
    public ResultJson login(@RequestBody User loginDTO,HttpServletRequest request) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        //用户信息
        User user = login1Service.findByUsername(username); //1.先用用户名查询用户信息
        //账号不存在、密码错误
        if (user == null){
            return ResultJson.error().message("无此账号");
        }else{
            if (!user.getPassword().equals(password)){
                return ResultJson.error().message("密码错误");
            }else{

                int userId = user.getId();//获取到userId
                //生成token，并保存到数据库
                String token = JwtUtil.createToken(username);
                //获取IP地址
                String ipAddress = IpUtil.getIpAddr(request);
                //登录时间
                java.util.Date date=new java.util.Date();
                java.sql.Date currentTime=new java.sql.Date(date.getTime());

                AccessToken accessToken = new AccessToken();
                accessToken.setUserId(userId); //写入userid
                accessToken.setAccessToken(token); //写入token
                accessToken.setLoginIp(ipAddress); //写入IP地址
                accessToken.setLoginTime(currentTime); //写入登录时间


                AccessToken accessToken_get= login1Service.findByUserId(userId);
                System.out.println(accessToken_get);
                try {   //空指针的判断，accessToken_get可能为null
                    int accessToken_id = accessToken_get.getId();
                    accessToken.setId(accessToken_id); //写入access_token的id
                    accessToken.setUpdatedAt(currentTime); //写入修改时间
                    login1Service.update(accessToken); //修改数据
                } catch(Exception e){
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


                return ResultJson.ok().data("accessToken",token);
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

    @GetMapping("/checkToken")
    public Boolean checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        return JwtUtil.checkToken(token);
    }

//    /**
//     * 登出
//     *
//     * @param
//     * @return
//     */
//    @PostMapping("/logout")
//    public ResultJson logout(HttpServletRequest request) {
//        //从request中取出token
//        String token = TokenUtil.getRequestToken(request);
//        loginService.logout(token);
//        return ResultJson.ok();
//    }
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
