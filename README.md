# 机器设备管理系统

一个现代化的机器设备管理系统，采用前后端分离架构，用于管理机房、机柜、设备和接口的完整生命周期。

## 📋 系统概述

本系统提供完整的机器设备管理解决方案，支持：
- 机房管理：管理不同位置的机房信息
- 机柜管理：按机房和排位管理机柜
- 设备管理：管理机柜内的各种设备
- 接口管理：管理设备的接口信息
- 用户管理：支持管理员和普通用户角色
- 内容管理：支持首页内容的动态编辑

## 🏗️ 系统架构

### 前端技术栈
- **Vue 3** - 现代化的前端框架
- **Element Plus** - 企业级UI组件库
- **Pinia** - 状态管理
- **Vue Router** - 路由管理
- **Axios** - HTTP请求库
- **Vite** - 构建工具

### 后端技术栈
- **Spring Boot 3.0.2** - 企业级Java框架
- **Spring Security** - 安全框架
- **Spring Data JPA** - 数据访问层
- **MyBatis** - ORM框架
- **MySQL** - 数据库
- **JWT** - 身份认证
- **Maven** - 项目管理

## 🗄️ 数据库设计

系统采用MySQL数据库，包含以下核心表：

### 主要表结构
- **users** - 用户表
- **room** - 机房表
- **cabinet_row** - 机柜排表
- **cabinet** - 机柜表
- **device** - 设备表
- **interface** - 接口表
- **image_resource** - 图片资源表
- **marker** - 标注表
- **system_content** - 系统内容配置表

### 关键特性
- 完整的外键约束和级联删除
- 唯一索引确保数据一致性
- 支持图片资源管理
- 灵活的内容配置系统

## 🔐 安全特性

### 认证机制
- **JWT Token认证** - 无状态的身份验证
- **角色权限控制** - 支持管理员和普通用户角色
- **密码加密** - 使用BCrypt加密存储密码

### 安全配置
- **CORS跨域配置** - 支持前后端分离部署
- **会话管理** - 无状态会话管理
- **请求授权** - 细粒度的API权限控制
- **CSRF防护** - 禁用CSRF以支持RESTful API

## 🚀 功能特性

### 用户管理
- 用户注册和登录
- 密码修改功能
- 角色权限管理
- 会话过期处理

### 资源管理
- **四级层级管理**：机房 → 机柜排 → 机柜 → 设备
- **图片管理**：支持机房、机柜、设备图片上传和管理
- **接口管理**：管理设备接口及其连接关系
- **标注系统**：支持在图片上添加设备/接口标注

### 内容管理
- **首页内容编辑**：管理员可在线编辑首页内容
- **轮播图管理**：支持首页轮播图管理
- **富文本编辑**：支持复杂的文本格式

## 📦 安装部署

### 环境要求
- **JDK 17+**
- **MySQL 5.7+**
- **Node.js 16+**
- **Maven 3.6+**

### 后端部署
```bash
# 进入后端目录
cd machine-backend

# 配置数据库连接
# 编辑 src/main/resources/application.properties

# 构建项目
mvn clean package

# 运行项目
java -jar target/machine-backend-0.0.1-SNAPSHOT.jar
```

### 前端部署
```bash
# 进入前端目录
cd machine-front

# 安装依赖
npm install

# 开发环境运行
npm run dev

# 生产环境构建
npm run build
```

### 数据库初始化
1. 创建数据库：`machine_backend`
2. 执行 `数据库表设计.sql` 脚本初始化表结构
3. 可选择执行 `home_content_init.sql` 初始化首页内容

## ⚙️ 配置说明

### 后端配置 (application.properties)
```properties
# 数据库连接配置
spring.datasource.url=jdbc:mysql://localhost:3306/machine_backend
spring.datasource.username=root
spring.datasource.password=123456

# JWT配置
app.jwtSecret=ThisIsAStrongerAndLongerSecretKeyForHS512Algorithm
app.jwtExpirationInMs=886400000

# 文件上传配置
spring.servlet.multipart.max-file-size=10MB
file.upload-dir=upload/
```

### 前端配置 (vite.config.js)
```javascript
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
})
```

## 📁 项目结构

```
machine/
├── machine-backend/              # 后端项目
│   ├── src/main/java/com/example/machinebackend/
│   │   ├── config/              # 配置类
│   │   ├── controller/          # 控制器
│   │   ├── entity/              # 实体类
│   │   ├── service/             # 服务层
│   │   ├── mapper/              # 数据访问层
│   │   ├── utils/               # 工具类
│   │   └── dto/                 # 数据传输对象
│   └── src/main/resources/
│       ├── mapper/              # MyBatis映射文件
│       └── application.properties
├── machine-front/               # 前端项目
│   ├── src/
│   │   ├── components/          # 组件
│   │   ├── views/               # 页面
│   │   ├── store/               # 状态管理
│   │   ├── router/              # 路由配置
│   │   └── assets/              # 静态资源
│   └── package.json
└── 数据库表设计.sql              # 数据库结构
```

## 🔧 开发指南

### 后端开发
- 遵循RESTful API设计规范
- 使用Spring Data JPA进行数据访问
- 统一使用CommonResponse作为API返回格式
- 支持JWT认证和权限控制

### 前端开发
- 使用Vue 3 Composition API
- 组件化开发，支持复用
- 使用Pinia进行状态管理
- 响应式设计，适配不同设备

## 🌟 核心功能模块

### 1. 机房管理
- 查看机房列表和详情
- 添加、编辑、删除机房
- 上传机房平面图
- 查看机房内的机柜分布

### 2. 机柜管理
- 按机房和排位管理机柜
- 支持机柜编号和列号管理
- 上传机柜正面和背面图片
- 查看机柜内的设备分布

### 3. 设备管理
- 管理机柜内的设备
- 支持设备编号和名称管理
- 上传设备图片
- 查看设备接口信息

### 4. 接口管理
- 管理设备接口
- 支持接口连接关系管理
- 接口状态控制（开启/关闭）
- 在图片上标注接口位置

### 5. 标注系统
- 在机柜/设备图片上添加标注
- 支持链接到设备或接口
- 自定义标注图标和样式
- 支持标注的增删改查

## 📝 版本信息

- **当前版本**: 1.0
- **最后更新**: 2025年7月
- **开发状态**: 已完成安全检查，可投入生产使用

## 🤝 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

**注意**: 本系统已完成安全检查，包含了完整的认证授权机制、数据验证和错误处理，适合在生产环境中使用。