<!-- 纠纷提交组件 -->
<template>
  <u-popup
    v-model:show="localShow"
    mode="bottom"
    round="20"
    @close="handleClose"
    :safe-area-inset-bottom="true"
    :customStyle="{
      maxHeight: '92vh',
      borderRadius: '24rpx 24rpx 0 0',
      overflow: 'hidden'
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
      <scroll-view 
        class="dispute-content" 
        scroll-y 
        :scroll-top="scrollTop"
        @scroll="handleScroll"
        refresher-enabled
        :refresher-triggered="isRefreshing"
        @refresherrefresh="onRefresh"
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
      </scroll-view>

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
const isRefreshing = ref(false)

const handleScroll = (e) => {
  scrollTop.value = e.detail.scrollTop
}

const onRefresh = async () => {
  isRefreshing.value = true
  await new Promise(resolve => setTimeout(resolve, 1000))
  isRefreshing.value = false
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
  height: 92vh;
  background: linear-gradient(to bottom, #ffffff, #fafafa);
}

.dispute-header {
  padding: 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 2rpx solid rgba(0, 0, 0, 0.05);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  position: relative;
  z-index: 1;
  
  .title {
    font-size: 36rpx;
    font-weight: 600;
    color: #333;
    letter-spacing: 1px;
  }
  
  .close-btn {
    width: 64rpx;
    height: 64rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    background: #f5f5f5;
    transition: all 0.3s;
    
    &:active {
      transform: scale(0.9);
      background: #e8e8e8;
    }
  }
}

.dispute-content {
  flex: 1;
  padding: 30rpx;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.form-section {
  margin-bottom: 40rpx;
  background: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
  
  &.type-section {
    background: transparent;
    box-shadow: none;
    padding: 0;
  }
  
  .section-title {
    font-size: 30rpx;
    font-weight: 600;
    color: #333;
    margin-bottom: 24rpx;
    display: inline-block;
    
    &.required::before {
      content: '*';
      color: #ff4d4f;
      margin-right: 4rpx;
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
  gap: 20rpx;
  
  .type-item {
    background: #ffffff;
    border-radius: 16rpx;
    padding: 24rpx;
    display: flex;
    align-items: center;
    gap: 16rpx;
    transition: all 0.3s;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
    position: relative;
    overflow: hidden;
    
    &::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: linear-gradient(135deg, rgba(255, 149, 0, 0.1), rgba(255, 113, 0, 0.1));
      opacity: 0;
      transition: opacity 0.3s;
    }
    
    .type-icon {
      width: 80rpx;
      height: 80rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f7fa;
      border-radius: 50%;
      transition: all 0.3s;
      margin-right: 16rpx;
      
      :deep(.u-icon) {
        display: flex;
        align-items: center;
        justify-content: center;
      }
    }
    
    .type-text {
      flex: 1;
      font-size: 28rpx;
      color: #333;
      transition: all 0.3s;
    }
    
    .type-check {
      opacity: 0;
      transform: scale(0);
      transition: all 0.3s;
    }
    
    &.active {
      background: #fff8f0;
      border: 2rpx solid #ff9500;
      
      &::after {
        opacity: 1;
      }
      
      .type-icon {
        background: rgba(255, 149, 0, 0.15);
        transform: scale(1.05);
      }
      
      .type-text {
        color: #ff9500;
        font-weight: 500;
      }
      
      .type-check {
        opacity: 1;
        transform: scale(1);
      }
    }
    
    &:active {
      transform: scale(0.98);
    }
  }
}

.input-wrapper {
  background: #f8f9fa;
  border-radius: 12rpx;
  padding: 4rpx 20rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s;
  
  &:focus-within {
    border-color: #ff9500;
    background: #fff8f0;
  }
}

.textarea-wrapper {
  background: #f8f9fa;
  border-radius: 12rpx;
  padding: 20rpx;
  position: relative;
  border: 2rpx solid transparent;
  transition: all 0.3s;
  
  &:focus-within {
    border-color: #ff9500;
    background: #fff8f0;
  }
  
  .content-textarea {
    width: 100%;
    height: 240rpx;
    font-size: 28rpx;
    line-height: 1.6;
  }
  
  .word-count {
    position: absolute;
    right: 20rpx;
    bottom: 20rpx;
    font-size: 24rpx;
    color: #999;
    
    &.word-limit {
      color: #ff4d4f;
    }
  }
}

.upload-wrapper {
  margin: 20rpx 0;
}

.upload-tip {
  font-size: 24rpx;
  color: #999;
  margin-top: 16rpx;
  display: block;
}

.form-tips {
  display: flex;
  align-items: center;
  gap: 12rpx;
  padding: 24rpx;
  background: #fff8f0;
  border-radius: 12rpx;
  margin-bottom: 100rpx;
  
  .tips-text {
    font-size: 26rpx;
    color: #ff9500;
    flex: 1;
  }
}

.dispute-footer {
  padding: 30rpx;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-top: 2rpx solid rgba(0, 0, 0, 0.05);
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1;
  
  .submit-text {
    font-size: 32rpx;
    font-weight: 500;
    color: #ffffff;
  }
}

/* 优化滚动条样式 */
::-webkit-scrollbar {
  width: 0;
  height: 0;
  background: transparent;
}
</style> 