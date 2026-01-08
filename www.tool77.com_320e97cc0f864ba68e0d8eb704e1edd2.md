# **4. 数据库物理设计及实施**
**（这一部分重点写自己负责的模块的相关内容）**

**（选用具体DBMS，创建数据库，数据库的配置参数如：大小，存放位置，增长方式等，创建各个表，包括主码、外码、自定义约束等、视图、索引的SQL语句）**
## **4.1 创建数据库**
## **4.2 创建表**
## **4.3 SQL实现**
**（介绍自己负责模块的重点SQL、存储过程、触发器等）**
# **4. 数据库物理设计及实施**（活动信息查询与统计模块）
本系统选用MySQL 8.0作为数据库管理系统，本人主要负责活动信息查询与统计模块的数据库物理设计与实现，重点围绕活动信息的多条件查询、统计分析以及查询性能优化展开。
## **4.1 创建数据库**
1. **CREATE** **DATABASE** campus\_activity\_db  
1. **DEFAULT** **CHARACTER** **SET** utf8mb4  
1. **COLLATE** utf8mb4\_general\_ci;  

**数据库名称**：campus\_activity\_db

**字符集**：utf8mb4（支持中文及特殊字符）

**排序规则**：utf8mb4\_general\_ci

**存放位置**：由 MySQL 默认数据目录管理

**增长方式**：按需自动扩展（由 InnoDB 存储引擎管理）
## **4.2 创建表**
**4.2.1 活动信息表（Activity）**

该表为活动信息查询与统计模块的核心数据来源。

1. **CREATE** **TABLE** Activity (  
1. `    `activity\_id **INT** AUTO\_INCREMENT **PRIMARY** **KEY**,  
1. `    `activity\_name **VARCHAR**(100) NOT NULL,  
1. `    `activity\_type **VARCHAR**(50),  
1. `    `location **VARCHAR**(100) NOT NULL,  
1. `    `start\_time DATETIME NOT NULL,  
1. `    `end\_time DATETIME NOT NULL,  
1. `    `max\_people **INT** **CHECK** (max\_people >= 0),  
1. `    `publisher\_id **INT** NOT NULL,  
1. `    `status **VARCHAR**(20),  
1. `    `**CONSTRAINT** fk\_activity\_user  
1. `        `**FOREIGN** **KEY** (publisher\_id) **REFERENCES** User(user\_id)  
1. ) ENGINE=InnoDB;  

**4.2.2 报名信息表（Registration）**

该表用于统计活动报名人数及报名状态。

1. **CREATE** **TABLE** Registration (  
1. `    `register\_id **INT** AUTO\_INCREMENT **PRIMARY** **KEY**,  
1. `    `activity\_id **INT** NOT NULL,  
1. `    `user\_id **INT** NOT NULL,  
1. `    `register\_time DATETIME **DEFAULT** CURRENT\_TIMESTAMP,  
1. `    `audit\_status **VARCHAR**(20),  
1. `     `**CONSTRAINT** fk\_reg\_activity **FOREIGN** **KEY** (activity\_id) **REFERENCES** Activity(activity\_id),  
1. `    `**CONSTRAINT** fk\_reg\_user **FOREIGN** **KEY** (user\_id) **REFERENCES** User(user\_id)  
1. ) ENGINE=InnoDB;
## **4.3 SQL实现**
**4.3.1 活动信息查询存储过程**

实现按活动类型、时间范围进行查询，并支持排序。

1. **CREATE** **PROCEDURE** query\_activity(  
1. `    `IN a\_type **VARCHAR**(50),  
1. `    `IN start\_t DATETIME,  
1. `    `IN end\_t DATETIME  
1. )  
1. **BEGIN**  
1. `    `**SELECT** \*  
1. `    `**FROM** Activity  
1. `    `**WHERE** (a\_type **IS** NULL OR activity\_type = a\_type)  
1. `      `AND start\_time >= start\_t  
1. `      `AND end\_time <= end\_t  
1. `    `**ORDER** **BY** start\_time;  
1. **END**;  

**4.3.2 活动报名人数统计视图**

1. **CREATE** **VIEW** v\_activity\_registration\_count **AS**  
1. **SELECT** a.activity\_id, a.activity\_name,  
1. `       `COUNT(r.register\_id) **AS** register\_count  
1. **FROM** Activity a  
1. LEFT JOIN Registration r **ON** a.activity\_id = r.activity\_id  
1. **GROUP** **BY** a.activity\_id, a.activity\_name;  

**4.3.3 索引设计**

1. **CREATE** **INDEX** idx\_activity\_type  
1. **ON** Activity(activity\_type);  
# **4. 数据库物理设计及实施**（活动报名审核模块和活动签到与积分管理模块）
本系统选用MySQL 8.0作为数据库管理系统，本人主要负责活动报名审核模块和活动签到与积分管理模块的数据库物理设计与实现，重点实现审核流程控制、签到记录管理及积分自动累加功能。
## **4.1 创建数据库**
1. **CREATE** **DATABASE** campus\_activity\_db  
1. **DEFAULT** **CHARACTER** **SET** utf8mb4  
1. **COLLATE** utf8mb4\_general\_ci;  

**数据库名称**：campus\_activity\_db

**字符集**：utf8mb4（支持中文及特殊字符）

**排序规则**：utf8mb4\_general\_ci

**存放位置**：由 MySQL 默认数据目录管理

