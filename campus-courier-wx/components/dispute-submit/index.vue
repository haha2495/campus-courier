<!-- 纠纷提交组件 -->
<template>
  <u-popup
    v-model:show="localShow"
    mode="bottom"
    round="20"
    @close="handleClose"
    :safe-area-inset-bottom="true"
    :customStyle="{
      height: '92vh',
      borderRadius: '24rpx 24rpx 0 0'
    }"
  >
    <view class="dispute-popup">
      <!-- 头部 -->
      <view class="dispute-header">
        <text class="title">提交纠纷</text>
        <view class="close-btn" @click="handleClose">
          <u-icon name="close" color="#666" size="32"></u-icon>
        </view>
      </view>

      <!-- 内容区域 -->
      <view class="dispute-body">
        <view 
          class="dispute-content" 
          @scroll="handleScroll"
        >
          <!-- 纠纷类型选择 -->
          <view class="form-section type-section">
            <text class="section-title required">选择纠纷类型</text>
            <view class="type-grid">
              <view
                v-for="type in disputeTypes"
                :key="type.code"
                class="type-item"
                :class="{ active: form.type === type.code }"
                @click="selectType(type.code)"
              >
                <view class="type-icon">
                  <u-icon :name="type.icon" :color="form.type === type.code ? '#ff9500' : '#666'" size="32"></u-icon>
                </view>
                <text class="type-text">{{ type.desc }}</text>
                <view class="type-check" v-if="form.type === type.code">
                  <u-icon name="checkmark-circle" color="#ff9500" size="32"></u-icon>
                </view>
              </view>
            </view>
          </view>

          <!-- 纠纷标题 -->
          <view class="form-section">
            <text class="section-title required">纠纷标题</text>
            <view class="input-wrapper">
              <u-input
                v-model="form.title"
                placeholder="简要描述纠纷主要问题"
                :maxlength="50"
                :border="false"
                prefixIcon="edit-pen"
                prefixIconStyle="color: #ff9500"
              ></u-input>
            </view>
          </view>

          <!-- 纠纷内容 -->
          <view class="form-section">
            <text class="section-title required">详细说明</text>
            <view class="textarea-wrapper">
              <textarea
                v-model="form.content"
                class="content-textarea"
                placeholder="请详细描述纠纷发生的经过、原因和您的诉求..."
                :maxlength="500"
                show-confirm-bar="false"
              ></textarea>
              <text class="word-count" :class="{ 'word-limit': form.content.length >= 500 }">
                {{ form.content.length }}/500
              </text>
            </view>
          </view>

          <!-- 图片上传 -->
          <view class="form-section">
            <view class="section-header">
              <text class="section-title">上传凭证</text>
              <text class="section-tip">（最多6张，可选）</text>
            </view>
            <view class="upload-wrapper">
              <u-upload
                :fileList="form.images"
                @afterRead="afterRead"
                @delete="deleteImage"
                name="3"
                multiple
                :maxCount="6"
                :imageMode="'aspectFill'"
                uploadIcon="camera-fill"
                uploadIconColor="#ff9500"
              ></u-upload>
            </view>
            <text class="upload-tip">上传相关证据图片，如商品照片、聊天记录等</text>
          </view>

          <!-- 底部提示 -->
          <view class="form-tips">
            <u-icon name="info-circle" color="#ff9500" size="28"></u-icon>
            <text class="tips-text">提交后客服会尽快处理，请保持联系方式畅通</text>
          </view>
        </view>
      </view>

      <!-- 底部按钮 -->
      <view class="dispute-footer">
        <u-button
          type="primary"
          :disabled="!isValid"
          @click="handleSubmit"
          :loading="submitting"
          shape="circle"
          customStyle="width: 100%; height: 80rpx; background: linear-gradient(to right, #ff9500, #ff7100);"
        >
          <text class="submit-text">提交纠纷</text>
        </u-button>
      </view>
    </view>
  </u-popup>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { DisputeTypeEnum } from '@/constants/disputeType'
import { uploadApi } from '@/api/upload'

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  taskId: {
    type: [String, Number],
    required: true
  }
})

const emit = defineEmits(['update:show', 'submit'])

// 滚动相关
const scrollTop = ref(0)

const handleScroll = (e) => {
  scrollTop.value = e.detail.scrollTop
}

// 本地显示状态
const localShow = computed({
  get: () => props.show,
  set: (val) => emit('update:show', val)
})

// 表单数据
const form = ref({
  type: '',
  title: '',
  content: '',
  images: []
})

// 提交状态
const submitting = ref(false)

// 纠纷类型列表
const disputeTypes = Object.values(DisputeTypeEnum)

