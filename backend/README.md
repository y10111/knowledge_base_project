# 鞍钢青年技术人才知识库与智能问答系统 - 后端

## 技术栈
- Java 8
- Spring Boot 2.7.18
- MySQL 5.7
- Spring Data JPA
- Swagger 3.0

## 项目结构
```
backend/
├── src/main/java/com/ansteel/knowledge/
│   ├── config/          # 配置类
│   ├── controller/      # 控制器层
│   ├── service/         # 服务层
│   ├── repository/      # 数据访问层
│   ├── entity/          # 实体类
│   ├── dto/             # 数据传输对象
│   ├── exception/       # 异常处理
│   ├── common/          # 通用类
│   └── util/            # 工具类
└── src/main/resources/
    └── application.yml  # 配置文件
```

## 快速开始

### 1. 数据库配置
创建数据库并执行 `docs/db` 中的SQL脚本

### 2. 修改配置
修改 `application.yml` 中的数据库连接信息

### 3. 运行项目
```bash
mvn spring-boot:run
```

### 4. 访问接口文档
http://localhost:8080/swagger-ui/index.html

## API接口

### 对话管理
- POST   /api/conversations      创建对话
- GET    /api/conversations      获取对话列表
- GET    /api/conversations/{id} 获取对话详情
- DELETE /api/conversations/{id} 删除对话
- POST   /api/conversations/{id}/messages 发送消息

### 文档管理
- POST   /api/docs              创建文档
- GET    /api/docs              获取文档列表
- GET    /api/docs/{id}         获取文档详情
- PUT    /api/docs/{id}         更新文档
- DELETE /api/docs/{id}         删除文档
- POST   /api/docs/{id}/view    增加浏览次数

### 分类管理
- GET    /api/categories        获取分类树
- POST   /api/categories        创建分类
- PUT    /api/categories/{id}   更新分类
- DELETE /api/categories/{id}   删除分类

### 搜索
- GET    /api/search?q=keyword&type=all 统一搜索

### 系统状态
- GET    /api/health            健康检查
