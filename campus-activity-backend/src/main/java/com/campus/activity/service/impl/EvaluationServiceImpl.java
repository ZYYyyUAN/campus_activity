package com.campus.activity.service.impl;

import com.campus.activity.entity.Evaluation;
import com.campus.activity.mapper.EvaluationMapper;
import com.campus.activity.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {
    
    @Autowired
    private EvaluationMapper evaluationMapper;
    
    @Override
    public Evaluation getEvaluationById(Integer evalId) {
        return evaluationMapper.selectById(evalId);
    }
    
    @Override
    public List<Evaluation> getAllEvaluations() {
        return evaluationMapper.selectAll();
    }
    
    @Override
    public List<Evaluation> getEvaluationByActivityId(Integer activityId) {
        return evaluationMapper.selectByActivityId(activityId);
    }
    
    @Override
    public List<Evaluation> getEvaluationByUserId(Integer userId) {
        return evaluationMapper.selectByUserId(userId);
    }
    
    @Override
    public List<Evaluation> getEvaluationByKeyword(String keyword) {
        return evaluationMapper.selectByKeyword(keyword);
    }
    
    @Override
    public int addEvaluation(Evaluation evaluation) {
        // 检查是否已评价
        if (checkAlreadyEvaluated(evaluation.getActivityId(), evaluation.getUserId())) {
            return 0;
        }
        
        return evaluationMapper.insert(evaluation);
    }
    
    @Override
    public int updateEvaluation(Evaluation evaluation) {
        return evaluationMapper.update(evaluation);
    }
    
    @Override
    public int deleteEvaluation(Integer evalId) {
        return evaluationMapper.deleteById(evalId);
    }
    
    @Override
    public boolean checkAlreadyEvaluated(Integer activityId, Integer userId) {
        Evaluation existing = evaluationMapper.selectByActivityAndUser(activityId, userId);
        return existing != null;
    }
}