// 选择纠纷类型
const selectType = (code) => {
  form.value.type = code
}

// 表单验证
const isValid = computed(() => {
  return form.value.type && 
         form.value.title.trim() && 
         form.value.content.trim()
})

// 处理图片上传
const afterRead = async (event) => {
  const { file } = event
  
  // 更新上传状态
  const fileListIndex = form.value.images.length
  form.value.images.push({
    url: file.url,
    status: 'uploading',
    message: '上传中...'
  })
  
  try {
    // 调用上传API
    const uploadedUrl = await uploadApi.upload(file.url)
    
    // 更新上传成功状态
    form.value.images[fileListIndex] = {
      url: uploadedUrl,
      status: 'success',
      message: '上传成功'
    }
  } catch (error) {
    // 更新上传失败状态
    form.value.images[fileListIndex] = {
      url: file.url,
      status: 'error',
      message: error.message || '上传失败'
    }
    
    // 显示错误提示
    uni.showToast({
      title: error.message || '图片上传失败',
      icon: 'none'
    })
  }
}

// 删除图片
const deleteImage = (event) => {
  form.value.images.splice(event.index, 1)
}

// 关闭弹窗
const handleClose = () => {
  localShow.value = false
  resetForm()
}

// 重置表单
const resetForm = () => {
  form.value = {
    type: '',
    title: '',
    content: '',
    images: []
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!isValid.value || submitting.value) return
  
  submitting.value = true
  try {
    // 构建提交数据
    const submitData = {
      taskId: props.taskId,
      type: form.value.type,
      title: form.value.title.trim(),
      content: form.value.content.trim(),
      evidence: form.value.images.map(img => img.url)
    }
    
    emit('submit', submitData)
    handleClose()
  } catch (error) {
    console.error('提交纠纷失败：', error)
    uni.showToast({
      title: error.message || '提交失败',
      icon: 'none'
    })
  } finally {
    submitting.value = false
  }
}
</script>

<style lang="scss" scoped>
.dispute-popup {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  overflow: hidden;
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 200rpx;
    background: linear-gradient(135deg, #ff9500 0%, #ff7100 100%);
    opacity: 0.1;
    z-index: 0;
  }
}

.dispute-header {
  min-height: 100rpx;
  padding: 0 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-bottom: 2rpx solid rgba(0, 0, 0, 0.05);
  position: relative;
  z-index: 1;
  
  .title {
    font-size: 36rpx;
    font-weight: 600;
    color: #333;
    text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
    position: relative;
    
    &::after {
      content: '';
      position: absolute;
      bottom: -6rpx;
      left: 0;
      width: 40%;
      height: 4rpx;
      background: linear-gradient(to right, #ff9500, transparent);
      border-radius: 4rpx;
    }
  }
  
  .close-btn {
    width: 64rpx;
    height: 64rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    background: rgba(0, 0, 0, 0.05);
    transition: all 0.3s;
    
    &:active {
      transform: scale(0.9);
      background: rgba(0, 0, 0, 0.1);
    }
  }
}

.dispute-body {
  flex: 1;
  overflow: hidden;
}

.dispute-content {
  height: 100%;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  padding: 30rpx 30rpx 150rpx;
  box-sizing: border-box;
  position: relative;
  z-index: 1;
  
  &::-webkit-scrollbar {
    display: none;
  }
}

.dispute-footer {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  min-height: 120rpx;
  padding: 20rpx 30rpx;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-top: 2rpx solid rgba(0, 0, 0, 0.05);
  z-index: 99;
  
  .submit-text {
    font-size: 32rpx;
    font-weight: 600;
    color: #ffffff;
    text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
  }
  
  :deep(.u-button) {
    overflow: hidden;
    position: relative;
    
    &::after {
      content: '';
      position: absolute;
      top: -50%;
      left: -50%;
      width: 200%;
      height: 200%;
      background: radial-gradient(circle, rgba(255, 255, 255, 0.2) 0%, transparent 60%);
      transform: scale(0);
      transition: transform 0.6s;
    }
    
    &:active::after {
      transform: scale(1);
      opacity: 0;
    }
  }
}

.form-section {
  margin-bottom: 30rpx;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 24rpx;
  padding: 30rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(20px);
  border: 2rpx solid rgba(255, 255, 255, 0.5);
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-2rpx);
    box-shadow: 0 12rpx 36rpx rgba(0, 0, 0, 0.08);
  }
  
  &.type-section {
    background: transparent;
    box-shadow: none;
    padding: 0;
    border: none;
    
    &:hover {
      transform: none;
    }
  }
  
  .section-title {
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 24rpx;
    display: inline-block;
    position: relative;
    
    &.required::before {
      content: '*';
      color: #ff4d4f;
      margin-right: 8rpx;
      font-size: 36rpx;
      position: relative;
      top: 4rpx;
    }
  }
  
  .section-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .section-tip {
      font-size: 24rpx;
      color: #999;
      margin-left: 10rpx;
    }
  }
}

