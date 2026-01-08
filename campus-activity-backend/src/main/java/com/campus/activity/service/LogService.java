package com.campus.activity.service;

import com.campus.activity.entity.Log;
import java.util.Date;
import java.util.List;

/**
 * 系统日志服务接口
 */
public interface LogService {
    
    /**
     * 根据ID查询日志
     */
    Log getLogById(Integer logId);
    
    /**
     * 查询所有日志
     */
    List<Log> getAllLogs();
    
    /**
     * 根据用户ID查询日志
     */
    List<Log> getLogsByUserId(Integer userId);
    
    /**
     * 根据操作类型查询日志
     */
    List<Log> getLogsByOperationType(String operationType);
    
    /**
     * 多条件查询日志
     */
    List<Log> queryLogs(Integer userId, String operationType, 
                       Date startTime, Date endTime);
    
    /**
     * 添加日志
     */
    int addLog(Log log);
    
    /**
     * 删除日志
     */
    int deleteLog(Integer logId);
    
    /**
     * 清理过期日志
     */
    int cleanExpiredLogs(Date date);
}

