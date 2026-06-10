package com.campus.activity.mapper;

import com.campus.activity.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;


@Mapper
public interface UserMapper {
    
    User selectById(Integer userId);
    
    User selectByUsername(String username);

    List<User> selectAll();

    List<User> selectByRole(String role);
    
    int insert(User user);

    int update(User user);
    
    int deleteById(Integer userId);
    
    int updatePassword(@Param("userId") Integer userId, @Param("password") String password);

    int updateStatus(@Param("userId") Integer userId, @Param("status") Integer status);
    
    @Update("CALL reset_user_password(#{userId})")
    int resetPasswordByProcedure(@Param("userId") Integer userId);
}
