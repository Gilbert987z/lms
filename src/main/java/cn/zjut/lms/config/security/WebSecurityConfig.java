package cn.zjut.lms.config.security;

import cn.zjut.lms.config.security.filter.CaptchaFilter;
import cn.zjut.lms.config.security.filter.TokenAuthenticationFilter;
import cn.zjut.lms.config.security.handler.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security配置文件
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 向spring容器中创建一个Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录token的过滤
     * @return
     */
//    @Bean
//    public TokenAuthenticationFilter jwtAuthenticationTokenFilter() {
//        return new TokenAuthenticationFilter();
//    }

    @Bean
    TokenAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
        TokenAuthenticationFilter jwtAuthenticationTokenFilter = new TokenAuthenticationFilter(authenticationManager());
        return jwtAuthenticationTokenFilter;
    }
    /**
     * 验证码的过滤
     */
    @Autowired
    CaptchaFilter captchaFilter;

    /**
     * 当匿名请求需要登录的接口时,拦截处理
     */
    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    /**
     * 依赖注入自定义的登录成功处理器
     */
    @Autowired
    private LmsAuthenticationSuccessHandler lmsAuthenticationSuccessHandler;

    /**
     * 依赖注入自定义的登录失败处理器
     */
    @Autowired
    private LmsAuthenticationFailureHandler lmsAuthenticationFailureHandler;

    /**
     * 当登录后,访问接口没有权限的时候,该处理类的方法被调用
     */
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /**
     * 登出成功处理
     */
    @Autowired
    private LmsLogoutSuccessHandler lmsLogoutSuccessHandler;


    @Autowired
    private UserDetailsService userDetailsService;

    private static final String[] URL_WHITELIST = {

            "/login",
            "/register",
            "/logout",
            "/captcha", //验证码
            "/favicon.ico",
            "/index/**",//index接口公开，不用校验token
//        "/**" //全部路径都能通过
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        log.info("WebSecurityConfig");

        http.   //表单登录
                formLogin()
                .loginProcessingUrl("/login") // 登录请求路径
                .successHandler(lmsAuthenticationSuccessHandler) // 验证成功处理器
                .failureHandler(lmsAuthenticationFailureHandler) // 验证失败处理器

                //登出处理
                .and()
                .logout()
                .logoutSuccessHandler(lmsLogoutSuccessHandler) //登出成功的处理
                .permitAll()

                //配置安全访问规则
                .and()
                .authorizeRequests() //http.authorizeRequests()主要是对 url 进行控制
                .antMatchers(URL_WHITELIST) // 相关请求路径不进行过滤  antMatcher()用于匹配 URL规则
                .permitAll()//表示所匹配的 URL 任何人都允许访问
                .anyRequest()//表示匹配所有的请求
                .authenticated() //表示所匹配的 URL 都需要被认证才能访问。

                //异常处理
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint) //匿名访问,没有权限的处理类
                .accessDeniedHandler(jwtAccessDeniedHandler)//登录后,访问没有权限处理类

                // 启用CORS支持
                .and()
                .cors()

                // 取消跨站请求伪造防护
                .and()
                .csrf()
                .disable()

                // 基于token，所以不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 配置自定义的过滤器
                // 添加我们的JWT过滤器
                .and()
                .addFilter(jwtAuthenticationTokenFilter())
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
