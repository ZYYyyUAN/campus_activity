package com.campus.activity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SignScore implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer signId;
    private Integer activityId;
    private Integer userId;
    private Date signTime;
    private Integer score;
    
    private String activityName;
    private String userName;
    private String realName;
}

