package com.campus.activity.service;

import com.campus.activity.entity.Evaluation;
import java.util.List;

public interface EvaluationService {
    
    Evaluation getEvaluationById(Integer evalId);
    
    List<Evaluation> getAllEvaluations();
    
    List<Evaluation> getEvaluationByActivityId(Integer activityId);
    
    List<Evaluation> getEvaluationByUserId(Integer userId);
    
    List<Evaluation> getEvaluationByKeyword(String keyword);
    
    int addEvaluation(Evaluation evaluation);
    
    int updateEvaluation(Evaluation evaluation);
    
    int deleteEvaluation(Integer evalId);
    
    boolean checkAlreadyEvaluated(Integer activityId, Integer userId);
}
