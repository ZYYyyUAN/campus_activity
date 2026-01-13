package com.campus.activity.service;

import com.campus.activity.entity.SignScore;
import java.util.List;

public interface SignScoreService {
    
    SignScore getSignScoreById(Integer signId);
    
    List<SignScore> getSignScoreByUserId(Integer userId);
    
    List<SignScore> getSignScoreByActivityId(Integer activityId);
    
    int countByActivityId(Integer activityId);
    
    Integer getTotalScoreByUserId(Integer userId);
    
    int signActivity(SignScore signScore);
    
    boolean checkAlreadySigned(Integer activityId, Integer userId);
}
