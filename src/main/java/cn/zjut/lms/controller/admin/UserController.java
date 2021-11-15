package cn.zjut.lms.controller.admin;

import cn.zjut.lms.model.User;
import cn.zjut.lms.service.UserService;
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
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    UserService userService;

    //查询所有数据
    @GetMapping("list")
    public ResultJson selectAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        Map<String, Object> data = userService.list(page, size);
        return ResultJson.ok().data(data);
    }

    //查询所有数据
    @GetMapping("detail")
    public ResultJson detail(@RequestParam(value = "id") int id) {
        User user = userService.getById(id);
        return ResultJson.ok().data("detail", user);
    }

    //增加
    @PostMapping(value = "create", consumes = "application/json")
    public ResultJson add(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //数据校验
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else { //数据校验成功
            String username = user.getUsername();
            String mobile = user.getMobile();

            int countUsername = userService.countUsername(username);
            int countMobile = userService.countMobile(mobile);

            if (countUsername > 0) { //查重校验
                return ResultJson.error().message("用户名已被占用");
            } else if (countMobile > 0) {
                return ResultJson.error().message("电话号码已被占用");
            } else {
                boolean result = userService.add(user);
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
    public ResultJson update(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> fieldErrorsMap = new HashMap<>();

            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                fieldErrorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            return ResultJson.validation_error().data("fieldErrors", fieldErrorsMap);
        } else {
            String username = user.getUsername();
            String mobile = user.getMobile();

            int countUsername = userService.countUsername(username);
            int countMobile = userService.countMobile(mobile);

            if (countUsername > 0) { //查重校验
                return ResultJson.error().message("用户名已被占用");
            } else if (countMobile > 0) {
                return ResultJson.error().message("电话号码已被占用");
            } else {
                boolean result = userService.update(user);
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
    public ResultJson deleteById(@RequestBody User user) {
//        int id = user.getId();
        boolean result = userService.delete(user);
        if (result) {
            return ResultJson.ok().message("删除成功");
        } else {
            return ResultJson.error().message("数据不存在");
        }
    }

}
