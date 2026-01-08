package com.campus.activity.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer logId;
    private Integer userId;
    private String operationType;
    private String operationContent;
    private Date operationTime;
    
    private String userName;
    private String realName;
}

