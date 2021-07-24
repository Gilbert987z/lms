package cn.zjut.lms.config;

import cn.zjut.lms.config.intercepors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 注册拦截器配置类
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Autowired
    LoginInterceptor loginInterceptor;


    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")// 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
                .excludePathPatterns("/login");  // 不拦截路径

    }

    // 这个方法是用来配置静态资源的，比如html，js，css，等等
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }
//    @Bean   //报错https://blog.csdn.net/qq_37663871/article/details/105577394
//    public LoginInterceptor loginInterceptor() {
//        return new LoginInterceptor();
//    }
}
