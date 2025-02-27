<!-- 任务状态时间线组件 -->
<template>
  <view class="timeline-container">
    <!-- 空状态 -->
    <view v-if="!taskLogs || taskLogs.length === 0" class="empty-state">
      <u-empty mode="data" text="暂无任务进度"></u-empty>
    </view>

    <!-- 时间线 -->
    <view v-else class="timeline">
      <view 
        v-for="(log, index) in taskLogs" 
        :key="index" 
        class="timeline-item"
        :class="{ 'timeline-item-last': index === taskLogs.length - 1 }"
      >
        <!-- 时间线节点 -->
        <view class="timeline-node" :class="getActionClass(log.action)">
          <u-icon 
            :name="getActionIcon(log.action)"
            :color="getActionColor(log.action)"
            size="40"
          ></u-icon>
        </view>

        <!-- 内容区域 -->
        <view class="timeline-content">
          <!-- 操作信息 -->
          <view class="action-info">
            <text class="action-text">{{ getActionText(log.action) }}</text>
            <text class="action-time">{{ formatTime(log.actionTime) }}</text>
          </view>

          <!-- 备注信息 -->
          <view v-if="log.remark" class="remark">
            {{ log.remark }}
          </view>

          <!-- 图片展示 -->
          <view v-if="log.images && log.images.length > 0" class="image-grid">
            <image
              v-for="(image, imgIndex) in log.images"
              :key="imgIndex"
              :src="image"
              mode="aspectFill"
              class="log-image"
              @click="previewImage(image, log.images)"
            ></image>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue'
import { 
  getActionText, 
  getActionIcon, 
  getActionColor, 
  getActionClass 
} from '@/constants/taskAction'

// 接收任务日志列表参数
const props = defineProps({
  taskLogs: {
    type: Array,
    default: () => []
  }
})

// 格式化时间
const formatTime = (timeString) => {
  if (!timeString) return ''
  const date = new Date(timeString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

// 预览图片
const previewImage = (current, urls) => {
  uni.previewImage({
    current,
    urls
  })
}
</script>

<style lang="scss" scoped>
.timeline-container {
  padding: 30rpx;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300rpx;
}

.timeline {
  position: relative;
  padding: 20rpx 0;
}

.timeline-item {
  position: relative;
  padding-left: 80rpx;
  padding-bottom: 60rpx;

  &::before {
    content: '';
    position: absolute;
    left: 39rpx;
    top: 80rpx;
    bottom: 0;
    width: 2rpx;
    background: linear-gradient(180deg, 
      var(--timeline-line-color, rgba(203, 213, 225, 0.5)) 0%,
      var(--timeline-line-color, rgba(203, 213, 225, 0.3)) 100%);
  }

  &.timeline-item-last {
    padding-bottom: 0;
    
    &::before {
      display: none;
    }
  }
}

.timeline-node {
  position: absolute;
  left: 20rpx;
  top: 0;
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);
  z-index: 1;
}

.timeline-content {
  background: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  
  .action-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16rpx;
    
    .action-text {
      font-size: 30rpx;
      font-weight: 600;
      color: #1F2937;
    }
    
    .action-time {
      font-size: 24rpx;
      color: #6B7280;
    }
  }
  
  .remark {
    font-size: 28rpx;
    color: #4B5563;
    line-height: 1.5;
    margin-bottom: 16rpx;
    padding: 16rpx;
    background: #F9FAFB;
    border-radius: 8rpx;
  }
  
  .image-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 16rpx;
    margin-top: 16rpx;
    
    .log-image {
      width: 100%;
      height: 180rpx;
      border-radius: 8rpx;
      background: #F3F4F6;
    }
  }
}

/* 节点样式变体 */
.node-publish {
  --timeline-line-color: rgba(59, 130, 246, 0.5);
  .u-icon {
    animation: fadeInScale 0.3s ease;
  }
}

.node-accept {
  --timeline-line-color: rgba(16, 185, 129, 0.5);
}

.node-cancel {
  --timeline-line-color: rgba(239, 68, 68, 0.5);
}

.node-start {
  --timeline-line-color: rgba(245, 158, 11, 0.5);
}

.node-complete {
  --timeline-line-color: rgba(16, 185, 129, 0.5);
}

.node-confirm {
  --timeline-line-color: rgba(99, 102, 241, 0.5);
}

.node-dispute {
  --timeline-line-color: rgba(249, 115, 22, 0.5);
}

.node-resolve {
  --timeline-line-color: rgba(16, 185, 129, 0.5);
}

.node-evaluate {
  --timeline-line-color: rgba(139, 92, 246, 0.5);
}

/* 动画效果 */
@keyframes fadeInScale {
  from {
    opacity: 0;
    transform: scale(0.8);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

/* 适配暗黑模式 */
@media (prefers-color-scheme: dark) {
  .timeline-content {
    background: #1F2937;
    
    .action-info {
      .action-text {
        color: #F9FAFB;
      }
      
      .action-time {
        color: #9CA3AF;
      }
    }
    
    .remark {
      background: #374151;
      color: #D1D5DB;
    }
  }
}
</style> 