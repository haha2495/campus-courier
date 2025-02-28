<template>
  <view class="evaluation-container">
    <!-- 评价统计 -->
    <view class="stats-card">
      <view class="stats-item">
        <text class="stats-value">4.9</text>
        <text class="stats-label">综合评分</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-value">98%</text>
        <text class="stats-label">好评率</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-value">128</text>
        <text class="stats-label">评价总数</text>
      </view>
    </view>

    <!-- 评价列表 -->
    <view class="evaluation-list">
      <!-- 暂无数据展示 -->
      <view class="empty-state">
        <u-empty mode="data" text="暂无评价记录"></u-empty>
      </view>
    </view>
  </view>
</template>

<script setup>
// 返回上一页
const navigateBack = () => {
  uni.navigateBack()
}
</script>

<style lang="scss" scoped>
.evaluation-container {
  min-height: 100vh;
  background-color: #f7f8fa;
}

.nav-bar {
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 100;
  
  .back-btn {
    width: 60rpx;
    height: 60rpx;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .nav-title {
    flex: 1;
    text-align: center;
    font-size: 32rpx;
    font-weight: 500;
    margin-right: 60rpx; // 为了保持标题居中
  }
}

.stats-card {
  margin: 30rpx;
  background-color: #ffffff;
  border-radius: 20rpx;
  padding: 40rpx 30rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);

  .stats-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .stats-value {
      font-size: 40rpx;
      font-weight: 600;
      color: #333333;
      margin-bottom: 8rpx;
    }
    
    .stats-label {
      font-size: 24rpx;
      color: #999999;
    }
  }

  .stats-divider {
    width: 2rpx;
    height: 60rpx;
    background-color: #f0f0f0;
  }
}

.evaluation-list {
  margin: 30rpx;
  background-color: #ffffff;
  border-radius: 20rpx;
  min-height: 400rpx;

  .empty-state {
    padding: 100rpx 0;
  }
}
</style> 