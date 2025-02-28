<!-- 余额充值页面 -->
<template>
  <view class="recharge-container">
    <view class="amount-input">
      <text class="label">充值金额</text>
      <input 
        type="digit"
        v-model="amount"
        placeholder="请输入充值金额"
        @input="handleInput"
      />
    </view>
    
    <view class="quick-amounts">
      <text class="section-title">快速充值金额</text>
      <view class="amount-grid">
        <view 
          v-for="item in quickAmounts" 
          :key="item"
          class="amount-item"
          :class="{ active: amount === item }"
          @click="selectAmount(item)"
        >
          {{ item }}元
        </view>
      </view>
    </view>

    <view class="tips">
      <text class="tip-item">单次充值金额不超过10000元</text>
      <text class="tip-item">充值金额必须大于0元</text>
    </view>

    <button 
      class="submit-btn"
      :disabled="!isValidAmount"
      @click="handleSubmit"
    >
      确认充值
    </button>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import balanceApi from '@/api/balance'
import { showToast, showLoading, hideLoading } from '@/utils/uniapi'

// 快速选择金额
const quickAmounts = [10, 20, 50, 100, 200, 500]

// 充值金额
const amount = ref('')

// 选择快速金额
const selectAmount = (value) => {
  amount.value = value
}

// 处理输入
const handleInput = (e) => {
  const value = e.detail.value
  // 限制只能输入数字和小数点
  if (!/^\d*\.?\d{0,2}$/.test(value)) {
    amount.value = value.slice(0, -1)
    return
  }
  amount.value = value
}

// 验证金额是否有效
const isValidAmount = computed(() => {
  const num = Number(amount.value)
  return num > 0 && num <= 10000
})

// 提交充值
const handleSubmit = async () => {
  if (!isValidAmount.value) {
    showToast('请输入有效的充值金额')
    return
  }

  try {
    showLoading('充值中...')
    await balanceApi.recharge(Number(amount.value))
    showToast('充值成功')
    // 延迟返回上一页
    setTimeout(() => {
      uni.navigateBack()
    }, 1500)
  } catch (error) {
    showToast(error.message || '充值失败')
  } finally {
    hideLoading()
  }
}
</script>

<style lang="scss" scoped>
.recharge-container {
  padding: 20rpx;
  
  .amount-input {
    background: #fff;
    padding: 30rpx;
    border-radius: 12rpx;
    margin-bottom: 30rpx;
    
    .label {
      font-size: 28rpx;
      color: #333;
      margin-bottom: 20rpx;
      display: block;
    }
    
    input {
      height: 88rpx;
      font-size: 32rpx;
      border-bottom: 1px solid #eee;
    }
  }
  
  .quick-amounts {
    background: #fff;
    padding: 30rpx;
    border-radius: 12rpx;
    margin-bottom: 30rpx;
    
    .section-title {
      font-size: 28rpx;
      color: #333;
      margin-bottom: 20rpx;
      display: block;
    }
    
    .amount-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 20rpx;
      
      .amount-item {
        height: 88rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 28rpx;
        color: #333;
        background: #f5f5f5;
        border-radius: 8rpx;
        
        &.active {
          background: #e1f3ff;
          color: #2979ff;
        }
      }
    }
  }
  
  .tips {
    padding: 0 30rpx;
    margin-bottom: 60rpx;
    
    .tip-item {
      font-size: 24rpx;
      color: #999;
      line-height: 1.8;
      display: block;
    }
  }
  
  .submit-btn {
    width: 90%;
    height: 88rpx;
    line-height: 88rpx;
    background: #2979ff;
    color: #fff;
    font-size: 32rpx;
    border-radius: 44rpx;
    margin: 0 auto;
    
    &:disabled {
      background: #ccc;
    }
  }
}
</style> 