package cn.zjut.lms.service;

import cn.zjut.lms.dao.LoginDao;
import cn.zjut.lms.model.AccessToken;
import cn.zjut.lms.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Login1Service {

    //12小时后失效
    private final static int EXPIRE = 12;
    @Autowired
    LoginDao loginDao;


    public SysUser findByUsername(String username) {
        return loginDao.findByUsername(username);

    }
    public AccessToken findByUserId(int userId) {
        try { //数据为空的判断
            return loginDao.findByUserId(userId);
        }catch (Exception e) {
            System.out.println("无数据");

            return new AccessToken();
        }
    }


    public boolean update(AccessToken accessToken) {
        int rows = loginDao.update(accessToken);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增联系方式失败");

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
            throw new RuntimeException("新增联系方式失败");

        }
        return true;
    }

}
