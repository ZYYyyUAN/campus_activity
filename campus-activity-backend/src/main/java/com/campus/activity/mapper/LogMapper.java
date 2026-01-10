package com.campus.activity.mapper;

import com.campus.activity.entity.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface LogMapper {
    
    Log selectById(Integer logId);
    
    List<Log> selectAll();
    
    List<Log> selectByUserId(Integer userId);
    
    List<Log> selectByOperationType(String operationType);
    
    List<Log> selectByTimeRange(@Param("startTime") Date startTime, 
                                @Param("endTime") Date endTime);
    
    List<Log> selectByConditions(@Param("userId") Integer userId,
                                 @Param("operationType") String operationType,
                                 @Param("startTime") Date startTime,
                                 @Param("endTime") Date endTime);
    
    int insert(Log log);
    
    int deleteById(Integer logId);
    
    int deleteBeforeDate(Date date); //删除指定时间之前的日志

    int count();
}

