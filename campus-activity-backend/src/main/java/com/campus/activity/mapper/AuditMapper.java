package com.campus.activity.mapper;

import com.campus.activity.entity.Audit;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AuditMapper {

    Audit selectById(Integer auditId);
    
    Audit selectByRegisterId(Integer registerId);

    List<Audit> selectByAuditorId(Integer auditorId);
    
    int insert(Audit audit);
}