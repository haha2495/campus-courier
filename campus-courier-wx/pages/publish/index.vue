<template>
  <view class="publish-container" :class="containerClass">
    <!-- 自定义导航栏 -->
    <view class="custom-nav-bar" :style="{ paddingTop: statusBarHeightValue + 'px' }">
      <view class="nav-left" @click="handleBack">
        <u-icon name="close" size="40rpx" color="#333"></u-icon>
      </view>
      <view class="nav-title">发布任务</view>
    </view>

    <!-- 页面内容 -->
    <scroll-view scroll-y class="publish-content" :style="{ top: contentTopValue + 'px' }">
      <!-- 任务类型选择 -->
      <view class="section-title">选择任务类型</view>
      <view class="task-types">
        <view 
          v-for="(type, index) in taskTypes" 
          :key="index" 
          class="task-type-item"
          :class="{ 'active': selectedTaskType === type.value }"
          @click="selectTaskType(type.value)"
        >
          <view class="type-icon">{{ type.icon }}</view>
          <text class="type-name">{{ type.name }}</text>
        </view>
      </view>
      
      <!-- 任务表单 -->
      <view v-if="selectedTaskType">
        <view class="section-title">任务信息</view>
        
        <!-- 任务标题 -->
        <view class="form-item">
          <text class="form-label">任务标题</text>
          <input 
            type="text" 
            placeholder="请输入任务标题（5-20字）" 
            placeholder-class="placeholder"
            class="form-input"
            maxlength="20"
            v-model="taskForm.title"
          />
        </view>
        
        <!-- 任务描述 -->
        <view class="form-item">
          <text class="form-label">任务描述</text>
          <textarea 
            placeholder="请详细描述您的任务需求..." 
            placeholder-class="placeholder"
            class="form-textarea"
            auto-height
            maxlength="500"
            v-model="taskForm.description"
          ></textarea>
        </view>
        
        <!-- 地点信息 -->
        <view class="form-item">
          <text class="form-label">取货地点</text>
          <view class="location-picker" @click="showLocationPicker('pickup')">
            <text class="location-placeholder">{{ taskForm.pickupLocation || '点击选择位置' }}</text>
            <view class="location-icon">📍</view>
          </view>
        </view>
        
        <view class="form-item">
          <text class="form-label">送达地点</text>
          <view class="location-picker" @click="showLocationPicker('delivery')">
            <text class="location-placeholder">{{ taskForm.deliveryLocation || '点击选择位置' }}</text>
            <view class="location-icon">📍</view>
          </view>
        </view>
        
        <!-- 时间选择 -->
        <view class="form-item">
          <text class="form-label">期望送达</text>
          <view class="time-picker">
            <picker 
              mode="date" 
              @change="onDateChange"
              :start="currentDate"
            >
              <view class="picker-item">
                <text class="picker-text">{{ taskForm.deliveryDate || '选择日期' }}</text>
                <text class="picker-arrow">▼</text>
              </view>
            </picker>
            <picker 
              mode="time" 
              @change="onTimeChange"
            >
              <view class="picker-item">
                <text class="picker-text">{{ taskForm.deliveryTime || '选择时间' }}</text>
                <text class="picker-arrow">▼</text>
              </view>
            </picker>
          </view>
        </view>
        
        <!-- 金额输入 -->
        <view class="form-item">
          <text class="form-label">悬赏金额</text>
          <view class="reward-input-wrapper">
            <text class="currency-symbol">¥</text>
            <input 
              type="digit" 
              placeholder="输入金额" 
              placeholder-class="placeholder"
              class="reward-input"
              v-model="taskForm.reward"
            />
          </view>
        </view>
        
        <!-- 上传图片 -->
        <view class="form-item">
          <text class="form-label">上传图片(选填)</text>
          <view class="upload-area">
            <view 
              v-for="(image, index) in taskForm.images" 
              :key="index"
              class="image-preview"
            >
              <image :src="image" mode="aspectFill" class="preview-image"></image>
              <view class="delete-icon" @click.stop="deleteImage(index)">×</view>
            </view>
            <view class="upload-button" @click="chooseImage" v-if="taskForm.images.length < 3">
              <text class="upload-icon">+</text>
              <text class="upload-text">上传图片</text>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 未选择任务类型的提示 -->
      <view v-else class="select-type-hint">
        <view class="hint-icon">👆</view>
        <text class="hint-text">请先选择任务类型</text>
      </view>
      
      <!-- 提交按钮 -->
      <view 
        class="submit-button" 
        :class="{ 'button-disabled': !selectedTaskType }"
        @click="submitTask"
      >
        <text class="button-text">{{ selectedTaskType ? '发布任务' : '请先选择任务类型' }}</text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'

// 状态栏高度
const statusBarHeight = ref(20)
const statusBarHeightValue = computed(() => statusBarHeight.value)
const contentTopValue = computed(() => statusBarHeight.value + 44)
const currentDate = new Date().toISOString().split('T')[0]

// 创建动画控制变量
const isPageReady = ref(false)
const isClosing = ref(false)
const containerClass = computed(() => {
  return {
    'page-show': isPageReady.value,
    'page-hide': isClosing.value
  }
})

