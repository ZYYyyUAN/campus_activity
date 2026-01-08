package com.campus.activity.mapper;

import com.campus.activity.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

/**
 * 系统日志Mapper接口
 */
@Mapper
public interface LogMapper {
    
    /**
     * 根据日志ID查询
     */
    Log selectById(Integer logId);
    
    /**
     * 查询所有日志
     */
    List<Log> selectAll();
    
    /**
     * 根据用户ID查询日志
     */
    List<Log> selectByUserId(Integer userId);
    
    /**
     * 根据操作类型查询
     */
    List<Log> selectByOperationType(String operationType);
    
    /**
     * 根据时间范围查询
     */
    List<Log> selectByTimeRange(@Param("startTime") Date startTime, 
                                @Param("endTime") Date endTime);
    
    /**
     * 多条件查询日志
     */
    List<Log> selectByConditions(@Param("userId") Integer userId,
                                 @Param("operationType") String operationType,
                                 @Param("startTime") Date startTime,
                                 @Param("endTime") Date endTime);
    
    /**
     * 插入日志
     */
    int insert(Log log);
    
    /**
     * 删除日志
     */
    int deleteById(Integer logId);
    
    /**
     * 删除指定时间之前的日志
     */
    int deleteBeforeDate(Date date);
    
    /**
     * 统计日志数量
     */
    int count();
}

