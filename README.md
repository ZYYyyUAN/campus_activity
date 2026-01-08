# 校园活动报名管理系统

## 项目简介

这是一个基于Vue + SpringBoot的校园活动报名管理系统，实现了活动发布、报名审核、签到积分、活动评价等完整业务流程。

## 技术栈

### 后端
- Spring Boot 2.7.14
- MyBatis
- MySQL 8.0
- Druid 数据库连接池
- Lombok

### 前端
- Vue 2.6.14
- Element UI 2.15.14
- Vue Router 3.5.1
- Vuex 3.6.2
- Axios 1.6.0

## 项目结构

```
campus_activity/
├── campus-activity-backend/          # SpringBoot后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/campus/activity/
│   │   │   │   ├── entity/          # 实体类
│   │   │   │   ├── mapper/          # MyBatis Mapper接口
│   │   │   │   ├── service/         # 业务逻辑层
│   │   │   │   ├── controller/      # 控制器层
│   │   │   │   ├── config/          # 配置类
│   │   │   │   └── common/          # 公共类
│   │   │   └── resources/
│   │   │       ├── mapper/          # MyBatis XML映射文件
│   │   │       └── application.yml  # 配置文件
│   │   └── test/
│   └── pom.xml
│
├── campus-activity-frontend/         # Vue前端项目
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
│
└── campus_activity_db.sql           # 数据库初始化SQL
```

## 功能模块

### 1. 活动发布与管理模块（郑媛元负责）
- 活动的新增、修改、删除
- 活动信息的查询与筛选
- 活动时间校验
- 活动人数限制校验
- 自动记录操作日志（通过触发器）

### 2. 系统用户与权限管理模块（郑媛元负责）
- 用户信息的新增、修改、删除
- 用户角色管理（管理员、发布者、学生）
- 用户状态管理（启用/禁用）
- 密码重置功能
- 密码修改功能

### 3. 系统日志管理模块（郑媛元负责）
- 系统操作日志查询
- 日志筛选（按用户、操作类型、时间范围）
- 日志删除功能
- 过期日志清理

### 4. 活动信息查询与统计模块（康淑文负责）
- 多条件查询活动
- 活动报名人数统计
- 活动签到情况统计

### 5. 活动报名与审核管理模块（田濡奥负责）
- 学生在线报名
- 报名审核（通过/拒绝）
- 报名状态管理
- 自动记录审核日志

### 6. 活动签到与积分管理模块（田濡奥负责）
- 活动签到功能
- 积分自动累计
- 积分查询

### 7. 活动评价与反馈模块（赵冠杰负责）
- 活动评价功能
- 评价信息查询
- 评价统计分析

## 数据库设计

系统使用MySQL 8.0，包含以下7张核心表：
- User（用户表）
- Activity（活动表）
- Registration（报名表）
- Audit（审核表）
- Sign_Score（签到积分表）
- Evaluation（评价表）
- Log（日志表）

详细信息请参考 `campus_activity_db.sql` 文件。

## 快速开始

### 1. 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 14+
- MySQL 8.0+

### 2. 数据库配置
1. 创建数据库：
```sql
CREATE DATABASE campus_activity_db
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;
```

2. 执行SQL脚本：
```bash
mysql -u root -p campus_activity_db < campus_activity_db.sql
```

3. 修改后端配置文件 `campus-activity-backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_activity_db?...
    username: root  # 修改为你的MySQL用户名
    password: root  # 修改为你的MySQL密码
```

### 3. 后端启动
```bash
cd campus-activity-backend
mvn clean install
mvn spring-boot:run
```

后端服务将运行在 http://localhost:8080

### 4. 前端启动
```bash
cd campus-activity-frontend
npm install
npm run serve
```

前端服务将运行在 http://localhost:8081

### 5. 访问系统
打开浏览器访问：http://localhost:8081

默认登录账号：
- 管理员：admin / 123456
- 发布者：publisher / 123456
- 学生：student / 123456

（注：首次使用时需要在数据库中手动创建用户）

## API接口文档

### 用户相关接口
- POST /api/user/login - 用户登录
- GET /api/user/list - 获取用户列表
- POST /api/user/add - 添加用户
- PUT /api/user/update - 更新用户
- DELETE /api/user/{userId} - 删除用户
- POST /api/user/resetPassword/{userId} - 重置密码

### 活动相关接口
- GET /api/activity/list - 获取活动列表
- GET /api/activity/{activityId} - 获取活动详情
- POST /api/activity/add - 添加活动
- PUT /api/activity/update - 更新活动
- DELETE /api/activity/{activityId} - 删除活动
- GET /api/activity/query - 多条件查询活动

### 日志相关接口
- GET /api/log/list - 获取日志列表
- GET /api/log/query - 多条件查询日志
- DELETE /api/log/{logId} - 删除日志

## 开发规范

1. 后端采用分层架构：Controller -> Service -> Mapper
2. 前端采用组件化开发，使用Vue Router进行路由管理
3. 统一使用Result类封装API响应
4. 使用Element UI组件库构建界面
5. 遵循RESTful API设计规范

## 注意事项

1. 确保MySQL服务已启动并正确配置
2. 确保前后端端口未被占用（后端8080，前端8081）
3. 首次运行前端需要安装依赖：`npm install`
4. 数据库初始化后需要手动创建测试用户

## 作者

- 郑媛元：活动发布与管理、用户权限管理、系统日志管理
- 康淑文：活动信息查询与统计
- 田濡奥：活动报名审核、签到与积分管理
- 赵冠杰：活动评价与反馈

## 许可证

本项目为课程设计项目，仅供学习和参考使用。