// 任务类型选择
const selectedTaskType = ref('')

// 表单数据
const taskForm = reactive({
  title: '',
  description: '',
  pickupLocation: '',
  deliveryLocation: '',
  deliveryDate: '',
  deliveryTime: '',
  reward: '',
  images: []
})

// 任务类型列表
const taskTypes = [
  { name: '校园跑腿', value: 'errand', icon: '🛵' },
  { name: '快递代取', value: 'express', icon: '📦' },
  { name: '食品外卖', value: 'food', icon: '🍔' },
  { name: '校园专车', value: 'taxi', icon: '🚗' },
  { name: '文印服务', value: 'print', icon: '📄' },
  { name: '其他服务', value: 'other', icon: '🔍' }
]

// 页面加载获取设备信息
onMounted(() => {
  getSystemInfo()
  
  // 添加短暂延迟，确保页面已经加载
  setTimeout(() => {
    isPageReady.value = true
  }, 50)
})

// 获取系统信息
const getSystemInfo = () => {
  try {
    const systemInfo = uni.getSystemInfoSync()
    console.log('System info:', systemInfo)
    statusBarHeight.value = systemInfo.statusBarHeight || 20
  } catch (e) {
    console.error('获取系统信息失败:', e)
  }
}

// 返回上一页
const handleBack = () => {
  // 开始关闭动画
  isClosing.value = true
  
  // 动画结束后再执行返回操作
  setTimeout(() => {
    uni.navigateBack({
      delta: 1
    })
  }, 350) // 与transition时间匹配
}

// 选择任务类型
const selectTaskType = (type) => {
  selectedTaskType.value = type
}

// 选择位置
const showLocationPicker = (type) => {
  uni.showToast({
    title: '位置选择功能开发中',
    icon: 'none'
  })
}

// 日期选择
const onDateChange = (e) => {
  taskForm.deliveryDate = e.detail.value
}

// 时间选择
const onTimeChange = (e) => {
  taskForm.deliveryTime = e.detail.value
}

// 选择图片
const chooseImage = () => {
  uni.chooseImage({
    count: 3 - taskForm.images.length,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: (res) => {
      taskForm.images = [...taskForm.images, ...res.tempFilePaths]
    }
  })
}

// 删除图片
const deleteImage = (index) => {
  taskForm.images.splice(index, 1)
}

// 显示消息
const showMessage = (message) => {
  uni.showToast({
    title: message,
    icon: 'none'
  })
}

// 提交任务
const submitTask = () => {
  if (!selectedTaskType.value) {
    showMessage('请先选择任务类型')
    return
  }
  
  // 表单验证
  if (!taskForm.title) {
    showMessage('请输入任务标题')
    return
  }
  
  if (!taskForm.description) {
    showMessage('请输入任务描述')
    return
  }
  
  if (!taskForm.pickupLocation) {
    showMessage('请选择取货地点')
    return
  }
  
  if (!taskForm.deliveryLocation) {
    showMessage('请选择送达地点')
    return
  }
  
  if (!taskForm.deliveryDate || !taskForm.deliveryTime) {
    showMessage('请选择期望送达时间')
    return
  }
  
  if (!taskForm.reward) {
    showMessage('请输入悬赏金额')
    return
  }
  
  // 提交表单
  showMessage('发布任务功能待实现')
  console.log('任务表单数据:', {
    type: selectedTaskType.value,
    ...taskForm
  })
  
  // TODO: 提交到服务器
  // 提交成功后返回首页
  // uni.navigateBack()
}
</script>

<style lang="scss" scoped>
.publish-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 100;
  border-radius: 30rpx 30rpx 0 0;
  overflow: hidden;
  box-shadow: 0 -10rpx 30rpx rgba(0, 0, 0, 0.1);
  /* 初始状态：位于屏幕底部外 */
  transform: translateY(100%);
  transition: transform 0.35s cubic-bezier(0.16, 1, 0.3, 1);
  will-change: transform;
  
  /* 显示动画 */
  &.page-show {
    transform: translateY(0);
  }
  
  /* 隐藏动画 */
  &.page-hide {
    transform: translateY(100%);
  }
}

// 自定义导航栏
.custom-nav-bar {
  position: fixed;
  left: 0;
  right: 0;
  height: 44px;
  background-color: #ffffff;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  border-radius: 30rpx 30rpx 0 0;
  
  .nav-left {
    position: absolute;
    left: 30rpx;
    height: 100%;
    display: flex;
    align-items: center;
  }
  
  .nav-title {
    font-size: 34rpx;
    font-weight: bold;
    color: #333;
  }
}

// 页面主体内容
.publish-content {
  height: calc(100vh - var(--window-top));
  padding: 30rpx;
  box-sizing: border-box;
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
}

// 任务类型选择
.section-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.task-types {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -10rpx 40rpx;
}

