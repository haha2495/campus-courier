<!-- 任务留言组件 -->
<template>
  <view class="message-card">
    <view class="card-header">
      <text class="section-title">任务留言</text>
      <view class="message-stats" v-if="messages.length > 0">
        <text class="message-count">{{ messages.length }}</text>
        <text class="message-label">条对话</text>
      </view>
    </view>

    <!-- 暂无留言提示 -->
    <view v-if="messages.length === 0" class="empty-messages">
      <u-empty
        mode="message"
        icon="/static/images/empty-message.png"
        text="还没有人留言哦~"
      >
        <template #bottom>
          <text class="empty-tip">快来发送第一条留言吧！</text>
        </template>
      </u-empty>
    </view>

    <!-- 留言列表 -->
    <scroll-view 
      v-else 
      class="message-list"
      scroll-y
      :scroll-top="scrollTop"
      @scrolltoupper="handleLoadMore"
      refresher-enabled
      :refresher-triggered="isRefreshing"
      @refresherrefresh="handleRefresh"
    >
      <view 
        v-for="(message, index) in messages" 
        :key="message.id" 
        class="message-item"
        :class="{'message-item-highlight': index === messages.length - 1 && isNewMessage}"
      >
        <!-- 用户信息 -->
        <view class="user-info" :class="{'user-info-right': message.userRole === 'publisher'}">
          <image 
            class="avatar" 
            :src="message.userAvatar || '/static/images/default-avatar.png'" 
            mode="aspectFill"
            @click="handleAvatarClick(message)"
          />
          <view class="user-meta">
            <text class="username">{{ message.userName }}</text>
            <view class="user-role" :class="message.userRole">
              {{ message.userRole === 'publisher' ? '发布者' : '接单者' }}
            </view>
          </view>
        </view>

        <!-- 消息气泡 -->
        <view 
          class="message-bubble" 
          :class="{'message-bubble-right': message.userRole === 'publisher'}"
        >
          <view class="message-content" v-if="message.content">
            {{ message.content }}
          </view>
          
          <!-- 图片网格 -->
          <view 
            v-if="message.images && message.images.length > 0" 
            class="image-grid"
            :class="getImageGridClass(message.images.length)"
          >
            <view 
              v-for="(image, imgIndex) in message.images" 
              :key="imgIndex"
              class="grid-item"
              :class="{'grid-item-single': message.images.length === 1}"
              @click="handlePreviewImage(image, message.images)"
            >
              <image 
                :src="image" 
                mode="aspectFill"
                :class="{'single-image': message.images.length === 1}"
              />
              <view class="image-loading" v-if="!image">
                <u-loading-icon mode="circle"></u-loading-icon>
              </view>
            </view>
          </view>
          
          <text class="message-time">{{ formatTime(message.createTime) }}</text>
        </view>
      </view>
    </scroll-view>

    <!-- 底部输入区域 -->
    <view class="message-bar safe-area-bottom">
      <view class="input-wrapper">
        <textarea 
          class="message-input" 
          v-model="messageContent"
          :maxlength="200"
          :show-confirm-bar="false"
          :cursor-spacing="20"
          :auto-height="true"
          placeholder="说点什么吧..."
          @confirm="handleSendMessage"
        />
        <view class="input-tools">
          <view class="char-count" v-if="messageContent">
            {{ messageContent.length }}/200
          </view>
          <view class="tool-item" @click="handleChooseImage">
            <u-icon name="photo" color="#666" size="40"></u-icon>
          </view>
        </view>
      </view>
      <view 
        class="send-btn" 
        :class="{ active: canSend }" 
        @click="handleSendMessage"
      >
        发送
      </view>
    </view>

    <!-- 图片上传预览 -->
    <view v-if="selectedImages.length > 0" class="upload-preview">
      <scroll-view scroll-x class="preview-scroll">
        <view class="preview-list">
          <view 
            v-for="(image, index) in selectedImages" 
            :key="index" 
            class="preview-item"
          >
            <image :src="image" mode="aspectFill" />
            <view class="delete-btn" @click="handleDeleteImage(index)">
              <u-icon name="close" color="#fff" size="24"></u-icon>
            </view>
            <view class="upload-progress" v-if="uploadProgress[index]">
              <u-line-progress 
                :percentage="uploadProgress[index]"
                :show-text="false"
                height="4"
              ></u-line-progress>
            </view>
          </view>
          <view 
            class="add-image" 
            v-if="selectedImages.length < 9"
            @click="handleChooseImage"
          >
            <u-icon name="plus" size="40" color="#999"></u-icon>
          </view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { addTaskMessage } from '@/api/task'
