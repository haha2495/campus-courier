# Campus Courier・校园跑腿平台（含 AI 纠纷智能判责）

> 一个面向高校学生的校园跑腿 O2O 平台：快递代取 / 食堂打饭 / 商品代购，完整跑通「发单 → 抢单 → 实时沟通 → 结算 → 评价 → 纠纷处理」交易闭环。
>
> **核心亮点：独立设计并落地「AI 纠纷智能判责」模块**
>
> 接入 DeepSeek 大模型，自动读取订单上下文与聊天记录，输出结构化责任判定，实现 "AI 初判 → 人工复核" 的人机协同。

![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F)
![Vue](https://img.shields.io/badge/Frontend-Vue3-4FC08D)
![uni-app](https://img.shields.io/badge/MiniProgram-uni-app-2B9939)
![LLM](https://img.shields.io/badge/LLM-DeepSeek-3C7CFF)

---

## 项目亮点

* **完整交易闭环**：微信小程序（用户端）+ Vue 管理后台（运营端）+ Spring Boot 后端，鉴权 / 实时通讯 / 余额结算 / 评价体系一应俱全
* **AI 纠纷判责**：引入大模型解决交易纠纷人工判责慢、成本高的痛点，详见下方 [AI 模块](#ai-纠纷智能判责模块)
* **工程规范**：分层清晰（Controller → Service → Handler → Mapper）、统一响应封装、事务管理、权限控制

## 产品截图

> （上传小程序首页 / 任务大厅 / 纠纷详情 / AI 判责卡片截图后替换此处）

---

## 系统架构

```
┌─────────────────────┐   ┌──────────────────────┐
│  微信小程序（用户端）  │   │  Vue3 管理后台（运营端）│
│  Vue3 + uni-app     │   │  Element Plus        │
└─────────┬───────────┘   └──────────┬───────────┘
          │  HTTPS / WebSocket       │
┌─────────▼──────────────────────────▼───────────┐
│            Spring Boot 后端服务                  │
│  Controller → Service → Handler → Mapper        │
│  Sa-Token 鉴权 │ Redis │ WebSocket 实时消息      │
│  AI 模块：DeepSeek API → 大模型判责             │
└─────────────────────┬────────────────────────────┘
                      │
              ┌───────▼───────┐
              │  MySQL 8.0     │
              └───────────────┘
```

## 核心功能

### 小程序端

* 发单（选择任务类型 / 取件送达地址 / 酬劳）、任务大厅抢单
* 任务详情：双方实时聊天（WebSocket）、进度时间线、凭证上传
* 余额体系：充值 / 提现 / 结算
* 双向评价、纠纷申诉提交

### 管理后台

* 用户与学生管理、任务类型配置、订单管理
* 数据看板（ECharts）、纠纷审核工作台
* **AI 判责审核**：查看 AI 判定建议，一键采纳 / 修正

### AI 模块

* 纠纷提交后，大模型自动输出结构化责任判定
* 按置信度分级流转，高置信自动采纳、低置信转人工

---

## AI 纠纷智能判责模块

### 解决的问题

平台交易涉及金钱，历史纠纷（超时、丢件、物品损坏、酬劳争议）依赖管理员人工查看聊天记录与凭证，单条处理约 20 分钟，高峰期积压严重。

### 方案设计

采用 **大模型 + 人机协同**，而非全自动判责 —— 在效率与资金安全之间做平衡。

```
纠纷提交
  → 大模型读取【订单信息 + 聊天记录 + 时间线 + 凭证】
  → 输出 JSON：{责任方, 责任比例, 判定依据, 处理建议, 置信度}
  → 按置信度流转：
       > 0.85 自动采纳
       0.6~0.85 管理员一键确认
       < 0.6 转人工
  → 管理员修正结果 → Badcase 回流 → 迭代 Prompt
```

### 技术要点

* **大模型**：DeepSeek，对比评测豆包 / 通义千问后选型
* **Prompt 工程**：System 层注入角色与平台判责规则，上下文层拼接订单与聊天记录，Few-shot 稳定输出格式，强约束 JSON 结构化输出
* **效果**：历史纠纷回放判责一致率约 82%，单条处理时长由约 20min 降至约 5min

> 设计思路与 Prompt 细节见
>
> [docs / 简历项目包装 - AI 产品经理.md](docs/简历项目包装-AI产品经理.md)

---

## 技术栈

| 层    | 技术                                                      |
| ---- | ------------------------------------------------------- |
| 小程序端 | Vue 3、uni-app、uview-plus、Pinia                          |
| 管理后台 | Vue 3、Element Plus、Vue Router、Axios                     |
| 后端   | Spring Boot、MyBatis-Plus、MySQL、Redis、Sa-Token、WebSocket |
| AI   | DeepSeek API、Prompt Engineering、JSON 结构化输出               |
| 其他   | ECharts、Maven、JDK 17                                    |

## 快速启动

环境要求：JDK 17+、Node.js 16+、MySQL 8.0+、Redis 6.0+

```
# 1. 初始化数据库
mysql -u root -p < sql/campus_courier.sql

# 2. 启动后端
mvn spring-boot:run

# 3. 启动管理后台
cd campus-courier-web && npm install && npm run dev

# 4. 启动小程序
cd campus-courier-wx && npm install && npm run dev:mp-weixin
```

## 目录结构

```
campus-courier/
├── campus-courier-wx/     # 微信小程序端（uni-app）
├── campus-courier-web/    # Vue3 管理后台
├── src/                   # Spring Boot 后端
│   └── main/java/.../courier/
│       ├── controller/    # 接口层
│       ├── service/       # 业务层
│       ├── mapper/        # 数据访问层
│       └── model/         # 实体 / 请求对象
├── sql/                   # 数据库脚本
└── docs/                  # 设计与简历素材
```

## 作者

张若水・人工智能专业・求职方向 AI 产品经理
