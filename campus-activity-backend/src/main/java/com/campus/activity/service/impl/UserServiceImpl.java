package com.campus.activity.service.impl;

import com.campus.activity.entity.User;
import com.campus.activity.mapper.UserMapper;
import com.campus.activity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.getStatus() == 1) {
            return user;
        }
        return null;
    }
    
    @Override
    public User getUserById(Integer userId) {
        return userMapper.selectById(userId);
    }
    
    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }
    
    @Override
    public List<User> getUsersByRole(String role) {
        return userMapper.selectByRole(role);
    }
    
    @Override
    public int addUser(User user) {
        //设置默认密码
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword("123456");
        }
        //设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        return userMapper.insert(user);
    }
    
    @Override
    public int updateUser(User user) {
        return userMapper.update(user);
    }
    
    @Override
    public int deleteUser(Integer userId) {
        return userMapper.deleteById(userId);
    }
    @Override
    public int resetPassword(Integer userId) {
        // 调用存储过程重置密码
        return userMapper.resetPasswordByProcedure(userId);
    }
    
    @Override
    public int updateStatus(Integer userId, Integer status) {
        return userMapper.updateStatus(userId, status);
    }
}

