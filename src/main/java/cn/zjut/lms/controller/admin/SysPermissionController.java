package cn.zjut.lms.controller.admin;


import cn.zjut.lms.model.SysPermission;
import cn.zjut.lms.service.SysPermissionService;
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
@RequestMapping("/admin/permission")
public class SysPermissionController {
    @Autowired
    SysPermissionService sysPermissionService;


    //查询所有数据
    @GetMapping("")
    public ResultJson list() {
        Map<String, Object> data = sysPermissionService.list();
        return ResultJson.ok().data(data);
    }
    //分页查询
    @GetMapping("list")
    public ResultJson selectAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        Map<String, Object> data = sysPermissionService.listByPage(page, size);
        return ResultJson.ok().data(data);
    }

    //查询所有数据
    @GetMapping("detail")
    public ResultJson detail(@RequestParam(value = "id") int id) {
        SysPermission sysPermission = sysPermissionService.getById(id);
        return ResultJson.ok().data("detail", sysPermission);
    }
}
