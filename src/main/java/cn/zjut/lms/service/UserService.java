package cn.zjut.lms.service;


import cn.zjut.lms.entity.SysRole;
import cn.zjut.lms.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {
    User getByUsername(String username);

    List<SysRole> getRolesByUserId(long userId);

    List<Long> getPermissionIds(Long userId);

    //获取用户权限
    String getUserAuthorityInfo(Long userId);


}
