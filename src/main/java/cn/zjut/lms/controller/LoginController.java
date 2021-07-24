//package cn.zjut.lms.controller;
//
//import cn.zjut.lms.model.User;
//import cn.zjut.lms.service.LoginService;
//import cn.zjut.lms.util.ResultJson;
//import cn.zjut.lms.util.TokenUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import javax.xml.transform.Result;
//
//
///**
// * 登录校验
// */
//@RestController("/")
//public class LoginController  {
//
//    @Autowired
//    private LoginService loginService;
//
//    /**
//     * 登录
//     *
//     * @param loginDTO
//     * @return token登录凭证
//     */
//    @PostMapping("/login")
//    public ResultJson login(@RequestBody User loginDTO) {
//        String username = loginDTO.getUsername();
//        String password = loginDTO.getPassword();
//        //用户信息
//        User user = loginService.findByUsername(username);
//        //账号不存在、密码错误
//        if (user == null || !user.getPassword().equals(password)) {
//            return ResultJson.login_error();
//        } else {
//            //生成token，并保存到数据库
//            String token = loginService.createToken(user);
//            TokenVO tokenVO = new TokenVO();
//            tokenVO.setToken(token);
//            return ResultJson.ok(tokenVO);
//        }
//    }
//
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
//
//}
