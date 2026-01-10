package com.campus.activity.mapper;

import com.campus.activity.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface ActivityMapper {
    
    Activity selectById(Integer activityId);

    List<Activity> selectAll();
    
    List<Activity> selectByPublisherId(Integer publisherId);

    List<Activity> selectByConditions(@Param("activityType") String activityType,
                                     @Param("startTime") Date startTime,
                                     @Param("endTime") Date endTime,
                                     @Param("keyword") String keyword,
                                     @Param("publisherId") Integer publisherId);

    int insert(Activity activity);

    int update(Activity activity);

    int deleteById(Integer activityId);
    
    int count();
}