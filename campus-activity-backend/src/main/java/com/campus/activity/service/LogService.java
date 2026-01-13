package com.campus.activity.service;

import com.campus.activity.entity.Log;
import java.util.Date;
import java.util.List;

public interface LogService {
    
    Log getLogById(Integer logId);
    
    List<Log> getAllLogs();
    
    List<Log> getLogByUserId(Integer userId);
    
    List<Log> getLogByOperationType(String operationType);
    
    List<Log> queryLog(Date startDate, Date endDate, String operationType);
    
    int addLog(Integer userId, String operationType, String operationContent);
    
    int deleteLog(Integer logId);
}
