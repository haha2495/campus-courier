<template>
  <view class="my-tasks-container">
    <!-- 顶部切换栏 -->
    <view class="tab-header">
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'published' }" 
        @click="switchTab('published')"
      >
        我发布的
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'accepted' }" 
        @click="switchTab('accepted')"
      >
        我接单的
      </view>
      <view class="tab-slider" :style="tabSliderStyle"></view>
    </view>

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
        <image class="empty-image" src="/static/images/empty-tasks.png" mode="aspectFit"></image>
        <text class="empty-text">暂无数据</text>
        <text class="empty-subtext">下拉刷新试试看</text>
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

// 任务类型标签页
const activeTab = ref('published') // 默认选中"我发布的"
const tabSliderStyle = computed(() => {
  return {
    transform: activeTab.value === 'published' ? 'translateX(0)' : 'translateX(100%)'
  }
})

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

// 切换标签页
const switchTab = (tab) => {
  activeTab.value = tab
  refreshTasks()
}

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

.tab-header {
  display: flex;
  position: relative;
  background-color: #ffffff;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  padding: 20rpx 0;
  z-index: 10;
  
  .tab-item {
    flex: 1;
    text-align: center;
    font-size: 30rpx;
    color: #666;
    position: relative;
    padding: 16rpx 0;
    transition: color 0.3s;
    
    &.active {
      color: #ff9500;
      font-weight: 500;
    }
  }
  
  .tab-slider {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 50%;
    height: 4rpx;
    background-color: #ff9500;
    transition: transform 0.3s ease;
  }
}

.status-filter {
  display: flex;
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  white-space: nowrap;
  overflow-x: auto;
  margin-top: 2rpx;
  
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
  padding-top: 200rpx;
  
  .empty-image {
    width: 240rpx;
    height: 240rpx;
    margin-bottom: 20rpx;
    opacity: 0.6;
  }
  
  .empty-text {
    font-size: 32rpx;
    color: #999;
    margin-bottom: 10rpx;
  }
  
  .empty-subtext {
    font-size: 26rpx;
    color: #bbb;
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
</style> 