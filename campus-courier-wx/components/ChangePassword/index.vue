<template>
  <u-popup
    :show="show"
    @close="handleClose"
    mode="bottom"
    :round="10"
    :closeable="true"
    :safe-area-inset-bottom="true"
  >
    <view class="change-password">
      <!-- 顶部标题区域 -->
      <view class="header">
        <u-icon name="lock" color="#ff9500" size="60"></u-icon>
        <view class="title">修改密码</view>
        <view class="subtitle">请设置一个安全的新密码</view>
      </view>

      <view class="form">
        <!-- 旧密码 -->
        <view class="input-group">
          <view class="input-label">
            <u-icon name="lock-fill" size="36" color="#ff9500"></u-icon>
            <text>旧密码</text>
          </view>
          <u-input
            v-model="form.oldPassword"
            :type="showOldPassword ? 'text' : 'password'"
            placeholder="请输入旧密码"
            :custom-style="{
              background: '#f8f9fc',
              padding: '24rpx 32rpx',
              borderRadius: '16rpx'
            }"
          >
            <template #suffix>
              <view class="password-eye" @click="showOldPassword = !showOldPassword">
                <u-icon :name="showOldPassword ? 'eye-fill' : 'eye-off'" size="38" color="#909399"></u-icon>
              </view>
            </template>
          </u-input>
        </view>
        
        <!-- 新密码 -->
        <view class="input-group">
          <view class="input-label">
            <u-icon name="lock-open" size="36" color="#ff9500"></u-icon>
            <text>新密码</text>
          </view>
          <u-input
            v-model="form.newPassword"
            :type="showNewPassword ? 'text' : 'password'"
            placeholder="请输入新密码"
            :custom-style="{
              background: '#f8f9fc',
              padding: '24rpx 32rpx',
              borderRadius: '16rpx'
            }"
            @input="checkPasswordStrength"
          >
            <template #suffix>
              <view class="password-eye" @click="showNewPassword = !showNewPassword">
                <u-icon :name="showNewPassword ? 'eye-fill' : 'eye-off'" size="38" color="#909399"></u-icon>
              </view>
            </template>
          </u-input>
          <!-- 密码强度提示 -->
          <view class="password-strength" v-if="form.newPassword">
            <text>密码强度：</text>
            <view class="strength-bars">
              <view 
                class="bar" 
                v-for="i in 3" 
                :key="i"
                :class="[
                  passwordStrength >= i ? 'active' : '',
                  passwordStrength === 1 ? 'weak' : '',
                  passwordStrength === 2 ? 'medium' : '',
                  passwordStrength === 3 ? 'strong' : ''
                ]"
              ></view>
            </view>
            <text :class="[
              'strength-text',
              passwordStrength === 1 ? 'weak' : '',
              passwordStrength === 2 ? 'medium' : '',
              passwordStrength === 3 ? 'strong' : ''
            ]">{{ strengthText }}</text>
          </view>
        </view>
        
        <!-- 确认密码 -->
        <view class="input-group">
          <view class="input-label">
            <u-icon name="checkmark-circle" size="36" color="#ff9500"></u-icon>
            <text>确认密码</text>
          </view>
          <u-input
            v-model="form.confirmPassword"
            :type="showConfirmPassword ? 'text' : 'password'"
            placeholder="请再次输入新密码"
            :custom-style="{
              background: '#f8f9fc',
              padding: '24rpx 32rpx',
              borderRadius: '16rpx'
            }"
          >
            <template #suffix>
              <view class="password-eye" @click="showConfirmPassword = !showConfirmPassword">
                <u-icon :name="showConfirmPassword ? 'eye-fill' : 'eye-off'" size="38" color="#909399"></u-icon>
              </view>
            </template>
          </u-input>
        </view>

        <!-- 温馨提示 -->
        <view class="tips">
          <u-icon name="info-circle" size="28" color="#909399"></u-icon>
          <text>温馨提示：密码至少包含6位字符，建议使用字母、数字和符号的组合</text>
        </view>
        
        <!-- 提交按钮 -->
        <view class="submit-btn">
          <u-button
            type="primary"
            text="确认修改"
            :loading="loading"
            @click="handleSubmit"
            :custom-style="{
              width: '100%',
              height: '88rpx',
              borderRadius: '44rpx',
              background: 'linear-gradient(135deg, #ff9500, #ff7d00)'
            }"
          />
        </view>
      </view>
    </view>
  </u-popup>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { userApi } from '@/api/user'

// 定义props
const props = defineProps({
  show: {
    type: Boolean,
    default: false
  }
})

// 定义emit
const emit = defineEmits(['update:show', 'success'])

