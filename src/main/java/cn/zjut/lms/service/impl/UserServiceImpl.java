package cn.zjut.lms.service.impl;


import cn.zjut.lms.mapper.UserMapper;
import cn.zjut.lms.model.SysRole;
import cn.zjut.lms.model.User;
import cn.zjut.lms.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

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
}
