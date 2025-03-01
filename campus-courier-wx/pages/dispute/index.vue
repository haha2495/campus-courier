<template>
  <view class="dispute-page">
    <!-- 页面标题 -->
    <view class="header">
      <text class="title">纠纷大厅</text>
      <text class="subtitle">公平公正，共建和谐校园</text>
    </view>

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

    <!-- 后续会添加纠纷列表展示 -->
  </view>
</template>

<script setup>
import { ref } from 'vue'
import EmptyState from '@/components/empty-state/index.vue'

// 后续会添加数据加载和处理逻辑
const loading = ref(false)
const disputes = ref([])
</script>

<style lang="scss" scoped>
.dispute-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 32rpx;
}

.header {
  padding: 20rpx 0 40rpx;
  text-align: center;

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
</style> 