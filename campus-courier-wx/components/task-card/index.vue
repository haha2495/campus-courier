<template>
  <view class="task-card" :class="{ 'urgent-card': task.task.urgent }" @click="handleClick">
    <!-- 卡片头部 - 类型和状态 -->
    <view class="card-header">
      <view class="left-section">
        <!-- 加急标记 -->
        <view v-if="task.task.urgent" class="urgent-tag">
          <text>急</text>
        </view>
        <view class="task-type">
          <image class="type-icon" :src="task.taskType.icon" mode="aspectFit"></image>
          <text>{{ task.taskType.typeName }}</text>
        </view>
      </view>
      <view class="task-status" :style="getStatusStyle(task.task.status)">
        {{ getStatusText(task.task.status) }}
      </view>
    </view>

    <!-- 卡片内容主体 -->
    <view class="card-body">
      <!-- 任务标题 -->
      <view class="task-title">{{ task.task.title }}</view>

      <!-- 任务描述 -->
      <view class="task-desc">{{ task.task.description }}</view>

      <!-- 地址信息 -->
      <view class="address-info" v-if="task.task.pickupAddress || task.task.deliveryAddress">
        <view class="address-item" v-if="task.task.pickupAddress">
          <view class="address-icon pickup-icon">取</view>
          <text class="address-value">{{ task.task.pickupAddress }}</text>
        </view>
        <view class="address-item" v-if="task.task.deliveryAddress">
          <view class="address-icon delivery-icon">送</view>
          <text class="address-value">{{ task.task.deliveryAddress }}</text>
        </view>
      </view>

      <!-- 任务规格信息 -->
      <view class="task-specs" v-if="task.task.estimatedWeight || task.task.estimatedSize">
        <view class="spec-item" v-if="task.task.estimatedWeight">
          <text class="spec-label">预估重量：</text>
          <text class="spec-value">{{ task.task.estimatedWeight }}kg</text>
        </view>
        <view class="spec-item" v-if="task.task.estimatedSize">
          <text class="spec-label">预估大小：</text>
          <text class="spec-value">{{ task.task.estimatedSize }}</text>
        </view>
      </view>

      <!-- 任务关键信息：赏金和截止时间 -->
      <view class="task-info">
        <view class="info-item reward">
          <text class="reward-icon">¥</text>
          <text class="reward-value">{{ task.task.reward }}</text>
          <text v-if="task.task.urgent && task.task.urgentReward" class="urgent-reward">{{
              task.task.urgentReward
            }}
          </text>
        </view>
        <view class="info-item time">
          <text class="time-label">期望送达：</text>
          <text class="time-value">{{ formatTime(task.task.expectedFinishTime) }}</text>
        </view>
      </view>
    </view>

    <!-- 卡片底部 - 时间和操作按钮 -->
    <view class="card-footer">
      <view class="time-info">{{ formatRelativeTime(task.task.createTime) }}</view>
      <!-- 操作按钮区域 - 通过插槽实现自定义 -->
      <view class="action-area">
        <slot name="actions"></slot>
      </view>
    </view>
  </view>
</template>

<script setup>
import {TASK_STATUS} from '@/constants/taskStatus'

