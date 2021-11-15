package cn.zjut.lms.security_s;

import cn.zjut.lms.security_s.handler.LmsAuthenticationFailureHandler;
import cn.zjut.lms.security_s.handler.LmsAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    /** 依赖注入自定义的登录成功处理器 */
    @Autowired
    private LmsAuthenticationSuccessHandler lmsAuthenticationSuccessHandler;

    /** 依赖注入自定义的登录失败处理器 */
    @Autowired
    private LmsAuthenticationFailureHandler lmsAuthenticationFailureHandler;


    @Autowired
    private UserDetailsService userDetailsService;


    /** 向spring容器中创建一个Bean */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
            .loginProcessingUrl("/login") // 登录请求路径
            .successHandler(lmsAuthenticationSuccessHandler) // 验证成功处理器
            .failureHandler(lmsAuthenticationFailureHandler) // 验证失败处理器
                .and()
            .authorizeRequests() //http.authorizeRequests()主要是对 url 进行控制
            .antMatchers("/login") // 登录请求路径不进行过滤  antMatcher()用于匹配 URL规则
            .permitAll()//表示所匹配的 URL 任何人都允许访问
            .anyRequest()//表示匹配所有的请求
            .authenticated() //表示所匹配的 URL 都需要被认证才能访问。
                .and()
            .cors() // 启用CORS支持
                .and()
            .csrf().disable(); // 取消跨站请求伪造防护
    }
}
