package com.campus.activity.controller;

import com.campus.activity.common.Result;
import com.campus.activity.entity.Activity;
import com.campus.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 活动控制器
 */
@RestController
@RequestMapping("/activity")
@CrossOrigin
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;
    
    /**
     * 根据ID查询活动
     */
    @GetMapping("/{activityId}")
    public Result<Activity> getActivityById(@PathVariable Integer activityId) {
        Activity activity = activityService.getActivityById(activityId);
        if (activity != null) {
            return Result.success(activity);
        }
        return Result.error("活动不存在");
    }
    
    /**
     * 查询所有活动
     */
    @GetMapping("/list")
    public Result<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        return Result.success(activities);
    }
    
    /**
     * 根据发布者ID查询活动
     */
    @GetMapping("/publisher/{publisherId}")
    public Result<List<Activity>> getActivitiesByPublisherId(@PathVariable Integer publisherId) {
        List<Activity> activities = activityService.getActivitiesByPublisherId(publisherId);
        return Result.success(activities);
    }
    
    /**
     * 多条件查询活动
     */
    @GetMapping("/query")
    public Result<List<Activity>> queryActivities(
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer publisherId) {
        List<Activity> activities = activityService.queryActivities(
                activityType, startTime, endTime, keyword, publisherId);
        return Result.success(activities);
    }
    
    /**
     * 添加活动
     */
    @PostMapping("/add")
    public Result<String> addActivity(@RequestBody Activity activity) {
        // 校验活动时间
        if (!activityService.validateActivityTime(activity.getStartTime(), activity.getEndTime())) {
            return Result.error("活动时间设置不合理");
        }
        
        int result = activityService.addActivity(activity);
        if (result > 0) {
            return Result.success("活动发布成功", null);
        }
        return Result.error("活动发布失败");
    }
    
    /**
     * 更新活动
     */
    @PutMapping("/update")
    public Result<String> updateActivity(@RequestBody Activity activity) {
        // 校验活动时间
        if (!activityService.validateActivityTime(activity.getStartTime(), activity.getEndTime())) {
            return Result.error("活动时间设置不合理");
        }
        
        int result = activityService.updateActivity(activity);
        if (result > 0) {
            return Result.success("活动更新成功", null);
        }
        return Result.error("活动更新失败");
    }
    
    /**
     * 删除活动
     */
    @DeleteMapping("/{activityId}")
    public Result<String> deleteActivity(@PathVariable Integer activityId) {
        int result = activityService.deleteActivity(activityId);
        if (result > 0) {
            return Result.success("活动删除成功", null);
        }
        return Result.error("活动删除失败");
    }
    
    /**
     * 检查活动是否已满
     */
    @GetMapping("/checkFull/{activityId}")
    public Result<Boolean> checkActivityFull(@PathVariable Integer activityId) {
        boolean isFull = activityService.checkActivityFull(activityId);
        return Result.success(isFull);
    }
}

