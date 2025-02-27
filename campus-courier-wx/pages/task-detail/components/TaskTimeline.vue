<!-- 任务状态时间线组件 -->
<template>
  <view class="timeline-card">
    <view class="card-header">
      <text class="section-title">任务进度</text>
    </view>
    
    <view class="timeline-list">
      <view 
        v-for="(log, index) in taskLogs" 
        :key="index"
        class="timeline-item"
        :class="{ 'is-last': index === taskLogs.length - 1 }"
      >
        <!-- 时间线节点 -->
        <view class="timeline-node" :class="getActionClass(log.action)">
          <view class="node-icon"></view>
          <view class="node-line" v-if="index !== taskLogs.length - 1"></view>
        </view>
        
        <!-- 内容区域 -->
        <view class="timeline-content">
          <view class="log-action">{{ getActionText(log.action) }}</view>
          <view class="log-desc">{{ log.description }}</view>
          <view class="log-time">{{ formatTime(log.createTime) }}</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue'
import { TASK_ACTION, getActionText, getActionClass } from '@/constants/taskAction'

// 接收父组件传递的任务日志数据
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
</script>

<style lang="scss" scoped>
.timeline-card {
  margin: 30rpx;
  background: #fff;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.card-header {
  padding: 24rpx 30rpx;
  border-bottom: 2rpx solid #f5f7fa;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    display: flex;
    align-items: center;

    &::before {
      content: '';
      width: 8rpx;
      height: 32rpx;
      background-color: #07c160;
      margin-right: 16rpx;
      border-radius: 4rpx;
    }
  }
}

.timeline-list {
  padding: 30rpx;
}

.timeline-item {
  display: flex;
  position: relative;
  padding-bottom: 40rpx;
  
  &.is-last {
    padding-bottom: 0;
  }
}

.timeline-node {
  position: relative;
  width: 40rpx;
  margin-right: 30rpx;
  
  .node-icon {
    width: 24rpx;
    height: 24rpx;
    border-radius: 50%;
    background: #ddd;
    position: relative;
    z-index: 1;
  }
  
  .node-line {
    position: absolute;
    left: 11rpx;
    top: 24rpx;
    width: 2rpx;
    height: calc(100% + 16rpx);
    background: #eee;
  }
  
  // 不同状态节点的样式
  &.node-publish .node-icon {
    background: #07c160;
  }
  
  &.node-accept .node-icon {
    background: #1890ff;
  }
  
  &.node-start .node-icon {
    background: #722ed1;
  }
  
  &.node-finish .node-icon {
    background: #52c41a;
  }
  
  &.node-cancel .node-icon {
    background: #ff4d4f;
  }
}

.timeline-content {
  flex: 1;
  padding-bottom: 16rpx;
  
  .log-action {
    font-size: 28rpx;
    color: #333;
    font-weight: 600;
    margin-bottom: 8rpx;
  }
  
  .log-desc {
    font-size: 26rpx;
    color: #666;
    line-height: 1.6;
  }
  
  .log-time {
    font-size: 24rpx;
    color: #999;
    margin-top: 8rpx;
  }
}
</style> 