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
        <view 
          v-for="(record, index) in transactions" 
          :key="record.id" 
          class="transaction-item"
          :class="{ 'no-border': index === transactions.length - 1 }"
        >
          <view class="left-content">
            <view class="main-info">
              <text class="transaction-type">{{ getTransactionType(record.type) }}</text>
              <text 
                class="transaction-amount"
                :class="{ 'income': isIncome(record.type) }"
              >{{ isIncome(record.type) ? '+' : '-' }}{{ record.amount }}</text>
            </view>
            <view class="sub-info">
              <text class="transaction-time">{{ formatTime(record.createTime) }}</text>
              <text class="balance-text">余额: {{ isIncome(record.type) ? 
                `${record.beforeBalance} → ${record.afterBalance}` : 
                `${record.afterBalance} → ${record.beforeBalance}` }}</text>
            </view>
          </view>
        </view>
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

// 获取交易记录
const getBalanceRecords = async () => {
  try {
    const res = await userApi.getBalanceRecords()
    if (res.code === 200) {
      transactions.value = res.data
    }
  } catch (error) {
    console.error('获取交易记录失败:', error)
    uni.showToast({
      title: '获取交易记录失败',
      icon: 'none'
    })
  }
}

// 刷新余额和记录
const refreshBalance = async () => {
  await getUserInfo()
  await getBalanceRecords()
}

// 处理充值按钮点击
const handleRecharge = () => {
  uni.navigateTo({
    url: '/pages/mine/balance/recharge'
  })
}

// 处理提现按钮点击
const handleWithdraw = () => {
  uni.navigateTo({
    url: '/pages/mine/balance/withdraw'
  })
}

// 获取交易类型文本
const getTransactionType = (type) => {
  const typeMap = {
    1: '充值',
    2: '提现',
    3: '收入',
    4: '支出'
  }
  return typeMap[type] || '未知类型'
}

// 判断是否为收入类型
const isIncome = (type) => {
  return [1, 3].includes(type)
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return time.replace('T', ' ').substring(0, 19)
}

onMounted(() => {
  refreshBalance()
})

// 返回上一页并刷新数据
const navigateBack = () => {
  // 获取页面栈
  const pages = getCurrentPages()
  const prevPage = pages[pages.length - 2]
  
  // 如果上一页存在且有onShow方法，调用它来刷新数据
  if (prevPage && prevPage.$vm && typeof prevPage.$vm.onShow === 'function') {
    prevPage.$vm.onShow()
  }
  
  // 返回上一页
  uni.navigateBack({ delta: 1 })
}

// 暴露页面方法
defineExpose({
  onShow() {
    refreshBalance()
  },
  refreshBalance
})
</script>

<style lang="scss" scoped>
.balance-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  padding: 30rpx;
}

.nav-bar {
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
  
  .back-btn {
    width: 60rpx;
    height: 60rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #ffffff;
    border-radius: 30rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
  }
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

  .list-content {
    .transaction-item {
      padding: 24rpx 0;
      border-bottom: 2rpx solid #f5f5f5;

      &.no-border {
        border-bottom: none;
      }

      .left-content {
        .main-info {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8rpx;

          .transaction-type {
            font-size: 32rpx;
            color: #333333;
            font-weight: 500;
          }

          .transaction-amount {
            font-size: 32rpx;
            font-weight: 500;
            color: #ff4d4f;

            &.income {
              color: #52c41a;
            }
          }
        }

        .sub-info {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .transaction-time {
            font-size: 24rpx;
            color: #999999;
          }

          .balance-text {
            font-size: 24rpx;
            color: #999999;
          }
        }
      }
    }
  }
}
</style> 