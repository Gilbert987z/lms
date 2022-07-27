package cn.zjut.lms.config.security.filter;


import cn.zjut.lms.config.security.UserDetailsServiceImpl;
import cn.zjut.lms.config.security.handler.LmsAuthenticationFailure2Handler;
import cn.zjut.lms.entity.User;
import cn.zjut.lms.service.UserService;
import cn.zjut.lms.util.JwtUtil;
import cn.zjut.lms.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * token校验
 * @ClassName: TokenAuthenticationFilter
 * @Description: TokenAuthenticationFilter
 * @Author oyc
 * @Date 2021/1/18 10:59
 * @Version 1.0
 */
@Slf4j
//@Components
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    LmsAuthenticationFailure2Handler lmsAuthenticationFailure2andler;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * @param request     rq
     * @param response    rs
     * @param filterChain 链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("TokenAuthenticationFilter");

        String token = request.getHeader(JwtUtil.HEADER_TOKEN);  //获取header的token值
        log.info("token-->" + token);

        //token没有值，直接退出filter
        if (ObjectUtils.isEmpty(token)) {
            log.info("token没有值");
            log.info("tokenFilter放行");

            //*************************
            //测试用的userId的数据   仅测试接口时，postman使用，请求参数中带userId
            //************************
            try{
                if(!request.getHeader("userId").isEmpty()){

                    User user =userService.getById(request.getHeader("userId"));
                    //将用户信息存入 authentication，方便后续校验
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    //authUser.getId(),
                                    user.getUsername(),
                                    null,
                                    user.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    //*************************
                    //测试用的userId的数据
                    //************************

                }
            }catch (Exception exception){
                System.out.println(exception);
            }


            filterChain.doFilter(request, response);
            return;
        }
        log.info("token有值");


//        String username = JwtUtil.getUsernameFromToken(token);//用token获取用户名称
        Long userId = JwtUtil.getUserIdFromToken(token);//用token获取用户ID

        if(userId==null){//因为validateToken方法中的equals方法的空指针的报错，先提前处理username
            filterChain.doFilter(request, response);
            return;
        }

        User authUser = userService.getById(userId);
        log.info("" + authUser);

        //检查token是否有效
        if (JwtUtil.validateToken(token, authUser)) {//有效
            log.info("token有效");


            //用户封禁操作
            if (authUser.getStatus() == 0) { //被封禁
                // 交给认证失败处理器
                lmsAuthenticationFailure2andler.onAuthenticationFailure(request, response);
            }


//            //通过用户信息得到UserDetails
//            List<SimpleGrantedAuthority> list = new ArrayList<>();
//            list.add(new SimpleGrantedAuthority("root"));

            //从UserDetailsServiceImpl中的获取用户的权限方法中获取权限的数据
            Collection<GrantedAuthority> grantedAuthorities = userDetailsService.getUserAuthorities(authUser.getId());

            //将用户信息存入 authentication，方便后续校验
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            //authUser.getId(),
                            authUser.getUsername(),
                            null,
                            grantedAuthorities
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else{
            log.info("token无效");
        }

        //todo 没有和redis的token做交互，那退出登录，删token就毫无作用了
        //todo 之后再做token刷新吧，需要和前端配合，目前前端代码不行，需继续努力
//        //是否过期
//        boolean check = true; //true 过期
//        try {
//            check = JwtUtil.isTokenExpired(token); //一般就传true
//            log.info("token是否过期："+check);
//        } catch (Exception e) {
//            request.setAttribute("error", e.getMessage());
//        }
//        //不过期
//        if (!check) {
//            log.info("token没有过期");
//
//            if (username != null) {
//
//                //通过用户信息得到UserDetails
//                List<SimpleGrantedAuthority> list = new ArrayList<>();
//                list.add(new SimpleGrantedAuthority("root"));
//
//
//                //将用户信息存入 authentication，方便后续校验
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(
//                                authUser.getUsername(),
//                                null,
//                                authUser.getAuthorities()
//                        );
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            }
//        }else{  //正常token但已过期    //todo 这个token是从前端拿的，那怎么把前端的token刷新过期时间呢？？？？
//            log.info("token过期");
//
//            //redis更新token过期时间
//            redisUtil.expire(RedisUtil.USER_TOKEN + authUser.getId(), JwtUtil.EXPIRE_TIME);
//            //获取过期时间
//            long expireTime = redisUtil.getExpire(RedisUtil.USER_TOKEN + authUser.getId());
//            System.out.println("expireTime:" + expireTime);
//        }

        log.info("tokenFilter放行");
        //放行
        filterChain.doFilter(request, response);//将请求转发给过滤器链下一个filter,如果没有filter那就是你请求的资源
    }
}
