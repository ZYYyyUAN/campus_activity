-- 测试数据插入脚本
-- 用于测试发布者和学生功能

-- 1. 插入测试用户（如果不存在）
INSERT INTO User (user_name, password, real_name, role, status) VALUES
('admin', '123456', '系统管理员', '管理员', 1),
('publisher1', '123456', '张老师', '发布者', 1),
('publisher2', '123456', '李老师', '发布者', 1),
('student1', '123456', '王小明', '学生', 1),
('student2', '123456', '刘小红', '学生', 1),
('student3', '123456', '陈小华', '学生', 1)
ON DUPLICATE KEY UPDATE user_name=user_name;

-- 2. 获取发布者ID（用于后续插入）
SET @publisher1_id = (SELECT user_id FROM User WHERE user_name = 'publisher1' LIMIT 1);
SET @publisher2_id = (SELECT user_id FROM User WHERE user_name = 'publisher2' LIMIT 1);
SET @student1_id = (SELECT user_id FROM User WHERE user_name = 'student1' LIMIT 1);
SET @student2_id = (SELECT user_id FROM User WHERE user_name = 'student2' LIMIT 1);
SET @student3_id = (SELECT user_id FROM User WHERE user_name = 'student3' LIMIT 1);

-- 3. 插入测试活动
INSERT INTO Activity (activity_name, activity_type, location, start_time, end_time, max_people, status, publisher_id) VALUES
('人工智能前沿技术讲座', '讲座', '教学楼A101', DATE_ADD(NOW(), INTERVAL 3 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY) + INTERVAL 2 HOUR, 100, '报名中', @publisher1_id),
('校园篮球比赛', '比赛', '体育馆', DATE_ADD(NOW(), INTERVAL 7 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY) + INTERVAL 3 HOUR, 50, '报名中', @publisher1_id),
('大数据与云计算学术论坛', '学术论坛', '图书馆报告厅', DATE_ADD(NOW(), INTERVAL 10 DAY), DATE_ADD(NOW(), INTERVAL 10 DAY) + INTERVAL 4 HOUR, 200, '报名中', @publisher2_id),
('班级团建活动', '班级活动', '校园广场', DATE_ADD(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 5 DAY) + INTERVAL 5 HOUR, 40, '报名中', @publisher1_id),
('志愿服务：社区义教', '志愿服务', '社区服务中心', DATE_ADD(NOW(), INTERVAL 14 DAY), DATE_ADD(NOW(), INTERVAL 14 DAY) + INTERVAL 3 HOUR, 30, '报名中', @publisher2_id);

-- 4. 获取活动ID
SET @activity1_id = (SELECT activity_id FROM Activity WHERE activity_name = '人工智能前沿技术讲座' LIMIT 1);
SET @activity2_id = (SELECT activity_id FROM Activity WHERE activity_name = '校园篮球比赛' LIMIT 1);
SET @activity3_id = (SELECT activity_id FROM Activity WHERE activity_name = '大数据与云计算学术论坛' LIMIT 1);

-- 5. 插入一些测试报名记录
INSERT INTO Registration (activity_id, user_id, register_time, audit_status) VALUES
(@activity1_id, @student1_id, NOW(), '待审'),
(@activity1_id, @student2_id, NOW(), '待审'),
(@activity2_id, @student1_id, NOW(), '通过'),
(@activity2_id, @student3_id, NOW(), '待审'),
(@activity3_id, @student2_id, NOW(), '通过');

-- 6. 插入一些审核记录
INSERT INTO Audit (register_id, auditor_id, audit_result, audit_time, audit_comment)
SELECT r.register_id, @publisher1_id, '通过', NOW(), '符合条件，审核通过'
FROM Registration r
WHERE r.activity_id = @activity2_id AND r.user_id = @student1_id;

INSERT INTO Audit (register_id, auditor_id, audit_result, audit_time, audit_comment)
SELECT r.register_id, @publisher2_id, '通过', NOW(), '符合条件，审核通过'
FROM Registration r
WHERE r.activity_id = @activity3_id AND r.user_id = @student2_id;

-- 7. 插入一些签到记录
INSERT INTO Sign_Score (activity_id, user_id, sign_time, score)
SELECT @activity2_id, @student1_id, NOW(), 10
WHERE EXISTS (SELECT 1 FROM Registration WHERE activity_id = @activity2_id AND user_id = @student1_id AND audit_status = '通过');

-- 8. 插入一些评价记录
INSERT INTO Evaluation (activity_id, user_id, rating, content, eval_time)
SELECT @activity2_id, @student1_id, 5, '活动组织很好，内容丰富，收获很大！', NOW()
WHERE EXISTS (SELECT 1 FROM Sign_Score WHERE activity_id = @activity2_id AND user_id = @student1_id);

-- 显示插入结果
SELECT '用户数据' AS '表名', COUNT(*) AS '记录数' FROM User
UNION ALL
SELECT '活动数据', COUNT(*) FROM Activity
UNION ALL
SELECT '报名数据', COUNT(*) FROM Registration
UNION ALL
SELECT '审核数据', COUNT(*) FROM Audit
UNION ALL
SELECT '签到数据', COUNT(*) FROM Sign_Score
UNION ALL
SELECT '评价数据', COUNT(*) FROM Evaluation;
