package com.campus.activity.service.impl;

import com.campus.activity.entity.Log;
import com.campus.activity.mapper.LogMapper;
import com.campus.activity.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 系统日志服务实现类
 */
@Service
@Transactional
public class LogServiceImpl implements LogService {
    
    @Autowired
    private LogMapper logMapper;
    
    @Override
    public Log getLogById(Integer logId) {
        return logMapper.selectById(logId);
    }
    
    @Override
    public List<Log> getAllLogs() {
        return logMapper.selectAll();
    }
    
    @Override
    public List<Log> getLogsByUserId(Integer userId) {
        return logMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Log> getLogsByOperationType(String operationType) {
        return logMapper.selectByOperationType(operationType);
    }
    
    @Override
    public List<Log> queryLogs(Integer userId, String operationType, 
                               Date startTime, Date endTime) {
        return logMapper.selectByConditions(userId, operationType, startTime, endTime);
    }
    
    @Override
    public int addLog(Log log) {
        if (log.getOperationTime() == null) {
            log.setOperationTime(new Date());
        }
        return logMapper.insert(log);
    }
    
    @Override
    public int deleteLog(Integer logId) {
        return logMapper.deleteById(logId);
    }
    
    @Override
    public int cleanExpiredLogs(Date date) {
        return logMapper.deleteBeforeDate(date);
    }
}