import { uploadApi } from '@/api/upload'

// 接收父组件传递的数据
const props = defineProps({
  taskId: {
    type: [Number, String],
    required: true
  },
  messages: {
    type: Array,
    default: () => []
  }
})

// 定义事件
const emit = defineEmits(['refresh'])

// 响应式数据
const messageContent = ref('')
const selectedImages = ref([])
const scrollTop = ref(0)
const isRefreshing = ref(false)
const isNewMessage = ref(false)
const uploadProgress = ref({})

// 计算属性
const canSend = computed(() => {
  return messageContent.value.trim() || selectedImages.value.length > 0
})

// 获取图片网格类名
const getImageGridClass = (count) => {
  if (count === 1) return 'grid-single'
  if (count === 2) return 'grid-two'
  if (count === 4) return 'grid-four'
  return 'grid-three'
}

// 处理头像点击
const handleAvatarClick = (message) => {
  // 可以添加查看用户信息等功能
}

// 处理加载更多
const handleLoadMore = () => {
  // 实现加载更多历史消息的逻辑
}

// 处理下拉刷新
const handleRefresh = async () => {
  isRefreshing.value = true
  await emit('refresh')
  isRefreshing.value = false
}

// 选择图片优化
const handleChooseImage = () => {
  const maxCount = 9 - selectedImages.value.length
  if (maxCount <= 0) {
    uni.showToast({
      title: '最多只能上传9张图片',
      icon: 'none'
    })
    return
  }

  uni.chooseImage({
    count: maxCount,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: (res) => {
      // 预处理图片
      const newImages = res.tempFilePaths.map(path => {
        // 可以在这里添加图片压缩等处理
        return path
      })
      selectedImages.value = [...selectedImages.value, ...newImages]
    },
    fail: (err) => {
      console.error('选择图片失败:', err)
      uni.showToast({
        title: '选择图片失败',
        icon: 'none'
      })
    }
  })
}

// 发送消息优化
const handleSendMessage = async () => {
  if (!canSend.value) return

  try {
    const imageUrls = []
    if (selectedImages.value.length > 0) {
      uni.showLoading({
        title: '正在处理...',
        mask: true
      })

      try {
        // 并行上传图片并显示进度
        const uploadPromises = selectedImages.value.map((image, index) => {
          return new Promise((resolve, reject) => {
            const upload = uploadApi.upload(image)
            // 模拟上传进度
            let progress = 0
            const interval = setInterval(() => {
              progress += Math.random() * 30
              if (progress > 100) progress = 100
              uploadProgress.value[index] = Math.floor(progress)
            }, 200)

            upload.then(url => {
              clearInterval(interval)
              uploadProgress.value[index] = 100
              resolve(url)
            }).catch(err => {
              clearInterval(interval)
              reject(err)
            })
          })
        })

        const results = await Promise.all(uploadPromises)
        imageUrls.push(...results)
      } catch (error) {
        console.error('上传图片失败:', error)
        uni.showToast({
          title: '上传图片失败',
          icon: 'none'
        })
        return
      } finally {
        uni.hideLoading()
        uploadProgress.value = {}
      }
    }

    const res = await addTaskMessage({
      taskId: props.taskId,
      content: messageContent.value.trim(),
      imageUrls
    })

    if (res.code === 200) {
      // 发送成功动画
      uni.showToast({
        title: '发送成功',
        icon: 'success'
      })
      
      // 清空输入
      messageContent.value = ''
      selectedImages.value = []
      
      // 标记新消息以显示动画
      isNewMessage.value = true
      setTimeout(() => {
        isNewMessage.value = false
      }, 1000)
      
      // 刷新消息列表
      emit('refresh')
      
      // 滚动到底部
      scrollTop.value = 9999
    }
  } catch (error) {
    console.error('发送失败:', error)
    uni.showToast({
      title: '发送失败',
      icon: 'none'
    })
  }
}

