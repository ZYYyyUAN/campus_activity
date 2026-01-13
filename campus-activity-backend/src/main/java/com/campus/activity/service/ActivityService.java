package com.campus.activity.service;

import com.campus.activity.entity.Activity;
import java.util.Date;
import java.util.List;

public interface ActivityService {
    
    Activity getActivityById(Integer activityId);
    
    List<Activity> getAllActivities();

    List<Activity> getActivitiesByPublisherId(Integer publisherId);
    
    List<Activity> queryActivities(String activityType, Date startTime, 
                                   Date endTime, String keyword, Integer publisherId);
    
    List<Activity> queryActivitiesByProcedure(String activityType, Date startTime, Date endTime);
    
    int addActivity(Activity activity);
    
    int updateActivity(Activity activity);
    
    int deleteActivity(Integer activityId);
    
    boolean validateActivityTime(Date startTime, Date endTime);
    
    boolean checkActivityFull(Integer activityId);
}
