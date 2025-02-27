<template>
  <view class="current-tasks-container">
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
        <text class="empty-text">还没有接单哦！</text>
        <text class="empty-subtext">快去抢单大厅看看吧～</text>
        <view class="empty-action" @click="navigateToTaskHall">
          <text class="action-text">去抢单</text>
          <text class="action-arrow">→</text>
        </view>
      </view>

      <!-- 加载中状态 -->
      <view v-if="isLoading && taskList.length === 0" class="loading-container">
        <u-loading-icon mode="circle" size="36"></u-loading-icon>
        <text class="loading-text">加载中...</text>
      </view>

      <!-- 任务列表 -->
      <view v-else class="task-list">
        <task-card
            v-for="task in taskList"
            :key="task.task.id"
            :task="task"
            @click="navigateToTaskDetail(task.task.id)"
        >
          <template #actions>
            <!-- 根据任务状态显示不同的操作按钮 -->
            <view class="action-buttons">
              <!-- 待确认状态 -->
              <template v-if="task.task.status === TASK_STATUS.AWAITING_CONFIRMATION">
                <view class="waiting-confirm-tip">
                  <u-icon name="clock-fill" color="#F59E0B" size="28"></u-icon>
                  <text class="tip-text">等待发布者确认收货...</text>
                </view>
              </template>
              
              <!-- 已接单状态 -->
              <template v-else-if="task.task.status === TASK_STATUS.ACCEPTED">
                <view class="action-btn primary-btn" @click.stop="startTask(task.task.id)">
                  开始执行
                </view>
              </template>
              
              <!-- 进行中状态 -->
              <template v-else-if="task.task.status === TASK_STATUS.IN_PROGRESS">
                <view class="action-btn success-btn" @click.stop="completeTask(task.task.id)">
                  送达
                </view>
              </template>
            </view>
          </template>
        </task-card>
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
import TaskCard from '@/components/task-card/index.vue'
import {TASK_STATUS} from '@/constants/taskStatus'
import {getMyAcceptedTasks, startTask as startTaskApi, deliverTask as deliverTaskApi} from '@/api/task'

// 列表数据
const taskList = ref([])
const isRefreshing = ref(false)
const isLoading = ref(false)
const isLoadingMore = ref(false)
const hasMoreData = ref(true)
const currentPage = ref(1)
const pageSize = 10

// 获取任务列表
const fetchTaskList = async () => {
  try {
    isLoading.value = !isRefreshing.value && currentPage.value === 1
    isLoadingMore.value = currentPage.value > 1

    // 调用获取当前任务列表API
    const res = await getMyAcceptedTasks()

    // 处理响应数据
    if (res.data) {
      taskList.value = res.data
      hasMoreData.value = false // 因为不分页，所以没有更多数据
    } else {
      taskList.value = []
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
  setTimeout(() => {
    isRefreshing.value = false
  }, 300)
}

// 加载更多任务
const loadMoreTasks = async () => {
  if (!hasMoreData.value || isLoadingMore.value) return
  currentPage.value++
  await fetchTaskList()
}

// 开始执行任务
const startTask = async (taskId) => {
  uni.showModal({
    title: '确认开始',
    content: '确定要开始执行这个任务吗？',
    async success(res) {
      if (res.confirm) {
        uni.showLoading({title: '处理中...'})
        try {
          await startTaskApi(taskId)
          await uni.showToast({
            title: '已开始执行',
            icon: 'success'
          })
          await refreshTasks()
        } catch (error) {
          console.error('开始任务失败：', error)
          uni.showToast({
            title: error.message || '操作失败',
            icon: 'none'
          })
        } finally {
          uni.hideLoading()
        }
      }
    }
  })
}

// 完成配送任务
const completeTask = async (taskId) => {
  uni.showModal({
    title: '确认送达',
    content: '确定已将物品送达目的地了吗？',
    async success(res) {
      if (res.confirm) {
        uni.showLoading({title: '处理中...'})
        try {
          await deliverTaskApi(taskId)
          await uni.showToast({
            title: '已完成配送',
            icon: 'success'
          })
          await refreshTasks()
        } catch (error) {
          console.error('完成任务失败：', error)
          uni.showToast({
            title: error.message || '操作失败',
            icon: 'none'
          })
        } finally {
          uni.hideLoading()
        }
      }
    }
  })
}

// 跳转到任务详情
const navigateToTaskDetail = (taskId) => {
  uni.navigateTo({
    url: `/pages/task-detail/index?id=${taskId}`,
    animationType: 'slide-in-right'
  })
}

// 跳转到抢单大厅
const navigateToTaskHall = () => {
  uni.navigateTo({
    url: '/pages/task-hall/index'
  })
}

// 页面加载时获取任务列表
onMounted(() => {
  refreshTasks()
})
</script>

<style lang="scss" scoped>
.current-tasks-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f7fa;
}

.task-list-scroll {
  flex: 1;
  height: 0;
  padding: 20rpx 30rpx;
  box-sizing: border-box;
  -webkit-overflow-scrolling: touch;
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
    background: linear-gradient(to right, #07c160, #10d273);
    padding: 18rpx 40rpx;
    border-radius: 40rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 6rpx 16rpx rgba(7, 193, 96, 0.3);

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

.task-list {
  padding-bottom: env(safe-area-inset-bottom);
  min-height: 101%;
}

.load-more {
  padding: 20rpx 0;
}

.action-buttons {
  display: flex;
  gap: 16rpx;
}

.action-btn {
  font-size: 24rpx;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  transition: all 0.2s;

  &:active {
    transform: scale(0.95);
  }

  &.primary-btn {
    color: #1989fa;
    background-color: #ecf5ff;

    &:active {
      background-color: #e5f1ff;
    }
  }

  &.success-btn {
    color: #07c160;
    background-color: #e8f8f1;

    &:active {
      background-color: #d7f6e6;
    }
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