// 表单数据
const form = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 密码显示状态
const showOldPassword = ref(false)
const showNewPassword = ref(false)
const showConfirmPassword = ref(false)

// 加载状态
const loading = ref(false)

// 密码强度
const passwordStrength = ref(0)

// 密码强度文字
const strengthText = computed(() => {
  switch (passwordStrength.value) {
    case 1:
      return '弱'
    case 2:
      return '中'
    case 3:
      return '强'
    default:
      return ''
  }
})

// 检查密码强度
const checkPasswordStrength = (value) => {
  if (!value) {
    passwordStrength.value = 0
    return
  }
  
  let strength = 0
  // 检查长度
  if (value.length >= 6) strength++
  // 检查是否包含数字和字母
  if (/[a-zA-Z]/.test(value) && /[0-9]/.test(value)) strength++
  // 检查是否包含特殊字符
  if (/[!@#$%^&*]/.test(value)) strength++
  
  passwordStrength.value = strength
}

// 关闭弹窗
const handleClose = () => {
  emit('update:show', false)
  // 重置表单
  form.oldPassword = ''
  form.newPassword = ''
  form.confirmPassword = ''
  passwordStrength.value = 0
  // 重置密码显示状态
  showOldPassword.value = false
  showNewPassword.value = false
  showConfirmPassword.value = false
}

// 表单验证
const validateForm = () => {
  if (!form.oldPassword) {
    uni.showToast({
      title: '请输入旧密码',
      icon: 'none'
    })
    return false
  }
  if (!form.newPassword) {
    uni.showToast({
      title: '请输入新密码',
      icon: 'none'
    })
    return false
  }
  if (!form.confirmPassword) {
    uni.showToast({
      title: '请确认新密码',
      icon: 'none'
    })
    return false
  }
  if (form.newPassword !== form.confirmPassword) {
    uni.showToast({
      title: '两次输入的密码不一致',
      icon: 'none'
    })
    return false
  }
  if (form.newPassword.length < 6) {
    uni.showToast({
      title: '密码长度不能少于6位',
      icon: 'none'
    })
    return false
  }
  return true
}

// 提交表单
const handleSubmit = async () => {
  if (!validateForm()) return
  
  loading.value = true
  try {
    const res = await userApi.changePassword({
      oldPassword: form.oldPassword,
      newPassword: form.newPassword,
      confirmPassword: form.confirmPassword
    })
    
    if (res.code === 200) {
      uni.showToast({
        title: '修改成功',
        icon: 'success'
      })
      emit('success')
      handleClose()
    } else {
      throw new Error(res.message)
    }
  } catch (error) {
    uni.showToast({
      title: error.message || '修改失败',
      icon: 'none'
    })
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.change-password {
  padding: 40rpx 30rpx;
  
  .header {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 50rpx;
    
    .title {
      font-size: 36rpx;
      font-weight: 600;
      color: #333;
      margin: 20rpx 0 10rpx;
    }
    
    .subtitle {
      font-size: 26rpx;
      color: #909399;
    }
  }
  
  .form {
    .input-group {
      margin-bottom: 36rpx;
      
      .input-label {
        display: flex;
        align-items: center;
        gap: 12rpx;
        margin-bottom: 16rpx;
        
        text {
          font-size: 28rpx;
          color: #333;
          font-weight: 500;
        }
      }
      
      .password-eye {
        padding: 10rpx;
        margin-right: -10rpx;
      }
    }
    
    .password-strength {
      margin-top: 16rpx;
      display: flex;
      align-items: center;
      gap: 12rpx;
      
      text {
        font-size: 24rpx;
        color: #909399;
      }
      
      .strength-bars {
        display: flex;
        gap: 8rpx;
        
        .bar {
          width: 40rpx;
          height: 8rpx;
          background: #ebeef5;
          border-radius: 4rpx;
          transition: all 0.3s;
          
          &.active {
            &.weak {
              background: #ff4d4f;
            }
            &.medium {
              background: #faad14;
            }
            &.strong {
              background: #52c41a;
            }
          }
        }
      }
      
      .strength-text {
        &.weak {
          color: #ff4d4f;
        }
        &.medium {
          color: #faad14;
        }
        &.strong {
          color: #52c41a;
        }
      }
    }
    
    .tips {
      display: flex;
      align-items: flex-start;
      gap: 8rpx;
      margin: 30rpx 0;
      padding: 20rpx;
      background: #fff9f0;
      border-radius: 12rpx;
      
      text {
        font-size: 24rpx;
        color: #909399;
        line-height: 1.5;
        flex: 1;
      }
    }
  }
  
  .submit-btn {
    margin-top: 40rpx;
  }
}
</style> 