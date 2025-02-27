<!-- 任务留言组件 -->
<template>
  <view class="message-container">
    <!-- 消息列表区域 -->
    <scroll-view
        class="message-list"
        scroll-y
        :scroll-into-view="lastMessageId"
        @scrolltoupper="loadMoreMessages"
    >
      <!-- 加载更多提示 -->
      <view v-if="hasMore" class="load-more">
        <u-loading-icon></u-loading-icon>
        <text class="load-text">加载更多...</text>
      </view>

      <!-- 消息列表 -->
      <view class="messages-wrapper">
        <template v-for="(messageDTO, index) in mergedMessages" :key="messageDTO.message.id">
          <!-- 时间分割线 -->
          <view v-if="shouldShowTimeDivider(messageDTO, index)" class="time-divider">
            <text class="time-text">{{ formatTime(messageDTO.message.createTime) }}</text>
          </view>

          <view
              :id="'msg-' + messageDTO.message.id"
              class="message-item"
              :class="{ 
                'message-right': isPublisher(messageDTO),
                'message-sending': isTemp(messageDTO)
              }"
          >
            <!-- 头像或首字母头像 -->
            <view class="avatar-wrapper">
              <image
                  v-if="messageDTO.student.avatar"
                  class="avatar"
                  :src="messageDTO.student.avatar"
                  mode="aspectFill"
                  @click="handleAvatarClick(messageDTO.student)"
              ></image>
              <view
                  v-else
                  class="avatar-letter"
                  :style="{ backgroundColor: getAvatarColor(messageDTO.student.name) }"
                  @click="handleAvatarClick(messageDTO.student)"
              >
                {{ getNameFirstLetter(messageDTO.student.name) }}
              </view>
            </view>

            <!-- 消息内容 -->
            <view class="message-content">
              <!-- 用户名和角色标签 -->
              <view class="user-info">
                <text class="username">{{ messageDTO.student.name }}</text>
                <text class="role-tag" :class="{ 'publisher-tag': isPublisher(messageDTO) }">
                  {{ isPublisher(messageDTO) ? '发布者' : '接单者' }}
                </text>
              </view>

              <!-- 文本内容 -->
              <view class="message-bubble" v-if="messageDTO.message.content">
                {{ messageDTO.message.content }}
              </view>

              <!-- 图片内容 -->
              <view
                  v-if="messageDTO.message.images && messageDTO.message.images.length > 0"
                  class="image-grid"
                  :class="{ 'single-image': messageDTO.message.images.length === 1 }"
              >
                <image
                    v-for="(image, imgIndex) in messageDTO.message.images"
                    :key="imgIndex"
                    :src="image"
                    mode="aspectFill"
                    class="message-image"
                    @click="previewImage(image, messageDTO.message.images)"
                ></image>
              </view>

              <!-- 发送状态 -->
              <view v-if="isTemp(messageDTO)" class="sending-status">发送中...</view>
            </view>
          </view>
        </template>
      </view>
    </scroll-view>

    <!-- 输入区域 -->
    <view class="input-area" :style="{ bottom: keyboardHeight + 'px' }">
      <view class="input-wrapper">
        <input
            class="message-input"
            v-model="inputContent"
            type="text"
            confirm-type="send"
            :disabled="isSending"
            :cursor-spacing="20"
            @focus="handleInputFocus"
            @blur="handleInputBlur"
            @confirm="sendMessage"
            placeholder="输入消息..."
        />
        <view class="action-buttons">
          <view class="image-button" @click="chooseImage" :class="{ 'disabled': isSending }">
            <u-icon name="photo" size="48rpx" :color="isSending ? '#999' : '#666'"></u-icon>
          </view>
          <button
              class="send-button"
              :class="{ 
                'send-button-active': canSend,
                'send-button-disabled': isSending
              }"
              @click="sendMessage"
          >{{ isSending ? '发送中...' : '发送' }}
          </button>
        </view>
      </view>

      <!-- 图片预览区域 -->
      <view v-if="selectedImages.length > 0" class="preview-area">
        <view
            v-for="(image, index) in selectedImages"
            :key="index"
            class="preview-item"
        >
          <image
              :src="image"
              mode="aspectFill"
              class="preview-image"
          ></image>
          <view class="delete-button" @click="removeImage(index)" v-if="!isSending">
            <u-icon name="close" color="#fff" size="24rpx"></u-icon>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import {computed, nextTick, ref, watch} from 'vue'
import {addTaskMessage} from '@/api/task'

