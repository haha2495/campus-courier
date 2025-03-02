<template>
  <div class="task-card" @click="handleClick">
    <!-- 卡片头部 - 任务编号和状态 -->
    <div class="card-header">
      <div class="left-section">
        <span class="task-no">{{ task.taskNo }}</span>
        <el-tag
          :type="getStatusType(task.status)"
          effect="light"
          class="status-tag"
        >
          {{ getStatusText(task.status) }}
        </el-tag>
      </div>
      <div v-if="task.urgent" class="urgent-tag">加急</div>
    </div>

    <!-- 卡片内容 -->
    <div class="card-body">
      <!-- 任务类型 -->
      <div class="task-type">
        <img class="type-icon" :src="taskType?.icon" alt="任务类型">
        <span>{{ taskType?.name }}</span>
      </div>

      <!-- 任务标题和描述 -->
      <div class="task-info">
        <div class="task-title">{{ task.title }}</div>
        <div class="task-desc">{{ task.description }}</div>
      </div>

      <!-- 地址信息 -->
      <div class="address-info">
        <div class="address-item" v-if="task.pickupAddress">
          <div class="address-icon pickup">取</div>
          <div class="address-content">
            <div class="address-label">取件地址</div>
            <div class="address-value">{{ task.pickupAddress }}</div>
          </div>
        </div>
        <div class="address-item" v-if="task.deliveryAddress">
          <div class="address-icon delivery">送</div>
          <div class="address-content">
            <div class="address-label">送达地址</div>
            <div class="address-value">{{ task.deliveryAddress }}</div>
          </div>
        </div>
      </div>

      <!-- 规格信息 -->
      <div class="specs-info">
        <div class="spec-item">
          <span class="spec-label">重量</span>
          <span class="spec-value">{{ task.estimatedWeight }}kg</span>
        </div>
        <div class="spec-item">
          <span class="spec-label">大小</span>
          <span class="spec-value">{{ task.estimatedSize }}</span>
        </div>
      </div>
    </div>

    <!-- 卡片底部 -->
    <div class="card-footer">
      <div class="left-info">
        <div class="price-info">
          <span class="amount">{{ task.reward }}</span>
          <span v-if="task.urgent" class="urgent-price">+{{ task.urgentReward }}</span>
        </div>
        <div class="time-info">
          <el-icon><Timer /></el-icon>
          <span>{{ formatDateTime(task.createTime) }}</span>
        </div>
      </div>
      <el-button type="primary" plain @click.stop="handleAction">
        {{ actionText }}
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { Timer } from '@element-plus/icons-vue'
import { TaskStatusEnum } from '@/utils/enums/taskEnums'

const props = defineProps({
  task: {
    type: Object,
    required: true
  },
  taskType: {
    type: Object,
    default: () => ({})
  },
  actionText: {
    type: String,
    default: '查看详情'
  }
})

const emit = defineEmits(['click', 'action'])

// 获取状态类型
const getStatusType = (status) => {
  return TaskStatusEnum.getType(status)
}

// 获取状态文本
const getStatusText = (status) => {
  return TaskStatusEnum.getDesc(status)
}

// 格式化日期时间
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return ''
  const date = new Date(dateTimeStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  }).replace(/\//g, '-')
}

// 点击卡片
const handleClick = () => {
  emit('click', props.task)
}

// 点击操作按钮
const handleAction = () => {
  emit('action', props.task)
}
</script>

<style scoped lang="scss">
/* 卡片基础样式 */
.task-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #ebeef5;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  min-width: 320px;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  }

  /* 卡片头部 */
  .card-header {
    padding: 12px 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #f0f0f0;
    background: #fafafa;

    .left-section {
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .task-no {
      font-family: monospace;
      color: #666;
      font-size: 13px;
    }

    .status-tag {
      font-size: 12px;
      padding: 2px 10px;
      border-radius: 12px;
    }

    .urgent-tag {
      background: #fff2f0;
      color: #ff4d4f;
      padding: 2px 8px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
      display: flex;
      align-items: center;
      gap: 4px;

      &::before {
        content: '🔥';
        font-size: 14px;
      }
    }
  }

  /* 卡片内容 */
  .card-body {
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 16px;

    .task-type {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 12px;

      .type-icon {
        width: 24px;
        height: 24px;
        border-radius: 4px;
      }

      span {
        font-size: 14px;
        color: #333;
        font-weight: 500;
      }
    }

    .task-info {
      margin-bottom: 16px;

      .task-title {
        font-size: 16px;
        font-weight: 600;
        color: #333;
        margin-bottom: 8px;
        line-height: 1.4;
      }

      .task-desc {
        font-size: 14px;
        color: #666;
        line-height: 1.6;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }
    }

    .address-info {
      margin-bottom: 20px;

      .address-item {
        display: flex;
        align-items: flex-start;
        gap: 16px;
        margin-bottom: 16px;

        &:last-child {
          margin-bottom: 0;
        }

        .address-icon {
          width: 28px;
          height: 28px;
          border-radius: 6px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-size: 12px;
          flex-shrink: 0;

          &.pickup {
            background: #409eff;
          }

          &.delivery {
            background: #67c23a;
          }
        }

        .address-content {
          flex: 1;

          .address-label {
            font-size: 12px;
            color: #999;
            margin-bottom: 4px;
          }

          .address-value {
            font-size: 14px;
            color: #333;
            line-height: 1.4;
          }
        }
      }
    }

    .specs-info {
      padding: 16px 20px;
      margin-bottom: 20px;
      display: flex;
      gap: 24px;
      background: #f9fafb;
      border-radius: 8px;

      .spec-item {
        display: flex;
        align-items: center;
        gap: 8px;

        .spec-label {
          font-size: 13px;
          color: #999;
        }

        .spec-value {
          font-size: 14px;
          color: #333;
          font-weight: 500;
        }
      }
    }
  }

  /* 卡片底部 */
  .card-footer {
    padding: 16px 20px;
    border-top: 1px solid #f0f0f0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fafafa;

    .left-info {
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    .price-info {
      display: flex;
      align-items: baseline;
      gap: 4px;

      .amount {
        font-size: 20px;
        font-weight: 600;
        color: #ff9500;

        &::before {
          content: '¥';
          font-size: 14px;
          margin-right: 2px;
        }
      }

      .urgent-price {
        font-size: 14px;
        color: #ff4d4f;
        font-weight: 500;
        margin-left: 4px;
      }
    }

    .time-info {
      display: flex;
      align-items: center;
      gap: 4px;
      color: #999;
      font-size: 12px;

      .el-icon {
        font-size: 14px;
      }
    }

    .el-button {
      padding: 8px 20px;
      border-radius: 20px;
      font-size: 14px;
    }
  }
}
</style> 