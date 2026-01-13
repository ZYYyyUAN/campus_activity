package com.campus.activity.service.impl;

import com.campus.activity.entity.Activity;
import com.campus.activity.entity.Registration;
import com.campus.activity.mapper.ActivityMapper;
import com.campus.activity.mapper.RegistrationMapper;
import com.campus.activity.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService {
    
    @Autowired
    private RegistrationMapper registrationMapper;
    
    @Autowired
    private ActivityMapper activityMapper;
    
    @Override
    public Registration getRegistrationById(Integer registerId) {
        return registrationMapper.selectById(registerId);
    }
    
    @Override
    public List<Registration> getRegistrationByUserId(Integer userId) {
        return registrationMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Registration> getRegistrationByActivityId(Integer activityId) {
        return registrationMapper.selectByActivityId(activityId);
    }
    
    @Override
    public List<Registration> getRegistrationByPublisherId(Integer publisherId) {
        return registrationMapper.selectByPublisherId(publisherId);
    }
    
    @Override
    public List<Registration> getAllRegistrations() {
        return registrationMapper.selectAll();
    }
    
    @Override
    public int countByActivityId(Integer activityId) {
        List<Registration> registrations = registrationMapper.selectByActivityId(activityId);
        return registrations != null ? registrations.size() : 0;
    }
    
    @Override
    public int countPassedByActivityId(Integer activityId) {
        List<Registration> registrations = registrationMapper.selectByActivityId(activityId);
        if (registrations == null || registrations.isEmpty()) {
            return 0;
        }
        return (int) registrations.stream()
                .filter(reg -> "通过".equals(reg.getAuditStatus()))
                .count();
    }
    
    @Override
    public int registerActivity(Registration registration) {
        if (checkAlreadyRegistered(registration.getActivityId(), registration.getUserId())) {
            return 0;
        }

        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity == null) {
            return -4; // 活动不存在
        }
        
        if (activity.getMaxPeople() != null) {
            int registeredCount = countPassedByActivityId(registration.getActivityId());
            if (registeredCount >= activity.getMaxPeople()) {
                return -2; // 活动报名人数已满
            }
        }
        
        registration.setAuditStatus("待审");
        return registrationMapper.insert(registration);
    }
    
    @Override
    public int cancelRegistration(Integer registerId) {
        return registrationMapper.deleteById(registerId);
    }
    
    @Override
    public boolean checkAlreadyRegistered(Integer activityId, Integer userId) {
        Registration existing = registrationMapper.selectByActivityAndUser(activityId, userId);
        return existing != null;
    }
}
