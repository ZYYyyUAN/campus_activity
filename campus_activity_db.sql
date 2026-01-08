
DROP DATABASE IF EXISTS campus_activity_db;
CREATE DATABASE campus_activity_db
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE campus_activity_db;


CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL DEFAULT '123456',
    real_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE Activity (
    activity_id INT AUTO_INCREMENT PRIMARY KEY,
    activity_name VARCHAR(100) NOT NULL,
    activity_type VARCHAR(50),
    location VARCHAR(100) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    max_people INT CHECK (max_people >= 0),
    publisher_id INT NOT NULL,
    status VARCHAR(20),
    CONSTRAINT fk_activity_user
        FOREIGN KEY (publisher_id) REFERENCES User(user_id)
) ENGINE=InnoDB;

CREATE TABLE Log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    operation_type VARCHAR(50),
    operation_content VARCHAR(255),
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_user
        FOREIGN KEY (user_id) REFERENCES User(user_id)
) ENGINE=InnoDB;


CREATE TABLE Registration (
    register_id INT AUTO_INCREMENT PRIMARY KEY,
    activity_id INT NOT NULL,
    user_id INT NOT NULL,
    register_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    audit_status VARCHAR(20),
    CONSTRAINT fk_reg_activity
        FOREIGN KEY (activity_id) REFERENCES Activity(activity_id),
    CONSTRAINT fk_reg_user
        FOREIGN KEY (user_id) REFERENCES User(user_id)
) ENGINE=InnoDB;

CREATE TABLE Audit (
    audit_id INT AUTO_INCREMENT PRIMARY KEY,
    register_id INT NOT NULL,
    auditor_id INT NOT NULL,
    audit_result VARCHAR(20),
    audit_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    audit_comment VARCHAR(255),
    CONSTRAINT fk_audit_reg
        FOREIGN KEY (register_id) REFERENCES Registration(register_id),
    CONSTRAINT fk_audit_user
        FOREIGN KEY (auditor_id) REFERENCES User(user_id),
    CONSTRAINT chk_audit_result
        CHECK (audit_result IN ('通过','拒绝'))
) ENGINE=InnoDB;

CREATE TABLE Sign_Score (
    sign_id INT AUTO_INCREMENT PRIMARY KEY,
    activity_id INT NOT NULL,
    user_id INT NOT NULL,
    sign_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    score INT CHECK (score >= 0),
    CONSTRAINT fk_sign_activity
        FOREIGN KEY (activity_id) REFERENCES Activity(activity_id),
    CONSTRAINT fk_sign_user
        FOREIGN KEY (user_id) REFERENCES User(user_id)
) ENGINE=InnoDB;

CREATE TABLE Evaluation (
    eval_id INT AUTO_INCREMENT PRIMARY KEY,
    activity_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    content VARCHAR(255),
    eval_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_eval_activity
        FOREIGN KEY (activity_id) REFERENCES Activity(activity_id),
    CONSTRAINT fk_eval_user
        FOREIGN KEY (user_id) REFERENCES User(user_id)
) ENGINE=InnoDB;


CREATE INDEX idx_activity_publisher
ON Activity(publisher_id);

CREATE INDEX idx_activity_type
ON Activity(activity_type);


CREATE VIEW v_activity_registration_count AS
SELECT a.activity_id,
       a.activity_name,
       COUNT(r.register_id) AS register_count
FROM Activity a
LEFT JOIN Registration r
ON a.activity_id = r.activity_id
GROUP BY a.activity_id, a.activity_name;

CREATE VIEW v_activity_evaluation AS
SELECT activity_id,
       AVG(rating) AS avg_rating,
       COUNT(eval_id) AS eval_count
FROM Evaluation
GROUP BY activity_id;


DELIMITER $$
CREATE PROCEDURE reset_user_password(IN uid INT)
BEGIN
    UPDATE User
    SET password = '123456'
    WHERE user_id = uid;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE query_activity(
    IN a_type VARCHAR(50),
    IN start_t DATETIME,
    IN end_t DATETIME
)
BEGIN
    SELECT *
    FROM Activity
    WHERE (a_type IS NULL OR activity_type = a_type)
      AND start_time >= start_t
      AND end_time <= end_t
    ORDER BY start_time;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE sign_and_add_score(
    IN aid INT,
    IN uid INT,
    IN add_score INT
)
BEGIN
    INSERT INTO Sign_Score(activity_id, user_id, score)
    VALUES (aid, uid, add_score);
END$$
DELIMITER ;


DELIMITER $$
CREATE TRIGGER trg_activity_insert
AFTER INSERT ON Activity
FOR EACH ROW
BEGIN
    INSERT INTO Log(user_id, operation_type, operation_content)
    VALUES (NEW.publisher_id, '发布活动',
            CONCAT('发布活动：', NEW.activity_name));
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER trg_activity_delete
AFTER DELETE ON Activity
FOR EACH ROW
BEGIN
    INSERT INTO Log(user_id, operation_type, operation_content)
    VALUES (OLD.publisher_id, '删除活动',
            CONCAT('删除活动：', OLD.activity_name));
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER trg_audit_pass
AFTER INSERT ON Audit
FOR EACH ROW
BEGIN
    IF NEW.audit_result = '通过' THEN
        UPDATE Registration
        SET audit_status = '通过'
        WHERE register_id = NEW.register_id;
    END IF;
END$$
DELIMITER ;
