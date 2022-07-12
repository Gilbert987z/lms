package cn.zjut.lms.service;


import cn.zjut.lms.entity.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysPermissionService extends IService<SysPermission> {


    List<SysPermission> tree();
}
