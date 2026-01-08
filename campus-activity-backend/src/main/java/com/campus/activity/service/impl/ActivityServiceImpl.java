package com.campus.activity.service.impl;

import com.campus.activity.entity.Activity;
import com.campus.activity.mapper.ActivityMapper;
import com.campus.activity.mapper.RegistrationMapper;
import com.campus.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 活动服务实现类
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {
    
    @Autowired
    private ActivityMapper activityMapper;
    
    @Autowired
    private RegistrationMapper registrationMapper;
    
    @Override
    public Activity getActivityById(Integer activityId) {
        return activityMapper.selectById(activityId);
    }
    
    @Override
    public List<Activity> getAllActivities() {
        return activityMapper.selectAll();
    }
    
    @Override
    public List<Activity> getActivitiesByPublisherId(Integer publisherId) {
        return activityMapper.selectByPublisherId(publisherId);
    }
    
    @Override
    public List<Activity> queryActivities(String activityType, Date startTime, 
                                          Date endTime, String keyword, Integer publisherId) {
        return activityMapper.selectByConditions(activityType, startTime, endTime, keyword, publisherId);
    }
    
    @Override
    public int addActivity(Activity activity) {
        // 设置默认状态
        if (activity.getStatus() == null || activity.getStatus().isEmpty()) {
            activity.setStatus("报名中");
        }
        return activityMapper.insert(activity);
    }
    
    @Override
    public int updateActivity(Activity activity) {
        return activityMapper.update(activity);
    }
    
    @Override
    public int deleteActivity(Integer activityId) {
        return activityMapper.deleteById(activityId);
    }
    
    @Override
    public boolean validateActivityTime(Date startTime, Date endTime) {
        Date now = new Date();
        // 开始时间不能早于当前时间
        if (startTime.before(now)) {
            return false;
        }
        // 结束时间不能早于开始时间
        return !endTime.before(startTime);
    }
    
    @Override
    public boolean checkActivityFull(Integer activityId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null || activity.getMaxPeople() == null) {
            return false;
        }
        int registeredCount = registrationMapper.countPassedByActivityId(activityId);
        return registeredCount >= activity.getMaxPeople();
    }
}

