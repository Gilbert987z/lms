package cn.zjut.lms.controller;

import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.common.Const;
import cn.zjut.lms.model.SysRole;
import cn.zjut.lms.model.SysRolePermission;
import cn.zjut.lms.model.SysUserRole;
import cn.zjut.lms.model.User;
import cn.zjut.lms.service.SysRoleService;
import cn.zjut.lms.service.UserService;
import cn.zjut.lms.util.ResultJson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/role")
public class SysRoleController  extends BaseController {


    /**
     * 分页列表
     * @return
     */
    @GetMapping("list")
    public ResultJson list(@RequestParam(value = "name", defaultValue = "") String name) {

        //分页查询
//        Page<SysRole> roles = sysRoleService.page(new Page<>(page, size), new QueryWrapper<SysRole>().isNull("deleted_at")
//                .like(StrUtil.isNotBlank(name), "name", name));
        Page<SysRole> roles = sysRoleService.page(getPage(), new QueryWrapper<SysRole>().isNull("deleted_at")
                .like(StrUtil.isNotBlank(name), "name", name).orderBy(true,true,"created_at")); //按时间正排

        return ResultJson.ok().data(roles);
    }

    /**
     * 添加
     * @param
     * @return
     */
    @PostMapping("save")
    public ResultJson save(@Validated @RequestBody SysRole role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //数据校验
//            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        //设置当前的创建时间
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());


        sysRoleService.save(role);

        return ResultJson.ok().data(role);
    }


    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    @Transactional
    public ResultJson info(@RequestBody Long[] ids) {

        sysRoleService.removeByIds(Arrays.asList(ids));

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
}
