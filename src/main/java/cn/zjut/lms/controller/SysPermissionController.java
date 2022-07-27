package cn.zjut.lms.controller;

import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.entity.SysPermission;
import cn.zjut.lms.entity.SysRole;
import cn.zjut.lms.entity.SysRolePermission;
import cn.zjut.lms.entity.SysUserRole;
import cn.zjut.lms.util.ResultJson;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/sys/permission")
public class SysPermissionController extends BaseController {

    @PreAuthorize("hasAuthority('sys.permission.info')")
    @GetMapping("info")
    public ResultJson info(@RequestParam(value = "id") Long id) {

        SysPermission sysPermission = sysPermissionService.getById(id);


//        // 获取角色相关联的菜单id
//        List<SysRolePermission> rolePermissions = sysRolePermissionService.list(new QueryWrapper<SysRolePermission>().eq("role_id", id));
//        List<Long> permissionIds = rolePermissions.stream().map(p -> p.getPermissionId()).collect(Collectors.toList());
//
//        sysRole.setPermissionIds(permissionIds);

        //todo 将实体对象转成map
        JSONObject jsonObject = new JSONObject();
        System.out.println(jsonObject.toJSONString(sysPermission));//通过toJSONString( )将实体类转化成json对象

        String jsonString = jsonObject.toJSONString(sysPermission);
        Map<String, Object> testMap = JSONObject.parseObject(jsonString, Map.class); //转成map

        System.out.println(testMap);

        String paretName;
        if(sysPermission.getParentId() == 0){
            paretName = "无父节点";
        }else{
            SysPermission sysPermissionParent = sysPermissionService.getById(sysPermission.getParentId());
            paretName = sysPermissionParent.getName();
        }

        testMap.put("paretName",paretName);

        return ResultJson.ok().data(testMap);
//        return ResultJson.ok().data(sysPermission);
    }
    /**
     * 分页列表
     *
     * @return
     */
    @PreAuthorize("hasAuthority('sys.permission.list')")
    @GetMapping("list")
    public ResultJson list() {

//        //分页查询
//        Page<SysPermission> permissions = sysPermissionService.page(getPage(), new QueryWrapper<SysPermission>().isNull("deleted_at")
//                .like(StrUtil.isNotBlank(name), "name", name).orderBy(true, true, "created_at")); //按时间正排
        List<SysPermission> permissions = sysPermissionService.tree();

        return ResultJson.ok().data(permissions);
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @PreAuthorize("hasAuthority('sys.permission.save')")
    @PostMapping("save")
    public ResultJson save(@Validated @RequestBody SysPermission sysPermission, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //数据校验
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        long count = sysPermissionService.count(new QueryWrapper<SysPermission>().isNull("deleted_at").eq("name", sysPermission.getName()));
        if (count > 0) {
            return ResultJson.error().message("权限名称已被占用");
        }

        //设置当前的创建时间
//        sysPermission.setCreatedAt(LocalDateTime.now());
//        sysPermission.setUpdatedAt(LocalDateTime.now());

        sysPermissionService.save(sysPermission);

        return ResultJson.ok().data(sysPermission);
    }
    @PreAuthorize("hasAuthority('sys.permission.update')")
    @PostMapping("update")
    public ResultJson update(@Validated @RequestBody SysPermission sysPermission, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //数据校验
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        long count = sysPermissionService.count(new QueryWrapper<SysPermission>().ne("id",sysPermission.getId()).eq("name", sysPermission.getName()).isNull("deleted_at"));
        if (count > 0) {
//            String name = sysPermissionService.getOne(new QueryWrapper<SysPermission>().eq("id",sysPermission.getId()).select("name")).getName(); //SELECT name FROM sys_role WHERE (id = ?)
//            if(name.equals(sysPermission.getName())){ //修改时，名称不变
//                return ResultJson.ok().data(sysPermission);
//            }
            return ResultJson.error().message("角色名称已被占用");
        }
        if(sysPermission.getStatus()==0){//禁用操作，删除中间表中的权限
            sysRolePermissionService.remove(new QueryWrapper<SysRolePermission>().in("permission_id", sysPermission.getId()));
        }

        //设置当前的修改时间
//        sysPermission.setUpdatedAt(LocalDateTime.now());

        sysPermissionService.updateById(sysPermission);

//        // 更新缓存
//        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());

        return ResultJson.ok().data(sysPermission);
    }
    @PreAuthorize("hasAuthority('sys.permission.delete')")
    @PostMapping("delete")
    @Transactional //添加事务
    public ResultJson delete(@RequestBody Long[] ids) {
//        List<SysRole> sysRoleList = new ArrayList<>();
        for (long id:ids){
//            sysRoleList.set(sysRoleService.getById(id));
            SysPermission sysPermission = sysPermissionService.getById(id);
            sysPermission.setDeletedAt(LocalDateTime.now()); //设置删除时间
            sysPermissionService.updateById(sysPermission);
        }

//        sysRoleService.removeByIds(Arrays.asList(ids));//批量删除 硬删除
//        sysRoleService.updateBatchById(Arrays.asList(ids));//批量删除 硬删除

        // 删除中间表
//        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id", ids));
        sysRolePermissionService.remove(new QueryWrapper<SysRolePermission>().in("permission_id", ids));

        // 缓存同步删除
//        Arrays.stream(ids).forEach(id -> {
//            // 更新缓存
//            userService.clearUserAuthorityInfoByRoleId(id);
//        });

        return ResultJson.ok().message("删除成功");
    }
}
