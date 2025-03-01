<template>
  <view class="dispute-supports">
    <!-- 后续添加具体内容 -->
    <view class="support-content">
      支持信息
    </view>
  </view>
</template>

<script setup>
defineProps({
  dispute: {
    type: Object,
    default: () => null
  },
  disputeSupports: {
    type: Array,
    default: () => []
  }
})
</script>

<style lang="scss" scoped>
.dispute-supports {
  padding: 20rpx;
  
  .support-content {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 30rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  }
}
</style> 