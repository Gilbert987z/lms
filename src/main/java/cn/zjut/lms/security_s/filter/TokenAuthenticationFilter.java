package cn.zjut.lms.security_s.filter;


import cn.zjut.lms.dao.LoginDao;
import cn.zjut.lms.model.User;
import cn.zjut.lms.service.LoginService;
import cn.zjut.lms.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName: TokenAuthenticationFilter
 * @Description: TokenAuthenticationFilter
 * @Author oyc
 * @Date 2021/1/18 10:59
 * @Version 1.0
 */
@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
//    public final String HEADER = "Authorization";
    public final String HEADER = "token";


    @Autowired
    LoginService loginService;
    /**
     * @param request     rq
     * @param response    rs
     * @param filterChain 链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("TokenAuthenticationFilter");

        String token = request.getHeader(HEADER);
        log.info("token-->" + token);
        if (!ObjectUtils.isEmpty(token)) {
            log.info("token-->" + token);
            //是否过期
            boolean check = false;
            try {
                check = JwtUtil.isTokenExpired(token);
            } catch (Exception e) {
                request.setAttribute("error", e.getMessage());
            }
            if (!check) {
                String username = JwtUtil.getUsernameFromToken(token);
                if (username != null) {
                    log.info(username);

                    //todo
                    //通过用户信息得到UserDetails
                    List<SimpleGrantedAuthority> list = new ArrayList<>();
                    list.add(new SimpleGrantedAuthority("root"));
//                    org.springframework.security.core.userdetails.User authUser = new org.springframework.security.core.userdetails.User(username, null, list);

                    username="lisi";
                    User authUser =loginService.findByUsername(username);
                    //将用户信息存入 authentication，方便后续校验
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    authUser.getUsername(),
                                    null,
                                    authUser.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        log.info("tokenFilter放行");
        //放行
        filterChain.doFilter(request, response);
    }
}
