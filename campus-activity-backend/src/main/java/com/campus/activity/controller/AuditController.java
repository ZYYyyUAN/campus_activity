package com.campus.activity.controller;

import com.campus.activity.common.Result;
import com.campus.activity.entity.Audit;
import com.campus.activity.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
@CrossOrigin
public class AuditController {
    
    @Autowired
    private AuditService auditService;
    
    @GetMapping("/{auditId}")
    public Result<Audit> getAuditById(@PathVariable Integer auditId) {
        Audit audit = auditService.getAuditById(auditId);
        if (audit != null) {
            return Result.success(audit);
        }
        return Result.error("审核记录不存在");
    }
    
    @GetMapping("/activity/{activityId}")
    public Result<List<Audit>> getAuditByActivityId(@PathVariable Integer activityId) {
        List<Audit> audits = auditService.getAuditByActivityId(activityId);
        return Result.success(audits);
    }
    
    @GetMapping("/auditor/{auditorId}")
    public Result<List<Audit>> getAuditByAuditorId(@PathVariable Integer auditorId) {
        List<Audit> audits = auditService.getAuditByAuditorId(auditorId);
        return Result.success(audits);
    }
    
    @PostMapping("/add")
    public Result<String> auditRegistration(@RequestBody Audit audit) {
        int result = auditService.auditRegistration(audit);
        if (result > 0) {
            return Result.success("审核成功", null);
        }
        return Result.error("审核失败");
    }
}
