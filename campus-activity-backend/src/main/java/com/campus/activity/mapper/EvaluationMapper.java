package com.campus.activity.mapper;

import com.campus.activity.entity.Evaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 活动评价Mapper接口
 */
@Mapper
public interface EvaluationMapper {
    
    /**
     * 根据评价ID查询
     */
    Evaluation selectById(Integer evalId);
    
    /**
     * 根据活动ID查询评价列表
     */
    List<Evaluation> selectByActivityId(Integer activityId);
    
    /**
     * 根据用户ID查询评价列表
     */
    List<Evaluation> selectByUserId(Integer userId);
    
    /**
     * 查询用户对某个活动的评价
     */
    Evaluation selectByActivityAndUser(@Param("activityId") Integer activityId, 
                                       @Param("userId") Integer userId);
    
    /**
     * 根据关键词查询评价
     */
    List<Evaluation> selectByKeyword(String keyword);
    
    /**
     * 插入评价
     */
    int insert(Evaluation evaluation);
    
    /**
     * 更新评价
     */
    int update(Evaluation evaluation);
    
    /**
     * 删除评价
     */
    int deleteById(Integer evalId);
}

