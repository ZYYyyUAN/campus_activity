package com.campus.activity.mapper;

import com.campus.activity.entity.Registration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RegistrationMapper {

    Registration selectById(Integer registerId);

    List<Registration> selectByActivityId(Integer activityId);
    
    List<Registration> selectByUserId(Integer userId);
    
    Registration selectByActivityAndUser(@Param("activityId") Integer activityId, 
                                         @Param("userId") Integer userId);
    
    int countByActivityId(Integer activityId);

    int countPassedByActivityId(Integer activityId);

    int insert(Registration registration);
    
    int updateAuditStatus(@Param("registerId") Integer registerId, 
                         @Param("auditStatus") String auditStatus);
    
    int deleteById(Integer registerId);
}
