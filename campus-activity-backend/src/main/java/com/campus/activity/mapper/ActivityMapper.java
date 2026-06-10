package com.campus.activity.mapper;

import com.campus.activity.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    
    @Select("CALL query_activity(#{activityType}, #{startTime}, #{endTime})")
    List<Activity> queryActivityByProcedure(@Param("activityType") String activityType,
                                            @Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime);

    int insert(Activity activity);

    int update(Activity activity);

    int deleteById(Integer activityId);
}
