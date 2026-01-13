package com.campus.activity.config;

import com.campus.activity.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 * 用于配置拦截器等
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private AuthInterceptor authInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加权限验证拦截器
        // 目前暂不启用，保留接口供后续使用
        // registry.addInterceptor(authInterceptor)
        //         .addPathPatterns("/**")
        //         .excludePathPatterns("/user/login", "/user/register");
    }
}
