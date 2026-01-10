package com.campus.activity.mapper;

import com.campus.activity.entity.SignScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SignScoreMapper {
    
    SignScore selectById(Integer signId);
    
    List<SignScore> selectByActivityId(Integer activityId);
    
    List<SignScore> selectByUserId(Integer userId);
    
    SignScore selectByActivityAndUser(@Param("activityId") Integer activityId, 
                                      @Param("userId") Integer userId);

    int countByActivityId(Integer activityId);
    
    Integer sumScoreByUserId(Integer userId);
    
    int insert(SignScore signScore);
}