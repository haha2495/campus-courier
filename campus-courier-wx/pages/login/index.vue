<template>
  <view class="login-container" :class="{ 'fade-out': isPageFading }">
    <!-- 动态背景元素 -->
    <view class="animated-bg">
      <!-- 顶部装饰元素 -->
      <view class="decoration-element dot-1"></view>
      <view class="decoration-element dot-2"></view>
      <view class="decoration-element dot-3"></view>
      <view class="decoration-element dot-4"></view>
      <view class="decoration-element dot-5"></view>
      
      <!-- 跑腿动画元素 -->
      <view class="courier-animation">
        <view class="courier-vehicle">🛵</view>
        <view class="courier-track"></view>
        <view class="courier-packages">
          <view class="package package-1">📦</view>
          <view class="package package-2">🍔</view>
          <view class="package package-3">📚</view>
        </view>
      </view>
    </view>

    <!-- 登录卡片 -->
    <view class="login-card" :class="{ 'success-animation': loginSuccess }">
      <!-- 标题区域 -->
      <view class="header">
        <view class="logo-container">
          <text class="logo-emoji">🛵</text>
        </view>
        <text class="title">校园跑腿系统</text>
        <text class="subtitle">你的校园生活好帮手</text>
      </view>

      <!-- 特点展示 -->
      <view class="features">
        <view class="feature-item">
          <view class="feature-icon lightning">
            <text class="emoji">⚡</text>
          </view>
          <text class="text">极速配送</text>
        </view>
        <view class="feature-item">
          <view class="feature-icon wallet">
            <text class="emoji">💰</text>
          </view>
          <text class="text">超值价格</text>
        </view>
        <view class="feature-item">
          <view class="feature-icon shield">
            <text class="emoji">🔒</text>
          </view>
          <text class="text">安全保障</text>
        </view>
      </view>

      <!-- 状态提示 -->
      <view class="status-container" v-if="registerStatus.show">
        <view class="status-box" :class="registerStatus.class">
          <u-icon :name="registerStatus.icon" size="36" :color="registerStatus.color"></u-icon>
          <text class="status-text">{{ registerStatus.text }}</text>
        </view>
      </view>

      <!-- 表单区域 -->
      <view class="form-container" v-if="!loginSuccess">
        <u--form
          :model="formData"
          ref="uForm"
          labelPosition="left"
          labelWidth="0"
        >
          <!-- 用户名输入框 -->
          <view class="input-wrapper">
            <u-form-item prop="studentNo">
              <u--input
                v-model="formData.studentNo"
                border="none"
                placeholder="输入学号/手机号"
                placeholderStyle="color: #9399a6"
                :customStyle="inputStyle"
                prefixIcon="account"
                prefixIconStyle="font-size: 40rpx; color: #6979f8"
              ></u--input>
            </u-form-item>
          </view>

          <!-- 密码输入框 -->
          <view class="input-wrapper">
            <u-form-item prop="password">
              <u--input
                v-model="formData.password"
                type="password"
                border="none"
                placeholder="输入密码"
                placeholderStyle="color: #9399a6"
                :customStyle="inputStyle"
                prefixIcon="lock"
                prefixIconStyle="font-size: 40rpx; color: #6979f8"
              ></u--input>
            </u-form-item>
          </view>
        </u--form>

        <!-- 登录按钮 -->
        <view class="action-area">
          <u-button
            type="primary"
            text="登 录"
            :customStyle="buttonStyle"
            @click="handleLogin"
            :loading="loading"
          ></u-button>
        </view>
      </view>
      
      <!-- 登录成功动画 -->
      <view class="success-container" v-if="loginSuccess">
        <view class="success-icon">
          <text class="success-emoji">✅</text>
        </view>
        <text class="success-text">登录成功</text>
        <text class="success-hint">正在进入校园跑腿系统...</text>
      </view>
    </view>
    
    <!-- 底部装饰元素 -->
    <view class="bottom-decoration">
      <view class="campus-element building-1"></view>
      <view class="campus-element building-2"></view>
      <view class="campus-element tree-1"></view>
      <view class="campus-element tree-2"></view>
    </view>
  </view>
