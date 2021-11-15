package cn.zjut.lms.service;

import cn.zjut.lms.dao.UserDao;
import cn.zjut.lms.model.User;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    /*
        Service层介于controller和dao之间作为服务层进行一些逻辑处理，
        这里逻辑太简单所以知识单纯调用dao所以不做注释
     */
    public Map<String, Object> list(int page, int size) {
        Map<String, Object> resultMap = new HashMap<>();
        // 使用PageHelper插件，实现分页逻辑。
        PageHelper.startPage(page, size);
        List<User> userList = userDao.list();


        // 查询总计数据行数，计算总计页码
        int totalSize = userDao.selectCount();
        int totalPages = totalSize % size == 0 ? totalSize / size : (totalSize / size + 1);

        // 当前页面显示的内容
        resultMap.put("list", userList);
        // 当前页码
        resultMap.put("currentPage", page);
        //每页条数
        resultMap.put("pageSize", size);
        // 总计页码
        resultMap.put("totalPages", totalPages);
        //总条数
        resultMap.put("totalSize", totalSize);


        return resultMap;
    }

    public User getById(int id) {
        return userDao.getById(id);
    }

    public boolean delete(User user) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        user.setUpdatedAt(currentTime);
        user.setDeletedAt(currentTime);

        int flag = userDao.delete(user);
        if (flag != 1) {
            // 删除失败，回滚事务
            throw new RuntimeException("删除失败");

        }
        return true;
    }

    public boolean update(User user) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        user.setUpdatedAt(currentTime);

        int rows = userDao.update(user);
        if (rows != 1) {
            // 修改失败，回滚事务
            throw new RuntimeException("修改失败");

        }
        return true;
    }

    public boolean add(User user) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        user.setCreatedAt(currentTime);
        user.setUpdatedAt(currentTime);

        int rows = userDao.add(user);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增失败");
        }
        return true;
    }

    public int countUsername(String username){
        return userDao.countUsername(username);
    }
    public int countMobile(String mobile){
        return userDao.countMobile(mobile);
    }
}
