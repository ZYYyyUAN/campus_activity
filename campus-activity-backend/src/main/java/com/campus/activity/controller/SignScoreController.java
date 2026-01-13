package com.campus.activity.controller;

import com.campus.activity.common.Result;
import com.campus.activity.entity.SignScore;
import com.campus.activity.service.SignScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/signscore")
@CrossOrigin
public class SignScoreController {
    
    @Autowired
    private SignScoreService signScoreService;
    
    @GetMapping("/{signId}")
    public Result<SignScore> getSignScoreById(@PathVariable Integer signId) {
        SignScore signScore = signScoreService.getSignScoreById(signId);
        if (signScore != null) {
            return Result.success(signScore);
        }
        return Result.error("签到记录不存在");
    }
    
    @GetMapping("/user/{userId}")
    public Result<List<SignScore>> getSignListByUserId(@PathVariable Integer userId) {
        List<SignScore> signScores = signScoreService.getSignScoreByUserId(userId);
        return Result.success(signScores);
    }
    
    @GetMapping("/activity/{activityId}")
    public Result<List<SignScore>> getSignListByActivityId(@PathVariable Integer activityId) {
        List<SignScore> signScores = signScoreService.getSignScoreByActivityId(activityId);
        return Result.success(signScores);
    }
    
    @GetMapping("/activity/{activityId}/count")
    public Result<Integer> getSignCountByActivityId(@PathVariable Integer activityId) {
        int count = signScoreService.countByActivityId(activityId);
        return Result.success(count);
    }
    
    @PostMapping("/sign")
    public Result<String> signActivity(@RequestBody SignScore signScore) {
        int result = signScoreService.signActivity(signScore);
        if (result > 0) {
            return Result.success("签到成功", null);
        } else if (result == -1) {
            return Result.error("未报名或未通过审核，无法签到");
        }
        return Result.error("您已经签到过该活动或签到失败");
    }
    
    @GetMapping("/total/{userId}")
    public Result<Integer> getTotalScore(@PathVariable Integer userId) {
        Integer totalScore = signScoreService.getTotalScoreByUserId(userId);
        return Result.success(totalScore);
    }
}
