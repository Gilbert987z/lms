package cn.zjut.lms.dao;

import cn.zjut.lms.model.SysPermission;
import cn.zjut.lms.model.SysRole;
import cn.zjut.lms.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserDao {
    /*
    查所有
    return List<User>
     */
    List<User> list();

    /*
    根据ID查询
    {id} 要查询人员的 id
     */
    User getById(int id);

    /*
    删除
    {id} 要删除人员的 id
     */
//    int delete(int id);
    int delete(User user);

    /*
    更新
    {p} 要更新的User实例
     */
    int update(User user);

    /*
    增加
    {p} 要新增的User实例
     */
    int add(User user);


    int selectCount(); //个数计数，分页使用

    int countUsername(String username); //查重

    int countMobile(String mobile); //查重


    //brac权限
    List<SysPermission> getUserPermissionPath(int userId); //获取用户权限

    List<SysRole> getUserRole(int userId); //获取用户的角色

    /**
     * 查询用户的权限
     * @param userName
     * @return
     */
    @Select(" select permission.* from sys_user user"
            + " inner join sys_user_role user_role on user.id = user_role.user_id "
            + " inner join sys_role_permission role_permission on user_role.role_id = role_permission.role_id "
            + " inner join sys_permission permission on role_permission.permission_id = permission.id"
            + " where user.username = #{userName};")
    List<SysPermission> findPermissionByUsername(@Param("userName") String userName);
}
