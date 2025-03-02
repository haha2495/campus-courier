<template>
  <view class="runner-tasks-container">
    <!-- 统计卡片区域 -->
    <view class="stats-section">
      <view class="stats-card total">
        <view class="stats-title">总计收入</view>
        <view class="stats-value">¥{{ formatAmount(stats.totalEarnings) }}</view>
        <view class="stats-subtitle">完成 {{ stats.totalCompleted }} 单</view>
      </view>
      <view class="stats-card monthly">
        <view class="stats-title">本月收入</view>
        <view class="stats-value">¥{{ formatAmount(stats.monthlyEarnings) }}</view>
        <view class="stats-subtitle">完成 {{ stats.monthlyCompleted }} 单</view>
      </view>
    </view>

    <!-- 任务列表 -->
    <scroll-view 
      class="task-list"
      scroll-y
      refresher-enabled
      :refresher-triggered="isRefreshing"
      @refresherpulling="onPulling"
      @refresherrefresh="onRefresh"
      @refresherrestore="onRestore"
      @refresherabort="onAbort"
    >
      <!-- 加载中状态 -->
      <view v-if="loading" class="loading-state">
        <u-loading-icon mode="circle"></u-loading-icon>
        <text>加载中...</text>
      </view>

      <!-- 任务列表内容 -->
      <block v-else>
        <view v-if="taskList.length > 0" class="task-list-content">
          <task-card
            v-for="item in taskList"
            :key="item.task.id"
            :task="item"
            @click="navigateToDetail(item.task.id)"
          >
          </task-card>
        </view>

        <!-- 空状态 -->
        <view v-else class="empty-state">
          <view class="empty-content">
            <image 
              class="empty-image" 
              src="https://cdn-icons-png.flaticon.com/128/7486/7486754.png" 
              mode="aspectFit"
            ></image>
            <text class="empty-title">暂无已完成的任务</text>
            <text class="empty-desc">快去抢单大厅接单赚钱吧~</text>
            <button class="go-hall-btn" @click="goToTaskHall">
              <text>去抢单</text>
              <u-icon name="arrow-right" color="#FFFFFF" size="28"></u-icon>
            </button>
          </view>
        </view>
      </block>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyCompletedTasks } from '@/api/task'
import TaskCard from '@/components/task-card/index.vue'

// 页面状态
const loading = ref(false)
const isRefreshing = ref(false)
const taskList = ref([])

// 统计数据
const stats = ref({
  totalCompleted: 0,
  totalEarnings: 0,
  monthlyCompleted: 0,
  monthlyEarnings: 0
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

// 加载任务列表和统计数据
const loadTaskList = async () => {
  if (loading.value) return
  
  loading.value = true
  try {
    const res = await getMyCompletedTasks()
    if (res.data) {
      taskList.value = res.data.taskList
      // 更新统计数据
      stats.value = {
        totalCompleted: res.data.totalCompleted,
        totalEarnings: res.data.totalEarnings,
        monthlyCompleted: res.data.monthlyCompleted,
        monthlyEarnings: res.data.monthlyEarnings
      }
    }
  } catch (error) {
    console.error('加载任务列表失败:', error)
    uni.showToast({
      title: '加载失败，请重试',
      icon: 'none'
    })
  } finally {
    loading.value = false
    isRefreshing.value = false
  }
}

// 下拉刷新相关方法
const onPulling = () => {
  console.log('下拉中...')
}

const onRefresh = () => {
  isRefreshing.value = true
  loadTaskList()
}

const onRestore = () => {
  console.log('刷新还原')
}

const onAbort = () => {
  console.log('刷新中断')
}

// 跳转到详情页
const navigateToDetail = (taskId) => {
  uni.navigateTo({
    url: `/pages/task-detail/index?id=${taskId}`
  })
}

// 跳转到抢单大厅
const goToTaskHall = () => {
  uni.navigateTo({
    url: '/pages/task-hall/index'
  })
}

// 格式化金额
const formatAmount = (amount) => {
  return amount ? amount.toFixed(2) : '0.00'
}

onMounted(() => {
  loadTaskList()
})
</script>

<style lang="scss" scoped>
.runner-tasks-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-top: 16rpx;
}

.stats-section {
  padding: 16rpx;
  display: flex;
  gap: 12rpx;

  .stats-card {
    flex: 1;
    background: #ffffff;
    border-radius: 12rpx;
    padding: 16rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
    text-align: center;

    &.total {
      background: linear-gradient(135deg, #3B82F6, #60A5FA);
      .stats-title, .stats-value, .stats-subtitle {
        color: #ffffff;
      }
    }

    &.monthly {
      background: linear-gradient(135deg, #10B981, #34D399);
      .stats-title, .stats-value, .stats-subtitle {
        color: #ffffff;
      }
    }

    .stats-title {
      font-size: 28rpx;
      margin-bottom: 16rpx;
    }

    .stats-value {
      font-size: 48rpx;
      font-weight: bold;
      margin-bottom: 8rpx;
    }

    .stats-subtitle {
      font-size: 24rpx;
      opacity: 0.9;
    }
  }
}

.task-list {
  flex: 1;
  height: calc(100vh - 180rpx);
  padding: 16rpx;
  box-sizing: border-box;

  .task-list-content {
    padding: 0 16rpx;
  }
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40rpx 0;
  
  text {
    font-size: 26rpx;
    color: #999;
    margin-top: 16rpx;
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 40rpx;
  
  .empty-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .empty-image {
      width: 240rpx;
      height: 240rpx;
      margin-bottom: 40rpx;
    }
    
    .empty-title {
      font-size: 32rpx;
      color: #333;
      font-weight: 500;
      margin-bottom: 16rpx;
    }
    
    .empty-desc {
      font-size: 28rpx;
      color: #666;
      margin-bottom: 48rpx;
    }
    
    .go-hall-btn {
      background: linear-gradient(135deg, #3B82F6, #60A5FA);
      color: #ffffff;
      padding: 20rpx 80rpx;
      border-radius: 44rpx;
      font-size: 32rpx;
      border: none;
      box-shadow: 0 8rpx 16rpx rgba(59, 130, 246, 0.2);
      display: flex;
      align-items: center;
      gap: 8rpx;
      transition: all 0.3s ease;
      
      &:active {
        transform: scale(0.96);
        box-shadow: 0 4rpx 8rpx rgba(59, 130, 246, 0.15);
      }
    }
  }
}
</style> 