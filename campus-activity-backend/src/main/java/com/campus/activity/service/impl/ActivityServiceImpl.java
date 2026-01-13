package com.campus.activity.service.impl;

import com.campus.activity.entity.Activity;
import com.campus.activity.mapper.ActivityMapper;
import com.campus.activity.service.ActivityService;
import com.campus.activity.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional //保证事务性
public class ActivityServiceImpl implements ActivityService {
    
    @Autowired
    private ActivityMapper activityMapper;
    
    @Autowired
    private RegistrationService registrationService;
    
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
    public List<Activity> queryActivitiesByProcedure(String activityType, Date startTime, Date endTime) {
        return activityMapper.queryActivityByProcedure(activityType, startTime, endTime);
    }
    
    @Override
    public int addActivity(Activity activity) {
        Date activityStartTime = activity.getStartTime();
        Date activityEndTime = activity.getEndTime();

        boolean isTimeValid = validateActivityTime(activityStartTime, activityEndTime);
        if (!isTimeValid) {
            throw new RuntimeException("活动时间设置无效！");
        }
        
        if (activity.getMaxPeople() == null || activity.getMaxPeople() <= 0) {
            throw new RuntimeException("人数上限必须大于0");
        }

        if (activity.getActivityName() == null || activity.getActivityName().trim().isEmpty()) {
            throw new RuntimeException("活动名称不能为空");
        }
        if (activity.getLocation() == null || activity.getLocation().trim().isEmpty()) {
            throw new RuntimeException("活动地点不能为空");
        }
        
        if (activity.getStatus() == null || activity.getStatus().trim().isEmpty()) {
            activity.setStatus("报名中");
        }
        
        // 6. 插入活动（触发器会自动记录日志）
        return activityMapper.insert(activity);
    }
    
    @Override
    public int updateActivity(Activity activity) {
        Date activityStartTime = activity.getStartTime();
        Date activityEndTime = activity.getEndTime();

        boolean isTimeValid = validateActivityTime(activityStartTime, activityEndTime);
        if (!isTimeValid) {
            throw new RuntimeException("活动时间设置无效！");
        }
        
        if (activity.getMaxPeople() != null && activity.getMaxPeople() <= 0) {
            throw new RuntimeException("人数上限必须大于0");
        }
        
        return activityMapper.update(activity);
    }
    
    @Override
    public int deleteActivity(Integer activityId) {
        return activityMapper.deleteById(activityId);
    }
    
    @Override
    public boolean validateActivityTime(Date startTime, Date endTime) {
        Date now = new Date();
        if (startTime.before(now)) {
            return false;
        }
        return endTime.after(startTime);
    }
    
    @Override
    public boolean checkActivityFull(Integer activityId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null || activity.getMaxPeople() == null) {
            return false;
        }
        // 通过Service层统计已通过审核的报名人数
        int registeredCount = registrationService.countPassedByActivityId(activityId);
        return registeredCount >= activity.getMaxPeople();
    }
}

