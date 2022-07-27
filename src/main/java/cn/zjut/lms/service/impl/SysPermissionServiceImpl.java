package cn.zjut.lms.service.impl;


import cn.hutool.json.JSONUtil;
import cn.zjut.lms.mapper.SysPermissionMapper;
import cn.zjut.lms.entity.SysPermission;
import cn.zjut.lms.service.SysPermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
    @Override
    public List<SysPermission> tree() {
        // 获取所有菜单信息
        List<SysPermission> SysPermissions = this.list(new QueryWrapper<SysPermission>().orderByAsc("orderNum").isNull("deleted_at"));

        // 转成树状结构
        return buildTreeMenu(SysPermissions);
    }

    private List<SysPermission> buildTreeMenu(List<SysPermission> menus) {

        List<SysPermission> finalMenus = new ArrayList<>();

        // 先各自寻找到各自的孩子
        for (SysPermission menu : menus) {

            for (SysPermission e : menus) {
                if (menu.getId() == e.getParentId()) {
                    menu.getChildren().add(e);
                }
            }

            // 提取出父节点
            if (menu.getParentId() == 0L) {
                finalMenus.add(menu);
            }
        }

        System.out.println(JSONUtil.toJsonStr(finalMenus));
        return finalMenus;
    }
}
