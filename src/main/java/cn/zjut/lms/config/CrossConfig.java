package cn.zjut.lms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
跨域配置
 */
@Configuration
public class CrossConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //解决Vue与SpringBoot通信跨域问题
        registry.addMapping("/**")  //设置允许跨域的路径
                .allowedOriginPatterns("*")          //设置允许跨域请求的域名
                .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTIONS")   //设置允许的方法
                .allowCredentials(true)       //这里：是否允许证书 不再默认开启
                .maxAge(3600)                 //跨域允许时间
                .allowedHeaders("*");
    }


    //原文链接：https://blog.csdn.net/Vicky_2020/article/details/120290111
    //上传图片的地址映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //此处路径和上面的图片上传位置保持一致
        registry.addResourceHandler("/image/**").addResourceLocations("file:D:/file/picture/");
    }


}

