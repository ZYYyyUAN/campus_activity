# 校园活动报名管理系统

基于 Spring Boot + MyBatis + Vue 的校园活动全流程管理平台，覆盖活动发布、学生报名、管理员审核、现场签到、积分累计与活动评价。

---

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 2.7.14 |
| ORM | MyBatis (Spring Boot Starter) | 2.3.1 |
| 数据库 | MySQL | 8.0 |
| 连接池 | Alibaba Druid | 1.2.18 |
| JSON | Alibaba Fastjson | 2.0.40 |
| 简化代码 | Lombok | — |
| 前端 | Vue 2.6.14 + Element UI 2.15.14 | — |
| 构建工具 | Maven | — |
| Java | JDK | 1.8 |

---

## 项目结构

```
campus_activity/
├── campus-activity-backend/           # 后端 Spring Boot 项目
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/campus/activity/
│       │   ├── CampusActivityApplication.java   # 启动类
│       │   ├── common/Result.java               # 统一响应体
│       │   ├── config/CorsConfig.java           # 跨域配置
│       │   ├── controller/                      # 控制器层
│       │   │   ├── ActivityController.java
│       │   │   ├── AuditController.java
│       │   │   ├── EvaluationController.java
│       │   │   ├── LogController.java
│       │   │   ├── RegistrationController.java
│       │   │   ├── SignScoreController.java
│       │   │   └── UserController.java
│       │   ├── entity/                          # 实体类
│       │   ├── mapper/                          # MyBatis Mapper 接口
│       │   ├── service/                         # 服务接口
│       │   │   └── impl/                        # 服务实现
│       │   └── interceptor/                     # 拦截器（预留）
│       └── resources/
│           ├── application.yml                  # 核心配置
│           └── mapper/                          # MyBatis XML 映射文件
├── campus-activity-frontend/          # 前端 Vue 项目
│   ├── public/
│   ├── src/
│   │   ├── api/                     # API接口
│   │   ├── assets/                  # 静态资源
│   │   ├── components/              # 公共组件
│   │   ├── layout/                  # 布局组件
│   │   ├── router/                  # 路由配置
│   │   ├── store/                   # Vuex状态管理
│   │   ├── views/                   # 页面组件
│   │   │   ├── activity/           # 活动管理
│   │   │   ├── user/               # 用户管理
│   │   │   └── log/                # 日志管理
│   │   ├── App.vue
│   │   └── main.js
│   ├── package.json
│   └── vue.config.js
└── campus_activity_db.sql             # 数据库建表脚本（含触发器与存储过程）
```

---

## 功能模块

### 用户角色

| 角色 | 权限 |
|------|------|
| **学生 (student)** | 浏览活动、报名、签到、评价 |
| **发布者 (publisher)** | 创建/修改/删除活动，查看报名情况 |
| **管理员 (admin)** | 审核报名、管理用户、查看日志 |

### 业务模块

| 模块 | 核心功能 | 关键实现 |
|------|----------|----------|
| **活动管理** | 发布、修改、删除、多条件组合查询 | MyBatis 动态 SQL（`<where>` + `<if>`） |
| **报名管理** | 报名、取消报名、重复校验、满员检测 | 审核通过人数过滤计数 |
| **审核管理** | 通过/拒绝报名、审核记录写入 | `@Transactional` 保证状态更新 + 日志的原子性 |
| **签到积分** | 现场签到、积分累加 | MySQL 存储过程 `sign_and_add_score` |
| **活动评价** | 评分(1-5)、文字评价、去重校验 | JOIN 查询关联活动名与用户名 |
| **操作日志** | 自动记录活动增删、审核、签到 | 触发器（自动）+ Service 层（手动）双通道 |
| **用户管理** | 登录、增删改查、角色筛选、密码重置 | 存储过程 `reset_user_password` |

---

## 数据库设计

### ER 关系

