<template>
  <view class="task-detail-container">
    <!-- 加载状态 -->
    <view v-if="isLoading" class="loading-container">
      <u-loading-icon mode="circle" size="36"></u-loading-icon>
      <text class="loading-text">加载中...</text>
    </view>

    <template v-else>
      <!-- 任务基本信息卡片 -->
      <view class="detail-card">
        <!-- 任务状态标签 -->
        <view class="status-tag" :style="getStatusStyle(taskDetail?.task?.status)">
          {{ getStatusText(taskDetail?.task?.status) }}
        </view>

        <!-- 任务标题和类型 -->
        <view class="task-header">
          <view class="task-type">
            <image class="type-icon" :src="taskDetail?.taskType?.icon" mode="aspectFit"></image>
            <text>{{ taskDetail?.taskType?.typeName }}</text>
          </view>
          <view class="task-title">{{ taskDetail?.task?.title }}</view>
        </view>

        <!-- 任务描述 -->
        <view class="task-desc">{{ taskDetail?.task?.description }}</view>

        <!-- 地址信息 -->
        <view class="address-info" v-if="taskDetail?.task?.pickupAddress || taskDetail?.task?.deliveryAddress">
          <view class="address-item" v-if="taskDetail?.task?.pickupAddress">
            <view class="address-icon pickup-icon">取</view>
            <text class="address-value">{{ taskDetail?.task?.pickupAddress }}</text>
          </view>
          <view class="address-item" v-if="taskDetail?.task?.deliveryAddress">
            <view class="address-icon delivery-icon">送</view>
            <text class="address-value">{{ taskDetail?.task?.deliveryAddress }}</text>
          </view>
        </view>

        <!-- 任务规格信息 -->
        <view class="task-specs">
          <view class="spec-item" v-if="taskDetail?.task?.estimatedWeight">
            <text class="spec-label">预估重量：</text>
            <text class="spec-value">{{ taskDetail?.task?.estimatedWeight }}kg</text>
          </view>
          <view class="spec-item" v-if="taskDetail?.task?.estimatedSize">
            <text class="spec-label">预估大小：</text>
            <text class="spec-value">{{ taskDetail?.task?.estimatedSize }}</text>
          </view>
        </view>

        <!-- 任务金额信息 -->
        <view class="task-reward">
          <text class="reward-label">任务金额：</text>
          <view class="reward-value">
            <text class="currency">¥</text>
            <text class="amount">{{ taskDetail?.task?.reward }}</text>
            <text v-if="taskDetail?.task?.urgent" class="urgent-tag">加急</text>
            <text v-if="taskDetail?.task?.urgentReward" class="urgent-reward">+{{ taskDetail?.task?.urgentReward }}</text>
          </view>
        </view>

        <!-- 联系人信息 -->
        <view class="contact-info">
          <view class="contact-item">
            <text class="contact-label">联系人：</text>
            <text class="contact-value">{{ taskDetail?.task?.contactName }}</text>
          </view>
          <view class="contact-item">
            <text class="contact-label">联系电话：</text>
            <text class="contact-value">{{ taskDetail?.task?.contactPhone }}</text>
            <view class="call-btn" @click="makePhoneCall(taskDetail?.task?.contactPhone)">
              <u-icon name="phone-fill" color="#07c160" size="28"></u-icon>
            </view>
          </view>
        </view>

        <!-- 时间信息 -->
        <view class="time-info">
          <view class="time-item">
            <text class="time-label">发布时间：</text>
            <text class="time-value">{{ formatTime(taskDetail?.task?.createTime) }}</text>
          </view>
          <view class="time-item">
            <text class="time-label">期望送达：</text>
            <text class="time-value">{{ formatTime(taskDetail?.task?.expectedFinishTime) }}</text>
          </view>
        </view>

        <!-- 任务图片 -->
        <view class="task-images" v-if="taskDetail?.task?.images?.length">
          <text class="images-title">相关图片</text>
          <view class="images-grid">
            <image
              v-for="(image, index) in taskDetail.task.images"
              :key="index"
              :src="image"
              mode="aspectFill"
              @click="previewImage(image, taskDetail.task.images)"
            ></image>
          </view>
        </view>

        <!-- 备注信息 -->
        <view class="task-notes" v-if="taskDetail?.task?.notes">
          <text class="notes-title">备注说明</text>
          <text class="notes-content">{{ taskDetail.task.notes }}</text>
        </view>
      </view>

      <!-- 留言列表 -->
      <view class="messages-card">
        <view class="messages-header">
          <text class="messages-title">任务留言</text>
          <text class="messages-count">({{ taskDetail?.messages?.length || 0 }})</text>
        </view>
        
        <!-- 暂无留言提示 -->
        <view v-if="!taskDetail?.messages?.length" class="no-messages">
          <u-empty mode="message" icon="/static/images/no-messages.png" text="暂无留言"></u-empty>
        </view>

        <!-- 留言列表 -->
        <view v-else class="messages-list">
          <view 
            v-for="message in taskDetail.messages" 
            :key="message.id" 
            class="message-item"
          >
            <view class="message-content">{{ message.content }}</view>
            <!-- 留言图片 -->
            <view class="message-images" v-if="message.images?.length">
              <image
                v-for="(image, index) in message.images"
                :key="index"
                :src="image"
                mode="aspectFill"
                @click="previewImage(image, message.images)"
              ></image>
            </view>
            <view class="message-time">{{ formatTime(message.createTime) }}</view>
          </view>
        </view>
      </view>
    </template>

    <!-- 底部操作栏 -->
    <view class="bottom-bar">
      <!-- 留言输入框 -->
      <view class="message-input">
        <u-input
          v-model="messageContent"
          type="text"
          placeholder="输入留言内容"
          :border="false"
          :clearable="true"
        ></u-input>
      </view>
      <!-- 发送按钮 -->
      <view class="send-btn" @click="sendMessage">
        发送
      </view>
    </view>
  </view>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getTaskDetail } from '@/api/task'