**增长方式**：按需自动扩展（由 InnoDB 存储引擎管理）
## **4.2 创建表**
**4.2.1 审核记录表（Audit）**

1. **CREATE** **TABLE** Audit (  
1. `    `audit\_id **INT** AUTO\_INCREMENT **PRIMARY** **KEY**,  
1. `    `register\_id **INT** NOT NULL,  
1. `    `auditor\_id **INT** NOT NULL,  
1. `    `audit\_result **VARCHAR**(20),  
1. `    `audit\_time DATETIME **DEFAULT** CURRENT\_TIMESTAMP,  
1. `    `audit\_comment **VARCHAR**(255),  
1. `     `**CONSTRAINT** fk\_audit\_reg **FOREIGN** **KEY** (register\_id) **REFERENCES** Registration(register\_id),  
1. `    `**CONSTRAINT** fk\_audit\_user **FOREIGN** **KEY** (auditor\_id) **REFERENCES** User(user\_id),  
1. `    `**CONSTRAINT** chk\_audit\_result **CHECK** (audit\_result IN ('通过','拒绝'))  
1. ) ENGINE=InnoDB;  

**4.2.2 签到与积分表（Sign\_Score）**

1. **CREATE** **TABLE** Sign\_Score (  
1. `    `sign\_id **INT** AUTO\_INCREMENT **PRIMARY** **KEY**,  
1. `    `activity\_id **INT** NOT NULL,  
1. `    `user\_id **INT** NOT NULL,  
1. `    `sign\_time DATETIME **DEFAULT** CURRENT\_TIMESTAMP,  
1. `    `score **INT** **CHECK** (score >= 0),  
1. `    `**CONSTRAINT** fk\_sign\_activity **FOREIGN** **KEY** (activity\_id) **REFERENCES** Activity(activity\_id),  
1. `    `**CONSTRAINT** fk\_sign\_user **FOREIGN** **KEY** (user\_id) **REFERENCES** User(user\_id)  
1. ) ENGINE=InnoDB;  
## **4.3 SQL实现**
**4.3.1 报名审核触发器**

1. **CREATE** **TRIGGER** trg\_audit\_pass  
1. **AFTER** **INSERT** **ON** Audit  
1. **FOR** EACH ROW  
1. **BEGIN**  
1. `    `IF NEW.audit\_result = '通过' **THEN**  
1. `        `**UPDATE** Registration  
1. `        `**SET** audit\_status = '通过'  
1. `        `**WHERE** register\_id = NEW.register\_id;  
1. `    `**END** IF;  
1. **END**;  

**4.3.2 签到积分自动累加存储过程**

1. **CREATE** **PROCEDURE** sign\_and\_add\_score(  
1. `    `IN aid **INT**,  
1. `    `IN uid **INT**,  
1. `    `IN add\_score **INT**  
1. )  
1. **BEGIN**  
1. `    `**INSERT** **INTO** Sign\_Score(activity\_id, user\_id, score)  
1. `    `**VALUES** (aid, uid, add\_score);  
1. **END**;  
# **4. 数据库物理设计及实施**(活动评价与反馈模块)
本系统选用MySQL 8.0作为数据库管理系统，本人主要负责活动评价与反馈模块的数据库物理设计与实现，用于收集学生对活动的评价信息，并为活动质量分析提供数据支持。
## **4.1 创建数据库**
1. **CREATE** **DATABASE** campus\_activity\_db  
1. **DEFAULT** **CHARACTER** **SET** utf8mb4  
1. **COLLATE** utf8mb4\_general\_ci;  

**数据库名称**：campus\_activity\_db

**字符集**：utf8mb4（支持中文及特殊字符）

**排序规则**：utf8mb4\_general\_ci

**存放位置**：由 MySQL 默认数据目录管理

**增长方式**：按需自动扩展（由 InnoDB 存储引擎管理）
## **4.2 创建表**
**4.2.1 活动评价表（Evaluation）**

1. **CREATE** **TABLE** Evaluation (  
1. `    `eval\_id **INT** AUTO\_INCREMENT **PRIMARY** **KEY**,  
1. `    `activity\_id **INT** NOT NULL,  
1. `    `user\_id **INT** NOT NULL,  
1. `    `rating **INT** **CHECK** (rating BETWEEN 1 AND 5),  
1. `    `content **VARCHAR**(255),  
1. `    `eval\_time DATETIME **DEFAULT** CURRENT\_TIMESTAMP,  
1. `     `**CONSTRAINT** fk\_eval\_activity **FOREIGN** **KEY** (activity\_id) **REFERENCES** Activity(activity\_id),  
1. `    `**CONSTRAINT** fk\_eval\_user **FOREIGN** **KEY** (user\_id) **REFERENCES** User(user\_id)  
1. ) ENGINE=InnoDB;  
## **4.3 SQL实现**
**4.3.1 活动评价统计视图**

1. **CREATE** **VIEW** v\_activity\_evaluation **AS**  
1. **SELECT** activity\_id,  
1. `       `AVG(rating) **AS** avg\_rating,  
1. `       `COUNT(eval\_id) **AS** eval\_count  
1. **FROM** Evaluation  
1. **GROUP** **BY** activity\_id;  

**4.3.2 评价关键词查询示例**

1. **SELECT** \*  
1. **FROM** Evaluation  
1. **WHERE** content LIKE '%组织%';  
