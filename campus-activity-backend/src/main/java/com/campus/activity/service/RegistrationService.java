package com.campus.activity.service;

import com.campus.activity.entity.Registration;
import java.util.List;

public interface RegistrationService {
    
    Registration getRegistrationById(Integer registerId);
    
    List<Registration> getRegistrationByUserId(Integer userId);
    
    List<Registration> getRegistrationByActivityId(Integer activityId);
    
    List<Registration> getRegistrationByPublisherId(Integer publisherId);
    
    List<Registration> getAllRegistrations();
    
    int countByActivityId(Integer activityId);
    
    int countPassedByActivityId(Integer activityId);
    
    int registerActivity(Registration registration);
    
    int cancelRegistration(Integer registerId);
    
    boolean checkAlreadyRegistered(Integer activityId, Integer userId);
}
