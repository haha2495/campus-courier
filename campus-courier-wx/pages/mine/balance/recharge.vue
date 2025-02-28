<template>
  <view class="recharge-container">
    <!-- 充值金额选择 -->
    <view class="amount-section">
      <view class="section-title">选择充值金额</view>
      <view class="amount-grid">
        <view 
          v-for="amount in amountOptions" 
          :key="amount"
          class="amount-item"
          :class="{ active: selectedAmount === amount }"
          @click="handleAmountSelect(amount)"
        >
          <text class="amount-value">{{ amount }}</text>
          <text class="amount-unit">元</text>
        </view>
      </view>
    </view>

    <!-- 自定义金额 -->
    <view class="custom-amount">
      <view class="section-title">自定义金额</view>
      <view class="input-wrapper">
        <text class="currency-symbol">¥</text>
        <input
          type="digit"
          v-model="customAmount"
          placeholder="请输入充值金额"
          @input="handleCustomAmountInput"
          @blur="handleCustomAmountBlur"
        />
      </view>
    </view>

    <!-- 支付方式选择 -->
    <view class="payment-section">
      <view class="section-title">支付方式</view>
      <view class="payment-options">
        <view class="payment-item active">
          <view class="payment-icon">
            <u-icon name="weixin-fill" color="#07c160" size="40"></u-icon>
          </view>
          <view class="payment-info">
            <text class="payment-name">模拟支付</text>
            <text class="payment-desc">仅作演示使用</text>
          </view>
          <view class="payment-selected">
            <u-icon name="checkmark-circle" color="#ff9500" size="40"></u-icon>
          </view>
        </view>
      </view>
    </view>

    <!-- 充值按钮 -->
    <view class="bottom-button">
      <button 
        class="recharge-button" 
        :disabled="!canRecharge"
        @click="handleRechargeSubmit"
      >
        确认充值 ¥{{ finalAmount }}
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { userApi } from '@/api/user'

// 预设金额选项
const amountOptions = [10, 20, 50, 100, 200, 500]

// 选中的预设金额
const selectedAmount = ref(null)

// 自定义金额
const customAmount = ref('')

// 最终充值金额
const finalAmount = computed(() => {
  if (customAmount.value) {
    return Number(customAmount.value).toFixed(2)
  }
  return selectedAmount.value ? selectedAmount.value.toFixed(2) : '0.00'
})

// 是否可以充值
const canRecharge = computed(() => {
  const amount = Number(finalAmount.value)
  return amount > 0 && amount <= 10000
})

// 选择预设金额
const handleAmountSelect = (amount) => {
  selectedAmount.value = amount
  customAmount.value = ''
}

// 处理自定义金额输入
const handleCustomAmountInput = (e) => {
  const value = e.detail.value
  // 限制只能输入数字和小数点，且最多两位小数
  if (value && !/^\d*\.?\d{0,2}$/.test(value)) {
    return
  }
  customAmount.value = value
  selectedAmount.value = null
}

// 处理自定义金额失去焦点
const handleCustomAmountBlur = () => {
  if (customAmount.value) {
    customAmount.value = Number(customAmount.value).toFixed(2)
  }
}

// 提交充值
const handleRechargeSubmit = () => {
  const amount = Number(finalAmount.value)
  if (amount <= 0) {
    uni.showToast({
      title: '请选择或输入充值金额',
      icon: 'none'
    })
    return
  }
  if (amount > 10000) {
    uni.showToast({
      title: '单次充值不能超过10000元',
      icon: 'none'
    })
    return
  }

  // 显示模拟支付确认框
  uni.showModal({
    title: '模拟支付',
    content: `确认充值 ¥${amount} 吗？`,
    success: async (res) => {
      if (res.confirm) {
        uni.showLoading({
          title: '充值中...',
          mask: true
        })
        
        try {
          await userApi.recharge(amount)
          uni.showToast({
            title: '充值成功',
            icon: 'success'
          })
          
          // 延迟返回上一页
          setTimeout(() => {
            // 获取页面栈
            const pages = getCurrentPages()
            const prevPage = pages[pages.length - 2]
            
            // 先调用刷新方法，再返回
            if (prevPage && prevPage.$vm && typeof prevPage.$vm.refreshBalance === 'function') {
              prevPage.$vm.refreshBalance()
            }
            
            // 返回上一页
            uni.navigateBack({
              delta: 1
            })
          }, 1500)
        } catch (error) {
          uni.showToast({
            title: error.message || '充值失败，请重试',
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
</script>

<style lang="scss" scoped>
.recharge-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  padding: 30rpx;
}

.amount-section, .custom-amount, .payment-section {
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

.amount-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;

  .amount-item {
    height: 120rpx;
    background-color: #f7f8fa;
    border-radius: 12rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    transition: all 0.3s;

    &.active {
      background-color: rgba(255, 149, 0, 0.1);
      border: 2rpx solid #ff9500;

      .amount-value, .amount-unit {
        color: #ff9500;
      }
    }

    .amount-value {
      font-size: 36rpx;
      font-weight: 500;
      color: #333;
    }

    .amount-unit {
      font-size: 24rpx;
      color: #666;
      margin-top: 4rpx;
    }
  }
}

.custom-amount {
  .input-wrapper {
    display: flex;
    align-items: center;
    background-color: #f7f8fa;
    border-radius: 12rpx;
    padding: 20rpx 30rpx;

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
}

.payment-options {
  .payment-item {
    display: flex;
    align-items: center;
    padding: 30rpx;
    background-color: #f7f8fa;
    border-radius: 12rpx;

    &.active {
      background-color: rgba(255, 149, 0, 0.1);
    }

    .payment-info {
      flex: 1;
      margin-left: 20rpx;

      .payment-name {
        font-size: 32rpx;
        color: #333;
        font-weight: 500;
        display: block;
      }

      .payment-desc {
        font-size: 24rpx;
        color: #999;
        margin-top: 4rpx;
        display: block;
      }
    }
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

  .recharge-button {
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