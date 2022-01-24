package cn.zjut.lms.service.impl;


import cn.zjut.lms.mapper.UserMapper;
import cn.zjut.lms.model.SysPermission;
import cn.zjut.lms.model.SysRole;
import cn.zjut.lms.model.User;
import cn.zjut.lms.service.SysPermissionService;
import cn.zjut.lms.service.SysRoleService;
import cn.zjut.lms.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysPermissionService sysPermissionService;
    @Autowired
    UserMapper userMapper;

    @Override
    public User getByUsername(String username) {
        return getOne(new QueryWrapper<User>().isNull("deleted_at").eq("username", username));
    }

    @Override
    public List<SysRole> getRolesByUserId(long userId) {
        List<SysRole> roles = userMapper.getRolesByUserId(userId);

        return roles;
    }

    @Override
    public List<Long> getPermissionIds(Long userId) {
        List<Long> permissionIds = userMapper.getPermissionIds(userId);
        return permissionIds;
    }

    /**
     * 获取用户权限
     * @param userId
     * @return
     */
    @Override
    public String getUserAuthorityInfo(Long userId) {

        User user = userMapper.selectById(userId);

        //  ROLE_admin,ROLE_normal,sys:user:list,....
        String authority = "";

        //如果redis中有该用户权限的数据
//        if (redisUtil.hasKey("GrantedAuthority:" + sysUser.getUsername())) {
//            authority = (String) redisUtil.get("GrantedAuthority:" + sysUser.getUsername());
//
//        } else {

        if(user.getIsAdmin()==1){//超级管理员
            // 获取角色编码
            //获取所有角色
            List<SysRole> roles = sysRoleService.list();

            if (roles.size() > 0) {
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getName()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }

            // 获取菜单操作编码
            //获取所有权限
            List<SysPermission> permissions = sysPermissionService.list();

            if (permissions.size() > 0) {

                String permisssions = permissions.stream().map(p -> p.getName()).collect(Collectors.joining(","));

                authority = authority.concat(permisssions);
            }
        }else{ //不是超级管理员
            // 获取角色编码
            //获取用户所拥有的角色
            List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
                    .inSql("id", "select role_id from sys_user_role where user_id = " + userId));

            if (roles.size() > 0) {
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getName()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }

            // 获取菜单操作编码
            //获取用户所拥有的权限
            List<Long> permissionIds = userMapper.getPermissionIds(userId);
            if (permissionIds.size() > 0) {

                List<SysPermission> permissions = sysPermissionService.listByIds(permissionIds);
                String permisssions = permissions.stream().map(p -> p.getName()).collect(Collectors.joining(","));

                authority = authority.concat(permisssions);
            }
        }


//            redisUtil.set("GrantedAuthority:" + sysUser.getUsername(), authority, 60 * 60);
//        }

        return authority;
    }
}
