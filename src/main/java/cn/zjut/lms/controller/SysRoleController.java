package cn.zjut.lms.controller;

import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.entity.SysRole;
import cn.zjut.lms.entity.SysRolePermission;
import cn.zjut.lms.entity.SysUserRole;
import cn.zjut.lms.util.ResultJson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/sys/role")
public class SysRoleController extends BaseController {
//    @Autowired
//    SysRoleMapper sysRoleMapper;
    @PreAuthorize("hasAuthority('sys.role.info')")
    @GetMapping("info")
    public ResultJson info(@RequestParam(value = "id") Long id) {

        SysRole sysRole = sysRoleService.getById(id);


        QueryWrapper<SysRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", id);
//        queryWrapper.eq("status", 1); //展示正常的权限
        // 获取角色相关联的菜单id
        List<SysRolePermission> rolePermissions = sysRolePermissionService.list(queryWrapper);
        List<Long> permissionIds = rolePermissions.stream().map(p -> p.getPermissionId()).collect(Collectors.toList());

        sysRole.setPermissionIds(permissionIds);
        return ResultJson.ok().data(sysRole);
    }

    /**
     * 分页列表
     *
     * @return
     */
    @PreAuthorize("hasAuthority('sys.role.list')")  //会匹配登录时authentication中是否有权限
    @GetMapping("list")
    public ResultJson list(@RequestParam(value = "name", defaultValue = "") String name,
                           @RequestParam(value = "type", defaultValue = "") String type) {
        if(type.equals("tree")){
            List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>().isNull("deleted_at")
                    .orderBy(true, true, "created_at")); //按时间正排
            return ResultJson.ok().data(roles);
        }else {
            //分页查询
//        Page<SysRole> roles = sysRoleService.page(new Page<>(page, size), new QueryWrapper<SysRole>().isNull("deleted_at")
//                .like(StrUtil.isNotBlank(name), "name", name));
            Page<SysRole> roles = sysRoleService.page(getPage(), new QueryWrapper<SysRole>().isNull("deleted_at")
                    .like(StrUtil.isNotBlank(name), "name", name).orderBy(true, true, "created_at")); //按时间正排
            return ResultJson.ok().data(roles);
        }
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @PreAuthorize("hasAuthority('sys.role.save')")
    @PostMapping("save")
    public ResultJson save(@Validated @RequestBody SysRole sysrole, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //数据校验
//            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        long count = sysRoleService.count(new QueryWrapper<SysRole>().isNull("deleted_at").eq("name", sysrole.getName()));
        if (count > 0) {
            return ResultJson.error().message("角色名称已被占用");
        }

        //设置当前的创建时间
        sysrole.setCreatedAt(LocalDateTime.now());
        sysrole.setUpdatedAt(LocalDateTime.now());

        sysRoleService.save(sysrole);
//        long id = sysRoleMapper.insert(sysrole); // 获取自增id
//        System.out.println(id);
//        SysRole insertRole =sysRoleMapper.selectById(id);
        return ResultJson.ok().data(sysrole);
    }

    @PreAuthorize("hasAuthority('sys.role.update')")
    @PostMapping("update")
    public ResultJson update(@Validated @RequestBody SysRole sysRole, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //数据校验
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        long count = sysRoleService.count(new QueryWrapper<SysRole>().ne("id",sysRole.getId()).eq("name", sysRole.getName()).isNull("deleted_at")); //获取除了自己以外的
        if (count > 0) {
//            String name = sysRoleService.getOne(new QueryWrapper<SysRole>().eq("id",sysRole.getId()).select("name")).getName(); //SELECT name FROM sys_role WHERE (id = ?)
//            if(name.equals(sysRole.getName())){ //修改时，名称不变
//                return ResultJson.ok().data(sysRole);
//            }
            return ResultJson.error().message("角色名称已被占用");
        }
        long codeCount = sysRoleService.count(new QueryWrapper<SysRole>().ne("id",sysRole.getId()).eq("code", sysRole.getName()).isNull("deleted_at")); //获取除了自己以外的
        if (codeCount > 0) {
            return ResultJson.error().message("唯一编码已被占用");
        }

        if(sysRole.getStatus()==0){//禁用操作，删除中间表中的权限
            sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", sysRole.getId()));
        }

        //设置当前的修改时间
//        sysRole.setUpdatedAt(LocalDateTime.now());

        sysRoleService.updateById(sysRole);

//        // 更新缓存
//        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());

        return ResultJson.ok().data(sysRole);
    }
    @PreAuthorize("hasAuthority('sys.role.delete')")
    @PostMapping("delete")
    @Transactional
    public ResultJson delete(@RequestBody Long[] ids) {
//        List<SysRole> sysRoleList = new ArrayList<>();
        for (long id:ids){
//            sysRoleList.set(sysRoleService.getById(id));
            SysRole sysRole = sysRoleService.getById(id);
            sysRole.setDeletedAt(LocalDateTime.now()); //设置删除时间
            sysRoleService.updateById(sysRole);
        }

//        sysRoleService.removeByIds(Arrays.asList(ids));//批量删除 硬删除
//        sysRoleService.updateBatchById(Arrays.asList(ids));//批量删除 硬删除

        // 删除中间表
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", ids));
        sysRolePermissionService.remove(new QueryWrapper<SysRolePermission>().in("role_id", ids));

        // 缓存同步删除
//        Arrays.stream(ids).forEach(id -> {
//            // 更新缓存
//            userService.clearUserAuthorityInfoByRoleId(id);
//        });

        return ResultJson.ok().message("删除成功");
    }

    @Transactional
    @PreAuthorize("hasAuthority('sys.role.permission.edit')")
    @PostMapping("/permission/edit")
    public ResultJson permissionEdit(@RequestBody SysRole sysrole) { //前端传值{"id":1,"permissionIds":[2,9,10,11,24,23,26,1]}
        Long[] menuIds = sysrole.getPermissionIds().toArray(new Long[0]); //将动态数组转化为静态数组
        Long roleId = sysrole.getId();

        SysRole sysRole = sysRoleService.getById(roleId);
        if(sysRole.getStatus() == 0){
            return ResultJson.error().message("该角色已被禁用，无法操作！");
        }

        List<SysRolePermission> sysRolePermissions = new ArrayList<>();

        Arrays.stream(menuIds).forEach(menuId -> {
            SysRolePermission rolePermission = new SysRolePermission();
            rolePermission.setPermissionId(menuId);
            rolePermission.setRoleId(roleId);

            sysRolePermissions.add(rolePermission);
        });

        // 先删除原来的记录，再保存新的
        sysRolePermissionService.remove(new QueryWrapper<SysRolePermission>().eq("role_id", roleId));
        sysRolePermissionService.saveBatch(sysRolePermissions); //批量插入

        // 删除缓存
//        userService.clearUserAuthorityInfoByRoleId(roleId); //处理redis中的数据？以后再处理

        return ResultJson.ok().data(menuIds);
    }


}
