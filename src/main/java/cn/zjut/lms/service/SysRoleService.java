package cn.zjut.lms.service;


import cn.zjut.lms.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysRoleService extends IService<SysRole> {
    //获取用户的角色
    List<SysRole> listRolesByUserId(Long userId);
}
