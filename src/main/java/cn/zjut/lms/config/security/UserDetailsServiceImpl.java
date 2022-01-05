package cn.zjut.lms.config.security;


import cn.zjut.lms.model.User;
import cn.zjut.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username参数,是在登陆时,用户传递的表单数据username
        User user = userService.getByUsername(username);


        if (user == null) {
            throw new UsernameNotFoundException(String.format("用户未找到：'%s'", username));
        }

        Collection<GrantedAuthority> authList = getAuthorities();

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
    private Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
//        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
//        authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authList;
    }
}

