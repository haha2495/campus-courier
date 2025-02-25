# 校园跑腿系统

## 项目简介
校园跑腿系统是一个面向高校学生的跑腿服务平台，为校园内的学生提供快递代取、食堂打饭、商品代购等跑腿服务。

## 系统架构
项目采用前后端分离架构：
- 前端小程序：Vue 3 + uni-app
- 管理后台：Vue 3 + Element Plus
- 后端服务：Spring Boot + MyBatis-Plus + MySQL

## 功能特性

### 小程序端功能


## 技术栈
- 前端小程序：
  - Vue 3
  - uni-app
  - uview-plus
  - Pinia

- 管理后台：
  - Vue 3
  - Element Plus
  - Vue Router
  - Pinia
  - Axios

- 后端服务：
  - Spring Boot
  - MyBatis-Plus
  - MySQL
  - Redis
  - Sa-Token
  - WebSocket

## 开发规范

### API设计规范

#### 返回值规范

1. **增删改操作**:
   - 返回类型使用`boolean`，表示操作成功或失败
   - 不返回具体数据对象，保持接口干净简洁
   - 例如：`addTaskType`, `updateTaskType`, `deleteTaskType`等方法均返回`boolean`

2. **查询操作**:
   - 返回具体的数据对象或集合
   - 单条查询返回对应实体对象，如`SysTaskType`
   - 列表查询返回集合对象，如`List<SysTaskType>`或分页对象`PageResult<SysTaskType>`

#### 实体对象设计规范

1. **贫血模型实现**:
   - 实体类中提供`init`方法进行内部初始化
   - `init`方法返回`this`以支持链式调用
   - 初始化逻辑集中在实体类内部，提高代码内聚性

2. **创建对象流程**:
   ```java
   // 推荐做法
   SysTaskType taskType = new SysTaskType().init(request);
   
   // 不推荐做法
   SysTaskType taskType = new SysTaskType();
   taskType.setTypeName(request.getTypeName());
   // ... 其他设置
   ```

#### Controller层规范

1. **统一响应格式**:
   - 所有接口使用`Result<T>`封装返回结果
   - 增删改操作返回`Result<Boolean>`
   - 查询操作返回`Result<实体类型>`或`Result<List<实体类型>>`

2. **权限控制**:
   - 在Controller层进行权限验证
   - 使用`isAdmin()`等方法判断用户权限
   - 权限不足时统一返回`Result.forbidden()`

#### 事务管理规范

1. **事务注解**:
   - 在Service层方法上添加`@Transactional(rollbackFor = Exception.class)`
   - 确保操作的原子性和数据一致性
   - 特别是对于涉及多个数据库操作的方法，必须添加事务注解

### 分层职责

1. **Controller层**：
   - 处理HTTP请求，进行参数校验和权限验证
   - 调用Service层处理业务逻辑
   - 转换Service结果并封装为统一返回格式

2. **Service层**：
   - 实现业务逻辑
   - 处理事务控制
   - 调用Handler层进行复杂数据操作

3. **Handler层**：
   - 位于Mapper下，继承ServiceImpl
   - 处理复杂数据库操作，可被多个Service复用

4. **Mapper层**：
   - 定义基础的数据库CRUD操作接口
   - 继承BaseMapper接口

## 开发环境搭建
1. 环境要求
   - Node.js 16+
   - JDK 17+
   - MySQL 8.0+
   - Redis 6.0+

2. 项目启动
   ```bash
   # 启动后端服务
   cd campus-courier
   mvn spring-boot:run

   # 启动管理后台
   cd campus-courier-web
   npm install
   npm run dev

   # 启动小程序
   cd campus-courier-wx
   npm install
   npm run dev:mp-weixin
   ```

## 部署说明
[待补充]

## 项目成员
[待补充]

## 开源协议
MIT License
