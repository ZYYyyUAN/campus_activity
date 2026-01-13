package com.campus.activity.controller;

import com.campus.activity.common.Result;
import com.campus.activity.entity.Log;
import com.campus.activity.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/log")
@CrossOrigin
public class LogController {
    
    @Autowired
    private LogService logService;
    
    @GetMapping("/{logId}")
    public Result<Log> getLogById(@PathVariable Integer logId) {
        Log log = logService.getLogById(logId);
        if (log != null) {
            return Result.success(log);
        }
        return Result.error("日志不存在");
    }
    
    @GetMapping("/list")
    public Result<List<Log>> getAllLogs() {
        List<Log> logs = logService.getAllLogs();
        return Result.success(logs);
    }

    @GetMapping("/user/{userId}")
    public Result<List<Log>> getLogsByUserId(@PathVariable Integer userId) {
        List<Log> logs = logService.getLogByUserId(userId);
        return Result.success(logs);
    }

    @GetMapping("/type/{operationType}")
    public Result<List<Log>> getLogsByOperationType(@PathVariable String operationType) {
        List<Log> logs = logService.getLogByOperationType(operationType);
        return Result.success(logs);
    }
    
    @GetMapping("/query")
    public Result<List<Log>> queryLogs(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate,
            @RequestParam(required = false) String operationType) {
        List<Log> logs = logService.queryLog(startDate, endDate, operationType);
        return Result.success(logs);
    }
    
    @DeleteMapping("/{logId}")
    public Result<String> deleteLog(@PathVariable Integer logId) {
        int result = logService.deleteLog(logId);
        if (result > 0) {
            return Result.success("日志删除成功", null);
        }
        return Result.error("日志删除失败");
    }
    
}
