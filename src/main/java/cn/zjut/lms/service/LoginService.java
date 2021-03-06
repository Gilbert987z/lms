package cn.zjut.lms.service;

import cn.zjut.lms.dao.LoginDao;
import cn.zjut.lms.model.AccessToken;
import cn.zjut.lms.model.User;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    //12小时后失效
    private final static int EXPIRE = 12;
    @Autowired
    LoginDao loginDao;


    public User findByUsername(String username) {
        return loginDao.findByUsername(username);

    }

    public User findByUserId(Integer userId) {
        return loginDao.findByUserId(userId);
    }
    public AccessToken tokenFindByUserId(Integer userId) {
        try { //数据为空的判断
            return loginDao.tokenfindByUserId(userId);
        }catch (Exception e) {
            System.out.println("无数据");

            return new AccessToken();
        }
    }


    public boolean update(AccessToken accessToken) {
        int rows = loginDao.update(accessToken);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("更新token失败");

        }
        return true;
    }
    public boolean logout(AccessToken accessToken) {
        int rows = loginDao.logout(accessToken);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("登出失败");
        }
        return true;
    }

    public boolean add(AccessToken accessToken) {
        int rows = loginDao.add(accessToken);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增token失败");

        }
        return true;
    }
}
