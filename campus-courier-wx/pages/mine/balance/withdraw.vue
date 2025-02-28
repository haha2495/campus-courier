<template>
  <view class="withdraw-container">
    <!-- 可提现金额 -->
    <view class="available-balance">
      <view class="section-title">可提现金额</view>
      <view class="balance-amount">
        <text class="currency">￥</text>
        <text class="amount">{{ userInfo?.balance || '0.00' }}</text>
      </view>
    </view>

    <!-- 提现金额输入 -->
    <view class="withdraw-amount">
      <view class="section-title">提现金额</view>
      <view class="input-wrapper">
        <text class="currency-symbol">¥</text>
        <input
          type="digit"
          v-model="amount"
          placeholder="请输入提现金额"
          @input="handleAmountInput"
          @blur="handleAmountBlur"
        />
      </view>
      <view class="quick-actions">
        <text class="quick-action" @click="handleAllWithdraw">全部提现</text>
      </view>
    </view>

    <!-- 提现方式选择 -->
    <view class="withdraw-method">
      <view class="section-title">提现方式</view>
      <view class="method-options">
        <view class="method-item active">
          <view class="method-icon">
            <u-icon name="weixin-fill" color="#07c160" size="40"></u-icon>
          </view>
          <view class="method-info">
            <text class="method-name">模拟提现</text>
            <text class="method-desc">仅作演示使用</text>
          </view>
          <view class="method-selected">
            <u-icon name="checkmark-circle" color="#ff9500" size="40"></u-icon>
          </view>
        </view>
      </view>
    </view>

    <!-- 提示信息 -->
    <view class="tips">
      <text class="tip-item">单次提现金额不能超过账户余额</text>
      <text class="tip-item">提现金额必须大于1元</text>
      <text class="tip-item">提现后将自动发放到您的账户</text>
    </view>

    <!-- 提现按钮 -->
    <view class="bottom-button">
      <button 
        class="withdraw-button" 
        :disabled="!canWithdraw"
        @click="handleWithdrawSubmit"
      >
        确认提现 ¥{{ formattedAmount }}
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { userApi } from '@/api/user'

// 用户信息
const userInfo = ref(null)

// 提现金额
const amount = ref('')

// 格式化后的金额
const formattedAmount = computed(() => {
  if (!amount.value) return '0.00'
  return Number(amount.value).toFixed(2)
})

// 是否可以提现
const canWithdraw = computed(() => {
  const withdrawAmount = Number(amount.value)
  const balance = Number(userInfo.value?.balance || 0)
  return withdrawAmount >= 1 && withdrawAmount <= balance
})

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

// 处理金额输入
const handleAmountInput = (e) => {
  const value = e.detail.value
  // 限制只能输入数字和小数点，且最多两位小数
  if (value && !/^\d*\.?\d{0,2}$/.test(value)) {
    return
  }
  amount.value = value
}

// 处理金额输入框失焦
const handleAmountBlur = () => {
  if (amount.value) {
    amount.value = Number(amount.value).toFixed(2)
  }
}

// 全部提现
const handleAllWithdraw = () => {
  if (userInfo.value?.balance) {
    amount.value = userInfo.value.balance.toString()
  }
}

// 提交提现
const handleWithdrawSubmit = () => {
  const withdrawAmount = Number(amount.value)
  const balance = Number(userInfo.value?.balance || 0)

  if (withdrawAmount < 1) {
    uni.showToast({
      title: '提现金额不能小于1元',
      icon: 'none'
    })
    return
  }

  if (withdrawAmount > balance) {
    uni.showToast({
      title: '提现金额不能超过账户余额',
      icon: 'none'
    })
    return
  }

  // 显示模拟提现确认框
  uni.showModal({
    title: '模拟提现',
    content: `确认提现 ¥${withdrawAmount} 吗？`,
    success: async (res) => {
      if (res.confirm) {
        uni.showLoading({
          title: '提现中...',
          mask: true
        })
        
        try {
          await userApi.withdraw(withdrawAmount)
          
          // 获取页面栈
          const pages = getCurrentPages()
          const prevPage = pages[pages.length - 2]
          
          // 先调用刷新方法
          if (prevPage && prevPage.$vm && typeof prevPage.$vm.refreshBalance === 'function') {
            await prevPage.$vm.refreshBalance()
          }
          
          // 显示成功提示
          uni.showToast({
            title: '提现成功',
            icon: 'success',
            duration: 2000
          })
          
          // 延迟返回上一页
          setTimeout(() => {
            uni.navigateBack({ delta: 1 })
          }, 2000)
          
        } catch (error) {
          uni.showToast({
            title: error.message || '提现失败，请重试',
            icon: 'none',
            duration: 2000
          })
        } finally {
          uni.hideLoading()
        }
      }
    }
  })
}

onMounted(() => {
  getUserInfo()
})
</script>

<style lang="scss" scoped>
.withdraw-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  padding: 30rpx;
}

.available-balance, .withdraw-amount, .withdraw-method {
  background-color: #ffffff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 30rpx;
}

.balance-amount {
  display: flex;
  align-items: baseline;
  
  .currency {
    font-size: 36rpx;
    color: #333;
    margin-right: 8rpx;
  }
  
  .amount {
    font-size: 64rpx;
    font-weight: 600;
    color: #333;
  }
}

.withdraw-amount {
  .input-wrapper {
    display: flex;
    align-items: center;
    background-color: #f7f8fa;
    border-radius: 12rpx;
    padding: 20rpx 30rpx;
    margin-bottom: 20rpx;

    .currency-symbol {
      font-size: 40rpx;
      color: #333;
      margin-right: 20rpx;
    }

    input {
      flex: 1;
      font-size: 40rpx;
      color: #333;
    }
  }

  .quick-actions {
    display: flex;
    justify-content: flex-end;

    .quick-action {
      font-size: 28rpx;
      color: #ff9500;
      padding: 10rpx 20rpx;
    }
  }
}

.withdraw-method {
  .method-options {
    .method-item {
      display: flex;
      align-items: center;
      padding: 30rpx;
      background-color: #f7f8fa;
      border-radius: 12rpx;

      &.active {
        background-color: rgba(255, 149, 0, 0.1);
      }

      .method-info {
        flex: 1;
        margin-left: 20rpx;

        .method-name {
          font-size: 32rpx;
          color: #333;
          font-weight: 500;
          display: block;
        }

        .method-desc {
          font-size: 24rpx;
          color: #999;
          margin-top: 4rpx;
          display: block;
        }
      }
    }
  }
}

.tips {
  padding: 0 30rpx;
  margin-bottom: 120rpx;

  .tip-item {
    font-size: 24rpx;
    color: #999;
    line-height: 1.8;
    display: block;
  }
}

.bottom-button {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 30rpx;
  background-color: #ffffff;
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);

  .withdraw-button {
    width: 100%;
    height: 88rpx;
    background: linear-gradient(135deg, #ff9500, #ff7d00);
    border-radius: 44rpx;
    color: #ffffff;
    font-size: 32rpx;
    font-weight: 500;
    display: flex;
    align-items: center;
    justify-content: center;

    &:disabled {
      opacity: 0.6;
    }
  }
}
</style> 