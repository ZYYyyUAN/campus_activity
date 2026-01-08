package com.campus.activity.mapper;

import com.campus.activity.entity.Registration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RegistrationMapper {

    Registration selectById(Integer registerId);

    List<Registration> selectByActivityId(Integer activityId);
    
    /**
     * 根据用户ID查询报名列表
     */
    List<Registration> selectByUserId(Integer userId);
    
    /**
     * 查询用户对某个活动的报名
     */
    Registration selectByActivityAndUser(@Param("activityId") Integer activityId, 
                                         @Param("userId") Integer userId);
    
    /**
     * 统计活动的报名人数
     */
    int countByActivityId(Integer activityId);
    
    /**
     * 统计活动的审核通过人数
     */
    int countPassedByActivityId(Integer activityId);
    
    /**
     * 插入报名记录
     */
    int insert(Registration registration);
    
    /**
     * 更新报名状态
     */
    int updateAuditStatus(@Param("registerId") Integer registerId, 
                         @Param("auditStatus") String auditStatus);
    
    /**
     * 删除报名记录
     */
    int deleteById(Integer registerId);
}

