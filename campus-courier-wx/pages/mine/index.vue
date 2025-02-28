<template>
  <view class="mine-container">
    <!-- 状态栏占位 -->
    <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>
    
    <!-- 个人信息卡片 -->
    <view class="user-card">
      <view class="user-info">
        <image class="avatar" src="/static/images/default-avatar.png" mode="aspectFill"></image>
        <view class="info-content">
          <text class="nickname">未登录</text>
          <text class="subtitle">点击登录账号</text>
        </view>
      </view>
      <view class="card-footer">
        <view class="stat-item">
          <text class="stat-value">0</text>
          <text class="stat-label">我的余额</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">0</text>
          <text class="stat-label">优惠券</text>
        </view>
        <view class="stat-item">
          <text class="stat-value">0</text>
          <text class="stat-label">积分</text>
        </view>
      </view>
    </view>

    <!-- 功能列表 -->
    <view class="function-list">
      <view class="function-group">
        <view class="group-title">常用功能</view>
        <view class="group-content">
          <view class="function-item" v-for="(item, index) in commonFunctions" :key="index">
            <u-icon :name="item.icon" size="44" :color="item.color"></u-icon>
            <text class="function-name">{{ item.name }}</text>
          </view>
        </view>
      </view>

      <view class="function-group">
        <view class="group-title">其他服务</view>
        <view class="group-content">
          <view class="function-item" v-for="(item, index) in otherFunctions" :key="index">
            <u-icon :name="item.icon" size="44" :color="item.color"></u-icon>
            <text class="function-name">{{ item.name }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 版本信息 -->
    <view class="version-info">
      <text class="version-text">当前版本 1.0.0</text>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// 状态栏高度
const statusBarHeight = ref(20)

// 常用功能列表
const commonFunctions = ref([
  { name: '我的钱包', icon: 'wallet', color: '#ff9500' },
  { name: '优惠券', icon: 'coupon', color: '#ff6b6b' },
  { name: '收货地址', icon: 'map', color: '#4cd964' },
  { name: '联系客服', icon: 'service', color: '#5856d6' }
])

// 其他服务列表
const otherFunctions = ref([
  { name: '关于我们', icon: 'info-circle', color: '#007aff' },
  { name: '帮助中心', icon: 'question-circle', color: '#5856d6' },
  { name: '设置', icon: 'setting', color: '#8e8e93' }
])

// 获取系统信息
const getSystemInfo = () => {
  try {
    const info = uni.getSystemInfoSync()
    statusBarHeight.value = info.statusBarHeight || 20
  } catch (e) {
    console.error('获取系统信息失败', e)
  }
}

onMounted(() => {
  getSystemInfo()
})
</script>

<style lang="scss" scoped>
.mine-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-bottom: env(safe-area-inset-bottom);
}

.status-bar {
  width: 100%;
  background: linear-gradient(to right, #ff9500, #ff7d00);
}

.user-card {
  background: linear-gradient(to right, #ff9500, #ff7d00);
  padding: 30rpx;
  color: #ffffff;
  
  .user-info {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .avatar {
      width: 120rpx;
      height: 120rpx;
      border-radius: 60rpx;
      border: 4rpx solid rgba(255, 255, 255, 0.3);
    }
    
    .info-content {
      margin-left: 20rpx;
      
      .nickname {
        font-size: 36rpx;
        font-weight: bold;
        margin-bottom: 8rpx;
      }
      
      .subtitle {
        font-size: 24rpx;
        opacity: 0.9;
      }
    }
  }
  
  .card-footer {
    display: flex;
    justify-content: space-around;
    padding: 20rpx 0;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 16rpx;
    
    .stat-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .stat-value {
        font-size: 32rpx;
        font-weight: bold;
        margin-bottom: 4rpx;
      }
      
      .stat-label {
        font-size: 24rpx;
        opacity: 0.9;
      }
    }
  }
}

.function-list {
  padding: 20rpx;
  
  .function-group {
    background: #ffffff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    padding: 20rpx;
    
    .group-title {
      font-size: 28rpx;
      color: #333;
      font-weight: bold;
      margin-bottom: 20rpx;
      padding-left: 10rpx;
      border-left: 4rpx solid #ff9500;
    }
    
    .group-content {
      display: grid;
      grid-template-columns: repeat(4, 1fr);
      gap: 20rpx;
      
      .function-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20rpx 0;
        
        .function-name {
          font-size: 24rpx;
          color: #666;
          margin-top: 10rpx;
        }
      }
    }
  }
}

.version-info {
  text-align: center;
  padding: 40rpx 0;
  
  .version-text {
    font-size: 24rpx;
    color: #999;
  }
}
</style> 