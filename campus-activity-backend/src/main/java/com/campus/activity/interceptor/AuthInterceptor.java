package com.campus.activity.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限验证拦截器（可选）
 * 用于验证用户Token和权限
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token
        String token = request.getHeader("Authorization");
        
        // 如果是OPTIONS请求，直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        // 这里可以添加Token验证逻辑
        // 例如：解析JWT Token，验证用户身份和权限
        // 如果Token无效或权限不足，返回401或403
        
        // 目前直接放行，由前端路由守卫控制
        return true;
    }
}
