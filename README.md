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
