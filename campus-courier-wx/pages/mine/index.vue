<template>
  <view class="mine-container">
    <!-- 状态栏占位 -->
    <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>
    
    <!-- 个人信息卡片 -->
    <view class="user-card">
      <view class="user-info">
        <view v-if="userInfo?.avatar" class="avatar">
          <image :src="userInfo.avatar" mode="aspectFill"></image>
        </view>
        <view v-else class="avatar text-avatar">
          <text>{{ getFirstChar(userInfo?.name) }}</text>
        </view>
        <view class="info-content">
          <text class="nickname">{{ userInfo?.name }}{{ userInfo?.studentNo ? '' : '--' }}</text>
          <text class="subtitle">{{ userInfo?.studentNo || '--' }}</text>
        </view>
      </view>
      
      <!-- 余额展示 -->
      <view class="balance-section">
        <text class="balance-label">我的余额</text>
        <text class="balance-value">￥{{ userInfo?.balance || '0.00' }}</text>
      </view>
    </view>

    <!-- 功能列表 -->
    <view class="menu-list">
      <!-- 我收到的评价 -->
      <view class="menu-item" @click="handleMenuClick('evaluation')">
        <view class="menu-content">
          <view class="menu-left">
            <u-icon name="star-fill" color="#ff9500" size="40"></u-icon>
            <text class="menu-text">我收到的评价</text>
          </view>
          <u-icon name="arrow-right" color="#999" size="30"></u-icon>
        </view>
      </view>

      <!-- 退出登录 -->
      <view class="menu-item logout" @click="handleLogout">
        <view class="menu-content">
          <text class="menu-text">退出登录</text>
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
import { userApi } from '@/api/user'

// 状态栏高度
const statusBarHeight = ref(20)

// 用户信息
const userInfo = ref(null)

// 获取系统信息
const getSystemInfo = () => {
  try {
    const info = uni.getSystemInfoSync()
    statusBarHeight.value = info.statusBarHeight || 20
  } catch (e) {
    console.error('获取系统信息失败', e)
  }
}

// 获取用户名第一个字
const getFirstChar = (name) => {
  if (!name) return ''
  return name.charAt(0)
}

// 获取用户信息
const getUserInfo = async () => {
  try {
    const res = await userApi.getCurrentUser()
    if (res.code === 200) {
      userInfo.value = res.data
    } else {
      console.error('获取用户信息失败:', res.message)
      uni.showToast({
        title: '获取用户信息失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    uni.showToast({
      title: '获取用户信息失败',
      icon: 'none'
    })
  }
}

// 处理菜单点击
const handleMenuClick = (type) => {
  if (type === 'evaluation') {
    uni.showToast({ title: '评价功能开发中', icon: 'none' })
  }
}

// 退出登录
const handleLogout = () => {
  uni.showModal({
    title: '提示',
    content: '确定要退出登录吗？',
    success: (res) => {
      if (res.confirm) {
        // 清除登录状态
        uni.clearStorageSync()
        // 跳转到登录页
        uni.reLaunch({
          url: '/pages/login/index'
        })
      }
    }
  })
}

onMounted(() => {
  getSystemInfo()
  getUserInfo()
})

// 页面显示时刷新用户信息
defineExpose({
  onShow() {
    getUserInfo()
  }
})
</script>

<style lang="scss" scoped>
.mine-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  padding-bottom: env(safe-area-inset-bottom);
}

.status-bar {
  width: 100%;
  background: linear-gradient(135deg, #ff9500, #ff7d00);
}

.user-card {
  background: linear-gradient(135deg, #ff9500, #ff7d00);
  padding: 30rpx;
  color: #ffffff;
  
  .user-info {
    display: flex;
    align-items: center;
    margin-bottom: 40rpx;
    
    .avatar {
      width: 120rpx;
      height: 120rpx;
      border-radius: 60rpx;
      border: 4rpx solid rgba(255, 255, 255, 0.3);
      box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
      overflow: hidden;
      
      image {
        width: 100%;
        height: 100%;
      }
      
      &.text-avatar {
        background: rgba(255, 255, 255, 0.2);
        display: flex;
        align-items: center;
        justify-content: center;
        
        text {
          font-size: 48rpx;
          font-weight: 600;
          color: #ffffff;
        }
      }
    }
    
    .info-content {
      margin-left: 24rpx;
      flex: 1;
      
      .nickname {
        font-size: 36rpx;
        font-weight: 600;
        margin-bottom: 12rpx;
        text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
        display: block;
      }
      
      .subtitle {
        font-size: 26rpx;
        opacity: 0.8;
        display: block;
      }
    }
  }
  
  .balance-section {
    background: rgba(255, 255, 255, 0.15);
    border-radius: 16rpx;
    padding: 24rpx;
    backdrop-filter: blur(10px);
    
    .balance-label {
      font-size: 26rpx;
      opacity: 0.9;
      margin-bottom: 8rpx;
      display: block;
    }
    
    .balance-value {
      font-size: 48rpx;
      font-weight: 600;
      text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
    }
  }
}

.menu-list {
  margin-top: 20rpx;
  padding: 0 20rpx;
  
  .menu-item {
    background: #ffffff;
    border-radius: 16rpx;
    margin-bottom: 20rpx;
    
    .menu-content {
      padding: 30rpx 24rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      
      .menu-left {
        display: flex;
        align-items: center;
        gap: 16rpx;
      }
      
      .menu-text {
        font-size: 30rpx;
        color: #333;
      }
    }
    
    &.logout {
      margin-top: 30rpx;
      
      .menu-content {
        justify-content: center;
        
        .menu-text {
          color: #ff4d4f;
          font-weight: 500;
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