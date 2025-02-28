<template>
  <view class="balance-container">
    <!-- 顶部卡片 -->
    <view class="balance-card">
      <view class="balance-title">账户余额</view>
      <view class="balance-amount">
        <text class="currency">￥</text>
        <text class="amount">{{ userInfo?.balance || '0.00' }}</text>
      </view>
      <view class="balance-actions">
        <button class="action-btn recharge" @click="handleRecharge">充值</button>
        <button class="action-btn withdraw" @click="handleWithdraw">提现</button>
      </view>
    </view>

    <!-- 交易记录 -->
    <view class="transaction-list">
      <view class="list-header">
        <text>交易记录</text>
      </view>
      
      <!-- 暂无数据展示 -->
      <view v-if="!transactions.length" class="empty-state">
        <u-empty mode="data" text="暂无交易记录"></u-empty>
      </view>
      
      <!-- 交易记录列表 -->
      <view v-else class="list-content">
        <!-- 后续添加交易记录列表 -->
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userApi } from '@/api/user'

// 用户信息
const userInfo = ref(null)
// 交易记录
const transactions = ref([])

// 获取用户信息
const getUserInfo = async () => {
  try {
    const res = await userApi.getCurrentUser()
    if (res.code === 200) {
      userInfo.value = res.data
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    uni.showToast({
      title: '获取用户信息失败',
      icon: 'none'
    })
  }
}

// 处理充值按钮点击
const handleRecharge = () => {
  uni.navigateTo({
    url: '/pages/mine/balance/recharge'
  })
}

// 处理提现按钮点击
const handleWithdraw = () => {
  uni.showToast({
    title: '提现功能开发中',
    icon: 'none'
  })
}

onMounted(() => {
  getUserInfo()
})

// 页面显示时刷新数据
defineExpose({
  onShow() {
    getUserInfo()
  }
})
</script>

<style lang="scss" scoped>
.balance-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  padding: 30rpx;
}

.balance-card {
  background: linear-gradient(135deg, #ff9500, #ff7d00);
  border-radius: 20rpx;
  padding: 40rpx;
  color: #ffffff;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 16rpx rgba(255, 149, 0, 0.2);

  .balance-title {
    font-size: 28rpx;
    opacity: 0.9;
    margin-bottom: 20rpx;
  }

  .balance-amount {
    margin-bottom: 40rpx;
    
    .currency {
      font-size: 36rpx;
      margin-right: 8rpx;
    }
    
    .amount {
      font-size: 64rpx;
      font-weight: 600;
    }
  }

  .balance-actions {
    display: flex;
    gap: 20rpx;

    .action-btn {
      flex: 1;
      height: 80rpx;
      border-radius: 40rpx;
      font-size: 28rpx;
      font-weight: 500;
      display: flex;
      align-items: center;
      justify-content: center;
      
      &.recharge {
        background-color: #ffffff;
        color: #ff9500;
      }
      
      &.withdraw {
        background-color: rgba(255, 255, 255, 0.2);
        color: #ffffff;
      }
    }
  }
}

.transaction-list {
  background-color: #ffffff;
  border-radius: 20rpx;
  padding: 30rpx;

  .list-header {
    font-size: 32rpx;
    font-weight: 500;
    margin-bottom: 30rpx;
    color: #333333;
  }

  .empty-state {
    padding: 60rpx 0;
  }
}
</style> 