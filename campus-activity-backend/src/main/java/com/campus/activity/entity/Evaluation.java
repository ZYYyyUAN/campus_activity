package com.campus.activity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Evaluation implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer evalId;
    private Integer activityId;
    private Integer userId;
    private Integer rating;  
    private String content;
    private Date evalTime;
    
    private String activityName;
    private String userName;
    private String realName;
}