const props = defineProps({
  taskId: {
    type: [Number, String],
    required: true
  },
  messages: {
    type: Array,
    default: () => []
  },
  task: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['refresh'])

// 输入内容
const inputContent = ref('')
// 键盘高度
const keyboardHeight = ref(0)
// 选中的图片
const selectedImages = ref([])
// 是否有更多消息
const hasMore = ref(false)
// 是否正在发送消息
const isSending = ref(false)

// 本地消息列表
const localMessages = ref([])

// 合并后的消息列表
const mergedMessages = computed(() => {
  return [...props.messages, ...localMessages.value]
})

// 最后一条消息的ID，用于滚动定位
const lastMessageId = computed(() => {
  if (props.messages && props.messages.length > 0) {
    const lastMessage = props.messages[props.messages.length - 1]
    return 'msg-' + lastMessage.message.id
  }
  return ''
})

// 判断是否可以发送消息
const canSend = computed(() => {
  return !isSending.value && (inputContent.value.trim() || selectedImages.value.length > 0)
})

// 判断是否是发布者
const isPublisher = (messageDTO) => {
  return messageDTO?.message?.userId === props.task?.publisherId
}

// 判断是否是临时消息
const isTemp = (messageDTO) => {
  return typeof messageDTO?.message?.id === 'string' && messageDTO.message.id.includes('temp_')
}

// 处理头像点击
const handleAvatarClick = (student) => {
  uni.showToast({
    title: `${student.name} (${student.studentNo})`,
    icon: 'none'
  })
}

// 处理输入框获得焦点
const handleInputFocus = (e) => {
  keyboardHeight.value = e.detail.height || 0
}

// 处理输入框失去焦点
const handleInputBlur = () => {
  keyboardHeight.value = 0
}

// 选择图片
const chooseImage = async () => {
  if (isSending.value) return
  
  try {
    const res = await uni.chooseImage({
      count: 9 - selectedImages.value.length,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera']
    })
    selectedImages.value.push(...res.tempFilePaths)
  } catch (e) {
    console.error('选择图片失败:', e)
  }
}

// 移除选中的图片
const removeImage = (index) => {
  if (isSending.value) return
  selectedImages.value.splice(index, 1)
}

// 预览图片
const previewImage = (current, urls) => {
  uni.previewImage({
    current,
    urls
  })
}

// 发送消息
const sendMessage = async () => {
  if (!canSend.value) return
  
  const content = inputContent.value.trim()
  const images = [...selectedImages.value]
  
  try {
    isSending.value = true
    
    // 先清空输入内容和已选图片
    inputContent.value = ''
    selectedImages.value = []
    
    // 上传图片（如果有）
    let imageUrls = []
    if (images.length > 0) {
      imageUrls = await Promise.all(images.map(async (imagePath) => {
        try {
          const uploadRes = await uni.uploadFile({
            url: '/api/upload',
            filePath: imagePath,
            name: 'file',
          })
          const result = JSON.parse(uploadRes.data)
          return result.data
        } catch (error) {
          console.error('图片上传失败:', error)
          throw new Error('图片上传失败')
        }
      }))
    }
    
    // 调用发送消息接口
    await addTaskMessage({
      taskId: props.taskId,
      content: content,
      imageUrls: imageUrls
    })
    
    // 静默刷新消息列表
    emit('refresh')
    
  } catch (e) {
    console.error('发送消息失败:', e)
    uni.showToast({
      title: '发送失败',
      icon: 'error'
    })
  } finally {
    isSending.value = false
  }
}

// 滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  if (lastMessageId.value) {
    uni.pageScrollTo({
      selector: `#${lastMessageId.value}`,
      duration: 100
    })
  }
}

// 加载更多消息
const loadMoreMessages = () => {
  if (hasMore.value && !isSending.value) {
    emit('refresh')
  }
}

// 监听消息列表变化，自动滚动到底部
watch(() => props.messages, () => {
  scrollToBottom()
}, {deep: true})

// 格式化时间
const formatTime = (timeString) => {
  if (!timeString) return ''
  const date = new Date(timeString)
  const now = new Date()
  const diff = now - date
  
  // 如果是今天的消息，只显示时间
  if (date.toDateString() === now.toDateString()) {
    return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  }
  
  // 如果是昨天的消息
  const yesterday = new Date(now)
  yesterday.setDate(yesterday.getDate() - 1)
  if (date.toDateString() === yesterday.toDateString()) {
    return '昨天 ' + date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  }
  
  // 其他日期显示完整日期和时间
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 判断是否需要显示时间分割线
const shouldShowTimeDivider = (currentMessage, index) => {
  if (index === 0) return true
  
  const currentTime = new Date(currentMessage.message.createTime)
  const prevTime = new Date(mergedMessages.value[index - 1].message.createTime)
  
  // 如果两条消息相差超过5分钟，显示时间分割线
  return (currentTime - prevTime) > 5 * 60 * 1000
}

// 获取姓名首字母
const getNameFirstLetter = (name) => {
  if (!name) return '?'
  return name.charAt(0).toUpperCase()
}

// 根据姓名生成头像背景色
const getAvatarColor = (name) => {
  if (!name) return '#cccccc'
  const colors = [
    '#f56a00', '#7265e6', '#ffbf00', '#00a2ae',
    '#712fd1', '#04be02', '#2f54eb', '#fa541c'
  ]
  const index = Array.from(name).reduce((acc, char) => acc + char.charCodeAt(0), 0)
  return colors[index % colors.length]
}
</script>

<style lang="scss" scoped>
.message-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f5f5;
}

