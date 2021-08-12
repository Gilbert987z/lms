package cn.zjut.lms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity   // 开启web安全支持
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // user
                .authorizeRequests().antMatchers(HttpMethod.GET, "/user/code").permitAll()
                .antMatchers(HttpMethod.POST, "/user/resetPassword", "/user/alipayNotify").permitAll()
                .anyRequest().authenticated()  // 以上配置的路径不需要认证，anyRequest其他任何都需要认证
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()) // 异常时走这个自定义的提示
                .and()
                .addFilterBefore(  // 添加自定义登录拦截过滤器
                        new JWTLoginFilter(
                                new AntPathRequestMatcher("/login", HttpMethod.POST.name()),
                                authenticationManager()
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(new JWTAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);　　　　　　　// 添加JWT权限认证拦截器，用于将每个接口拦截进行token验证，将token里的信息拿取用户并放入安全上下文
    }　
}
