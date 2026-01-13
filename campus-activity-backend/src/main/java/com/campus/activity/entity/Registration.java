package com.campus.activity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Registration implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer registerId;
    private Integer activityId;
    private Integer userId;
    private Date registerTime;
    private String auditStatus; 
    
    // 关联的活动信息（transient字段，用于前端展示）
    private String activityName;
    private String activityType;
    private String location;
    private Date startTime;
    private Date endTime;
    
    // 关联的用户信息
    private String userName;
    private String realName;
}

