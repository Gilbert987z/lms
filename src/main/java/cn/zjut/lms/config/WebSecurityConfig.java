package cn.zjut.lms.config;


import cn.zjut.lms.config.security.MyAccessDecisionManager;
import cn.zjut.lms.config.security.MyFilterInvocationSecurityMetadataSource;
import cn.zjut.lms.config.security.handler.MyAccessDeniedHandler;
import cn.zjut.lms.config.security.handler.MyAuthenticationFailureHandler;
import cn.zjut.lms.config.security.handler.MyAuthenticationSuccessHandler;
import cn.zjut.lms.config.security.handler.MyLogoutSuccessHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.cors.CorsUtils;

/**
 * @Author: Galen
 * @Date: 2019/3/27-14:43
 * @Description: spring-security权限管理的核心配置
 **/
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private UserSecurityService userSecurityService;
    @Autowired
    private MyFilterInvocationSecurityMetadataSource filterMetadataSource; //权限过滤器
    @Autowired
    private MyAccessDecisionManager myAccessDecisionManager;//权限决策器

    /**
     * @Author: Galen
     * @Description: 配置userDetails的数据源，密码加密格式
     * @Date: 2019/3/28-9:24
     * @Param: [auth]
     * @return: void
     **/
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userSecurityService)
//                .passwordEncoder(new BCryptPasswordEncoder());// 实现自定义登录校验
//    }

    /**
     * @Author: Galen
     * @Description: 配置放行的资源
     * @Date: 2019/3/28-9:23
     * @Param: [web]
     * @return: void
     **/
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers( "/index.html", "/static/**", "/login_p", "/favicon.ico")
                // 给 swagger 放行；不需要权限能访问的资源
                .antMatchers("/swagger-ui.html", "/swagger-resources/**", "/images/**", "/webjars/**", "/v2/api-docs", "/configuration/ui", "/configuration/security");
    }

    /**
     * @Author: Galen
     * @Description: 拦截配置
     * @Date: 2019/4/4-10:44
     * @Param: [http]
     * @return: void
     **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//                // 使其支持跨域
//                .requestMatchers(CorsUtils :: isPreFlightRequest).permitAll()
//                // 其他路径需要授权访问
//                .anyRequest().authenticated()
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                        o.setSecurityMetadataSource(filterMetadataSource);
//                        o.setAccessDecisionManager(myAccessDecisionManager);
//                        return o;
//                    }
//                })
//                .and()
//            .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/login")
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .failureHandler(new MyAuthenticationFailureHandler())
//                .successHandler(new MyAuthenticationSuccessHandler())
//                .permitAll()
//                .and()
//            // 退出登录后的默认路径
//            .logout()
//                .logoutUrl("/logout") // 指定注销路径
//                .logoutSuccessHandler(new MyLogoutSuccessHandler())
//                .permitAll()
//                .and()
//            .csrf()
//                .disable() //关闭csrf
//                .exceptionHandling().accessDeniedHandler(new MyAccessDeniedHandler());



//        http.authorizeRequests()
//                // 只有 admin 角色才能访问路径 /admin/**
//                .antMatchers("/admin/**").hasRole("admin")
//                // admin 和 user 角色都能访问路径 /user/**
//                .antMatchers("/user/**").hasAnyRole("admin", "user")
//                // 其他路径请求，只要是登录用户都可以访问
//                .anyRequest().authenticated()
//                .and()
//                // 配置表单登录
//                .formLogin()
//                // 处理登录的 URL
//                .loginProcessingUrl("/doLogin")
//                // 与登录相关的请求都可以通过
//                .permitAll()
//                .and()
//                // 关闭 csrf 保护
//                .csrf().disable();
    }
}
