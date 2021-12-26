package cn.zjut.lms.service;

import cn.zjut.lms.dao.SysPermissionDao;
import cn.zjut.lms.model.SysPermission;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Service层介于controller和dao之间作为服务层进行一些逻辑处理，
    这里逻辑太简单所以知识单纯调用dao所以不做注释
 */
@Service
public class SysPermissionService {
    @Autowired
    SysPermissionDao sysPermissionDao;


    public List<SysMenuDto> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getByUsername(username);

        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        List<SysMenu> menus = this.listByIds(menuIds);

        // 转树状结构
        List<SysMenu> menuTree = buildTreeMenu(menus);

        // 实体转DTO
        return convert(menuTree);
    }

    public List<SysMenu> tree() {
        // 获取所有菜单信息
        List<SysMenu> sysMenus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));

        // 转成树状结构
        return buildTreeMenu(sysMenus);
    }

    private List<SysMenuDto> convert(List<SysMenu> menuTree) {
        List<SysMenuDto> menuDtos = new ArrayList<>();

        menuTree.forEach(m -> {
            SysMenuDto dto = new SysMenuDto();

            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());

            if (m.getChildren().size() > 0) {

                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }

            menuDtos.add(dto);
        });

        return menuDtos;
    }

    private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {

        List<SysMenu> finalMenus = new ArrayList<>();

        // 先各自寻找到各自的孩子
        for (SysMenu menu : menus) {

            for (SysMenu e : menus) {
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







    public Map<String, Object> list() {
        Map<String, Object> resultMap = new HashMap<>();

        List<SysPermission> list = sysPermissionDao.list();

        // 当前页面显示的内容
        resultMap.put("list", list);

        return resultMap;
    }

    public Map<String, Object> listByPage(int page, int size) {
        Map<String, Object> resultMap = new HashMap<>();
        // 使用PageHelper插件，实现分页逻辑。
        PageHelper.startPage(page, size);
        List<SysPermission> list = sysPermissionDao.listByPage();


        // 查询总计数据行数，计算总计页码
        int totalSize = sysPermissionDao.selectCount();
        int totalPages = totalSize % size == 0 ? totalSize / size : (totalSize / size + 1);

        // 当前页面显示的内容
        resultMap.put("list", list);
        // 当前页码
        resultMap.put("currentPage", page);
        //每页条数
        resultMap.put("pageSize", size);
        // 总计页码
        resultMap.put("totalPages", totalPages);
        //总条数
        resultMap.put("totalSize", totalSize);


        return resultMap;
    }

    public SysPermission getById(int id) {
        return sysPermissionDao.getById(id);
    }


}
