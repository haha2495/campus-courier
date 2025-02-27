<template>
  <view class="task-hall-container">
    <!-- 顶部搜索栏 -->
    <view class="search-bar">
      <u-search
          v-model="searchKeyword"
          placeholder="搜索任务"
          :show-action="false"
          :clearabled="true"
          @search="onSearch"
          @clear="onClear"
      ></u-search>
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
        <u-empty
            mode="list"
            icon="/static/images/empty-tasks.png"
            text="暂无可接的任务"
        ></u-empty>
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
            <view class="action-btn accept-btn" @click.stop="acceptTask(task.task.id)">
              抢单
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
import { getTaskHallList, acceptTask as acceptTaskApi } from '@/api/task'

// 搜索关键词
const searchKeyword = ref('')

// 列表数据
const taskList = ref([])
const isRefreshing = ref(false)
const isLoading = ref(false)
const isLoadingMore = ref(false)
const hasMoreData = ref(true)

// 搜索处理
const onSearch = () => {
  refreshTasks()
}

// 清除搜索
const onClear = () => {
  searchKeyword.value = ''
  refreshTasks()
}

// 获取任务列表
const fetchTaskList = async () => {
  try {
    isLoading.value = !isRefreshing.value

    // 调用获取任务列表API
    const res = await getTaskHallList()

    if (res.data) {
      taskList.value = res.data
    } else {
      taskList.value = []
    }

    hasMoreData.value = false // 因为不分页，所以没有更多数据
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
  await fetchTaskList()

  setTimeout(() => {
    isRefreshing.value = false
  }, 300)
}

// 加载更多任务 - 因为不分页，所以这个函数实际上不会做任何事
const loadMoreTasks = async () => {
  // Do nothing
}

// 跳转到任务详情
const navigateToTaskDetail = (taskId) => {
  uni.navigateTo({
    url: '/pages/task-detail/index?id=' + taskId
  })
}

// 抢单
const acceptTask = async (taskId) => {
  uni.showModal({
    title: '确认抢单',
    content: '确定要接这个任务吗？',
    async success(res) {
      if (res.confirm) {
        uni.showLoading({title: '抢单中...'})

        try {
          await acceptTaskApi(taskId)
          
          await uni.showToast({
            title: '抢单成功',
            icon: 'success',
            duration: 1500
          })

          // 等待提示显示完毕后再跳转
          setTimeout(() => {
            // 跳转到当前任务页面
            uni.navigateTo({
              url: '/pages/current-tasks/index'
            })
          }, 1500)
        } catch (error) {
          console.error('抢单失败：', error)
          uni.showToast({
            title: error.message || '抢单失败',
            icon: 'none'
          })
        } finally {
          uni.hideLoading()
        }
      }
    }
  })
}

// 页面加载时获取任务列表
onMounted(() => {
  refreshTasks()
})
</script>

<style lang="scss" scoped>
.task-hall-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f7fa;
  box-sizing: border-box;
  /* 添加安全区域，确保适配刘海屏 */
  padding-top: constant(safe-area-inset-top); /* iOS 11.0及以下 */
  padding-top: env(safe-area-inset-top); /* iOS 11.2+ */
}

.search-bar {
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  position: sticky;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  /* 移除安全区域，因为已经在容器中处理了 */
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.task-list-scroll {
  flex: 1;
  height: 0; /* 关键修复：添加这一行 */
  padding: 20rpx 30rpx;
  box-sizing: border-box;
  /* 添加弹性滚动 */
  -webkit-overflow-scrolling: touch;
}

/* 修改scroll-view的滚动条样式 */
::-webkit-scrollbar {
  width: 0;
  height: 0;
  background: transparent;
}

/* 确保内容区域可以正常滚动 */
.task-list {
  padding-bottom: env(safe-area-inset-bottom); /* 适配底部安全区域 */
  min-height: 101%; /* 确保有足够的滚动空间 */
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

.load-more {
  padding: 20rpx 0;
}

.action-btn {
  font-size: 24rpx;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  transition: all 0.2s;

  &:active {
    transform: scale(0.95);
  }

  &.accept-btn {
    color: #07c160;
    background-color: #e8f8f1;

    &:active {
      background-color: #d7f6e6;
    }
  }
}
</style> 