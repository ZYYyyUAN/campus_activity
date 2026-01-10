package com.campus.activity.service;

import com.campus.activity.entity.User;
import java.util.List;

public interface UserService {
    
    User login(String username, String password);

    User getUserById(Integer userId);
    
    User getUserByUsername(String username);
    
    List<User> getAllUsers();
    
    List<User> getUsersByRole(String role);
    
    int addUser(User user);
    
    int updateUser(User user);

    int deleteUser(Integer userId);

    int changePassword(Integer userId, String oldPassword, String newPassword);  //修改密码

    int resetPassword(Integer userId);  //重置密码（管理员）

    int updateStatus(Integer userId, Integer status);  //更新用户状态
}

