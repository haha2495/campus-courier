<!-- 任务留言组件 -->
<template>
  <view class="message-card">
    <view class="card-header">
      <text class="section-title">任务留言</text>
      <text class="message-count" v-if="messages.length > 0">
        ({{ messages.length }})
      </text>
    </view>

    <!-- 暂无留言提示 -->
    <view v-if="messages.length === 0" class="empty-messages">
      <text>暂无留言</text>
    </view>

    <!-- 留言列表 -->
    <view v-else class="message-list">
      <view 
        v-for="message in messages" 
        :key="message.id" 
        class="message-item"
      >
        <!-- 用户信息 -->
        <view class="user-info">
          <image class="avatar" :src="message.userAvatar || '/static/images/default-avatar.png'" mode="aspectFill"></image>
          <text class="username">{{ message.userName }}</text>
          <text class="user-role" :class="message.userRole">
            {{ message.userRole === 'publisher' ? '发布者' : '接单者' }}
          </text>
        </view>

        <view class="message-content">{{ message.content }}</view>
        
        <!-- 留言图片 -->
        <view v-if="message.images && message.images.length > 0" class="message-images">
          <image
            v-for="(image, index) in message.images"
            :key="index"
            :src="image"
            mode="aspectFill"
            @click="handlePreviewImage(image, message.images)"
            @error="handleImageError"
            class="message-image"
          >
            <view class="image-loading" v-if="!image">加载中...</view>
          </image>
        </view>
        
        <view class="message-time">{{ formatTime(message.createTime) }}</view>
      </view>
    </view>

    <!-- 底部留言栏 -->
    <view class="message-bar">
      <view class="input-wrapper">
        <input 
          class="message-input" 
          v-model="messageContent"
          placeholder="输入留言内容..."
          @confirm="handleSendMessage"
        />
        <view class="image-upload" @click="handleChooseImage">
          <u-icon name="camera-fill" color="#666" size="40"></u-icon>
        </view>
      </view>
      <view 
        class="send-btn" 
        :class="{ active: messageContent || selectedImages.length > 0 }" 
        @click="handleSendMessage"
      >
        发送
      </view>
    </view>

    <!-- 已选图片预览 -->
    <view v-if="selectedImages.length > 0" class="selected-images">
      <view 
        v-for="(image, index) in selectedImages" 
        :key="index" 
        class="image-preview"
      >
        <image :src="image" mode="aspectFill"></image>
        <view class="delete-btn" @click="handleDeleteImage(index)">
          <u-icon name="close" color="#fff" size="24"></u-icon>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { addTaskMessage } from '@/api/task'

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

// 选择图片
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
      selectedImages.value = [...selectedImages.value, ...res.tempFilePaths]
    },
    fail: (err) => {
      console.error('选择图片失败:', err)
    }
  })
}

// 删除已选图片
const handleDeleteImage = (index) => {
  selectedImages.value.splice(index, 1)
}

// 发送留言
const handleSendMessage = async () => {
  if (!messageContent.value.trim() && selectedImages.value.length === 0) {
    return
  }

  try {
    // 先上传图片
    const imageUrls = []
    if (selectedImages.value.length > 0) {
      for (const image of selectedImages.value) {
        try {
          const uploadRes = await uploadImage(image)
          imageUrls.push(uploadRes.url)
        } catch (error) {
          console.error('上传图片失败:', error)
          uni.showToast({
            title: '上传图片失败',
            icon: 'none'
          })
          return
        }
      }
    }

    // 发送留言
    const res = await addTaskMessage({
      taskId: props.taskId,
      content: messageContent.value.trim(),
      imageUrls
    })

    if (res.code === 200) {
      uni.showToast({
        title: '发送成功',
        icon: 'success'
      })
      // 清空输入
      messageContent.value = ''
      selectedImages.value = []
      // 通知父组件刷新留言列表
      emit('refresh')
    }
  } catch (error) {
    console.error('发送留言失败:', error)
    uni.showToast({
      title: '发送失败',
      icon: 'none'
    })
  }
}

