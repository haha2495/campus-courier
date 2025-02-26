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
        <text>任务列表（待实现）</text>
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

// 搜索关键词
const searchKeyword = ref('')

// 列表数据
const taskList = ref([])
const isRefreshing = ref(false)
const isLoading = ref(false)
const isLoadingMore = ref(false)
const hasMoreData = ref(true)
const currentPage = ref(1)
const pageSize = 10

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
    isLoading.value = currentPage.value === 1 && !isRefreshing.value
    isLoadingMore.value = currentPage.value > 1

    // TODO: 调用获取任务列表API
    
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
}

.search-bar {
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  position: sticky;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  /* 添加安全区域，确保适配刘海屏 */
  padding-top: calc(20rpx + constant(safe-area-inset-top));
  padding-top: calc(20rpx + env(safe-area-inset-top));
}

.task-list-scroll {
  flex: 1;
  padding: 20rpx 30rpx;
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

.task-list {
  padding-bottom: 30rpx;
}

.load-more {
  padding: 20rpx 0;
}
</style> 