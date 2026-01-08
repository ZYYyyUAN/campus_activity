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
    
    private String activityName;
    private String userName;
    private String realName;
}

