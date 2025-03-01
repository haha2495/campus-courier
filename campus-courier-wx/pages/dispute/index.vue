<template>
  <view class="dispute-page">
    <!-- 页面标题 -->
    <view class="header">
      <text class="title">纠纷大厅</text>
      <text class="subtitle">公平公正，共建和谐校园</text>
    </view>

    <!-- 下拉刷新区域 -->
    <scroll-view
      class="scroll-view"
      scroll-y
      refresher-enabled
      :refresher-triggered="refreshing"
      @refresherrefresh="onRefresh"
      @scrolltolower="onLoadMore"
    >
      <!-- 使用空状态组件 -->
      <empty-state
        v-if="!loading && !disputes.length"
        title="暂无纠纷案件"
        description="感谢您的关注，当前暂无需要处理的纠纷\n期待您为校园和谐贡献一份力量"
        :tips="[
          {
            icon: 'info',
            text: '纠纷案件将由管理员审核后展示'
          },
          {
            icon: 'star',
            text: '您的每一次参与都将帮助建设更好的校园'
          }
        ]"
      />

      <!-- 纠纷列表 -->
      <view class="dispute-list" v-else>
        <dispute-item
          v-for="item in disputes"
          :key="item.task.id"
          :dispute="item"
          @click="handleDisputeClick(item)"
          @judge="handleJudge(item)"
        />
      </view>

      <!-- 加载状态 -->
      <view class="loading-state" v-if="loading">
        <uni-load-more status="loading" />
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import EmptyState from '@/components/empty-state/index.vue'
import DisputeItem from '@/components/dispute-item/index.vue'
import { getHallDisputes } from '@/api/dispute'

// 数据状态
const loading = ref(false)
const refreshing = ref(false)
const disputes = ref([])

// 获取纠纷列表
const fetchDisputes = async () => {
  try {
    loading.value = true
    const res = await getHallDisputes()
    disputes.value = res.data || []
  } catch (error) {
    console.error('获取纠纷列表失败:', error)
    uni.showToast({
      title: error.message || '获取数据失败',
      icon: 'none'
    })
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

// 下拉刷新
const onRefresh = () => {
  refreshing.value = true
  fetchDisputes()
}

// 点击纠纷项
const handleDisputeClick = (dispute) => {
  uni.navigateTo({
    url: `/pages/dispute/detail?taskId=${dispute.task.id}`
  })
}

// 点击前去审判
const handleJudge = (dispute) => {
  uni.navigateTo({
    url: `/pages/dispute/detail?taskId=${dispute.task.id}&mode=judge`
  })
}

// 页面加载
onMounted(() => {
  fetchDisputes()
})
</script>

<style lang="scss" scoped>
.dispute-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
}

.header {
  padding: 32rpx 32rpx 20rpx;
  text-align: center;
  background: #fff;
  position: relative;
  z-index: 1;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);

  .title {
    font-size: 40rpx;
    font-weight: 600;
    color: #333;
    display: block;
    margin-bottom: 12rpx;
  }

  .subtitle {
    font-size: 28rpx;
    color: #666;
  }
}

.scroll-view {
  flex: 1;
  height: 0;
}

.dispute-list {
  padding: 24rpx;
}

.loading-state {
  padding: 24rpx 0;
  display: flex;
  justify-content: center;
}
</style> 