</template>

<script setup>
import {nextTick, onMounted, reactive, ref} from 'vue'
import {userApi} from '@/api/user'

// 表单数据
const formData = reactive({
  studentNo: '20220202',
  password: '20220202L'
})

// 加载状态
const loading = ref(false)

// 登录成功状态
const loginSuccess = ref(false)
const isPageFading = ref(false)

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
  backgroundColor: '#f5f7ff',
  borderRadius: '20rpx',
  fontSize: '30rpx',
  paddingLeft: '20rpx',
  border: 'none',
  boxShadow: '0 4rpx 16rpx rgba(105, 121, 248, 0.1)',
  transition: 'all 0.3s ease'
}

// 按钮样式
const buttonStyle = {
  height: '110rpx',
  marginTop: '50rpx',
  borderRadius: '55rpx',
  fontSize: '34rpx',
  fontWeight: 'bold',
  background: 'linear-gradient(to right, #6979f8, #a5affb)',
  boxShadow: '0 10rpx 20rpx rgba(105, 121, 248, 0.3)',
  border: 'none',
  letterSpacing: '6rpx'
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
  
  // 添加动画启动
  startCourierAnimation()
})

// 启动快递员动画
const startCourierAnimation = () => {
  // 这里可以添加一些JS动画控制逻辑
  console.log('Courier animation started')
}

