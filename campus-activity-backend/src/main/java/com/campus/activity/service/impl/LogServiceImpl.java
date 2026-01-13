package com.campus.activity.service.impl;

import com.campus.activity.entity.Log;
import com.campus.activity.mapper.LogMapper;
import com.campus.activity.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    public List<Log> getLogByUserId(Integer userId) {
        return logMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Log> getLogByOperationType(String operationType) {
        return logMapper.selectByOperationType(operationType);
    }
    
    @Override
    public List<Log> queryLog(Date startDate, Date endDate, String operationType) {
        return logMapper.selectByConditions(null, operationType, startDate, endDate);
    }
    
    @Override
    public int addLog(Integer userId, String operationType, String operationContent) {
        Log log = new Log();
        log.setUserId(userId);
        log.setOperationType(operationType);
        log.setOperationContent(operationContent);
        log.setOperationTime(new Date());
        return logMapper.insert(log);
    }
    
    @Override
    public int deleteLog(Integer logId) {
        return logMapper.deleteById(logId);
    }
}
