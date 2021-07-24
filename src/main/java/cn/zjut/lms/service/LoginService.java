//package cn.zjut.lms.service;
//
//import cn.zjut.lms.dao.LoginDao;
//import cn.zjut.lms.dao.UserDao;
//import cn.zjut.lms.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Service
//public class LoginService {
//
//    //12小时后失效
//    private final static int EXPIRE = 12;
//    @Autowired
//    LoginDao loginDao;
//
//    public User findByUsername(String username) {
//        return loginDao.findByUsername(username);
//
//    }
//
//    public String createToken(User user) {
//        //用UUID生成token
//        String token = UUID.randomUUID().toString();
//        //当前时间
//        LocalDateTime now = LocalDateTime.now();
//        //过期时间
//        LocalDateTime expireTime = now.plusHours(EXPIRE);
//        //保存到数据库
//        user.setLoginTime(now);
//        user.setExpireTime(expireTime);
//        user.setToken(token);
//        loginDao.save(user);
//        return token;
//    }
//
//
//    public void logout(String token) {
//        User user = loginDao.findByToken(token);
//        //用UUID生成token
//        token = UUID.randomUUID().toString();
//        User.setToken(token);
//        loginDao.save(user);
//
//    }
//
//
//    public User findByToken(String token) {
//        return loginDao.findByToken(token);
//
//    }
//
//}