```
User ──1:N──> Activity       (publisher_id)
User ──1:N──> Registration   (user_id)
User ──1:N──> Sign_Score     (user_id)
User ──1:N──> Evaluation     (user_id)
User ──1:N──> Log            (user_id)
User ──1:N──> Audit          (auditor_id)
Activity ──1:N──> Registration
Activity ──1:N──> Sign_Score
Activity ──1:N──> Evaluation
Registration ──1:1──> Audit
```

### 7 张表

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| `User` | 用户 | username, password, role, status |
| `Activity` | 活动 | activity_name, start_time, end_time, max_people, status |
| `Registration` | 报名记录 | activity_id, user_id, audit_status |
| `Audit` | 审核记录 | register_id, auditor_id, audit_result |
| `Sign_Score` | 签到积分 | activity_id, user_id, score |
| `Evaluation` | 活动评价 | activity_id, user_id, rating, content |
| `Log` | 操作日志 | user_id, operation_type, operation_content |
详细信息请参考 campus_activity_db.sql 文件。

### 存储过程

| 名称 | 用途 | 调用方式 |
|------|------|----------|
| `sign_and_add_score(aid, uid, score)` | 签到写入积分 | `@Update("CALL sign_and_add_score(...)")` |
| `reset_user_password(uid)` | 重置密码为 123456 | `@Update("CALL reset_user_password(...)")` |
| `query_activity(type, start, end)` | 按条件查询活动 | `@Select("CALL query_activity(...)")` |

### 触发器

| 名称 | 触发时机 | 行为 |
|------|----------|------|
| `trg_activity_insert` | AFTER INSERT ON Activity | 自动写入 Log（"发布活动"） |
| `trg_activity_delete` | AFTER DELETE ON Activity | 自动写入 Log（"删除活动"） |
| `trg_audit_pass` | AFTER INSERT ON Audit | audit_result='通过'时更新 Registration 状态 |

### 视图

| 名称 | 内容 |
|------|------|
| `v_activity_registration_count` | 每个活动的报名人数统计 |
| `v_activity_evaluation` | 每个活动的平均评分与评价人数 |

---

## 快速开始

### 1.环境要求

- JDK 1.8+
- MySQL 8.0+
- Maven 3.6+
- Node.js 14+（前端）

### 2.数据库配置

#### 1.创建数据库：

```bash
CREATE DATABASE campus_activity_db
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;
```

#### 2. 执行SQL脚本：

```bash
mysql -u root -p < campus_activity_db.sql
```

#### 3. 修改后端配置文件 campus-activity-backend/src/main/resources/application.yml：

```java
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_activity_db?...
    username: root  # 修改为你的MySQL用户名
    password: root  # 修改为你的MySQL密码
```

### 3. 启动后端

```bash
cd campus-activity-backend
mvn clean package -DskipTests
mvn spring-boot:run
```

后端运行在 `http://localhost:8080`。

### 4. 启动前端

```bash
cd campus-activity-frontend
npm install
npm run serve
```

默认登录账号：

```
管理员：admin / 123456
发布者：publisher / 123456
学生：student / 123456
（注：首次使用时需要在数据库中手动创建用户）
```
---

## API 接口一览

### 用户模块 `/api/user`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/login` | 用户登录 |
| GET | `/{userId}` | 查询用户 |
| GET | `/list` | 用户列表 |
| GET | `/role/{role}` | 按角色筛选 |
| POST | `/add` | 添加用户 |
| PUT | `/update` | 修改用户 |
| DELETE | `/{userId}` | 删除用户 |
| POST | `/resetPassword/{userId}` | 重置密码 |
| PUT | `/status` | 更新用户状态 |

