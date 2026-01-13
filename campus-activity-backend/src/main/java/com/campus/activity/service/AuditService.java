package com.campus.activity.service;

import com.campus.activity.entity.Audit;
import java.util.List;

public interface AuditService {
    
    Audit getAuditById(Integer auditId);
    
    Audit getAuditByRegisterId(Integer registerId);
    
    List<Audit> getAuditByActivityId(Integer activityId);
    
    List<Audit> getAuditByAuditorId(Integer auditorId);
    
    int auditRegistration(Audit audit);
}
