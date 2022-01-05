package cn.zjut.lms.mapper;

import cn.zjut.lms.model.SysRole;
import cn.zjut.lms.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT r.* FROM sys_role r " +
            "LEFT JOIN sys_user_role ur ON ur.role_id = r.id " +
            "WHERE ur.user_id = #{userId}")
    List<SysRole> getRolesByUserId(Long userId);

    //由用户ID查权限的所有ID  DISTINCT：去重
    @Select("SELECT DISTINCT rp.permission_id " +
            "FROM sys_user_role ur " +
            "LEFT JOIN sys_role_permission rp ON ur.role_id = rp.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<Long> getPermissionIds(Long userId);
}