### 活动模块 `/api/activity`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/{activityId}` | 查询活动详情 |
| GET | `/list` | 活动列表 |
| GET | `/publisher/{publisherId}` | 发布者的活动 |
| GET | `/query` | 多条件组合查询 |
| GET | `/query/procedure` | 存储过程查询 |
| POST | `/add` | 发布活动 |
| PUT | `/update` | 修改活动 |
| DELETE | `/{activityId}` | 删除活动 |
| GET | `/checkFull/{activityId}` | 检测是否满员 |

### 报名模块 `/api/registration`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/{registerId}` | 查询报名详情 |
| GET | `/user/{userId}` | 用户的报名列表 |
| GET | `/activity/{activityId}` | 活动的报名列表 |
| GET | `/activity/{activityId}/count` | 报名人数 |
| GET | `/activity/{activityId}/count/passed` | 审核通过人数 |
| GET | `/publisher/{publisherId}` | 发布者收到的报名 |
| POST | `/add` | 报名 |
| DELETE | `/{registerId}` | 取消报名 |

### 审核模块 `/api/audit`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/{auditId}` | 审核详情 |
| GET | `/activity/{activityId}` | 活动的审核记录 |
| GET | `/auditor/{auditorId}` | 审核人的审核记录 |
| POST | `/add` | 提交审核 |

### 签到模块 `/api/signscore`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/{signId}` | 签到详情 |
| GET | `/user/{userId}` | 用户签到记录 |
| GET | `/activity/{activityId}` | 活动签到列表 |
| GET | `/activity/{activityId}/count` | 签到人数 |
| POST | `/sign` | 签到 |
| GET | `/total/{userId}` | 用户总积分 |

### 评价模块 `/api/evaluation`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/{evalId}` | 评价详情 |
| GET | `/all` | 全部评价 |
| GET | `/activity/{activityId}` | 活动评价列表 |
| GET | `/user/{userId}` | 用户评价列表 |
| GET | `/keyword/{keyword}` | 关键词搜索评价 |
| POST | `/add` | 添加评价 |
| PUT | `/update` | 修改评价 |
| DELETE | `/{evalId}` | 删除评价 |

### 日志模块 `/api/log`

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/{logId}` | 日志详情 |
| GET | `/list` | 全部日志 |
| GET | `/user/{userId}` | 用户日志 |
| GET | `/type/{operationType}` | 按操作类型筛选 |
| GET | `/query` | 按时间范围查询 |
| DELETE | `/{logId}` | 删除日志 |

## 开发规范

1. 后端采用分层架构：Controller -> Service -> Mapper
2. 前端采用组件化开发，使用Vue Router进行路由管理
3. 统一使用Result类封装API响应
4. 使用Element UI组件库构建界面
5. 遵循RESTful API设计规范

### 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

| code | 含义 |
|------|------|
| 200 | 成功 |
| 500 | 失败 |

---
## 注意事项

1. 确保MySQL服务已启动并正确配置
2. 确保前后端端口未被占用（后端8080，前端8081）
3. 首次运行前端需要安装依赖：`npm install`
4. 数据库初始化后需要手动创建测试用户


## 配置说明

核心配置在 `application.yml`：

- **数据库**：`localhost:3306/campus_activity_db`，用户 `root`/`root`
- **连接池**：Druid，初始化 5 连接，最大 20 连接，检测间隔 60s
- **MyBatis**：开启驼峰映射，SQL 日志输出到 stdout
- **日期格式**：`yyyy-MM-dd HH:mm:ss`，时区 GMT+8

---

## 已知改进方向

- [ ] 密码加密（引入 BCrypt 替代明文存储）
- [ ] 报名防超卖（SELECT FOR UPDATE 或 Redis 分布式锁）
- [ ] JWT 认证拦截器（`interceptor/` 目录已预留）
- [ ] 全局异常处理（`@RestControllerAdvice`）
- [ ] `getAuditByActivityId` 的 N+1 查询优化
- [ ] 审核状态更新去重（触发器与 Java 代码重复更新）

## 许可证

本项目为课程设计项目，仅供学习和参考使用。