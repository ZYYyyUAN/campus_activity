package com.campus.activity.service.impl;

import com.campus.activity.entity.Activity;
import com.campus.activity.entity.Registration;
import com.campus.activity.entity.SignScore;
import com.campus.activity.mapper.RegistrationMapper;
import com.campus.activity.mapper.SignScoreMapper;
import com.campus.activity.service.ActivityService;
import com.campus.activity.service.LogService;
import com.campus.activity.service.SignScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SignScoreServiceImpl implements SignScoreService {
    
    @Autowired
    private SignScoreMapper signScoreMapper;
    
    @Autowired
    private RegistrationMapper registrationMapper;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private LogService logService;
    
    @Override
    public SignScore getSignScoreById(Integer signId) {
        return signScoreMapper.selectById(signId);
    }
    
    @Override
    public List<SignScore> getSignScoreByUserId(Integer userId) {
        return signScoreMapper.selectByUserId(userId);
    }
    
    @Override
    public List<SignScore> getSignScoreByActivityId(Integer activityId) {
        return signScoreMapper.selectByActivityId(activityId);
    }
    
    @Override
    public int countByActivityId(Integer activityId) {
        List<SignScore> signScores = signScoreMapper.selectByActivityId(activityId);
        return signScores != null ? signScores.size() : 0;
    }
    
    @Override
    public Integer getTotalScoreByUserId(Integer userId) {
        List<SignScore> signScores = signScoreMapper.selectByUserId(userId);
        if (signScores == null || signScores.isEmpty()) {
            return 0;
        }
        return signScores.stream()
                .mapToInt(score -> score.getScore() != null ? score.getScore() : 0)
                .sum();
    }
    
    @Override
    public int signActivity(SignScore signScore) {
        // 1. 检查是否已签到
        if (checkAlreadySigned(signScore.getActivityId(), signScore.getUserId())) {
            return 0; // 已签到
        }
        
        // 2. 检查是否已报名并通过审核
        Registration registration = registrationMapper.selectByActivityAndUser(
            signScore.getActivityId(), signScore.getUserId());
        if (registration == null || !"通过".equals(registration.getAuditStatus())) {
            return -1; // 未报名或未通过审核
        }
        
        // 3. 设置默认积分
        if (signScore.getScore() == null) {
            signScore.setScore(10);
        }
        
        // 4. 使用存储过程签到
        int result = signScoreMapper.signByProcedure(
            signScore.getActivityId(), 
            signScore.getUserId(), 
            signScore.getScore());
        
        // 5. 记录日志
        if (result > 0) {
            Activity activity = activityService.getActivityById(signScore.getActivityId());
            if (activity != null) {
                logService.addLog(signScore.getUserId(), "活动签到", 
                    "签到活动：" + activity.getActivityName() + "，获得积分：" + signScore.getScore());
            }
        }
        
        return result;
    }
    
    @Override
    public boolean checkAlreadySigned(Integer activityId, Integer userId) {
        SignScore existing = signScoreMapper.selectByActivityAndUser(activityId, userId);
        return existing != null;
    }
}
