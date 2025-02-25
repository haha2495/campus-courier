# 高校跑腿系统 - 前端

## 项目介绍
高校跑腿系统是专为校园环境设计的服务平台，帮助学生解决日常生活中的各种任务需求。本项目是系统的前端部分，基于Vue 3 + Vite构建，使用Element Plus组件库，提供现代化的用户界面和流畅的用户体验。

## 技术栈
- **前端框架**: Vue 3 (使用Composition API和`<script setup>`语法)
- **构建工具**: Vite
- **UI组件库**: Element Plus
- **路由管理**: Vue Router
- **状态管理**: Pinia
- **HTTP客户端**: Axios
- **样式处理**: SASS
- **富文本编辑器**: Wang Editor
- **事件总线**: mitt

## 系统架构
系统采用前后端分离架构，前端负责用户界面和交互逻辑，后端提供API接口和数据处理服务。

### 目录结构
```
campus-courier-web/
├── public/              # 静态资源目录
├── src/                 # 源代码目录
│   ├── api/             # API接口定义
│   ├── assets/          # 项目资源文件
│   ├── components/      # 公共组件
│   ├── config/          # 配置文件
│   ├── layout/          # 布局组件
│   ├── router/          # 路由配置
│   ├── utils/           # 工具函数
│   ├── views/           # 页面组件
│   ├── App.vue          # 根组件
│   ├── main.js          # 入口文件
│   └── style.css        # 全局样式
├── index.html           # HTML模板
├── package.json         # 依赖配置
└── vite.config.js       # Vite配置
```

## 功能模块
1. **用户认证**：用户登录、退出、权限控制
2. **管理员管理**：管理员列表、新增、编辑、删除管理员
3. **学生管理**：学生信息管理、状态管理
4. **任务类型管理**：任务类型的配置和管理
5. **个人信息管理**：用户个人资料设置

## 开发指南

### 安装依赖
```bash
npm install
# 或
yarn install
```

### 启动开发服务器
```bash
npm run dev
# 或
yarn dev
```

### 构建生产版本
```bash
npm run build
# 或
yarn build
```

### 预览生产构建
```bash
npm run preview
# 或
yarn preview
```

## 项目规范
- 使用Vue 3的Composition API进行开发
- 组件命名采用PascalCase（大驼峰）命名法
- API请求统一放在api目录下，按模块分类
- 公共组件放在components目录下
- 页面组件放在views目录下
- 使用Element Plus组件库进行UI开发
- 遵循ESLint代码规范
