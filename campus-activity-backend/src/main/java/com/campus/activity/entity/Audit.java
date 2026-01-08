package com.campus.activity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Audit implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer auditId;
    private Integer registerId;
    private Integer auditorId;
    private String auditResult;
    private Date auditTime;
    private String auditComment;
    
    private String auditorName;
    private String studentName;
    private String activityName;
}

