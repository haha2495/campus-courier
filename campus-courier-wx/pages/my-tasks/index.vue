<template>
  <view class="my-tasks-container">
    <!-- 状态筛选器 -->
    <view class="status-filter-wrapper">
      <scroll-view class="status-filter" scroll-x show-scrollbar="false">
        <view
            v-for="(status, index) in statusOptions"
            :key="index"
            class="status-item"
            :class="{ active: currentStatus === status.value }"
            @click="filterByStatus(status.value)"
        >
          {{ status.label }}
        </view>
      </scroll-view>
    </view>

    <!-- 任务列表 -->
    <scroll-view
        scroll-y
        class="task-list-scroll"
        @scrolltolower="loadMoreTasks"
        @refresherrefresh="refreshTasks"
        refresher-enabled
        :refresher-triggered="isRefreshing"
        refresher-background="#f5f7fa"
        :show-scrollbar="false"
        enhanced
        :bounces="true"
        :scroll-anchoring="true"
    >
      <!-- 暂无数据提示 -->
      <view v-if="taskList.length === 0 && !isLoading" class="empty-container">
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

      <!-- 加载中状态 -->
      <view v-if="isLoading && taskList.length === 0" class="loading-container">
        <u-loading-icon mode="circle" size="36"></u-loading-icon>
        <text class="loading-text">加载中...</text>
      </view>

      <!-- 任务卡片列表 -->
      <view v-else-if="taskList.length > 0" class="task-list">
        <!-- 任务卡片 -->
        <view
            v-for="task in taskList"
            :key="task.task.id"
            class="task-card"
            @click="navigateToTaskDetail(task.task.id)"
        >
          <!-- 卡片头部 - 类型和状态 -->
          <view class="card-header">
            <view class="task-type">
              <image class="type-icon" :src="task.taskType.icon" mode="aspectFit"></image>
              <text>{{ task.taskType.typeName }}</text>
            </view>
            <view class="task-status" :style="getStatusStyle(task.task.status)">
              {{ getStatusText(task.task.status) }}
            </view>
          </view>

          <!-- 卡片内容主体 -->
          <view class="card-body">
            <!-- 任务标题和加急标记 -->
            <view class="title-row">
              <view class="task-title">{{ task.task.title }}</view>
              <view v-if="task.task.urgent" class="urgent-tag">加急</view>
            </view>
            
            <!-- 任务描述 -->
            <view class="task-desc">{{ task.task.description }}</view>

            <!-- 地址信息 -->
            <view class="address-info" v-if="task.task.pickupAddress || task.task.deliveryAddress">
              <view class="address-item" v-if="task.task.pickupAddress">
                <text class="address-label">取件：</text>
                <text class="address-value">{{ task.task.pickupAddress }}</text>
              </view>
              <view class="address-item" v-if="task.task.deliveryAddress">
                <text class="address-label">送达：</text>
                <text class="address-value">{{ task.task.deliveryAddress }}</text>
              </view>
            </view>

            <!-- 任务关键信息：赏金和截止时间 -->
            <view class="task-info">
              <view class="info-item">
                <text class="info-label">赏金：</text>
                <text class="info-value price">
                  ¥{{ task.task.reward }}
                  <text v-if="task.task.urgent" class="urgent-reward">+{{ task.task.urgentReward }}(加急费)</text>
                </text>
              </view>
              <view class="info-item">
                <text class="info-label">截止时间：</text>
                <text class="info-value">{{ task.task.expectedFinishTime }}</text>
              </view>
            </view>
          </view>

          <!-- 卡片底部 - 时间和操作按钮 -->
          <view class="card-footer">
            <view class="time-info">{{ formatTime(task.task.createTime) }}</view>
            <view class="action-btn" v-if="canCancelTask(task.task.status)" @click.stop="cancelTask(task.task.id)">
              取消任务
            </view>
          </view>
        </view>
      </view>

      <!-- 加载更多 -->
      <view v-if="taskList.length > 0" class="load-more">
        <u-loadmore v-if="isLoadingMore" status="loading"/>
        <u-loadmore v-else-if="!hasMoreData" status="nomore"/>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {cancelTask as apiCancelTask, getMyPublishedTasks} from '@/api/task'
import {TASK_STATUS} from '@/constants/taskStatus'

// 状态筛选选项
const statusOptions = TASK_STATUS.getStatusOptions()
const currentStatus = ref(null) // 使用null表示全部

// 任务列表数据
const taskList = ref([])
const isRefreshing = ref(false)
const isLoading = ref(false)
const isLoadingMore = ref(false)
const hasMoreData = ref(true)
const currentPage = ref(1)
const pageSize = 10
const total = ref(0)

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

// 判断任务是否可以取消
const canCancelTask = (status) => {
  // 只有待接单状态的任务可以取消
  return status === TASK_STATUS.PENDING
}

// 状态筛选
const filterByStatus = (status) => {
  if (currentStatus.value === status) return

  currentStatus.value = status
  refreshTasks()
}

// 获取任务列表数据
const fetchTaskList = async () => {
  try {
    isLoading.value = currentPage.value === 1 && !isRefreshing.value
    isLoadingMore.value = currentPage.value > 1

    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize
    }

    // 如果状态不为空，添加状态筛选条件
    if (currentStatus.value !== null) {
      params.status = currentStatus.value
    }

    // 调用后端API获取任务列表
    const res = await getMyPublishedTasks(params)

    console.log('任务列表接口返回：', res)

    // 处理响应数据
    if (res.data && res.data.records) {
      // 如果是刷新或第一页，直接替换数据
      if (currentPage.value === 1) {
        taskList.value = res.data.records
      } else {
        // 否则追加数据
        taskList.value = [...taskList.value, ...res.data.records]
      }

      // 更新总数
      total.value = res.data.total

      // 判断是否有更多数据
      hasMoreData.value = taskList.value.length < total.value
    } else {
      if (currentPage.value === 1) {
        taskList.value = []
      }
      hasMoreData.value = false
    }
  } catch (error) {
    console.error('获取任务列表失败：', error)
    uni.showToast({
      title: '获取任务列表失败',
      icon: 'none'
    })
  } finally {
    isLoading.value = false
    isRefreshing.value = false
    isLoadingMore.value = false
  }
}

