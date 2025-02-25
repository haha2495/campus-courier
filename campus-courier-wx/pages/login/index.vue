<template>
  <view class="login-container">
    <!-- 顶部背景 -->
    <view class="top-bg">
      <view class="delivery-animation">
        <view class="delivery-icon">🛵</view>
      </view>
      <view class="wave"></view>
    </view>

    <!-- 登录表单 -->
    <view class="login-box">
      <view class="welcome">
        <text class="title">校园跑腿系统</text>
        <text class="subtitle">快速·便捷·安全的校园配送服务</text>
      </view>

      <!-- 功能特点展示 -->
      <view class="features">
        <view class="feature-item">
          <text class="emoji">⚡</text>
          <text class="text">极速配送</text>
        </view>
        <view class="feature-item">
          <text class="emoji">💰</text>
          <text class="text">合理价格</text>
        </view>
        <view class="feature-item">
          <text class="emoji">🔒</text>
          <text class="text">安全保障</text>
        </view>
      </view>

      <!-- 添加注册状态提示 -->
      <view class="register-status" v-if="registerStatus.show">
        <view class="status-box" :class="registerStatus.class">
          <u-icon :name="registerStatus.icon" size="32" :color="registerStatus.color"></u-icon>
          <text class="status-text">{{ registerStatus.text }}</text>
        </view>
      </view>

      <view class="form-box">
        <u--form
            :model="formData"
            ref="uForm"
            labelPosition="left"
            labelWidth="0"
        >
          <view class="input-item">
            <u-form-item prop="studentNo">
              <u--input
                  v-model="formData.studentNo"
                  border="none"
                  placeholder="请输入用户名"
                  placeholderStyle="color: #999"
                  :customStyle="inputStyle"
                  prefixIcon="account"
                  prefixIconStyle="font-size: 22px;color: #ff6b6b"
              ></u--input>
            </u-form-item>
          </view>

          <view class="input-item">
            <u-form-item prop="password">
              <u--input
                  v-model="formData.password"
                  type="password"
                  border="none"
                  placeholder="请输入密码"
                  placeholderStyle="color: #999"
                  :customStyle="inputStyle"
                  prefixIcon="lock"
                  prefixIconStyle="font-size: 22px;color: #ff6b6b"
              ></u--input>
            </u-form-item>
          </view>
        </u--form>

        <!-- 登录按钮 -->
        <view class="btn-box">
          <u-button
              type="primary"
              text="立即登录"
              :customStyle="buttonStyle"
              @click="handleLogin"
              :loading="loading"
          ></u-button>
        </view>

      </view>
    </view>
  </view>
</template>

<script setup>
import {nextTick, onMounted, reactive, ref} from 'vue'
import {userApi} from '@/api/user'

// 表单数据
const formData = reactive({
  studentNo: '',
  password: ''
})

// 加载状态
const loading = ref(false)

// 注册状态
const registerStatus = reactive({
  show: false,
  text: '',
  icon: '',
  color: '',
  class: ''
})

// 输入框样式
const inputStyle = {
  height: '110rpx',
  backgroundColor: '#f8f9fc',
  borderRadius: '16rpx',
  fontSize: '28rpx',
  paddingLeft: '20rpx',
  border: '2rpx solid #e8e8e8',
  transition: 'all 0.3s ease'
}

// 按钮样式
const buttonStyle = {
  height: '100rpx',
  marginTop: '60rpx',
  borderRadius: '50rpx',
  fontSize: '32rpx',
  fontWeight: '500',
  background: 'linear-gradient(to right, #ff6b6b, #ff8787)',
  boxShadow: '0 10rpx 20rpx rgba(255, 107, 107, 0.2)',
  border: 'none'
}

// 表单校验规则
const rules = {
  studentNo: [{
    required: true,
    message: '请输入学号',
    trigger: ['blur', 'change']
  }],
  password: [{
    required: true,
    message: '请输入密码',
    trigger: ['blur', 'change']
  }, {
    min: 5,
    max: 20,
    message: '密码长度在5-20个字符之间',
    trigger: ['blur', 'change']
  }]
}

// 表单ref
const uForm = ref(null)

// 在组件挂载后设置校验规则
onMounted(() => {
  // 确保表单实例存在
  nextTick(() => {
    if (uForm.value) {
      uForm.value.setRules(rules)
    }
  })
})

