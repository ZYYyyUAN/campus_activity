package com.campus.activity.mapper;

import com.campus.activity.entity.SignScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 签到积分Mapper接口
 */
@Mapper
public interface SignScoreMapper {
    
    /**
     * 根据签到ID查询
     */
    SignScore selectById(Integer signId);
    
    /**
     * 根据活动ID查询签到列表
     */
    List<SignScore> selectByActivityId(Integer activityId);
    
    /**
     * 根据用户ID查询签到列表
     */
    List<SignScore> selectByUserId(Integer userId);
    
    /**
     * 查询用户对某个活动的签到记录
     */
    SignScore selectByActivityAndUser(@Param("activityId") Integer activityId, 
                                      @Param("userId") Integer userId);
    
    /**
     * 统计活动签到人数
     */
    int countByActivityId(Integer activityId);
    
    /**
     * 统计用户总积分
     */
    Integer sumScoreByUserId(Integer userId);
    
    /**
     * 插入签到记录
     */
    int insert(SignScore signScore);
}

