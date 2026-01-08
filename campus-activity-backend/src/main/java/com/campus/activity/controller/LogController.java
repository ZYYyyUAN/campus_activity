package com.campus.activity.controller;

import com.campus.activity.common.Result;
import com.campus.activity.entity.Log;
import com.campus.activity.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 系统日志控制器
 */
@RestController
@RequestMapping("/log")
@CrossOrigin
public class LogController {
    
    @Autowired
    private LogService logService;
    
    /**
     * 根据ID查询日志
     */
    @GetMapping("/{logId}")
    public Result<Log> getLogById(@PathVariable Integer logId) {
        Log log = logService.getLogById(logId);
        if (log != null) {
            return Result.success(log);
        }
        return Result.error("日志不存在");
    }
    
    /**
     * 查询所有日志
     */
    @GetMapping("/list")
    public Result<List<Log>> getAllLogs() {
        List<Log> logs = logService.getAllLogs();
        return Result.success(logs);
    }
    
    /**
     * 根据用户ID查询日志
     */
    @GetMapping("/user/{userId}")
    public Result<List<Log>> getLogsByUserId(@PathVariable Integer userId) {
        List<Log> logs = logService.getLogsByUserId(userId);
        return Result.success(logs);
    }
    
    /**
     * 根据操作类型查询日志
     */
    @GetMapping("/type/{operationType}")
    public Result<List<Log>> getLogsByOperationType(@PathVariable String operationType) {
        List<Log> logs = logService.getLogsByOperationType(operationType);
        return Result.success(logs);
    }
    
    /**
     * 多条件查询日志
     */
    @GetMapping("/query")
    public Result<List<Log>> queryLogs(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        List<Log> logs = logService.queryLogs(userId, operationType, startTime, endTime);
        return Result.success(logs);
    }
    
    /**
     * 删除日志
     */
    @DeleteMapping("/{logId}")
    public Result<String> deleteLog(@PathVariable Integer logId) {
        int result = logService.deleteLog(logId);
        if (result > 0) {
            return Result.success("日志删除成功", null);
        }
        return Result.error("日志删除失败");
    }
    
    /**
     * 清理过期日志
     */
    @DeleteMapping("/clean")
    public Result<String> cleanExpiredLogs(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        int result = logService.cleanExpiredLogs(date);
        return Result.success("已清理" + result + "条过期日志", null);
    }
}

