package cn.zjut.lms.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.zjut.lms.common.Const;
import cn.zjut.lms.common.dto.UserRoleDto;
import cn.zjut.lms.entity.SysRole;
import cn.zjut.lms.entity.SysUserRole;
import cn.zjut.lms.entity.User;
import cn.zjut.lms.util.ResultJson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
//@RequestMapping("/sys/user")
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
//    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public ResultJson userInfo(Principal principal){
        User user = userService.getByUsername(principal.getName());
        return ResultJson.ok().data(user);
    }

//    /**
//     * 获取用户的角色
//     * @param principal
//     * @return
//     */
//    @GetMapping("/sys/user/roles")
//    public ResultJson userRoles(Principal principal){
//        User user = userService.getByUsername(principal.getName());
//        List<SysRole> roles = userService.getRolesByUserId(user.getId());
//        System.out.println(roles);
//        return ResultJson.ok().data(roles);
//    }
//    /**
//     * 获取用户的权限
//     * @param principal
//     * @return
//     */
//    @GetMapping("/sys/user/permissions")
//    public ResultJson userPermissions(Principal principal){
//        User user = userService.getByUsername(principal.getName());
//        List<Long> permissonIds = userService.getPermissionIds(user.getId());
//        System.out.println(permissonIds);
//        return ResultJson.ok().data(permissonIds);
//    }

    /**
     * 获取用户的权限
     * @param principal
     * @return
     */
    @GetMapping("/user/permissions")
    public ResultJson userPermissions(Principal principal){
        User user = userService.getByUsername(principal.getName());

        // 获取权限信息
        String authorityInfo = userService.getUserAuthorityInfo(user.getId());// ROLE_admin,ROLE_normal,sys:user:list,....
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");

        // 获取导航栏信息
//        List<SysPermission> navs = sys.getCurrentUserNav();

        return ResultJson.ok().data(MapUtil.builder()
                .put("authoritys", authorityInfoArray)
//                .put("nav", navs)
                .map()
        );
    }

    /**
     * 用户详情
     * @param id
     * @return
     */
    @GetMapping("/sys/user/detail")
    public ResultJson userInfo(@RequestParam("id") Long id) {

        User user = userService.getById(id);
        Assert.notNull(user, "找不到该用户");

        List<SysRole> roles = sysRoleService.listRolesByUserId(id); //用户的所属的角色列表
        user.setSysRoles(roles);
        return ResultJson.ok().data(user);
    }

    /**
     * 分页列表
     * @return
     */
    @GetMapping("/sys/user/list")
    public ResultJson list(@RequestParam(value = "username", defaultValue = "") String username) {

        //分页查询
        Page<User> pageData = userService.page(getPage(), new QueryWrapper<User>().isNull("deleted_at")
                .like(StrUtil.isNotBlank(username), "username", username).orderBy(true, true, "created_at")); //按时间正排

//        System.out.println(users);
//        System.out.println("数据为："+users.getRecords());
//        System.out.println("总数为："+users.getTotal()+",总页数为："+users.getPages());
//        System.out.println("当前页为："+users.getCurrent()+",每页限制："+users.getSize());

        System.out.println(pageData.getRecords());
        //forEach是list的方法，遍历列表；u是lambda的参数,取出user;
        pageData.getRecords().forEach(u->{
            System.out.println(u);
            System.out.println(sysRoleService.listRolesByUserId(u.getId()));
            u.setSysRoles(sysRoleService.listRolesByUserId(u.getId())); //取出用户所有角色，并设值
        });

        return ResultJson.ok().data(pageData);
    }

    /**
     * 添加
     * @param
     * @return
     */
    @GetMapping("/sys/user/save")
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

    @PostMapping("/sys/user/update")
    public ResultJson update(@Validated @RequestBody User user) {

        user.setUpdatedAt(LocalDateTime.now());

        userService.updateById(user);
        return ResultJson.ok();
    }

    /**
     * 分配角色
     * 只需要操作user_role的关联表就可以实现；增或删
     * @param
     * @return
     */
    @Transactional
    @PostMapping("/sys/user/role/update")
    public ResultJson rolePerm(@RequestBody UserRoleDto userRoleDto) {
        Long userId = userRoleDto.getUserId();
        List<Long> roleIds = userRoleDto.getRoleIds();

        List<SysUserRole> userRoles = new ArrayList<>();

        roleIds.forEach(roleId -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(roleId);
            sysUserRole.setUserId(userId);
            sysUserRole.setCreatedAt(LocalDateTime.now());
            sysUserRole.setUpdatedAt(LocalDateTime.now());

            userRoles.add(sysUserRole); //将元素插入到指定位置的 arraylist 中
        });

        //删除user_role所有userId的值
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        //批量新增userRole
        sysUserRoleService.saveBatch(userRoles);

        return ResultJson.ok();
    }
