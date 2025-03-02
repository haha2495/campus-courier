<template>
  <view class="runner-tasks-container">
    <!-- 状态切换器 -->
    <view class="status-tabs">
      <view 
        v-for="(tab, index) in statusTabs" 
        :key="index"
        class="tab-item"
        :class="{ active: currentStatus === tab.value }"
        @click="handleStatusChange(tab.value)"
      >
        <text class="tab-text">{{ tab.label }}</text>
        <view class="tab-line" :class="{ 'tab-line-active': currentStatus === tab.value }"></view>
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
      @scrolltolower="loadMore"
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
            :key="task.id" 
            class="task-card"
            @click="navigateToDetail(task.id)"
          >
            <!-- 任务类型和状态 -->
            <view class="task-header">
              <view class="type-info">
                <image class="type-icon" :src="task.taskType.icon" mode="aspectFit"></image>
                <text class="type-name">{{ task.taskType.typeName }}</text>
              </view>
              <view class="status-tag" :class="'status-' + task.status">
                {{ getStatusText(task.status) }}
              </view>
            </view>

            <!-- 任务标题 -->
            <view class="task-title">{{ task.title }}</view>

            <!-- 任务信息 -->
            <view class="task-info">
              <view class="info-item">
                <text class="label">配送地址</text>
                <text class="value">{{ task.deliveryAddress }}</text>
              </view>
              <view class="info-item">
                <text class="label">赏金</text>
                <text class="value price">¥{{ task.reward }}</text>
              </view>
              <view class="info-item">
                <text class="label">期望送达</text>
                <text class="value">{{ formatTime(task.expectedFinishTime) }}</text>
              </view>
            </view>

            <!-- 任务操作按钮 -->
            <view class="task-actions">
              <button 
                v-if="showActionButton(task.status)"
                class="action-btn"
                :class="getActionButtonClass(task.status)"
                @click.stop="handleTaskAction(task)"
              >
                {{ getActionButtonText(task.status) }}
              </button>
            </view>
          </view>
        </view>

        <!-- 空状态 -->
        <view v-else class="empty-state">
          <u-empty
            mode="list"
            text="暂无相关任务"
            margin-top="40"
          ></u-empty>
        </view>

        <!-- 加载更多 -->
        <view v-if="hasMore && taskList.length > 0" class="load-more">
          <u-loading-icon></u-loading-icon>
          <text>加载更多...</text>
        </view>

        <!-- 无更多数据 -->
        <view v-if="!hasMore && taskList.length > 0" class="no-more">
          <text>没有更多数据了</text>
        </view>
      </block>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
// import { getRunnerTasks } from '@/api/task'  // 暂时注释掉接口导入

// 系统状态栏高度
const statusBarHeight = ref(20)

// 页面状态
const loading = ref(false)
const isRefreshing = ref(false)
const hasMore = ref(true)
const currentPage = ref(1)
const pageSize = ref(10)

// 当前选中的状态
const currentStatus = ref(0)

// 任务列表
const taskList = ref([])

// 状态标签配置
const statusTabs = [
  { label: '全部', value: 0 },
  { label: '进行中', value: 1 },
  { label: '已完成', value: 2 },
  { label: '已取消', value: 3 }
]

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

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待接单',
    1: '进行中',
    2: '已完成',
    3: '已取消'
  }
  return statusMap[status] || '未知状态'
}

// 处理状态切换
const handleStatusChange = (status) => {
  if (currentStatus.value === status) return
  currentStatus.value = status
  resetList()
  loadTaskList()
}

// 重置列表
const resetList = () => {
  taskList.value = []
  currentPage.value = 1
  hasMore.value = true
}

// 加载任务列表
const loadTaskList = async () => {
  if (loading.value || !hasMore.value) return
  
  loading.value = true
  try {
    // 暂时使用模拟数据
    const mockData = {
      data: {
        records: [
          {
            id: 1,
            title: '帮取快递',
            status: 1,
            taskType: {
              icon: 'https://cdn-icons-png.flaticon.com/128/2203/2203124.png',
              typeName: '快递代取'
            },
            deliveryAddress: '第一教学楼',
            reward: 10,
            expectedFinishTime: '2024-03-20 14:30:00'
          },
          {
            id: 2,
            title: '帮买奶茶',
            status: 2,
            taskType: {
              icon: 'https://cdn-icons-png.flaticon.com/128/2203/2203124.png',
              typeName: '食品代购'
            },
            deliveryAddress: '图书馆',
            reward: 8,
            expectedFinishTime: '2024-03-20 15:00:00'
          }
        ]
      }
    }

    const res = mockData // 使用模拟数据
    
    if (res.data) {
      if (currentPage.value === 1) {
        taskList.value = res.data.records
      } else {
        taskList.value = [...taskList.value, ...res.data.records]
      }
      
      hasMore.value = res.data.records.length === pageSize.value
      currentPage.value++
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
  resetList()
  loadTaskList()
}

const onRestore = () => {
  console.log('刷新还原')
}

const onAbort = () => {
  console.log('刷新中断')
}

// 加载更多
const loadMore = () => {
  if (hasMore.value && !loading.value) {
    loadTaskList()
  }
}

// 跳转到详情页
const navigateToDetail = (taskId) => {
  uni.navigateTo({
    url: `/pages/task-detail/index?id=${taskId}`
  })
}

// 显示操作按钮条件
const showActionButton = (status) => {
  return [1, 2].includes(status)
}

// 获取操作按钮文本
const getActionButtonText = (status) => {
  const textMap = {
    1: '确认送达',
    2: '查看评价'
  }
  return textMap[status] || ''
}

// 获取操作按钮样式类
const getActionButtonClass = (status) => {
  return {
    'btn-confirm': status === 1,
    'btn-review': status === 2
  }
}

// 处理任务操作
const handleTaskAction = (task) => {
  switch (task.status) {
    case 1:
      // 确认送达
      uni.navigateTo({
        url: `/pages/task-confirm/index?id=${task.id}`
      })
      break
    case 2:
      // 查看评价
      uni.navigateTo({
        url: `/pages/task-detail/index?id=${task.id}&tab=3`
      })
      break
  }
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

.status-tabs {
  display: flex;
  background: #ffffff;
  padding: 0 16px;
  position: relative;
  height: 44px;
  
  .tab-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    position: relative;
    
    .tab-text {
      font-size: 14px;
      color: #666;
      transition: all 0.3s;
    }
    
    .tab-line {
      position: absolute;
      bottom: 0;
      width: 20px;
      height: 3px;
      border-radius: 2px;
      background: transparent;
      transition: all 0.3s;
    }
    
    &.active {
      .tab-text {
        color: #3B82F6;
        font-weight: 500;
      }
      
      .tab-line {
        background: #3B82F6;
      }
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
  
  .task-actions {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
    
    .action-btn {
      padding: 6px 16px;
      border-radius: 4px;
      font-size: 13px;
      
      &.btn-confirm {
        background: #3B82F6;
        color: #ffffff;
      }
      
      &.btn-review {
        background: #10B981;
        color: #ffffff;
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

.load-more, .no-more {
  text-align: center;
  padding: 16px 0;
  color: #999;
  font-size: 13px;
}
</style> 