// 跳转到首页（带动画）
const navigateToHome = () => {
  // 先显示页面淡出效果
  isPageFading.value = true
  
  // 延迟执行页面跳转
  setTimeout(() => {
    uni.reLaunch({
      url: '/pages/index/index',
      animationType: 'fade-in',
      animationDuration: 300
    })
  }, 800) // 与CSS动画时长匹配
}

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
          
          // 显示登录成功动画
          loading.value = false
          loginSuccess.value = true
          
          // 播放成功音效（可选）
          const successSound = uni.createInnerAudioContext()
          successSound.src = '/static/sounds/success.mp3' // 需要添加这个音频文件
          successSound.play()
          
          // 延迟跳转
          setTimeout(() => {
            // 执行带动画的页面跳转
            navigateToHome()
          }, 1200)
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
          
          loading.value = false
        }
      } catch (err) {
        console.error('登录失败:', err)
        registerStatus.show = true
        registerStatus.text = err.message || '登录失败，请稍后重试'
        registerStatus.icon = 'error-circle'
        registerStatus.color = '#ff4d4f'
        registerStatus.class = 'status-error'
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
  background: linear-gradient(135deg, #e9efff 0%, #f5f7ff 100%);
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40rpx;
  transition: opacity 0.8s ease;
  
  &.fade-out {
    opacity: 0;
  }
}

/* 动态背景元素 */
.animated-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  
  .decoration-element {
    position: absolute;
    border-radius: 50%;
    opacity: 0.6;
    
    &.dot-1 {
      width: 240rpx;
      height: 240rpx;
      background: linear-gradient(45deg, #6979f8, #a5affb);
      top: -120rpx;
      right: -100rpx;
      animation: float 8s ease-in-out infinite;
    }
    
    &.dot-2 {
      width: 160rpx;
      height: 160rpx;
      background: linear-gradient(45deg, #ffac41, #ff1e56);
      bottom: 10%;
      left: -80rpx;
      animation: float 6s ease-in-out infinite;
      animation-delay: 1s;
    }
    
    &.dot-3 {
      width: 120rpx;
      height: 120rpx;
      background: linear-gradient(45deg, #0acffe, #495aff);
      top: 20%;
      left: 10%;
      animation: float 10s ease-in-out infinite;
      animation-delay: 2s;
    }
    
    &.dot-4 {
      width: 80rpx;
      height: 80rpx;
      background: linear-gradient(45deg, #13e2da, #5d7aff);
      bottom: 15%;
      right: 10%;
      animation: float 7s ease-in-out infinite;
      animation-delay: 3s;
    }
    
    &.dot-5 {
      width: 60rpx;
      height: 60rpx;
      background: linear-gradient(45deg, #ff7eb3, #ff758c);
      top: 30%;
      right: 20%;
      animation: float 9s ease-in-out infinite;
      animation-delay: 4s;
    }
  }
}

/* 快递动画 */
.courier-animation {
  position: absolute;
  top: 80rpx;
  width: 100%;
  height: 120rpx;
  z-index: 1;
  
  .courier-vehicle {
    position: absolute;
    font-size: 80rpx;
    left: 10%;
    animation: moveRight 12s linear infinite;
    z-index: 3;
  }
  
  .courier-track {
    position: absolute;
    top: 90rpx;
    width: 100%;
    height: 6rpx;
    background: rgba(0, 0, 0, 0.05);
    z-index: 1;
  }
  
  .courier-packages {
    position: absolute;
    width: 100%;
    height: 100%;
    
    .package {
      position: absolute;
      font-size: 50rpx;
      opacity: 0;
      animation: showPackage 12s linear infinite;
      
      &.package-1 {
        top: 20rpx;
        left: 80%;
        animation-delay: 4s;
      }
      
      &.package-2 {
        top: 30rpx;
        left: 60%;
        animation-delay: 8s;
      }
      
      &.package-3 {
        top: 10rpx;
        left: 40%;
        animation-delay: 0s;
      }
    }
  }
}

/* 登录卡片 */
.login-card {
  width: 100%;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20rpx);
  border-radius: 40rpx;
  padding: 60rpx 40rpx;
  box-shadow: 0 20rpx 40rpx rgba(0, 0, 0, 0.08);
  position: relative;
  z-index: 10;
  max-width: 650rpx;
  transition: transform 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55), 
              box-shadow 0.6s ease,
              background 0.6s ease;
  
  &.success-animation {
    transform: scale(1.05);
    box-shadow: 0 30rpx 60rpx rgba(105, 121, 248, 0.2);
    background: rgba(255, 255, 255, 0.95);
  }
  
  .header {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 40rpx;
    
    .logo-container {
      width: 120rpx;
      height: 120rpx;
      background: linear-gradient(45deg, #6979f8, #a5affb);
      border-radius: 30rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 30rpx;
      box-shadow: 0 10rpx 20rpx rgba(105, 121, 248, 0.3);
      transform: rotate(-5deg);
      
      .logo-emoji {
        font-size: 60rpx;
      }
    }
    
    .title {
      font-size: 44rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 16rpx;
      background: linear-gradient(to right, #6979f8, #a5affb);
      -webkit-background-clip: text;
      color: transparent;
    }
    
    .subtitle {
      font-size: 28rpx;
      color: #666;
      margin-bottom: 10rpx;
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
      
      .feature-icon {
        width: 80rpx;
        height: 80rpx;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 16rpx;
        animation: pulse 2s infinite;
        
        &.lightning {
          background: linear-gradient(45deg, #ffde59, #ffa751);
          box-shadow: 0 6rpx 12rpx rgba(255, 167, 81, 0.3);
          animation-delay: 0s;
        }
        
        &.wallet {
          background: linear-gradient(45deg, #0acffe, #495aff);
          box-shadow: 0 6rpx 12rpx rgba(73, 90, 255, 0.3);
          animation-delay: 0.4s;
        }
        
        &.shield {
          background: linear-gradient(45deg, #11998e, #38ef7d);
          box-shadow: 0 6rpx 12rpx rgba(56, 239, 125, 0.3);
          animation-delay: 0.8s;
        }
        
        .emoji {
          font-size: 40rpx;
        }
      }
      
      .text {
        font-size: 26rpx;
        color: #333;
        font-weight: 500;
      }
    }
  }
  
  .status-container {
    margin-bottom: 30rpx;
    
    .status-box {
      padding: 20rpx;
      border-radius: 16rpx;
      display: flex;
      align-items: center;
      
      &.status-error {
        background-color: rgba(255, 77, 79, 0.1);
      }
      
      &.status-pending {
        background-color: rgba(250, 173, 20, 0.1);
      }
      
      &.status-rejected {
        background-color: rgba(255, 77, 79, 0.1);
      }
      
      &.status-disabled {
        background-color: rgba(255, 77, 79, 0.1);
      }
      
      .status-text {
        margin-left: 16rpx;
        font-size: 26rpx;
      }
    }
  }
  
  .form-container {
    .input-wrapper {
      margin-bottom: 30rpx;
    }
    
    .action-area {
      margin-top: 40rpx;
    }
  }
  
  /* 登录成功动画 */
  .success-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 40rpx 0;
    animation: fadeIn 0.5s ease-out;
    
    .success-icon {
      width: 160rpx;
      height: 160rpx;
      background: linear-gradient(45deg, #11998e, #38ef7d);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 40rpx;
      box-shadow: 0 15rpx 30rpx rgba(56, 239, 125, 0.3);
      animation: success-pulse 2s infinite;
      
      .success-emoji {
        font-size: 80rpx;
      }
    }
    
    .success-text {
      font-size: 44rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 20rpx;
      background: linear-gradient(to right, #11998e, #38ef7d);
      -webkit-background-clip: text;
      color: transparent;
    }
    
    .success-hint {
      font-size: 28rpx;
      color: #666;
      animation: blink 1.5s ease-in-out infinite;
    }
  }
}

/* 底部装饰 */
.bottom-decoration {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 160rpx;
  z-index: 1;
  
  .campus-element {
    position: absolute;
    bottom: 0;
    
    &.building-1 {
      width: 120rpx;
      height: 150rpx;
      background: rgba(105, 121, 248, 0.6);
      left: 10%;
      border-radius: 8rpx 8rpx 0 0;
    }
    
    &.building-2 {
      width: 100rpx;
      height: 120rpx;
      background: rgba(165, 175, 251, 0.6);
      left: 25%;
      border-radius: 8rpx 8rpx 0 0;
    }
    
    &.tree-1 {
      width: 60rpx;
      height: 90rpx;
      background: rgba(56, 239, 125, 0.6);
      right: 20%;
      border-radius: 50% 50% 0 0;
    }
    
    &.tree-2 {
      width: 40rpx;
      height: 60rpx;
      background: rgba(56, 239, 125, 0.7);
      right: 30%;
      border-radius: 50% 50% 0 0;
    }
  }
}

/* 动画定义 */
@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20rpx);
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes success-pulse {
  0% {
    transform: scale(1);
    box-shadow: 0 15rpx 30rpx rgba(56, 239, 125, 0.3);
  }
  50% {
    transform: scale(1.1);
    box-shadow: 0 20rpx 40rpx rgba(56, 239, 125, 0.4);
  }
  100% {
    transform: scale(1);
    box-shadow: 0 15rpx 30rpx rgba(56, 239, 125, 0.3);
  }
}

@keyframes moveRight {
  0% {
    left: -10%;
    transform: translateY(0);
  }
  25% {
    transform: translateY(-10rpx);
  }
  50% {
    transform: translateY(0);
  }
  75% {
    transform: translateY(-10rpx);
  }
  100% {
    left: 110%;
    transform: translateY(0);
  }
}

@keyframes showPackage {
  0%, 100% {
    opacity: 0;
    transform: translateY(0);
  }
  10%, 90% {
    opacity: 1;
  }
  50% {
    transform: translateY(-20rpx);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes blink {
  0%, 100% {
    opacity: 0.6;
  }
  50% {
    opacity: 1;
  }
}
</style> 