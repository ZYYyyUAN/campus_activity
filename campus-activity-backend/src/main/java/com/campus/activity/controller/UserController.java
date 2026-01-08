package com.campus.activity.controller;

import com.campus.activity.common.Result;
import com.campus.activity.entity.User;
import com.campus.activity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            // 不返回密码
            loginUser.setPassword(null);
            return Result.success("登录成功", loginUser);
        }
        return Result.error("用户名或密码错误");
    }
    
    /**
     * 根据ID查询用户
     */
    @GetMapping("/{userId}")
    public Result<User> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            user.setPassword(null);
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }
    
    /**
     * 查询所有用户
     */
    @GetMapping("/list")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        // 清除密码
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }
    
    /**
     * 根据角色查询用户
     */
    @GetMapping("/role/{role}")
    public Result<List<User>> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.getUsersByRole(role);
        users.forEach(u -> u.setPassword(null));
        return Result.success(users);
    }
    
    /**
     * 添加用户
     */
    @PostMapping("/add")
    public Result<String> addUser(@RequestBody User user) {
        int result = userService.addUser(user);
        if (result > 0) {
            return Result.success("添加成功", null);
        }
        return Result.error("添加失败");
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody User user) {
        int result = userService.updateUser(user);
        if (result > 0) {
            return Result.success("更新成功", null);
        }
        return Result.error("更新失败");
    }
    
    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    public Result<String> deleteUser(@PathVariable Integer userId) {
        int result = userService.deleteUser(userId);
        if (result > 0) {
            return Result.success("删除成功", null);
        }
        return Result.error("删除失败");
    }
    
    /**
     * 修改密码
     */
    @PostMapping("/changePassword")
    public Result<String> changePassword(@RequestBody ChangePasswordRequest request) {
        int result = userService.changePassword(request.getUserId(), 
                                               request.getOldPassword(), 
                                               request.getNewPassword());
        if (result > 0) {
            return Result.success("密码修改成功", null);
        }
        return Result.error("原密码错误或用户不存在");
    }
    
    /**
     * 重置密码（管理员）
     */
    @PostMapping("/resetPassword/{userId}")
    public Result<String> resetPassword(@PathVariable Integer userId) {
        int result = userService.resetPassword(userId);
        if (result > 0) {
            return Result.success("密码已重置为123456", null);
        }
        return Result.error("重置失败");
    }
    
    /**
     * 更新用户状态
     */
    @PutMapping("/status")
    public Result<String> updateStatus(@RequestBody UserStatusRequest request) {
        int result = userService.updateStatus(request.getUserId(), request.getStatus());
        if (result > 0) {
            return Result.success("状态更新成功", null);
        }
        return Result.error("状态更新失败");
    }
    
    /**
     * 修改密码请求对象
     */
    public static class ChangePasswordRequest {
        private Integer userId;
        private String oldPassword;
        private String newPassword;
        
        // getters and setters
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
    
    /**
     * 用户状态请求对象
     */
    public static class UserStatusRequest {
        private Integer userId;
        private Integer status;
        
        // getters and setters
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
    }
}