.message-list {
  flex: 1;
  padding: 20rpx;
  box-sizing: border-box;
  overflow-y: auto;
}

.messages-wrapper {
  padding-bottom: 120rpx;
}

.message-item {
  display: flex;
  margin-bottom: 30rpx;
  align-items: flex-start;

  &.message-right {
    flex-direction: row-reverse;

    .message-content {
      margin-left: 0;
      margin-right: 20rpx;
      align-items: flex-end;

      .message-bubble {
        background-color: #007AFF;
        color: #fff;
        border-radius: 20rpx 4rpx 20rpx 20rpx;
      }

      .image-grid {
        justify-content: flex-end;
      }
    }
  }
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background-color: #eee;
}

.message-content {
  margin-left: 20rpx;
  max-width: 70%;
  display: flex;
  flex-direction: column;
}

.user-info {
  margin-bottom: 8rpx;
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.username {
  font-size: 26rpx;
  color: #666;
}

.role-tag {
  font-size: 22rpx;
  padding: 2rpx 10rpx;
  border-radius: 8rpx;
  background-color: #e8e8e8;
  color: #666;

  &.publisher-tag {
    background-color: rgba(0, 122, 255, 0.1);
    color: #007AFF;
  }
}

.message-bubble {
  padding: 20rpx;
  background-color: #fff;
  border-radius: 4rpx 20rpx 20rpx 20rpx;
  font-size: 28rpx;
  line-height: 1.5;
  word-break: break-all;
  max-width: 100%;
  box-sizing: border-box;

  &:empty {
    display: none;
  }
}

.image-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
  margin-top: 10rpx;

  &.single-image {
    .message-image {
      width: 400rpx;
      height: auto;
      max-height: 600rpx;
      background-color: #f0f0f0;
    }
  }

  .message-image {
    width: 200rpx;
    height: 200rpx;
    border-radius: 8rpx;
    background-color: #f0f0f0;
  }
}

.message-time {
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
}

.input-area {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #fff;
  padding: 20rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.input-wrapper {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.message-input {
  flex: 1;
  height: 72rpx;
  background-color: #f5f5f5;
  border-radius: 36rpx;
  padding: 0 30rpx;
  font-size: 28rpx;
}

.action-buttons {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.image-button {
  padding: 10rpx;
}

.send-button {
  background-color: #007AFF;
  color: #fff;
  padding: 0 30rpx;
  height: 72rpx;
  line-height: 72rpx;
  border-radius: 36rpx;
  font-size: 28rpx;

  &.send-button-active {
    opacity: 0.8;
  }
}

.preview-area {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  padding-top: 20rpx;
}

.preview-item {
  position: relative;
  width: 140rpx;
  height: 140rpx;
}

.preview-image {
  width: 100%;
  height: 100%;
  border-radius: 8rpx;
}

.delete-button {
  position: absolute;
  top: -10rpx;
  right: -10rpx;
  width: 36rpx;
  height: 36rpx;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.load-more {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20rpx 0;
  color: #999;
  font-size: 24rpx;

  .load-text {
    margin-left: 10rpx;
  }
}

.message-sending {
  opacity: 0.8;
}

.message-status {
  display: flex;
  align-items: center;
  gap: 10rpx;
  margin-top: 8rpx;
}

.sending-status {
  font-size: 24rpx;
  color: #999;
}

.send-button-disabled {
  opacity: 0.6;
  background-color: #999;
}

.image-button.disabled {
  opacity: 0.6;
  pointer-events: none;
}

.time-divider {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 30rpx 0;
  
  .time-text {
    font-size: 24rpx;
    color: #999;
    background-color: rgba(0, 0, 0, 0.1);
    padding: 4rpx 16rpx;
    border-radius: 24rpx;
  }
}

.avatar-wrapper {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  overflow: hidden;
}

.avatar-letter {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  color: #fff;
  font-weight: bold;
}

.message-item {
  margin-bottom: 20rpx;
  
  &.message-right {
    .avatar-wrapper {
      margin-left: 20rpx;
      margin-right: 0;
    }
  }
}

.message-content {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 16rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
  
  .user-info {
    margin-bottom: 8rpx;
  }
}
</style> 