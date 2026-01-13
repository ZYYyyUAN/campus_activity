package com.campus.activity.service.impl;

import com.campus.activity.entity.Activity;
import com.campus.activity.entity.Audit;
import com.campus.activity.entity.Registration;
import com.campus.activity.mapper.AuditMapper;
import com.campus.activity.mapper.RegistrationMapper;
import com.campus.activity.service.ActivityService;
import com.campus.activity.service.AuditService;
import com.campus.activity.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {
    
    @Autowired
    private AuditMapper auditMapper;
    
    @Autowired
    private RegistrationMapper registrationMapper;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private LogService logService;
    
    @Override
    public Audit getAuditById(Integer auditId) {
        return auditMapper.selectById(auditId);
    }
    
    @Override
    public Audit getAuditByRegisterId(Integer registerId) {
        return auditMapper.selectByRegisterId(registerId);
    }
    
    @Override
    public List<Audit> getAuditByActivityId(Integer activityId) {
        // 先查询该活动的所有报名记录
        List<Registration> registrations = registrationMapper.selectByActivityId(activityId);
        List<Audit> audits = new ArrayList<>();
        
        // 为每个报名记录查询审核记录
        for (Registration registration : registrations) {
            Audit audit = auditMapper.selectByRegisterId(registration.getRegisterId());
            if (audit != null) {
                audits.add(audit);
            }
        }
        
        return audits;
    }
    
    @Override
    public List<Audit> getAuditByAuditorId(Integer auditorId) {
        return auditMapper.selectByAuditorId(auditorId);
    }
    
    @Override
    public int auditRegistration(Audit audit) {
        // 1. 更新Registration表的审核状态
        String auditStatus = "通过".equals(audit.getAuditResult()) ? "通过" : "拒绝";
        registrationMapper.updateAuditStatus(audit.getRegisterId(), auditStatus);
        
        // 2. 插入审核记录
        int result = auditMapper.insert(audit);
        
        // 3. 记录日志
        if (result > 0) {
            Registration registration = registrationMapper.selectById(audit.getRegisterId());
            if (registration != null) {
                Activity activity = activityService.getActivityById(registration.getActivityId());
                String activityName = activity != null ? activity.getActivityName() : "未知活动";
                logService.addLog(audit.getAuditorId(), "审核报名", 
                    "审核报名：" + activityName + "，审核结果：" + audit.getAuditResult());
            }
        }
        return result;
    }
}
