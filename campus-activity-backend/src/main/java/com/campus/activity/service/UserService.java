package com.campus.activity.service;

import com.campus.activity.entity.User;
import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    User login(String username, String password);
    
    /**
     * 根据ID查询用户
     */
    User getUserById(Integer userId);
    
    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);
    
    /**
     * 查询所有用户
     */
    List<User> getAllUsers();
    
    /**
     * 根据角色查询用户
     */
    List<User> getUsersByRole(String role);
    
    /**
     * 添加用户
     */
    int addUser(User user);
    
    /**
     * 更新用户信息
     */
    int updateUser(User user);
    
    /**
     * 删除用户
     */
    int deleteUser(Integer userId);
    
    /**
     * 修改密码
     */
    int changePassword(Integer userId, String oldPassword, String newPassword);
    
    /**
     * 重置密码（管理员）
     */
    int resetPassword(Integer userId);
    
    /**
     * 更新用户状态
     */
    int updateStatus(Integer userId, Integer status);
}

