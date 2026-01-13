package com.campus.activity.controller;

import com.campus.activity.common.Result;
import com.campus.activity.entity.Registration;
import com.campus.activity.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registration")
@CrossOrigin
public class RegistrationController {
    
    @Autowired
    private RegistrationService registrationService;
    
    @GetMapping("/{registerId}")
    public Result<Registration> getRegistrationById(@PathVariable Integer registerId) {
        Registration registration = registrationService.getRegistrationById(registerId);
        if (registration != null) {
            return Result.success(registration);
        }
        return Result.error("报名记录不存在");
    }
    
    @GetMapping("/user/{userId}")
    public Result<List<Registration>> getRegistrationByUserId(@PathVariable Integer userId) {
        List<Registration> registrations = registrationService.getRegistrationByUserId(userId);
        return Result.success(registrations);
    }
    
    @GetMapping("/activity/{activityId}")
    public Result<List<Registration>> getRegistrationByActivityId(@PathVariable Integer activityId) {
        List<Registration> registrations = registrationService.getRegistrationByActivityId(activityId);
        return Result.success(registrations);
    }
    
    @GetMapping("/activity/{activityId}/count")
    public Result<Integer> getRegistrationCountByActivityId(@PathVariable Integer activityId) {
        int count = registrationService.countByActivityId(activityId);
        return Result.success(count);
    }
    
    @GetMapping("/activity/{activityId}/count/passed")
    public Result<Integer> getPassedRegistrationCountByActivityId(@PathVariable Integer activityId) {
        int count = registrationService.countPassedByActivityId(activityId);
        return Result.success(count);
    }
    
    @GetMapping("/publisher/{publisherId}")
    public Result<List<Registration>> getRegistrationByPublisherId(@PathVariable Integer publisherId) {
        List<Registration> registrations = registrationService.getRegistrationByPublisherId(publisherId);
        return Result.success(registrations);
    }
    
    @GetMapping("/all")
    public Result<List<Registration>> getAllRegistrations() {
        List<Registration> registrations = registrationService.getAllRegistrations();
        return Result.success(registrations);
    }
       
    @PostMapping("/add")
    public Result<String> registerActivity(@RequestBody Registration registration) {
        int result = registrationService.registerActivity(registration);
        if (result > 0) {
            return Result.success("报名成功", null);
        } else if (result == 0) {
            return Result.error("您已经报名过该活动");
        } else if (result == -2) {
            return Result.error("活动报名人数已满");
        } else if (result == -3) {
            return Result.error("活动不在报名期内");
        } else if (result == -4) {
            return Result.error("活动不存在");
        }
        return Result.error("报名失败");
    }
    
    @DeleteMapping("/{registerId}")
    public Result<String> cancelRegistration(@PathVariable Integer registerId) {
        int result = registrationService.cancelRegistration(registerId);
        if (result > 0) {
            return Result.success("取消报名成功", null);
        }
        return Result.error("取消报名失败");
    }
}
