package com.campus.activity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Activity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer activityId;
    private String activityName;
    private String activityType;
    private String location;
    private Date startTime;
    private Date endTime;
    private Integer maxPeople;
    private Integer publisherId;
    private String status;  
    
    private String publisherName;
}

