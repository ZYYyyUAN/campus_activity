package com.campus.activity.service;

import com.campus.activity.entity.Log;
import java.util.Date;
import java.util.List;


public interface LogService {
    

    Log getLogById(Integer logId);

    List<Log> getAllLogs();
    
    List<Log> getLogsByUserId(Integer userId);
    
    List<Log> getLogsByOperationType(String operationType);
    
    List<Log> queryLogs(Integer userId, String operationType, 
                       Date startTime, Date endTime);  //多条件查询
    
    int addLog(Log log);
    
    int deleteLog(Integer logId);
    
    int cleanExpiredLogs(Date date); //清理过期日志
}

