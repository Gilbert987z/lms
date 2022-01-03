package cn.zjut.lms.dao;

import cn.zjut.lms.model.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SysRoleDao {
    /*
    查所有
     */
    List<SysRole> list();
    List<SysRole> listByPage();


    /**
     * 根据userId查出角色
     * @param userId
     * @return
     */
    List<SysRole> getRolesByUserId(int userId);

    /*
    根据ID查询
    {id} 要查询人员的 id
     */
    SysRole getById(int id);

    /*
    删除
    {id} 要删除人员的 id
     */
    int delete(SysRole role);

    /*
    更新
    {p} 要更新的User实例
     */
    int update(SysRole role);

    /*
    增加
    {p} 要新增的User实例
     */
    int add(SysRole role);


    int selectCount(); //个数计数，分页使用

    int countNumber(String roleName);  //查重
}
