package cn.zjut.lms.controller;

import cn.zjut.lms.common.Const;
import cn.zjut.lms.mapper.UserMapper;
import cn.zjut.lms.model.SysRole;
import cn.zjut.lms.model.User;
import cn.zjut.lms.service.UserService;
import cn.zjut.lms.util.IpUtil;
import cn.zjut.lms.util.JwtUtil;
import cn.zjut.lms.util.RedisUtil;
import cn.zjut.lms.util.ResultJson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sys/user")
public class UserController  extends BaseController {

    /**
     * 注册
     * @param user
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/register", consumes = "application/json")
    public ResultJson add(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { //数据校验
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        String username = user.getUsername();
        String mobile = user.getMobile();

        long countUsername = userService.count(new QueryWrapper<User>().eq("username",username));
        long countMobile = userService.count(new QueryWrapper<User>().eq("mobile",mobile));

        if (countUsername > 0) { //查重校验
            return ResultJson.error().message("用户名已被占用");
        } else if (countMobile > 0) {
            return ResultJson.error().message("电话号码已被占用");
        } else {
            //密码加密
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);

            //默认头像
            user.setAvatar(Const.DEFULT_AVATAR);
            //默认状态
            user.setStatus(Const.USER_STATUS);

            boolean result = userService.save(user);
            if (result) {
                return ResultJson.ok().message("注册成功");
            } else {
                return ResultJson.error().message("数据不存在");
            }
        }
    }
    /**
     * 获取用户信息接口
     * @param principal
     * @return
     */
    @GetMapping("/user/info")
    public ResultJson userInfo(Principal principal){
        User user = userService.getByUsername(principal.getName());
        return ResultJson.ok().data(user);
    }

    /**
     * 获取用户的角色
     * @param principal
     * @return
     */
    @GetMapping("roles")
    public ResultJson userRoles(Principal principal){
        User user = userService.getByUsername(principal.getName());
        List<SysRole> roles = userService.getRolesByUserId(user.getId());
        System.out.println(roles);
        return ResultJson.ok().data(roles);
    }
    /**
     * 获取用户的权限
     * @param principal
     * @return
     */
    @GetMapping("permissions")
    public ResultJson userPermissions(Principal principal){
        User user = userService.getByUsername(principal.getName());
        List<Long> permissonIds = userService.getPermissionIds(user.getId());
        System.out.println(permissonIds);
        return ResultJson.ok().data(permissonIds);
    }

    /**
     * 用户详情
     * @param id
     * @return
     */
    @GetMapping("detail")
    public ResultJson userInfo(@RequestParam("id") Long id) {

        User user = userService.getById(id);
        Assert.notNull(user, "找不到该用户");
        return ResultJson.ok().data(user);
    }

    /**
     * 分页列表
     * @return
     */
    @GetMapping("list")
    public ResultJson list(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size) {

        //分页查询
        Page<User> users = userService.page(new Page<>(page, size), new QueryWrapper<User>().isNull("deleted_at"));

//        System.out.println(users);
//        System.out.println("数据为："+users.getRecords());
//        System.out.println("总数为："+users.getTotal()+",总页数为："+users.getPages());
//        System.out.println("当前页为："+users.getCurrent()+",每页限制："+users.getSize());

        return ResultJson.ok().data(users);
    }

    /**
     * 添加
     * @param
     * @return
     */
    @PostMapping("save")
    public ResultJson save(@Validated @RequestBody User user, BindingResult bindingResult) {
        System.out.println(user);
        if (bindingResult.hasErrors()) { //数据校验
            return ResultJson.validation_error().data("fieldErrors", bindingResult.getFieldError().getDefaultMessage()); //输出错误信息
        }

        //设置当前的创建时间
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 密码加密处理
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);

        // 默认头像
        user.setAvatar(Const.DEFULT_AVATAR);
        //默认状态
        user.setStatus(Const.USER_STATUS);

        userService.save(user);

        return ResultJson.ok().data(user);
    }

    @PostMapping("update")
    public ResultJson update(@Validated @RequestBody User user) {

        user.setUpdatedAt(LocalDateTime.now());

        userService.updateById(user);
        return ResultJson.ok();
    }

    @Transactional
    @PostMapping("delete")
    public ResultJson delete(@RequestBody Long[] ids) {

        userService.removeByIds(Arrays.asList(ids));
        //todo 需要关联删除就要用到事务
//        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));

        return ResultJson.ok();
    }

}
