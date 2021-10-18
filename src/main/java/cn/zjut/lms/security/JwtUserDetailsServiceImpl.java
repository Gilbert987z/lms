//package cn.zjut.lms.security;
//
//import com.safepass.dao.IUserRepository;
//import com.safepass.domain.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.stream.Collectors;
//
///**
// * 用户验证方法
// *
// * @author hackyo
// * Created on 2017/12/8 9:18.
// */
//@Service
//public class JwtUserDetailsServiceImpl implements UserDetailsService {
//
//    private IUserRepository userRepository;
//
//    @Autowired
//    public JwtUserDetailsServiceImpl(IUserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
//        } else {
//            return new JwtUser(user.getUsername(), user.getPassword(), user.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//        }
//    }
//
//}
