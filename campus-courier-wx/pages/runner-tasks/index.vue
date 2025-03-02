<template>
  <view class="runner-tasks-container">
    <!-- 顶部导航栏 -->
    <view class="nav-bar" :style="{ paddingTop: statusBarHeight + 'px' }">
      <text class="nav-title">我的接单</text>
    </view>

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
          <view 
            v-for="task in taskList" 
            :key="task.task.id" 
            class="task-card"
            @click="navigateToDetail(task.task.id)"
          >
            <!-- 任务类型和状态 -->
            <view class="task-header">
              <view class="type-info">
                <image class="type-icon" :src="task.taskType.icon" mode="aspectFit"></image>
                <text class="type-name">{{ task.taskType.typeName }}</text>
              </view>
              <view class="status-tag status-2">已完成</view>
            </view>

            <!-- 任务标题 -->
            <view class="task-title">{{ task.task.title }}</view>

            <!-- 任务信息 -->
            <view class="task-info">
              <view class="info-item">
                <text class="label">配送地址</text>
                <text class="value">{{ task.task.deliveryAddress }}</text>
              </view>
              <view class="info-item">
                <text class="label">赏金</text>
                <text class="value price">¥{{ formatAmount(task.task.reward) }}</text>
              </view>
              <view class="info-item">
                <text class="label">完成时间</text>
                <text class="value">{{ formatTime(task.task.createTime) }}</text>
              </view>
            </view>
          </view>
        </view>

        <!-- 空状态 -->
        <view v-else class="empty-state">
          <u-empty
            mode="list"
            text="暂无已完成的任务"
            margin-top="40"
          ></u-empty>
        </view>
      </block>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyCompletedTasks } from '@/api/task'

// 系统状态栏高度
const statusBarHeight = ref(20)

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

// 获取系统信息
const getSystemInfo = () => {
  try {
    const info = uni.getSystemInfoSync()
    statusBarHeight.value = info.statusBarHeight
  } catch (e) {
    console.error('获取系统信息失败:', e)
  }
}

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

// 格式化金额
const formatAmount = (amount) => {
  return amount ? amount.toFixed(2) : '0.00'
}

onMounted(() => {
  getSystemInfo()
  loadTaskList()
})
</script>

<style lang="scss" scoped>
.runner-tasks-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.nav-bar {
  background: #ffffff;
  padding: 10px 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  
  .nav-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }
}

.stats-section {
  padding: 16px;
  display: flex;
  gap: 12px;

  .stats-card {
    flex: 1;
    background: #ffffff;
    border-radius: 12px;
    padding: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
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
      font-size: 14px;
      margin-bottom: 8px;
    }

    .stats-value {
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 4px;
    }

    .stats-subtitle {
      font-size: 12px;
      opacity: 0.9;
    }
  }
}

.task-list {
  flex: 1;
  height: calc(100vh - 44px - var(--status-bar-height));
  padding: 16px;
  box-sizing: border-box;
}

.task-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  
  .task-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    
    .type-info {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .type-icon {
        width: 20px;
        height: 20px;
      }
      
      .type-name {
        font-size: 14px;
        color: #666;
      }
    }
    
    .status-tag {
      padding: 4px 12px;
      border-radius: 4px;
      font-size: 12px;
      
      &.status-0 {
        background: #E5E7EB;
        color: #6B7280;
      }
      
      &.status-1 {
        background: #EFF6FF;
        color: #3B82F6;
      }
      
      &.status-2 {
        background: #ECFDF5;
        color: #059669;
      }
      
      &.status-3 {
        background: #FEF2F2;
        color: #DC2626;
      }
    }
  }
  
  .task-title {
    font-size: 16px;
    font-weight: 500;
    color: #333;
    margin-bottom: 12px;
  }
  
  .task-info {
    .info-item {
      display: flex;
      justify-content: space-between;
      margin-bottom: 8px;
      
      .label {
        font-size: 13px;
        color: #666;
      }
      
      .value {
        font-size: 13px;
        color: #333;
        
        &.price {
          color: #f5222d;
          font-weight: 500;
        }
      }
    }
  }
}

.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px 0;
  
  text {
    font-size: 13px;
    color: #999;
    margin-top: 8px;
  }
}

.empty-state {
  text-align: center;
  padding: 16px 0;
  color: #999;
  font-size: 13px;
}
</style> 