.task-type-item {
  width: calc(33.33% - 20rpx);
  margin: 0 10rpx 20rpx;
  height: 140rpx;
  background-color: #ffffff;
  border-radius: 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.2), rgba(255, 255, 255, 0));
    z-index: 1;
  }
  
  .type-icon {
    font-size: 50rpx;
    margin-bottom: 10rpx;
    position: relative;
    z-index: 2;
    transition: transform 0.3s ease;
  }
  
  .type-name {
    font-size: 24rpx;
    color: #666;
    position: relative;
    z-index: 2;
    transition: color 0.3s ease;
  }
  
  &:active {
    transform: scale(0.95);
  }
  
  &.active {
    background: linear-gradient(135deg, #ff9500, #ff7d00);
    box-shadow: 0 6rpx 15rpx rgba(255, 149, 0, 0.2);
    
    .type-icon {
      transform: scale(1.1);
    }
    
    .type-name {
      color: #ffffff;
      font-weight: 500;
    }
  }
}

// 表单样式
.form-item {
  margin-bottom: 30rpx;
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.03);
}

.form-label {
  display: block;
  font-size: 26rpx;
  color: #333;
  margin-bottom: 15rpx;
  font-weight: 500;
}

.form-input, .form-textarea {
  width: 100%;
  background-color: #f8f8f8;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
  border: 2rpx solid transparent;
  transition: all 0.3s;
  
  &:focus {
    border-color: #ff9500;
    background-color: #fff;
    box-shadow: 0 0 0 2rpx rgba(255, 149, 0, 0.1);
  }
}

.form-textarea {
  min-height: 180rpx;
  line-height: 1.5;
}

.placeholder {
  color: #bbb;
}

// 位置选择器
.location-picker {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #f8f8f8;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #333;
  
  .location-placeholder {
    color: #bbb;
  }
  
  .location-icon {
    font-size: 32rpx;
  }
}

// 时间选择器
.time-picker {
  display: flex;
  gap: 20rpx;
  
  .picker-item {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #f8f8f8;
    border-radius: 12rpx;
    padding: 20rpx;
    font-size: 28rpx;
    
    .picker-text {
      color: #bbb;
    }
    
    .picker-arrow {
      font-size: 24rpx;
      color: #999;
    }
  }
}

// 金额输入
.reward-input-wrapper {
  display: flex;
  align-items: center;
  background-color: #f8f8f8;
  border-radius: 12rpx;
  padding: 0 20rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s;
  
  &:focus-within {
    border-color: #ff9500;
    background-color: #fff;
    box-shadow: 0 0 0 2rpx rgba(255, 149, 0, 0.1);
  }
  
  .currency-symbol {
    font-size: 32rpx;
    color: #ff9500;
    font-weight: bold;
    margin-right: 8rpx;
  }
  
  .reward-input {
    flex: 1;
    height: 80rpx;
    font-size: 28rpx;
    color: #333;
    background-color: transparent;
  }
}

// 上传区域
.upload-area {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  
  .upload-button {
    width: 160rpx;
    height: 160rpx;
    background-color: #f8f8f8;
    border-radius: 12rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 2rpx dashed #ddd;
    
    .upload-icon {
      font-size: 48rpx;
      color: #bbb;
      line-height: 1;
      margin-bottom: 10rpx;
    }
    
    .upload-text {
      font-size: 24rpx;
      color: #bbb;
    }
  }
  
  .image-preview {
    width: 160rpx;
    height: 160rpx;
    position: relative;
    border-radius: 12rpx;
    overflow: hidden;
    
    .preview-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .delete-icon {
      position: absolute;
      top: 0;
      right: 0;
      width: 40rpx;
      height: 40rpx;
      background-color: rgba(0, 0, 0, 0.5);
      color: #fff;
      font-size: 28rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 0 0 0 12rpx;
    }
  }
}

// 选择类型提示
.select-type-hint {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60rpx 0;
  
  .hint-icon {
    font-size: 60rpx;
    margin-bottom: 20rpx;
    animation: bounce 2s infinite;
  }
  
  .hint-text {
    font-size: 28rpx;
    color: #999;
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-20rpx);
  }
  60% {
    transform: translateY(-10rpx);
  }
}

// 提交按钮
.submit-button {
  height: 90rpx;
  background: linear-gradient(to right, #ff9500, #ff7d00);
  border-radius: 45rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10rpx 20rpx rgba(255, 149, 0, 0.2);
  margin: 40rpx 0;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0));
    animation: shine 2s infinite;
  }
  
  .button-text {
    font-size: 32rpx;
    color: #ffffff;
    font-weight: bold;
  }
  
  &:active {
    transform: scale(0.98);
    box-shadow: 0 5rpx 10rpx rgba(255, 149, 0, 0.2);
  }
  
  &.button-disabled {
    background: linear-gradient(to right, #cccccc, #bbbbbb);
    box-shadow: 0 5rpx 10rpx rgba(0, 0, 0, 0.1);
    
    &::before {
      display: none;
    }
    
    &:active {
      transform: none;
    }
  }
}

@keyframes shine {
  0% {
    left: -100%;
  }
  20% {
    left: 100%;
  }
  100% {
    left: 100%;
  }
}
</style> 