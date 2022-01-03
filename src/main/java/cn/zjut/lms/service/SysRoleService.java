package cn.zjut.lms.service;

import cn.zjut.lms.dao.SysRoleDao;
import cn.zjut.lms.dao.SysRoleDao;
import cn.zjut.lms.model.SysRole;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleService {
    @Autowired
    SysRoleDao sysRoleDao;

    /*
        Service层介于controller和dao之间作为服务层进行一些逻辑处理，
        这里逻辑太简单所以知识单纯调用dao所以不做注释
     */
    public Map<String, Object> list() {
        Map<String, Object> resultMap = new HashMap<>();

        List<SysRole> list = sysRoleDao.list();

        // 当前页面显示的内容
        resultMap.put("list", list);

        return resultMap;
    }

    public Map<String, Object> listByPage(int page, int size) {
        Map<String, Object> resultMap = new HashMap<>();
        // 使用PageHelper插件，实现分页逻辑。
        PageHelper.startPage(page, size);
        List<SysRole> list = sysRoleDao.listByPage();


        // 查询总计数据行数，计算总计页码
        int totalSize = sysRoleDao.selectCount();
        int totalPages = totalSize % size == 0 ? totalSize / size : (totalSize / size + 1);

        // 当前页面显示的内容
        resultMap.put("list", list);
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

    public SysRole getById(int id) {
        return sysRoleDao.getById(id);
    }

    /**
     * 根据userId查询出所有role角色
     * @param userId
     * @return
     */
//    public List<SysRole> listRolesByUserId(Long userId) {
//
//        List<SysRole> sysRoles = this.list(new QueryWrapper<SysRole>()
//                .inSql("id", "select role_id from sys_user_role where user_id = " + userId));
//
//        return sysRoles;
//    }

    public boolean delete(SysRole SysRole) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        SysRole.setUpdatedAt(currentTime);
        SysRole.setDeletedAt(currentTime);

        int flag = sysRoleDao.delete(SysRole);
        if (flag != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增联系方式失败");

        }
        return true;
    }

    public boolean update(SysRole SysRole) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        SysRole.setUpdatedAt(currentTime);

        int rows = sysRoleDao.update(SysRole);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增联系方式失败");

        }
        return true;
    }

    public boolean add(SysRole SysRole) {
        java.util.Date date=new java.util.Date();
        java.sql.Date currentTime=new java.sql.Date(date.getTime());
        SysRole.setCreatedAt(currentTime);
        SysRole.setUpdatedAt(currentTime);

        int rows = sysRoleDao.add(SysRole);
        if (rows != 1) {
            // 新增失败，回滚事务
            throw new RuntimeException("新增联系方式失败");

        }
        return true;
    }

    public int countNumber(String roleName){
        return sysRoleDao.countNumber(roleName);
    }
}
