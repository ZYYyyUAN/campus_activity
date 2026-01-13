package com.campus.activity.controller;

import com.campus.activity.common.Result;
import com.campus.activity.entity.Evaluation;
import com.campus.activity.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluation")
@CrossOrigin
public class EvaluationController {
    
    @Autowired
    private EvaluationService evaluationService;
    
    @GetMapping("/{evalId}")
    public Result<Evaluation> getEvaluationById(@PathVariable Integer evalId) {
        Evaluation evaluation = evaluationService.getEvaluationById(evalId);
        if (evaluation != null) {
            return Result.success(evaluation);
        }
        return Result.error("评价记录不存在");
    }
    
    @GetMapping("/all")
    public Result<List<Evaluation>> getAllEvaluations() {
        List<Evaluation> evaluations = evaluationService.getAllEvaluations();
        return Result.success(evaluations);
    }
    
    @GetMapping("/activity/{activityId}")
    public Result<List<Evaluation>> getEvaluationByActivityId(@PathVariable Integer activityId) {
        List<Evaluation> evaluations = evaluationService.getEvaluationByActivityId(activityId);
        return Result.success(evaluations);
    }
    
    @GetMapping("/user/{userId}")
    public Result<List<Evaluation>> getEvaluationByUserId(@PathVariable Integer userId) {
        List<Evaluation> evaluations = evaluationService.getEvaluationByUserId(userId);
        return Result.success(evaluations);
    }
    
    @GetMapping("/keyword/{keyword}")
    public Result<List<Evaluation>> getEvaluationByKeyword(@PathVariable String keyword) {
        List<Evaluation> evaluations = evaluationService.getEvaluationByKeyword(keyword);
        return Result.success(evaluations);
    }
    
    @PostMapping("/add")
    public Result<String> addEvaluation(@RequestBody Evaluation evaluation) {
        int result = evaluationService.addEvaluation(evaluation);
        if (result > 0) {
            return Result.success("评价成功", null);
        }
        return Result.error("您已经评价过该活动或评价失败");
    }
    
    @PutMapping("/update")
    public Result<String> updateEvaluation(@RequestBody Evaluation evaluation) {
        int result = evaluationService.updateEvaluation(evaluation);
        if (result > 0) {
            return Result.success("更新成功", null);
        }
        return Result.error("更新失败");
    }
    
    @DeleteMapping("/{evalId}")
    public Result<String> deleteEvaluation(@PathVariable Integer evalId) {
        int result = evaluationService.deleteEvaluation(evalId);
        if (result > 0) {
            return Result.success("删除成功", null);
        }
        return Result.error("删除失败");
    }
}
