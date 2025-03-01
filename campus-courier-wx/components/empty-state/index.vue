<template>
  <view class="empty-state">
    <!-- CSS绘制的图标 -->
    <view class="empty-icon">
      <view class="balance-scale">
        <view class="scale-beam"></view>
        <view class="scale-pan left"></view>
        <view class="scale-pan right"></view>
        <view class="scale-stand"></view>
      </view>
    </view>
    
    <view class="empty-text">
      <text class="empty-title">{{ title }}</text>
      <text class="empty-desc">{{ description }}</text>
    </view>
    
    <view class="empty-tips" v-if="tips && tips.length">
      <view 
        class="tip-item"
        v-for="(tip, index) in tips"
        :key="index"
      >
        <uni-icons :type="tip.icon" size="16" color="#409EFF"></uni-icons>
        <text class="tip-text">{{ tip.text }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
defineProps({
  // 空状态标题
  title: {
    type: String,
    default: '暂无数据'
  },
  // 空状态描述
  description: {
    type: String,
    default: ''
  },
  // 提示信息数组
  tips: {
    type: Array,
    default: () => []
  }
})
</script>

<style lang="scss" scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80rpx 40rpx;
  background: #fff;
  border-radius: 24rpx;
  margin-top: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

  .empty-icon {
    width: 360rpx;
    height: 360rpx;
    margin-bottom: 40rpx;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  // CSS绘制天平图标
  .balance-scale {
    position: relative;
    width: 240rpx;
    height: 240rpx;

    .scale-beam {
      position: absolute;
      top: 80rpx;
      left: 20rpx;
      width: 200rpx;
      height: 12rpx;
      background: #409EFF;
      border-radius: 6rpx;
      transform-origin: center;
      animation: balance 3s ease-in-out infinite;
    }

    .scale-pan {
      position: absolute;
      width: 80rpx;
      height: 12rpx;
      background: #409EFF;
      border-radius: 6rpx;
      
      &::before {
        content: '';
        position: absolute;
        top: 12rpx;
        left: 50%;
        transform: translateX(-50%);
        width: 60rpx;
        height: 60rpx;
        border: 6rpx solid #409EFF;
        border-radius: 50%;
      }

      &.left {
        top: 120rpx;
        left: 0;
      }

      &.right {
        top: 120rpx;
        right: 0;
      }
    }

    .scale-stand {
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 24rpx;
      height: 120rpx;
      background: #409EFF;
      border-radius: 12rpx;

      &::before {
        content: '';
        position: absolute;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 120rpx;
        height: 12rpx;
        background: #409EFF;
        border-radius: 6rpx;
      }
    }
  }

  .empty-text {
    text-align: center;
    margin-bottom: 40rpx;

    .empty-title {
      font-size: 36rpx;
      font-weight: 600;
      color: #333;
      display: block;
      margin-bottom: 16rpx;
    }

    .empty-desc {
      font-size: 28rpx;
      color: #666;
      line-height: 1.6;
      display: block;
    }
  }

  .empty-tips {
    width: 100%;
    padding: 30rpx;
    background: #f0f9ff;
    border-radius: 16rpx;

    .tip-item {
      display: flex;
      align-items: center;
      margin-bottom: 20rpx;
      
      &:last-child {
        margin-bottom: 0;
      }

      .tip-text {
        font-size: 26rpx;
        color: #409EFF;
        margin-left: 12rpx;
      }
    }
  }
}

@keyframes balance {
  0%, 100% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(-5deg);
  }
  75% {
    transform: rotate(5deg);
  }
}
</style> 