.type-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
  
  .type-item {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 20rpx;
    padding: 24rpx;
    display: flex;
    align-items: center;
    gap: 20rpx;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.06);
    border: 2rpx solid rgba(255, 255, 255, 0.5);
    position: relative;
    overflow: hidden;
    cursor: pointer;
    
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg, rgba(255, 149, 0, 0.1), rgba(255, 113, 0, 0.15));
      opacity: 0;
      transition: opacity 0.3s;
    }
    
    .type-icon {
      width: 88rpx;
      height: 88rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f8f9fa;
      border-radius: 50%;
      transition: all 0.3s;
      position: relative;
      
      &::after {
        content: '';
        position: absolute;
        top: -4rpx;
        left: -4rpx;
        right: -4rpx;
        bottom: -4rpx;
        border-radius: 50%;
        background: linear-gradient(135deg, #ff9500, #ff7100);
        opacity: 0;
        transition: opacity 0.3s;
        z-index: -1;
      }
    }
    
    .type-text {
      flex: 1;
      font-size: 28rpx;
      color: #333;
      transition: all 0.3s;
      font-weight: 500;
    }
    
    .type-check {
      opacity: 0;
      transform: scale(0) rotate(-180deg);
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
    }
    
    &.active {
      background: #fff8f0;
      border: 2rpx solid #ff9500;
      transform: scale(1.02);
      
      &::before {
        opacity: 1;
      }
      
      .type-icon {
        background: rgba(255, 149, 0, 0.15);
        transform: scale(1.1);
        
        &::after {
          opacity: 0.1;
        }
      }
      
      .type-text {
        color: #ff9500;
        transform: scale(1.02);
      }
      
      .type-check {
        opacity: 1;
        transform: scale(1) rotate(0);
      }
    }
    
    &:active {
      transform: scale(0.98);
    }
  }
}

.input-wrapper {
  background: rgba(248, 249, 250, 0.8);
  border-radius: 16rpx;
  padding: 8rpx 24rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s;
  backdrop-filter: blur(10px);
  
  &:focus-within {
    border-color: #ff9500;
    background: rgba(255, 248, 240, 0.9);
    box-shadow: 0 0 0 4rpx rgba(255, 149, 0, 0.1);
  }
}

.textarea-wrapper {
  background: rgba(248, 249, 250, 0.8);
  border-radius: 16rpx;
  padding: 24rpx;
  position: relative;
  border: 2rpx solid transparent;
  transition: all 0.3s;
  backdrop-filter: blur(10px);
  
  &:focus-within {
    border-color: #ff9500;
    background: rgba(255, 248, 240, 0.9);
    box-shadow: 0 0 0 4rpx rgba(255, 149, 0, 0.1);
  }
  
  .content-textarea {
    width: 100%;
    height: 240rpx;
    font-size: 28rpx;
    line-height: 1.6;
  }
  
  .word-count {
    position: absolute;
    right: 24rpx;
    bottom: 24rpx;
    font-size: 24rpx;
    color: #999;
    background: rgba(255, 255, 255, 0.8);
    padding: 4rpx 12rpx;
    border-radius: 20rpx;
    backdrop-filter: blur(4px);
    
    &.word-limit {
      color: #ff4d4f;
      background: rgba(255, 77, 79, 0.1);
    }
  }
}

.upload-wrapper {
  margin: 24rpx 0;
  
  :deep(.u-upload__wrap) {
    background: rgba(248, 249, 250, 0.8);
    border-radius: 16rpx;
    transition: all 0.3s;
    
    &:hover {
      background: rgba(255, 248, 240, 0.9);
    }
  }
}

.upload-tip {
  font-size: 24rpx;
  color: #999;
  margin-top: 16rpx;
  display: block;
  padding: 8rpx 16rpx;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 20rpx;
  text-align: center;
}

.form-tips {
  margin-bottom: 30rpx;
  background: linear-gradient(135deg, rgba(255, 149, 0, 0.1), rgba(255, 113, 0, 0.15));
  border-radius: 16rpx;
  padding: 24rpx;
  display: flex;
  align-items: center;
  gap: 16rpx;
  border: 2rpx solid rgba(255, 149, 0, 0.1);
  
  .tips-text {
    font-size: 26rpx;
    color: #666;
    line-height: 1.5;
  }
}
</style> 