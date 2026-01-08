package com.campus.activity.mapper;

import com.campus.activity.entity.Audit;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 审核记录Mapper接口
 */
@Mapper
public interface AuditMapper {
    
    /**
     * 根据审核ID查询
     */
    Audit selectById(Integer auditId);
    
    /**
     * 根据报名ID查询审核记录
     */
    Audit selectByRegisterId(Integer registerId);
    
    /**
     * 根据审核人ID查询
     */
    List<Audit> selectByAuditorId(Integer auditorId);
    
    /**
     * 插入审核记录
     */
    int insert(Audit audit);
}

