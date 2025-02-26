<template>
  <view class="my-tasks-container">
    <!-- 状态筛选器 -->
    <view class="status-filter">
      <view 
        v-for="(status, index) in statusOptions" 
        :key="index"
        class="status-item"
        :class="{ active: currentStatus === status.value }"
        @click="filterByStatus(status.value)"
      >
        {{ status.label }}
      </view>
    </view>

    <!-- 任务列表 -->
    <scroll-view
      scroll-y
      class="task-list-scroll"
      @scrolltolower="loadMoreTasks"
      @refresherrefresh="refreshTasks"
      refresher-enabled
      :refresher-triggered="isRefreshing"
    >
      <!-- 暂无数据提示 -->
      <view v-if="taskList.length === 0" class="empty-container">
        <view class="empty-animation">
          <view class="empty-courier-icon">🛵</view>
          <view class="empty-dots">
            <view class="dot"></view>
            <view class="dot"></view>
            <view class="dot"></view>
          </view>
        </view>
        <text class="empty-text">还没有任务哦！</text>
        <text class="empty-subtext">快去发布一个新任务吧～</text>
        <view class="empty-action" @click="navigateToPublish">
          <text class="action-text">去发布</text>
          <text class="action-arrow">→</text>
        </view>
      </view>

      <!-- 任务卡片列表 -->
      <view v-else class="task-list">
        <!-- 任务卡片 -->
        <view 
          v-for="(task, index) in taskList" 
          :key="task.id"
          class="task-card"
          @click="navigateToTaskDetail(task.id)"
        >
          <!-- 任务卡片内容将在后续实现 -->
          <view class="card-placeholder">任务卡片 {{ index + 1 }}</view>
        </view>
      </view>

      <!-- 加载更多 -->
      <view v-if="taskList.length > 0" class="load-more">
        {{ hasMoreData ? '加载中...' : '没有更多了' }}
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// 状态筛选选项
const statusOptions = [
  { label: '全部', value: 'all' },
  { label: '待接单', value: 'pending' },
  { label: '进行中', value: 'ongoing' },
  { label: '已完成', value: 'completed' },
  { label: '已取消', value: 'cancelled' }
]
const currentStatus = ref('all')

// 任务列表数据
const taskList = ref([])
const isRefreshing = ref(false)
const hasMoreData = ref(true)
const currentPage = ref(1)
const pageSize = 10

// 状态筛选
const filterByStatus = (status) => {
  currentStatus.value = status
  refreshTasks()
}

// 刷新任务列表
const refreshTasks = () => {
  isRefreshing.value = true
  currentPage.value = 1
  
  // 这里将来需要调用API获取任务列表
  // fetchTaskList()
  
  // 模拟加载数据
  setTimeout(() => {
    taskList.value = [] // 先清空列表
    isRefreshing.value = false
    hasMoreData.value = true
  }, 1000)
}

// 加载更多任务
const loadMoreTasks = () => {
  if (!hasMoreData.value) return
  
  currentPage.value++
  
  // 这里将来需要调用API加载更多任务
  // fetchMoreTasks()
  
  // 模拟加载数据
  setTimeout(() => {
    // 模拟没有更多数据
    if (currentPage.value > 3) {
      hasMoreData.value = false
    }
  }, 1000)
}

// 跳转到任务详情
const navigateToTaskDetail = (taskId) => {
  uni.navigateTo({
    url: `/pages/task-detail/index?id=${taskId}`
  })
}

// 跳转到发布任务页面
const navigateToPublish = () => {
  uni.navigateTo({
    url: '/pages/publish/index'
  })
}

// 页面加载时获取任务列表
onMounted(() => {
  refreshTasks()
})
</script>

<style lang="scss" scoped>
.my-tasks-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f7fa;
}

.status-filter {
  display: flex;
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  white-space: nowrap;
  overflow-x: auto;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  
  &::-webkit-scrollbar {
    display: none;
  }
  
  .status-item {
    padding: 10rpx 30rpx;
    margin-right: 20rpx;
    font-size: 26rpx;
    color: #666;
    background-color: #f5f7fa;
    border-radius: 30rpx;
    transition: all 0.3s;
    
    &.active {
      background-color: #fff3e0;
      color: #ff9500;
      font-weight: 500;
    }
    
    &:last-child {
      margin-right: 0;
    }
  }
}

.task-list-scroll {
  flex: 1;
  padding: 30rpx;
}

.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding-top: 150rpx;
  
  .empty-animation {
    position: relative;
    width: 200rpx;
    height: 200rpx;
    margin-bottom: 40rpx;
    
    .empty-courier-icon {
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      font-size: 60rpx;
      animation: drive 3s infinite;
    }
    
    .empty-dots {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      display: flex;
      justify-content: center;
      
      .dot {
        width: 16rpx;
        height: 16rpx;
        border-radius: 50%;
        background-color: #cccccc;
        margin: 0 10rpx;
        opacity: 0.6;
        
        &:nth-child(1) {
          animation: pulse 1.5s infinite 0.2s;
        }
        
        &:nth-child(2) {
          animation: pulse 1.5s infinite 0.4s;
        }
        
        &:nth-child(3) {
          animation: pulse 1.5s infinite 0.6s;
        }
      }
    }
  }
  
  .empty-text {
    font-size: 34rpx;
    color: #666;
    margin-bottom: 15rpx;
    font-weight: 500;
  }
  
  .empty-subtext {
    font-size: 28rpx;
    color: #999;
    margin-bottom: 40rpx;
  }
  
  .empty-action {
    background: linear-gradient(to right, #ff9500, #ffbd5c);
    padding: 18rpx 40rpx;
    border-radius: 40rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 6rpx 16rpx rgba(255, 149, 0, 0.3);
    
    .action-text {
      color: #ffffff;
      font-size: 28rpx;
      font-weight: 500;
    }
    
    .action-arrow {
      color: #ffffff;
      font-size: 28rpx;
      margin-left: 10rpx;
      font-weight: 500;
    }
  }
}

.task-list {
  padding-bottom: 30rpx;
}

.task-card {
  background-color: #ffffff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
  
  &:active {
    transform: scale(0.98);
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
  }
  
  .card-placeholder {
    height: 180rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999;
    font-size: 28rpx;
  }
}

.load-more {
  text-align: center;
  font-size: 26rpx;
  color: #999;
  padding: 30rpx 0;
}

@keyframes drive {
  0% {
    left: -60rpx;
  }
  50% {
    left: 200rpx;
  }
  100% {
    left: -60rpx;
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.2;
    transform: scale(0.8);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.2);
  }
}
</style> 