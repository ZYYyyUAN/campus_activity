# 数据库脚本说明

## 文件列表

### 1. `activity_log_triggers.sql`
**活动操作日志触发器**

- **after_activity_insert**: 活动创建时自动记录日志
- **after_activity_delete**: 活动删除时自动记录日志

**使用方式**：
```bash
mysql -u root -p campus_activity_db < activity_log_triggers.sql
```

### 2. 存储过程说明

**签到并累加积分存储过程** (`sign_and_add_score`)

该存储过程已包含在主数据库文件 `campus_activity_db.sql` 中（第147-157行）

**参数说明**：
- `aid INT`: 活动ID
- `uid INT`: 用户ID  
- `add_score INT`: 获得积分（通常为10分）

**位置**：`campus_activity_db.sql` 中的存储过程定义

**当前状态**：✅ 系统正在使用此存储过程进行签到操作

**说明**：执行主数据库SQL文件时会自动创建此存储过程，无需单独执行

---

## 触发器设计说明

### 为什么使用触发器？

1. **自动化**: 活动创建/删除时自动记录日志，无需手动代码
2. **一致性**: 确保每次操作都会记录，不会遗漏
3. **解耦**: 业务逻辑与日志记录分离
4. **性能**: 数据库层面的操作，效率更高

### 触发器 vs 手动记录

| 方式 | 优点 | 缺点 | 适用场景 |
|------|------|------|----------|
| **触发器** | • 自动执行<br>• 不会遗漏<br>• 代码简洁 | • 调试困难<br>• 跨库不可见 | 固定的操作日志记录 |
| **手动记录** | • 灵活控制<br>• 便于调试<br>• 跨库可见 | • 容易遗漏<br>• 代码冗余 | 复杂的业务日志 |

### 当前项目的日志策略

#### 使用触发器的场景 ✅
- **活动创建**: `after_activity_insert`
- **活动删除**: `after_activity_delete`
- **审核状态变更**: `after_registration_update` (如需要)

#### 使用存储过程的场景 ✅
- **活动签到**: 使用 `sign_and_add_score` 存储过程，自动记录签到时间和积分

#### 使用手动记录的场景 ✅
- **用户登录**: 需要记录IP、设备等额外信息
- **报名操作**: 需要记录具体的业务细节
- **评价操作**: 需要记录评价内容摘要
- **管理员操作**: 需要记录详细的操作上下文

---

## 验证触发器是否生效

### 1. 查看已创建的触发器
```sql
SHOW TRIGGERS FROM campus_activity_db;
```

### 2. 查看特定触发器的定义
```sql
SHOW CREATE TRIGGER after_activity_insert;
```

### 3. 测试触发器
```sql
-- 创建一个测试活动
INSERT INTO Activity(activity_name, activity_type, location, start_time, end_time, max_people, status, publisher_id)
VALUES('测试触发器', '讲座', '测试地点', '2026-02-01 14:00:00', '2026-02-01 16:00:00', 50, '报名中', 2);

-- 查看日志是否自动记录
SELECT * FROM Log WHERE operation_type = '活动发布' ORDER BY operation_time DESC LIMIT 1;

-- 删除测试数据
DELETE FROM Activity WHERE activity_name = '测试触发器';

-- 查看删除日志
SELECT * FROM Log WHERE operation_type = '活动删除' ORDER BY operation_time DESC LIMIT 1;
```

---

## 常见问题

### Q1: 触发器没有生效？
**检查步骤**：
1. 确认触发器已创建：`SHOW TRIGGERS;`
2. 检查MySQL版本是否支持触发器（5.0+）
3. 检查用户权限：`SHOW GRANTS;`
4. 查看MySQL错误日志

### Q2: 触发器中的日志没有记录？
**可能原因**：
1. Log表结构不匹配
2. 触发器中的字段名与表结构不一致
3. 事务回滚导致触发器操作也被回滚

### Q3: 如何临时禁用触发器？
```sql
-- 删除触发器
DROP TRIGGER IF EXISTS after_activity_insert;
DROP TRIGGER IF EXISTS after_activity_delete;

-- 需要时重新创建
SOURCE activity_log_triggers.sql;
```

---

## 维护建议

1. **定期备份**: 触发器属于数据库结构，需要单独备份
2. **版本控制**: 触发器SQL文件应纳入版本控制
3. **文档更新**: 修改触发器时更新相关文档
4. **监控日志**: 定期检查日志表，确保触发器正常工作
