<template>
  <div class="admin-home">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-text">
        <h1>系统概览</h1>
        <p>管理员：{{ userInfo.username }} | 今日是 {{ currentDate }}</p>
      </div>
      <div class="quick-stats">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(stat, index) in stats" :key="index">
            <div class="stat-card">
              <el-icon class="icon"><component :is="stat.icon" /></el-icon>
              <div class="content">
                <div class="number">{{ stat.number }}</div>
                <div class="label">{{ stat.label }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 主要内容区 -->
    <el-row :gutter="20" class="main-content">
      <!-- 左侧：数据统计 -->
      <el-col :span="16">
        <!-- 活动审核 -->
        <el-card class="pending-activities">
          <template #header>
            <div class="card-header">
              <h3>待审核活动</h3>
              <el-button type="primary" size="small">查看全部</el-button>
            </div>
          </template>
          <el-table :data="pendingActivities" style="width: 100%">
            <el-table-column prop="club" label="申请社团" />
            <el-table-column prop="activity" label="活动名称" />
            <el-table-column prop="date" label="申请日期" width="180" />
            <el-table-column label="操作" width="200">
              <template #default>
                <el-button type="success" link>通过</el-button>
                <el-button type="danger" link>拒绝</el-button>
                <el-button type="primary" link>详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <!-- 系统日志 -->
        <el-card class="system-logs">
          <template #header>
            <div class="card-header">
              <h3>系统日志</h3>
              <el-button text>导出</el-button>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="(log, index) in systemLogs"
              :key="index"
              :type="log.type"
              :timestamp="log.time"
              :hollow="true"
            >
              {{ log.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>

      <!-- 右侧：快捷操作 -->
      <el-col :span="8">
        <!-- 快捷操作 -->
        <el-card class="quick-actions">
          <template #header>
            <div class="card-header">
              <h3>快捷操作</h3>
            </div>
          </template>
          <div class="action-grid">
            <div
              v-for="(action, index) in quickActions"
              :key="index"
              class="action-item"
            >
              <el-button :type="action.type" :icon="action.icon">
                {{ action.label }}
              </el-button>
            </div>
          </div>
        </el-card>

        <!-- 系统状态 -->
        <el-card class="system-status">
          <template #header>
            <div class="card-header">
              <h3>系统状态</h3>
              <el-tag type="success">运行正常</el-tag>
            </div>
          </template>
          <div class="status-list">
            <div
              v-for="(item, index) in systemStatus"
              :key="index"
              class="status-item"
            >
              <div class="label">{{ item.label }}</div>
              <el-progress
                :percentage="item.value"
                :type="item.type"
                :status="item.status"
              />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from "vue";
import {
  UserFilled,
  Flag,
  Trophy,
  Promotion,
  Plus,
  Setting,
  Download,
  Upload,
  Monitor,
  Warning,
  Document,
} from "@element-plus/icons-vue";

const userInfo = ref(JSON.parse(localStorage.getItem("userInfo") || "{}"));

// 当前日期
const currentDate = new Date().toLocaleDateString("zh-CN", {
  year: "numeric",
  month: "long",
  day: "numeric",
  weekday: "long",
});

// 统计数据
const stats = [
  { icon: "Flag", number: "25", label: "注册社团" },
  { icon: "UserFilled", number: "1,280", label: "注册学生" },
  { icon: "Promotion", number: "168", label: "本月活动" },
  { icon: "Trophy", number: "86%", label: "活动完成率" },
];

// 待审核活动
const pendingActivities = [
  {
    club: "摄影社",
    activity: "校园之美摄影大赛",
    date: "2024-03-20",
  },
  {
    club: "街舞社",
    activity: "街舞表演赛",
    date: "2024-03-21",
  },
  {
    club: "读书会",
    activity: "经典名著读书会",
    date: "2024-03-22",
  },
];

// 系统日志
const systemLogs = [
  {
    content: "新增社团申请：动漫社",
    time: "10分钟前",
    type: "primary",
  },
  {
    content: "系统备份完成",
    time: "30分钟前",
    type: "success",
  },
  {
    content: "用户反馈：功能建议",
    time: "2小时前",
    type: "warning",
  },
  {
    content: "系统更新完成：版本1.2.0",
    time: "1天前",
    type: "info",
  },
];

// 快捷操作
const quickActions = [
  { label: "添加社团", icon: Plus, type: "primary" },
  { label: "系统设置", icon: Setting, type: "info" },
  { label: "数据备份", icon: Download, type: "warning" },
  { label: "导入数据", icon: Upload, type: "success" },
];

// 系统状态
const systemStatus = [
  {
    label: "CPU使用率",
    value: 32,
    type: "line",
    status: "success",
  },
  {
    label: "内存使用率",
    value: 65,
    type: "line",
    status: "warning",
  },
  {
    label: "存储空间",
    value: 45,
    type: "line",
    status: "success",
  },
];
</script>

<style scoped lang="scss">
.admin-home {
  .welcome-section {
    background: white;
    border-radius: 12px;
    padding: 24px;
    margin-bottom: 24px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);

    .welcome-text {
      margin-bottom: 24px;

      h1 {
        font-size: 28px;
        color: #1f2937;
        margin-bottom: 8px;
      }

      p {
        color: #6b7280;
        font-size: 14px;
      }
    }
  }

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 20px;
    display: flex;
    align-items: center;
    gap: 16px;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
    }

    .icon {
      font-size: 24px;
      color: #4f46e5;
      background: rgba(79, 70, 229, 0.1);
      padding: 12px;
      border-radius: 12px;
    }

    .content {
      .number {
        font-size: 24px;
        font-weight: 600;
        color: #1f2937;
        line-height: 1;
        margin-bottom: 4px;
      }

      .label {
        font-size: 14px;
        color: #6b7280;
      }
    }
  }

  .main-content {
    margin-top: 24px;

    .el-card {
      margin-bottom: 20px;
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    h3 {
      font-size: 16px;
      color: #1f2937;
      margin: 0;
    }
  }

  .quick-actions {
    .action-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 12px;

      .action-item {
        .el-button {
          width: 100%;
          justify-content: center;
          gap: 8px;
        }
      }
    }
  }

  .system-status {
    .status-list {
      display: flex;
      flex-direction: column;
      gap: 16px;

      .status-item {
        .label {
          font-size: 14px;
          color: #4b5563;
          margin-bottom: 8px;
        }
      }
    }
  }

  .system-logs {
    .el-timeline {
      padding: 16px;
    }
  }
}
</style>
