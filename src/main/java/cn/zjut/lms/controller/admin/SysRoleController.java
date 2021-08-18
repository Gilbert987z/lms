package cn.zjut.lms.controller.admin;

import cn.zjut.lms.model.SysRole;
import cn.zjut.lms.service.SysRoleService;
import cn.zjut.lms.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

//@Api(description = "商户平台应用接口")
@RestController
@RequestMapping("/admin/role")
public class SysRoleController {
    @Autowired
    SysRoleService sysRoleService;

    //查询所有数据
    @GetMapping("")
    public ResultJson list() {
        Map<String, Object> data = sysRoleService.list();
        return ResultJson.ok().data(data);
    }

    //查询所有数据
    @GetMapping("list")
    public ResultJson listByPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Map<String, Object> data = sysRoleService.listByPage(page, size);
        return ResultJson.ok().data(data);
    }

    //查询所有数据
    @GetMapping("detail")
    public ResultJson detail(@RequestParam(value = "id") int id) {
        SysRole role = sysRoleService.getById(id);
        return ResultJson.ok().data("detail", role);
    }

    //增加
    @PostMapping(value = "create", consumes = "application/json")
    public ResultJson add(@Valid @RequestBody SysRole role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //数据校验
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else { //数据校验成功
            String roleName = role.getRoleName();

            int countNumber = sysRoleService.countNumber(roleName);

            if (countNumber > 0) { //查重校验
                return ResultJson.error().message("角色名已被占用");
            } else {
                boolean result = sysRoleService.add(role);
                if (result) {
                    return ResultJson.ok().message("增加成功");
                } else {
                    return ResultJson.error().message("数据不存在");
                }
            }
        }
    }

    //修改
    @PostMapping(value = "update", consumes = "application/json")
    public ResultJson update(@Valid @RequestBody SysRole role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            String roleName = role.getRoleName();

            int countNumber = sysRoleService.countNumber(roleName);

            if (countNumber > 0) { //查重校验
                return ResultJson.error().message("角色名已被占用");
            }  else {
                boolean result = sysRoleService.update(role);
                if (result) {
                    return ResultJson.ok().message("修改成功");
                } else {
                    return ResultJson.error().message("数据不存在");
                }
            }
        }
    }

    //根据id删除
    @PostMapping(value = "delete", consumes = "application/json")
    public ResultJson deleteById(@RequestBody SysRole role) {
//        int id = user.getId();
        boolean result = sysRoleService.delete(role);
        if (result) {
            return ResultJson.ok().message("删除成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }

}
