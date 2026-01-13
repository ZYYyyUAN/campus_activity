package com.campus.activity.controller;

import com.campus.activity.common.Result;
import com.campus.activity.entity.Activity;
import com.campus.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/activity")
@CrossOrigin
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;

    @GetMapping("/{activityId}")
    public Result<Activity> getActivityById(@PathVariable Integer activityId) {
        Activity activity = activityService.getActivityById(activityId);
        if (activity != null) {
            return Result.success(activity);
        }
        return Result.error("活动不存在");
    }

    @GetMapping("/list")
    public Result<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        return Result.success(activities);
    }
    
    @GetMapping("/publisher/{publisherId}")
    public Result<List<Activity>> getActivitiesByPublisherId(@PathVariable Integer publisherId) {
        List<Activity> activities = activityService.getActivitiesByPublisherId(publisherId);
        return Result.success(activities);
    }
    
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

    @PostMapping("/add")
    public Result<String> addActivity(@RequestBody Activity activity) {
        try {
            int result = activityService.addActivity(activity);
            if (result > 0) {
                return Result.success("活动发布成功", null);
            }
            return Result.error("活动发布失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/update")
    public Result<String> updateActivity(@RequestBody Activity activity) {
        try {
            int result = activityService.updateActivity(activity);
            if (result > 0) {
                return Result.success("活动更新成功", null);
            }
            return Result.error("活动更新失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{activityId}")
    public Result<String> deleteActivity(@PathVariable Integer activityId) {
        int result = activityService.deleteActivity(activityId);
        if (result > 0) {
            return Result.success("活动删除成功", null);
        }
        return Result.error("活动删除失败");
    }
    
    @GetMapping("/checkFull/{activityId}")
    public Result<Boolean> checkActivityFull(@PathVariable Integer activityId) {
        boolean isFull = activityService.checkActivityFull(activityId);
        return Result.success(isFull);
    }

    @GetMapping("/query/procedure")
    public Result<List<Activity>> queryActivitiesByProcedure(
            @RequestParam(required = false) String activityType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        List<Activity> activities = activityService.queryActivitiesByProcedure(activityType, startTime, endTime);
        return Result.success(activities);
    }
}