// 刷新任务列表
const refreshTasks = async () => {
  isRefreshing.value = true
  currentPage.value = 1
  await fetchTaskList()
  
  // 修复滑动问题：确保刷新状态正确重置
  setTimeout(() => {
    // 延迟重置刷新状态，确保UI有时间更新
    isRefreshing.value = false
  }, 300)
}

// 加载更多任务
const loadMoreTasks = async () => {
  if (!hasMoreData.value || isLoadingMore.value) return

  currentPage.value++
  await fetchTaskList()
}

// 取消任务
const cancelTask = async (taskId) => {
  try {
    uni.showModal({
      title: '确认取消',
      content: '确定要取消这个任务吗？取消后无法恢复。',
      success: async (res) => {
        if (res.confirm) {
          uni.showLoading({title: '取消中...'})

          try {
            // 调用取消任务API
            await apiCancelTask(taskId)

            uni.showToast({
              title: '任务已取消',
              icon: 'success'
            })
            // 刷新任务列表
            refreshTasks()
          } catch (error) {
            console.error('取消任务失败：', error)
            uni.showToast({
              title: '取消任务失败',
              icon: 'none'
            })
          } finally {
            uni.hideLoading()
          }
        }
      }
    })
  } catch (error) {
    console.error('取消任务失败：', error)
    uni.showToast({
      title: '取消任务失败',
      icon: 'none'
    })
  }
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
  /* 移除顶部内边距 */
}

.status-filter-wrapper {
  background-color: #ffffff;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  padding: 10rpx 0;
  /* 添加安全区域，确保适配刘海屏 */
  padding-top: constant(safe-area-inset-top); /* iOS 11.0及以下 */
  padding-top: env(safe-area-inset-top); /* iOS 11.2+ */
}

.status-filter {
  display: flex;
  white-space: nowrap;
  padding: 10rpx 30rpx;
  height: 70rpx;

  &::-webkit-scrollbar {
    display: none;
  }

  .status-item {
    padding: 6rpx 30rpx;
    margin-right: 20rpx;
    font-size: 26rpx;
    color: #666;
    background-color: #f5f7fa;
    border-radius: 30rpx;
    transition: all 0.3s;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    height: 56rpx;
    flex-shrink: 0;

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
  /* 修改左右padding为相同值，确保两侧间距一致 */
  padding: 30rpx 30rpx;
  /* 解决滑动问题 */
  height: 100%;
  box-sizing: border-box;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;

  .loading-text {
    font-size: 28rpx;
    color: #999;
    margin-top: 20rpx;
  }
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

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 30rpx 0;

    .task-type {
      display: flex;
      align-items: center;

      .type-icon {
        width: 32rpx;
        height: 32rpx;
        margin-right: 8rpx;
      }

      text {
        font-size: 24rpx;
        color: #333;
      }
    }

    .task-status {
      font-size: 24rpx;
      padding: 6rpx 16rpx;
      border-radius: 20rpx;
    }
  }

  .card-body {
    padding: 20rpx 30rpx;

    .title-row {
      display: flex;
      align-items: center;
      margin-bottom: 10rpx;
    }

    .task-title {
      font-size: 30rpx;
      font-weight: 500;
      color: #333;
      flex: 1;
      // 超出一行省略
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 1;
      overflow: hidden;
    }

    .urgent-tag {
      background-color: #ff4d4f;
      color: white;
      font-size: 22rpx;
      padding: 4rpx 12rpx;
      border-radius: 10rpx;
      margin-left: 10rpx;
      font-weight: bold;
    }

    .task-desc {
      font-size: 26rpx;
      color: #666;
      margin-bottom: 16rpx;
      // 超出两行省略
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }

    .address-info {
      margin-bottom: 16rpx;
      
      .address-item {
        display: flex;
        margin-bottom: 6rpx;
        
        .address-label {
          font-size: 24rpx;
          color: #999;
          flex-shrink: 0;
        }
        
        .address-value {
          font-size: 24rpx;
          color: #666;
          flex: 1;
          // 超出一行省略
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
    }

    .task-info {
      display: flex;
      justify-content: space-between;
      margin-top: 10rpx;

      .info-item {
        display: flex;
        align-items: center;

        .info-label {
          font-size: 24rpx;
          color: #999;
        }

        .info-value {
          font-size: 24rpx;
          color: #666;

          &.price {
            color: #ff9500;
            font-weight: 500;
            
            .urgent-reward {
              font-size: 22rpx;
              color: #ff4d4f;
              margin-left: 6rpx;
            }
          }
        }
      }
    }
  }

  .card-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20rpx 30rpx;
    border-top: 1rpx solid #f0f0f0;

    .time-info {
      font-size: 24rpx;
      color: #999;
    }

    .action-btn {
      font-size: 24rpx;
      color: #ee0a24;
      background-color: #feecef;
      padding: 8rpx 20rpx;
      border-radius: 20rpx;
    }
  }
}

.load-more {
  padding: 20rpx 0;
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

/* 修复刷新时的滑动问题 */
@keyframes refreshCompleted {
  from { transform: translateY(0); }
  to { transform: translateY(0); }
}

/* 刷新完成后应用此动画，平滑过渡 */
.refresh-completed {
  animation: refreshCompleted 0.3s ease;
}
</style> 