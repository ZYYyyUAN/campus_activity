package com.campus.activity.mapper;

import com.campus.activity.entity.Evaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EvaluationMapper {
    
    Evaluation selectById(Integer evalId);
    
    List<Evaluation> selectAll();
    
    List<Evaluation> selectByActivityId(Integer activityId);
    
    List<Evaluation> selectByUserId(Integer userId);
    
    Evaluation selectByActivityAndUser(@Param("activityId") Integer activityId, 
                                       @Param("userId") Integer userId);
    
    List<Evaluation> selectByKeyword(String keyword);
    
    int insert(Evaluation evaluation);
    
    int update(Evaluation evaluation);
    
    int deleteById(Integer evalId);
}