import { TASK_STATUS } from '@/constants/taskStatus'

// 页面数据
const taskDetail = ref(null)
const isLoading = ref(true)
const messageContent = ref('')

// 获取任务详情
const fetchTaskDetail = async (taskId) => {
  try {
    isLoading.value = true
    const res = await getTaskDetail(taskId)
    taskDetail.value = res.data
  } catch (error) {
    console.error('获取任务详情失败：', error)
    uni.showToast({
      title: '获取任务详情失败',
      icon: 'none'
    })
  } finally {
    isLoading.value = false
  }
}

// 格式化时间
const formatTime = (timeString) => {
  if (!timeString) return '';
  const date = new Date(timeString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
}

// 获取任务状态文本
const getStatusText = (status) => {
  return TASK_STATUS.getStatusText(status)
}

// 获取任务状态样式
const getStatusStyle = (status) => {
  const colors = TASK_STATUS.getStatusColor(status)
  return {
    color: colors.text,
    backgroundColor: colors.background
  }
}

// 拨打电话
const makePhoneCall = (phone) => {
  if (!phone) return
  uni.makePhoneCall({
    phoneNumber: phone
  })
}

// 预览图片
const previewImage = (current, urls) => {
  uni.previewImage({
    current,
    urls
  })
}

// 发送留言
const sendMessage = async () => {
  if (!messageContent.value.trim()) {
    uni.showToast({
      title: '请输入留言内容',
      icon: 'none'
    })
    return
  }

  // TODO: 调用发送留言API
  uni.showToast({
    title: '留言功能开发中',
    icon: 'none'
  })
}

// 页面加载时获取任务详情
onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const taskId = currentPage.options.id
  
  if (taskId) {
    fetchTaskDetail(taskId)
  } else {
    uni.showToast({
      title: '任务ID不存在',
      icon: 'none'
    })
  }
})
</script>

<style lang="scss" scoped>
.task-detail-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-bottom: 120rpx; // 为底部操作栏留出空间
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;

  .loading-text {
    font-size: 28rpx;
    color: #999;
    margin-top: 20rpx;
  }
}

.detail-card {
  background-color: #ffffff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 20rpx;
  position: relative;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

  .status-tag {
    position: absolute;
    top: 30rpx;
    right: 30rpx;
    padding: 6rpx 20rpx;
    border-radius: 20rpx;
    font-size: 24rpx;
    font-weight: 500;
  }
}

.task-header {
  margin-bottom: 20rpx;

  .task-type {
    display: flex;
    align-items: center;
    margin-bottom: 10rpx;

    .type-icon {
      width: 36rpx;
      height: 36rpx;
      margin-right: 10rpx;
    }

    text {
      font-size: 26rpx;
      color: #666;
    }
  }

  .task-title {
    font-size: 36rpx;
    font-weight: 600;
    color: #333;
  }
}

.task-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.5;
  margin-bottom: 30rpx;
}