// 登录方法
const handleLogin = () => {
  // 先检查表单实例是否存在
  if (!uForm.value) {
    console.error('表单实例不存在')
    uni.$u.toast('系统错误，请稍后重试')
    return
  }

  // 进行表单验证
  uForm.value.validate().then(async valid => {
    if (valid) {
      loading.value = true
      try {
        const res = await userApi.login({
          studentNo: formData.studentNo,
          password: formData.password
        })

        if (res.code === 200) {
          // 保存token
          uni.setStorageSync('token', res.data.token)

          uni.$u.toast('登录成功')
          // 跳转到首页
          uni.reLaunch({
            url: '/pages/index/index'
          })
        } else {
          // 显示具体的错误信息
          registerStatus.show = true
          registerStatus.text = res.message
          registerStatus.icon = 'error-circle'
          registerStatus.color = '#ff4d4f'
          registerStatus.class = 'status-error'

          // 如果是审核相关的错误，设置不同的样式
          if (res.message.includes('审核中')) {
            registerStatus.icon = 'clock-fill'
            registerStatus.color = '#faad14'
            registerStatus.class = 'status-pending'
          } else if (res.message.includes('审核未通过')) {
            registerStatus.icon = 'close-circle-fill'
            registerStatus.color = '#ff4d4f'
            registerStatus.class = 'status-rejected'
          } else if (res.message.includes('禁用')) {
            registerStatus.icon = 'lock-fill'
            registerStatus.color = '#ff4d4f'
            registerStatus.class = 'status-disabled'
          }
        }
      } catch (err) {
        console.error('登录失败:', err)
        registerStatus.show = true
        registerStatus.text = err.message || '登录失败，请稍后重试'
        registerStatus.icon = 'error-circle'
        registerStatus.color = '#ff4d4f'
        registerStatus.class = 'status-error'
      } finally {
        loading.value = false
      }
    } else {
      uni.$u.toast('请填写完整的登录信息')
    }
  }).catch(errors => {
    console.error('表单验证失败:', errors)
    uni.$u.toast('请填写完整的登录信息')
  })
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  background-color: #ffffff;
  position: relative;
  overflow: hidden;

  .top-bg {
    height: 460rpx;
    width: 100%;
    position: relative;
    background: linear-gradient(120deg, #ff6b6b, #ff8787);
    overflow: hidden;

    .delivery-animation {
      position: absolute;
      width: 100%;
      height: 100%;

      .delivery-icon {
        position: absolute;
        font-size: 80rpx;
        animation: moveAcross 8s linear infinite;
        top: 50%;
        transform: translateY(-50%);
      }
    }

    .wave {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 120rpx;
      background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1440 320'%3E%3Cpath fill='%23ffffff' fill-opacity='1' d='M0,96L48,112C96,128,192,160,288,160C384,160,480,128,576,112C672,96,768,96,864,112C960,128,1056,160,1152,160C1248,160,1344,128,1392,112L1440,96L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z'%3E%3C/path%3E%3C/svg%3E") no-repeat bottom;
      background-size: cover;
    }
  }

  .login-box {
    position: absolute;
    top: 300rpx;
    left: 40rpx;
    right: 40rpx;
    background-color: rgba(255, 255, 255, 0.98);
    border-radius: 24rpx;
    padding: 50rpx 40rpx;
    box-shadow: 0 20rpx 40rpx rgba(0, 0, 0, 0.08);
    backdrop-filter: blur(10px);

    .welcome {
      text-align: center;
      margin-bottom: 40rpx;

      .title {
        font-size: 44rpx;
        font-weight: bold;
        background: linear-gradient(120deg, #ff6b6b, #ff8787);
        -webkit-background-clip: text;
        color: transparent;
        display: block;
        margin-bottom: 16rpx;
      }

      .subtitle {
        font-size: 26rpx;
        color: #666;
        letter-spacing: 2rpx;
      }
    }

    .features {
      display: flex;
      justify-content: space-around;
      margin-bottom: 40rpx;

      .feature-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 10rpx;

        .emoji {
          font-size: 40rpx;
        }

        .text {
          font-size: 24rpx;
          color: #666;
        }
      }
    }

    .form-box {
      .input-item {
        margin-bottom: 24rpx;
        position: relative;

        :deep(.u-input--focus) {
          border-color: #ff6b6b !important;
          box-shadow: 0 0 0 2rpx rgba(255, 107, 107, 0.1);
          background-color: #ffffff;
        }
      }

      .register-link {
        text-align: center;
        margin-top: 30rpx;
        font-size: 26rpx;
        color: #666;

        .link {
          color: #ff6b6b;
          margin-left: 10rpx;
        }
      }
    }
  }
}

@keyframes moveAcross {
  0% {
    left: -100rpx;
  }
  100% {
    left: 100%;
  }
}

.register-status {
  margin-bottom: 30rpx;

  .status-box {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20rpx;
    border-radius: 12rpx;

    .status-text {
      margin-left: 12rpx;
      font-size: 28rpx;
    }
  }

  .status-pending {
    background-color: #fff7e6;
    border: 2rpx solid #ffd591;

    .status-text {
      color: #faad14;
    }
  }

  .status-rejected {
    background-color: #fff1f0;
    border: 2rpx solid #ffa39e;

    .status-text {
      color: #ff4d4f;
    }
  }

  .status-approved {
    background-color: #f6ffed;
    border: 2rpx solid #b7eb8f;

    .status-text {
      color: #ff6b6b;
    }
  }
}
</style> 