// 处理图片加载错误
const handleImageError = (e) => {
  console.error('图片加载失败:', e)
  e.target.src = '/static/images/image-error.png'
}

// 预览图片
const handlePreviewImage = (current, urls) => {
  try {
    if (!Array.isArray(urls) || urls.length === 0) {
      uni.showToast({
        title: '无法预览图片',
        icon: 'none'
      })
      return
    }

    const encodedUrls = urls.map(url => encodeURI(url))
    const encodedCurrent = encodedUrls[urls.indexOf(current)] || encodedUrls[0]

    uni.previewImage({
      current: encodedCurrent,
      urls: encodedUrls,
      fail: (err) => {
        console.error('预览图片失败:', err)
        uni.showToast({
          title: '预览图片失败',
          icon: 'none'
        })
      }
    })
  } catch (error) {
    console.error('预览图片出错:', error)
    uni.showToast({
      title: '预览图片失败',
      icon: 'none'
    })
  }
}

// 删除已选图片
const handleDeleteImage = (index) => {
  selectedImages.value.splice(index, 1)
}

// 格式化时间
const formatTime = (timeString) => {
  if (!timeString) return ''
  const date = new Date(timeString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}
</script>

<style lang="scss" scoped>
.message-card {
  margin: 30rpx;
  background: #fff;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
  overflow: hidden;
  padding-bottom: calc(180rpx + env(safe-area-inset-bottom));
}

.card-header {
  padding: 24rpx 30rpx;
  border-bottom: 2rpx solid #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: space-between;
  
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    display: flex;
    align-items: center;

    &::before {
      content: '';
      width: 8rpx;
      height: 32rpx;
      background: linear-gradient(180deg, #07c160, #10B981);
      margin-right: 16rpx;
      border-radius: 4rpx;
    }
  }
  
  .message-stats {
    display: flex;
    align-items: baseline;
    
    .message-count {
      font-size: 36rpx;
      font-weight: bold;
      color: #07c160;
      font-family: DIN;
    }
    
    .message-label {
      font-size: 24rpx;
      color: #999;
      margin-left: 8rpx;
    }
  }
}

.empty-messages {
  padding: 60rpx 0;
  
  .empty-tip {
    font-size: 26rpx;
    color: #999;
    margin-top: 16rpx;
  }
}

.message-list {
  max-height: 800rpx;
  padding: 30rpx;
}

.message-item {
  margin-bottom: 40rpx;
  opacity: 1;
  transform: translateY(0);
  transition: all 0.3s ease;
  
  &.message-item-highlight {
    animation: slideIn 0.3s ease;
  }
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
  
  &.user-info-right {
    flex-direction: row-reverse;
    
    .user-meta {
      margin-right: 16rpx;
      margin-left: 0;
      align-items: flex-end;
    }
  }
  
  .avatar {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    border: 4rpx solid #fff;
    box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease;
    
    &:active {
      transform: scale(0.95);
    }
  }
  
  .user-meta {
    margin-left: 16rpx;
    display: flex;
    flex-direction: column;
    
    .username {
      font-size: 28rpx;
      color: #333;
      font-weight: 600;
    }
    
    .user-role {
      font-size: 22rpx;
      padding: 2rpx 12rpx;
      border-radius: 20rpx;
      margin-top: 4rpx;
      
      &.publisher {
        color: #ff9500;
        background: #fff9f0;
        border: 2rpx solid rgba(255, 149, 0, 0.2);
      }
      
      &.runner {
        color: #07c160;
        background: #f6ffed;
        border: 2rpx solid rgba(7, 193, 96, 0.2);
      }
    }
  }
}

.message-bubble {
  max-width: 80%;
  margin-left: 96rpx;
  position: relative;
  
  &.message-bubble-right {
    margin-left: auto;
    margin-right: 96rpx;
    
    .message-content {
      background: #07c160;
      color: #fff;
      border-radius: 20rpx 4rpx 20rpx 20rpx;
      
      &::before {
        left: auto;
        right: -16rpx;
        border-left-color: #07c160;
        border-right-color: transparent;
      }
    }
    
    .message-time {
      text-align: right;
    }
  }
}

.message-content {
  background: #f8f8f8;
  padding: 20rpx 24rpx;
  border-radius: 4rpx 20rpx 20rpx 20rpx;
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
  word-break: break-all;
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    left: -16rpx;
    top: 20rpx;
    border: 8rpx solid transparent;
    border-right-color: #f8f8f8;
  }
}