const props = defineProps({
  task: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['click', 'start', 'complete'])

// 格式化时间显示 (YYYY-MM-DD HH:MM)
const formatTime = (timeString) => {
  if (!timeString) return '';
  const date = new Date(timeString);

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');

  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
}

// 格式化相对时间 (例如：刚刚、5分钟前、1小时前)
const formatRelativeTime = (timeString) => {
  if (!timeString) return '';

  const now = new Date();
  const date = new Date(timeString);
  const diff = Math.floor((now - date) / 1000); // 差值（秒）

  if (diff < 60) {
    return '刚刚';
  } else if (diff < 3600) {
    return Math.floor(diff / 60) + '分钟前';
  } else if (diff < 86400) {
    return Math.floor(diff / 3600) + '小时前';
  } else if (diff < 604800) { // 7天内
    return Math.floor(diff / 86400) + '天前';
  } else {
    // 超过7天显示具体日期
    return formatTime(timeString);
  }
}

// 获取任务状态文本
const getStatusText = (status) => {
  return TASK_STATUS.getStatusText(status)
}

// 获取任务状态样式
const getStatusStyle = (status) => {
  const colors = TASK_STATUS.getStatusColor(status)
  return {
    color: colors.text,
    backgroundColor: colors.background
  }
}

// 点击卡片
const handleClick = () => {
  emit('click', props.task)
}
</script>

<style lang="scss" scoped>
.task-card {
  background-color: #ffffff;
  border-radius: 20rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: all 0.3s ease;
  position: relative;
  border: 1px solid #f0f0f0;

  &:active {
    transform: translateY(2rpx);
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
  }

  // 加急任务卡片样式
  &.urgent-card {
    background: linear-gradient(135deg, #fff5f5 0%, #fff8f8 100%);
    border-left: 8rpx solid #ff4d4f;
    box-shadow: 0 4rpx 20rpx rgba(255, 77, 79, 0.2);
    border: 1px solid rgba(255, 77, 79, 0.3);
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 4rpx;
      background: linear-gradient(90deg, #ff4d4f, #ff7875);
    }

    .card-header {
      border-bottom-color: rgba(255, 77, 79, 0.1);
      background: linear-gradient(90deg, rgba(255, 77, 79, 0.05) 0%, transparent 100%);
    }

    .card-footer {
      border-top-color: rgba(255, 77, 79, 0.1);
      background: linear-gradient(90deg, rgba(255, 77, 79, 0.05) 0%, transparent 100%);
    }

    .task-title {
      color: #ff4d4f !important;
    }

    // 闪光效果
    &::after {
      content: '';
      position: absolute;
      top: -50%;
      left: -50%;
      width: 200%;
      height: 200%;
      background: linear-gradient(
              to right,
              rgba(255, 255, 255, 0) 0%,
              rgba(255, 255, 255, 0.1) 50%,
              rgba(255, 255, 255, 0) 100%
      );
      transform: rotate(45deg);
      animation: shine 3s infinite;
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 24rpx 30rpx 16rpx;
    border-bottom: 1rpx solid rgba(0, 0, 0, 0.03);

    .left-section {
      display: flex;
      align-items: center;
      gap: 12rpx;
    }

    .urgent-tag {
      background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
      color: white;
      font-size: 24rpx;
      font-weight: bold;
      width: 40rpx;
      height: 40rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4rpx 12rpx rgba(255, 77, 79, 0.4);
      animation: urgentBounce 1s ease infinite;
    }

    .task-type {
      display: flex;
      align-items: center;

      .type-icon {
        width: 36rpx;
        height: 36rpx;
        margin-right: 10rpx;
      }

      text {
        font-size: 26rpx;
        color: #333;
        font-weight: 500;
      }
    }

    .task-status {
      font-size: 24rpx;
      padding: 6rpx 16rpx;
      border-radius: 20rpx;
      font-weight: 500;
    }
  }

  .card-body {
    padding: 20rpx 30rpx;

    .task-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #333;
      margin-bottom: 12rpx;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 1;
      overflow: hidden;
    }

    .task-desc {
      font-size: 26rpx;
      color: #666;
      margin-bottom: 16rpx;
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
      line-height: 1.5;
    }

    .address-info {
      margin-bottom: 16rpx;

      .address-item {
        display: flex;
        align-items: center;
        margin-bottom: 10rpx;

        .address-icon {
          width: 36rpx;
          height: 36rpx;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 20rpx;
          color: white;
          font-weight: bold;
          margin-right: 10rpx;
          flex-shrink: 0;

          &.pickup-icon {
            background-color: #1989fa;
          }

          &.delivery-icon {
            background-color: #07c160;
          }
        }

        .address-value {
          font-size: 26rpx;
          color: #333;
          flex: 1;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
    }

    .task-specs {
      display: flex;
      flex-wrap: wrap;
      margin-bottom: 16rpx;

      .spec-item {
        display: flex;
        align-items: center;
        margin-right: 24rpx;
        margin-bottom: 8rpx;

        .spec-label {
          font-size: 24rpx;
          color: #999;
        }

        .spec-value {
          font-size: 24rpx;
          color: #333;
          font-weight: 500;
        }
      }
    }

    .task-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 16rpx;

      .info-item {
        display: flex;
        align-items: center;

        &.reward {
          .reward-icon {
            font-size: 24rpx;
            color: #ff9500;
            margin-right: 4rpx;
            font-weight: 700;
          }

          .reward-value {
            font-size: 36rpx;
            color: #ff9500;
            font-weight: 700;
          }

          .urgent-reward {
            font-size: 24rpx;
            color: #ff4d4f;
            margin-left: 6rpx;
            font-weight: 600;
            display: inline-block;
            animation: bounce 1s ease infinite;
            position: relative;

            &::before {
              content: '+';
              color: #ff4d4f;
              margin-right: 2rpx;
            }

            &::after {
              content: '加急';
              position: absolute;
              top: -20rpx;
              right: -10rpx;
              font-size: 20rpx;
              color: #ff4d4f;
              transform: scale(0.8);
              opacity: 0.8;
            }
          }
        }

        &.time {
          .time-label {
            font-size: 24rpx;
            color: #999;
          }

          .time-value {
            font-size: 24rpx;
            color: #333;
            font-weight: 500;
          }
        }
      }
    }
  }

  .card-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16rpx 30rpx;
    border-top: 1rpx solid rgba(0, 0, 0, 0.03);

    .time-info {
      font-size: 24rpx;
      color: #999;
    }

    .action-area {
      display: flex;
      gap: 16rpx;
    }
  }
}

// 闪光动画
@keyframes shine {
  0% {
    transform: translateX(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) rotate(45deg);
  }
}

// 添加跳动动画
@keyframes urgentBounce {
  0%, 100% {
    transform: scale(1) translateY(0);
  }
  20% {
    transform: scale(1.1) translateY(-4rpx);
  }
  40% {
    transform: scale(0.9) translateY(4rpx);
  }
  60% {
    transform: scale(1.05) translateY(-2rpx);
  }
  80% {
    transform: scale(0.95) translateY(2rpx);
  }
}

// 弹跳动画
@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-4rpx);
  }
}

.waiting-confirm-tip {
  display: flex;
  align-items: center;
  padding: 8rpx 16rpx;
  background-color: #FEF3C7;
  border-radius: 8rpx;

  .tip-text {
    margin-left: 8rpx;
    font-size: 24rpx;
    color: #F59E0B;
  }
}
</style> 