//    /**
//     * 分配角色
//     * 只需要操作user_role的关联表就可以实现；增或删
//     * @param
//     * @return
//     */
//    @Transactional
//    @PostMapping("/sys/user/role/update")
//    public ResultJson rolePerm(@RequestBody Map<String,Object> map) {
//
//
//
//
//        System.out.println(map);
//        System.out.println(map.get("roleIds").getClass().getName());
//        Long[] roleIds1 = new Long[5];
//        System.out.println(roleIds1.getClass().getName());
//
//        //Object 类型转换为 Long 类型，先将 Object 类型转换为 String，再将 String 转换为 Long
//        Long userId =  Long.valueOf(String.valueOf(map.get("userId")));
////        Long[] roleIds = (Long[])map.get("roleIds"); //todo 类型转换的报错问题
//        List<Long> roleIds = (List<Long>)map.get("roleIds");
//        System.out.println(userId);
////        System.out.println(roleIds);
//        System.out.println(map.get("roleIds"));
//
//        System.out.println("22222222222222222222222222222222222222222222");
//        List<SysUserRole> userRoles = new ArrayList<>();
//        System.out.println("33333333333333333333333333333333333333333333");
//
//
//        System.out.println(roleIds.getClass().getName());
//        System.out.println("123412341234213");
//        System.out.println(roleIds.get(1));
////        System.out.println(roleIds.get(1).getClass().getName());
//        Long roleId1 = roleIds.get(1);
//        System.out.println(roleId1+"   "+roleId1.getClass().getName());
//        for(Long roleId :roleIds ){
//            System.out.println("66666666666666666666666666666666666");
//            SysUserRole sysUserRole = new SysUserRole();
//            sysUserRole.setRoleId(roleId);
//            sysUserRole.setUserId(userId);
//
//            userRoles.add(sysUserRole); //将元素插入到指定位置的 arraylist 中
//            System.out.println("4444444444444444444444444444444444444444444");
//        }
//        roleIds.forEach(roleId -> {
//            System.out.println("66666666666666666666666666666666666");
//            SysUserRole sysUserRole = new SysUserRole();
//            sysUserRole.setRoleId(roleId);
//            sysUserRole.setUserId(userId);
//
//            userRoles.add(sysUserRole); //将元素插入到指定位置的 arraylist 中
//            System.out.println("4444444444444444444444444444444444444444444");
//        });
//        System.out.println("111111111111111111111111111111111111111111111111");
//        System.out.println(userRoles);
//
//        //删除user_role所有userId的值
//        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
//        //批量新增userRole
//        sysUserRoleService.saveBatch(userRoles);
//
//        return ResultJson.ok();
//    }

    @Transactional
    @PostMapping("/sys/user/delete")
    public ResultJson delete(@RequestBody Long[] ids) {

        userService.removeByIds(Arrays.asList(ids));
        //todo 需要关联删除就要用到事务
//        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));

        return ResultJson.ok();
    }

}
