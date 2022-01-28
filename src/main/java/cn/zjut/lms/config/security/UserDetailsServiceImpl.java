package cn.zjut.lms.config.security;


import cn.zjut.lms.entity.User;
import cn.zjut.lms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 用户信息的服务方法
 */
@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 按用户名加载用户
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername方法");

        //username参数,是在登陆时,用户传递的表单数据username
        User user = userService.getByUsername(username);


        if (user == null) {
            throw new UsernameNotFoundException(String.format("用户未找到：'%s'", username));
        }

        //获取用户的权限
        Collection<GrantedAuthority> authList = getUserAuthorities(user.getId());

        // //todo  校验密码，密码加密    密码竟然进行校验了，好神奇？？？？？？
        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), true, true, true, true,
                authList);
        return userDetail;
    }


    //https://blog.csdn.net/my_learning_road/article/details/79833802

    /**
     * 获取用户的角色权限,为了降低实验的难度，这里去掉了根据用户名获取角色的步骤
     *
     * @param
     * @return
     */
    public Collection<GrantedAuthority> getUserAuthorities(Long userId) {
        // 角色(ROLE_admin)、菜单操作权限 sys:user:list
        String authority = userService.getUserAuthorityInfo(userId);  // ROLE_admin,ROLE_normal,sys:user:list,....

        System.out.println(AuthorityUtils.commaSeparatedStringToAuthorityList(authority));

        // 通过内置的工具类，把权限字符串封装成GrantedAuthority列表
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);

//        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
////        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
////        authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        return authList;
    }
}

