package cn.zjut.lms.service;

import cn.zjut.lms.dao.SysPermissionDao;
import cn.zjut.lms.model.SysPermission;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysPermissionService {
    @Autowired
    SysPermissionDao sysPermissionDao;

    /*
        Service层介于controller和dao之间作为服务层进行一些逻辑处理，
        这里逻辑太简单所以知识单纯调用dao所以不做注释
     */
    public Map<String, Object> list() {
        Map<String, Object> resultMap = new HashMap<>();

        List<SysPermission> list = sysPermissionDao.list();

        // 当前页面显示的内容
        resultMap.put("list", list);

        return resultMap;
    }

    public Map<String, Object> listByPage(int page, int size) {
        Map<String, Object> resultMap = new HashMap<>();
        // 使用PageHelper插件，实现分页逻辑。
        PageHelper.startPage(page, size);
        List<SysPermission> list = sysPermissionDao.listByPage();


        // 查询总计数据行数，计算总计页码
        int totalSize = sysPermissionDao.selectCount();
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

    public SysPermission getById(int id) {
        return sysPermissionDao.getById(id);
    }


}
