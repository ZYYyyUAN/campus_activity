package com.campus.activity.service;

import com.campus.activity.entity.Activity;
import java.util.Date;
import java.util.List;

/**
 * 活动服务接口
 */
public interface ActivityService {
    
    /**
     * 根据ID查询活动
     */
    Activity getActivityById(Integer activityId);
    
    /**
     * 查询所有活动
     */
    List<Activity> getAllActivities();
    
    /**
     * 根据发布者ID查询活动
     */
    List<Activity> getActivitiesByPublisherId(Integer publisherId);
    
    /**
     * 多条件查询活动
     */
    List<Activity> queryActivities(String activityType, Date startTime, 
                                   Date endTime, String keyword, Integer publisherId);
    
    /**
     * 添加活动
     */
    int addActivity(Activity activity);
    
    /**
     * 更新活动
     */
    int updateActivity(Activity activity);
    
    /**
     * 删除活动
     */
    int deleteActivity(Integer activityId);
    
    /**
     * 校验活动时间是否合理
     */
    boolean validateActivityTime(Date startTime, Date endTime);
    
    /**
     * 检查报名人数是否已满
     */
    boolean checkActivityFull(Integer activityId);
}