// 上传图片
const uploadImage = (filePath) => {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: '/api/upload', // 替换为实际的上传接口
      filePath,
      name: 'file',
      success: (res) => {
        const data = JSON.parse(res.data)
        if (data.code === 200) {
          resolve(data.data)
        } else {
          reject(new Error(data.message))
        }
      },
      fail: reject
    })
  })
}
</script>

<style lang="scss" scoped>
.message-card {
  margin: 30rpx;
  background: #fff;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
  overflow: hidden;
  padding-bottom: calc(120rpx + env(safe-area-inset-bottom));
}

.card-header {
  padding: 24rpx 30rpx;
  border-bottom: 2rpx solid #f5f7fa;
  display: flex;
  align-items: center;
  
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
      background-color: #07c160;
      margin-right: 16rpx;
      border-radius: 4rpx;
    }
  }
  
  .message-count {
    font-size: 26rpx;
    color: #999;
    margin-left: 12rpx;
    background: #f5f7fa;
    padding: 4rpx 12rpx;
    border-radius: 20rpx;
  }
}

.empty-messages {
  text-align: center;
  color: #999;
  font-size: 28rpx;
  padding: 60rpx 0;
  background: #fafafa;
  margin: 30rpx;
  border-radius: 16rpx;
}

.message-list {
  padding: 0 30rpx;
  
  .message-item {
    padding: 24rpx 0;
    border-bottom: 2rpx solid #f5f7fa;
    
    &:last-child {
      border-bottom: none;
    }

    .user-info {
      display: flex;
      align-items: center;
      margin-bottom: 16rpx;

      .avatar {
        width: 64rpx;
        height: 64rpx;
        border-radius: 50%;
        margin-right: 16rpx;
      }

      .username {
        font-size: 28rpx;
        color: #333;
        font-weight: 600;
        margin-right: 12rpx;
      }

      .user-role {
        font-size: 24rpx;
        padding: 4rpx 12rpx;
        border-radius: 20rpx;

        &.publisher {
          color: #ff9500;
          background: #fff9f0;
        }

        &.runner {
          color: #07c160;
          background: #f6ffed;
        }
      }
    }
    
    .message-content {
      font-size: 28rpx;
      color: #333;
      line-height: 1.6;
      padding: 16rpx 20rpx;
      background: #f8f8f8;
      border-radius: 16rpx;
    }
    
    .message-images {
      display: flex;
      flex-wrap: wrap;
      gap: 16rpx;
      margin-top: 16rpx;
      padding: 16rpx;
      background: #f8f8f8;
      border-radius: 16rpx;
      
      .message-image {
        width: 180rpx;
        height: 180rpx;
        border-radius: 12rpx;
        box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
        position: relative;
        overflow: hidden;
      }

      .image-loading {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #f5f5f5;
        color: #999;
        font-size: 24rpx;
      }
    }
    
    .message-time {
      font-size: 24rpx;
      color: #999;
      margin-top: 16rpx;
      padding-left: 20rpx;
    }
  }
}

.message-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  padding: 20rpx 30rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  display: flex;
  align-items: center;
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.06);
  
  .input-wrapper {
    flex: 1;
    display: flex;
    align-items: center;
    background: #f5f7fa;
    border-radius: 40rpx;
    margin-right: 20rpx;
    padding-right: 20rpx;

    .message-input {
      flex: 1;
      height: 80rpx;
      padding: 0 30rpx;
      font-size: 28rpx;
    }

    .image-upload {
      padding: 10rpx;
      
      &:active {
        opacity: 0.7;
      }
    }
  }
  
  .send-btn {
    width: 140rpx;
    height: 80rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 40rpx;
    font-size: 30rpx;
    font-weight: 600;
    color: #999;
    background: #f5f7fa;
    transition: all 0.3s;
    
    &.active {
      background: #07c160;
      color: #fff;
    }
  }
}

.selected-images {
  padding: 20rpx 30rpx;
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  background: #f8f8f8;
  
  .image-preview {
    width: 160rpx;
    height: 160rpx;
    position: relative;
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
  }
}
</style> 