.address-info {
  margin-bottom: 30rpx;

  .address-item {
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;

    .address-icon {
      width: 36rpx;
      height: 36rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20rpx;
      color: white;
      margin-right: 10rpx;

      &.pickup-icon {
        background-color: #1989fa;
      }

      &.delivery-icon {
        background-color: #07c160;
      }
    }

    .address-value {
      font-size: 28rpx;
      color: #333;
    }
  }
}

.task-specs {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 30rpx;

  .spec-item {
    margin-right: 30rpx;
    margin-bottom: 10rpx;

    .spec-label {
      font-size: 26rpx;
      color: #999;
    }

    .spec-value {
      font-size: 26rpx;
      color: #333;
      font-weight: 500;
    }
  }
}

.task-reward {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;

  .reward-label {
    font-size: 28rpx;
    color: #999;
  }

  .reward-value {
    display: flex;
    align-items: center;

    .currency {
      font-size: 24rpx;
      color: #ff9500;
      margin-right: 4rpx;
    }

    .amount {
      font-size: 40rpx;
      color: #ff9500;
      font-weight: 600;
    }

    .urgent-tag {
      font-size: 22rpx;
      color: #fff;
      background-color: #ff4d4f;
      padding: 4rpx 12rpx;
      border-radius: 12rpx;
      margin-left: 12rpx;
    }

    .urgent-reward {
      font-size: 28rpx;
      color: #ff4d4f;
      margin-left: 8rpx;
      font-weight: 500;
    }
  }
}

.contact-info {
  margin-bottom: 30rpx;

  .contact-item {
    display: flex;
    align-items: center;
    margin-bottom: 10rpx;

    .contact-label {
      font-size: 26rpx;
      color: #999;
      width: 140rpx;
    }

    .contact-value {
      font-size: 26rpx;
      color: #333;
      flex: 1;
    }

    .call-btn {
      padding: 10rpx;

      &:active {
        opacity: 0.7;
      }
    }
  }
}

.time-info {
  margin-bottom: 30rpx;

  .time-item {
    display: flex;
    margin-bottom: 10rpx;

    .time-label {
      font-size: 26rpx;
      color: #999;
      width: 140rpx;
    }

    .time-value {
      font-size: 26rpx;
      color: #333;
    }
  }
}

.task-images {
  margin-bottom: 30rpx;

  .images-title {
    font-size: 28rpx;
    color: #333;
    font-weight: 500;
    margin-bottom: 16rpx;
  }

  .images-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20rpx;

    image {
      width: 100%;
      height: 200rpx;
      border-radius: 12rpx;
    }
  }
}

.task-notes {
  .notes-title {
    font-size: 28rpx;
    color: #333;
    font-weight: 500;
    margin-bottom: 16rpx;
  }

  .notes-content {
    font-size: 26rpx;
    color: #666;
    line-height: 1.5;
  }
}

.messages-card {
  background-color: #ffffff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

  .messages-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;

    .messages-title {
      font-size: 30rpx;
      color: #333;
      font-weight: 500;
    }

    .messages-count {
      font-size: 26rpx;
      color: #999;
      margin-left: 8rpx;
    }
  }

  .no-messages {
    padding: 40rpx 0;
  }

  .messages-list {
    .message-item {
      padding: 20rpx 0;
      border-bottom: 1px solid #f5f5f5;

      &:last-child {
        border-bottom: none;
      }

      .message-content {
        font-size: 28rpx;
        color: #333;
        line-height: 1.5;
        margin-bottom: 16rpx;
      }

      .message-images {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 20rpx;
        margin-bottom: 16rpx;

        image {
          width: 100%;
          height: 180rpx;
          border-radius: 12rpx;
        }
      }

      .message-time {
        font-size: 24rpx;
        color: #999;
      }
    }
  }
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.05);
  /* 适配底部安全区域 */
  padding-bottom: constant(safe-area-inset-bottom);
  padding-bottom: env(safe-area-inset-bottom);

  .message-input {
    flex: 1;
    margin-right: 20rpx;
    background-color: #f5f7fa;
    border-radius: 36rpx;
    padding: 0 20rpx;
  }

  .send-btn {
    width: 120rpx;
    height: 72rpx;
    background-color: #07c160;
    color: #ffffff;
    font-size: 28rpx;
    border-radius: 36rpx;
    display: flex;
    align-items: center;
    justify-content: center;

    &:active {
      opacity: 0.8;
    }
  }
}
</style> 