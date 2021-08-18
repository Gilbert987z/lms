package cn.zjut.lms.dao;

import cn.zjut.lms.model.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SysPermissionDao {
    /*
    查所有
     */
    List<SysPermission> list();

    List<SysPermission> listByPage();

    /*
    根据ID查询
     */
    SysPermission getById(int id);

    int selectCount(); //个数计数，分页使用
}