.image-grid {
  margin-top: 16rpx;
  border-radius: 16rpx;
  overflow: hidden;
  
  &.grid-single {
    width: 400rpx;
    
    .grid-item {
      width: 100%;
      padding-bottom: 100%;
      
      image {
        object-fit: contain;
      }
    }
  }
  
  &.grid-two {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 8rpx;
    width: 400rpx;
  }
  
  &.grid-three, &.grid-four {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 8rpx;
    width: 480rpx;
  }
  
  .grid-item {
    position: relative;
    width: 100%;
    padding-bottom: 100%;
    
    image {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      object-fit: cover;
      border-radius: 8rpx;
    }
  }
}

.message-time {
  font-size: 22rpx;
  color: #999;
  margin-top: 8rpx;
  padding: 0 8rpx;
}

.message-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: flex-end;
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.06);
  z-index: 100;
  
  .input-wrapper {
    flex: 1;
    background: #f5f7fa;
    border-radius: 20rpx;
    margin-right: 20rpx;
    padding: 16rpx;
    
    .message-input {
      width: 100%;
      min-height: 72rpx;
      max-height: 200rpx;
      font-size: 28rpx;
      line-height: 1.5;
    }
    
    .input-tools {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-top: 12rpx;
      
      .char-count {
        font-size: 24rpx;
        color: #999;
      }
      
      .tool-item {
        padding: 8rpx;
        margin-left: 16rpx;
        border-radius: 50%;
        
        &:active {
          background: rgba(0, 0, 0, 0.05);
        }
      }
    }
  }
  
  .send-btn {
    min-width: 120rpx;
    height: 72rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 36rpx;
    font-size: 28rpx;
    font-weight: 600;
    color: #999;
    background: #f5f7fa;
    transition: all 0.3s ease;
    
    &.active {
      background: #07c160;
      color: #fff;
      box-shadow: 0 4rpx 12rpx rgba(7, 193, 96, 0.3);
      
      &:active {
        transform: scale(0.95);
      }
    }
  }
}

.upload-preview {
  position: fixed;
  bottom: calc(120rpx + env(safe-area-inset-bottom));
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 0;
  border-top: 2rpx solid #f5f7fa;
  z-index: 99;
  
  .preview-scroll {
    white-space: nowrap;
    padding: 0 30rpx;
  }
  
  .preview-list {
    display: inline-flex;
    gap: 20rpx;
  }
  
  .preview-item {
    position: relative;
    width: 140rpx;
    height: 140rpx;
    border-radius: 12rpx;
    overflow: hidden;
    
    image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .delete-btn {
      position: absolute;
      top: 8rpx;
      right: 8rpx;
      width: 40rpx;
      height: 40rpx;
      background: rgba(0, 0, 0, 0.5);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      
      &:active {
        background: rgba(0, 0, 0, 0.7);
      }
    }
    
    .upload-progress {
      position: absolute;
      left: 8rpx;
      right: 8rpx;
      bottom: 8rpx;
      background: rgba(0, 0, 0, 0.3);
      border-radius: 4rpx;
      overflow: hidden;
    }
  }
  
  .add-image {
    width: 140rpx;
    height: 140rpx;
    border-radius: 12rpx;
    border: 2rpx dashed #ddd;
    display: flex;
    align-items: center;
    justify-content: center;
    
    &:active {
      background: #f5f7fa;
    }
  }
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.safe-area-bottom {
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
}
</style> 