<template>
  <view class="task-detail-container">
    <!-- 加载状态 -->
    <view v-if="loading" class="loading-state">
      <u-loading-icon mode="circle"></u-loading-icon>
      <text>努力加载中...</text>
    </view>

    <template v-else>
      <!-- 任务状态标签 -->
      <view class="status-tag" :style="getStatusStyle(taskDetail.task.status)">
        {{ getStatusText(taskDetail.task.status) }}
      </view>

      <!-- 基本信息卡片 -->
      <view class="detail-card">
        <view class="card-header">
          <view class="type-info">
            <!-- 加急标记 -->
            <view v-if="taskDetail.task.urgent" class="urgent-tag">
              <text>急</text>
            </view>
            <image class="type-icon" :src="taskDetail.taskType.icon" mode="aspectFit"></image>
            <text class="type-name">{{ taskDetail.taskType.typeName }}</text>
          </view>
        </view>

        <view class="card-title">{{ taskDetail.task.title }}</view>
        <view class="card-desc">{{ taskDetail.task.description }}</view>

        <!-- 地址信息 -->
        <view class="info-section">
          <view class="section-title">配送信息</view>
          <view class="info-content">
            <view class="info-item address-item">
              <text class="info-label">取件地址</text>
              <text class="info-value">{{ taskDetail.task.pickupAddress }}</text>
            </view>
            <view class="info-item address-item">
              <text class="info-label">送达地址</text>
              <text class="info-value">{{ taskDetail.task.deliveryAddress }}</text>
            </view>
          </view>
        </view>

        <!-- 规格信息 -->
        <view class="info-section">
          <view class="section-title">物品信息</view>
          <view class="info-content">
            <view class="info-item specs-grid">
              <view class="info-item spec-item">
                <text class="info-label">预估重量</text>
                <text class="info-value">{{ taskDetail.task.estimatedWeight }}kg</text>
              </view>
              <view class="info-item spec-item">
                <text class="info-label">预估大小</text>
                <text class="info-value">{{ taskDetail.task.estimatedSize }}</text>
              </view>
            </view>
          </view>
        </view>

        <!-- 赏金信息 -->
        <view class="info-section reward-section">
          <view class="section-title">赏金信息</view>
          <view class="reward-content">
            <view class="reward-main">
              <view class="reward-label">赏金金额</view>
              <view class="reward-value">
                <text class="currency">¥</text>
                <text class="amount">{{ taskDetail.task.reward }}</text>
              </view>
            </view>
            <view v-if="taskDetail.task.urgent" class="urgent-reward">
              <view class="reward-label">加急奖励</view>
              <view class="reward-value">
                <text class="currency">+¥</text>
                <text class="amount">{{ taskDetail.task.urgentReward }}</text>
                <text class="urgent-tag">加急</text>
              </view>
            </view>
          </view>
        </view>

        <!-- 联系方式 -->
        <view class="info-section">
          <view class="section-title">联系方式</view>
          <view class="info-content">
            <view class="info-item contact-item">
              <text class="info-label">联系人</text>
              <text class="info-value">{{ taskDetail.task.contactName }}</text>
            </view>
            <view class="info-item contact-item">
              <text class="info-label">联系电话</text>
              <view class="info-value phone-wrapper">
                <text>{{ taskDetail.task.contactPhone }}</text>
                <view class="phone-icon" @click.stop="makePhoneCall(taskDetail.task.contactPhone)">
                  <u-icon name="phone-fill" color="#07c160" size="40"></u-icon>
                </view>
              </view>
            </view>
          </view>
        </view>

        <!-- 时间信息 -->
        <view class="info-section">
          <view class="section-title">时间信息</view>
          <view class="info-content">
            <view class="info-item time-item">
              <text class="info-label">发布时间</text>
              <text class="info-value">{{ formatTime(taskDetail.task.createTime) }}</text>
            </view>
            <view class="info-item time-item">
              <text class="info-label">期望送达</text>
              <text class="info-value">{{ formatTime(taskDetail.task.expectedFinishTime) }}</text>
            </view>
          </view>
        </view>

        <!-- 图片信息 -->
        <view v-if="taskDetail.task.images && taskDetail.task.images.length > 0" class="info-section">
          <view class="section-title">相关图片</view>
          <view class="image-grid">
            <image
              v-for="(image, index) in taskDetail.task.images"
              :key="index"
              :src="image"
              mode="aspectFill"
              @click="previewImage(image, taskDetail.task.images)"
              class="task-image"
            ></image>
          </view>
        </view>

        <!-- 备注说明 -->
        <view v-if="taskDetail.task.notes" class="info-section">
          <view class="section-title">备注说明</view>
          <view class="notes-content">{{ taskDetail.task.notes }}</view>
        </view>
      </view>

      <!-- 标签页切换 -->
      <view class="tab-container">
        <view class="tab-header">
          <view 
            v-for="(tab, index) in tabs" 
            :key="index"
            class="tab-item"
            :class="{ active: activeTab === index }"
            @click="handleTabChange(index)"
          >
            <text>{{ tab.name }}</text>
            <text v-if="tab.count" class="tab-count">({{ tab.count }})</text>
          </view>
        </view>

        <!-- 标签页内容 -->
        <view class="tab-content">
          <!-- 任务进度 -->
          <TaskTimeline 
            v-if="activeTab === 0" 
            :taskLogs="taskDetail.logs"
          />

          <!-- 任务留言 -->
          <TaskMessages
            v-else-if="activeTab === 1"
            :taskId="taskId"
            :messages="taskDetail.messages"
            @refresh="fetchTaskDetail"
          />
        </view>
      </view>
    </template>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getTaskDetail } from '@/api/task'
import { TASK_STATUS } from '@/constants/taskStatus'
import TaskTimeline from './components/TaskTimeline.vue'
import TaskMessages from './components/TaskMessages.vue'

const loading = ref(true)
const taskDetail = ref(null)
const activeTab = ref(0)
const taskId = ref(null)

// 标签页配置
const tabs = computed(() => [
  {
    name: '任务进度',
    count: taskDetail.value?.logs?.length || 0
  },
  {
    name: '任务留言',
    count: taskDetail.value?.messages?.length || 0
  }
])

// 切换标签页
const handleTabChange = (index) => {
  activeTab.value = index
}

// 获取任务详情
const fetchTaskDetail = async () => {
  try {
    loading.value = true
    const pages = getCurrentPages()
    const currentPage = pages[pages.length - 1]
    taskId.value = currentPage.options.id

    if (!taskId.value) {
      uni.showToast({
        title: '任务ID不存在',
        icon: 'none'
      })
      setTimeout(() => {
        uni.navigateBack()
      }, 1500)
      return
    }

    const res = await getTaskDetail(taskId.value)
    if (res.data) {
      taskDetail.value = res.data
    }
  } catch (error) {
    console.error('获取任务详情失败：', error)
    uni.showToast({
      title: '获取任务详情失败',
      icon: 'none'
    })
  } finally {
    loading.value = false
  }
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

onMounted(() => {
  fetchTaskDetail()
})
</script>

<style lang="scss" scoped>
.task-detail-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-bottom: env(safe-area-inset-bottom);
  padding-top: env(safe-area-inset-top);
}

.loading-state {
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 28rpx;
  
  text {
    margin-top: 20rpx;
  }
}

.status-tag {
  position: fixed;
  top: calc(env(safe-area-inset-top) + 20rpx);
  right: 30rpx;
  z-index: 100;
  padding: 12rpx 30rpx;
  border-radius: 30rpx;
  font-size: 26rpx;
  font-weight: 600;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.detail-card, .message-card {
  margin: 30rpx;
  background: #fff;
  border-radius: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 30rpx;
  border-bottom: 2rpx solid #f5f7fa;
  margin-bottom: 0;

  .type-info {
    display: flex;
    align-items: center;
    gap: 12rpx;
    
    .urgent-tag {
      background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
      color: white;
      font-size: 24rpx;
      font-weight: bold;
      width: 40rpx;
      height: 40rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4rpx 12rpx rgba(255, 77, 79, 0.4);
      animation: urgentBounce 1s ease infinite;
    }
    
    .type-icon {
      width: 44rpx;
      height: 44rpx;
      margin-right: 16rpx;
    }
    
    .type-name {
      font-size: 30rpx;
      color: #333;
      font-weight: 600;
    }
  }
}

.card-title {
  font-size: 40rpx;
  font-weight: 700;
  color: #333;
  padding: 30rpx;
  padding-bottom: 20rpx;
  border-bottom: 2rpx solid #f5f7fa;
}

.card-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
  padding: 20rpx 30rpx;
  background: #fafafa;
  margin: 20rpx 30rpx;
  border-radius: 16rpx;
}

.info-section {
  background-color: #ffffff;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  padding: 24rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 24rpx;
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

  .info-content {
    padding: 16rpx;
    background-color: #f8f8f8;
    border-radius: 12rpx;
    margin-top: 16rpx;

    .info-item {
      display: flex;
      align-items: flex-start;
      margin-bottom: 16rpx;
      padding: 12rpx 16rpx;
      background-color: #ffffff;
      border-radius: 8rpx;

      &:last-child {
        margin-bottom: 0;
      }

      .info-label {
        font-size: 28rpx;
        color: #666;
        min-width: 160rpx;
        padding-right: 20rpx;
      }

      .info-value {
        flex: 1;
        font-size: 28rpx;
        color: #333;
        word-break: break-all;
      }

      &.contact-item {
        .info-value {
          color: #333;
          
          &.phone-wrapper {
            display: flex;
            align-items: center;
            justify-content: space-between;
            
            .phone-icon {
              padding: 8rpx;
              margin-left: 20rpx;
              border-radius: 50%;
              transition: all 0.2s;
              
              &:active {
                background-color: #f0f0f0;
              }
            }
          }
        }
      }

      &.time-item {
        .info-value {
          color: #666;
        }
      }
    }
  }
}

.address-item, .contact-item, .time-item {
  margin-bottom: 20rpx;
  padding: 16rpx 20rpx;
  background: #f8f8f8;
  border-radius: 12rpx;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  .label {
    font-size: 26rpx;
    color: #999;
    margin-bottom: 8rpx;
  }
  
  .value {
    font-size: 30rpx;
    color: #333;
    font-weight: 500;
  }
}

.specs-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;

  .spec-item {
    padding: 20rpx;
    background: #f8f8f8;
    border-radius: 12rpx;
    text-align: center;

    .label {
      font-size: 26rpx;
      color: #999;
      margin-bottom: 8rpx;
    }

    .value {
      font-size: 32rpx;
      color: #333;
      font-weight: 600;
    }
  }
}

.reward-section {
  .reward-content {
    background: linear-gradient(135deg, #fff9f0, #fff5f5);
    border-radius: 16rpx;
    padding: 24rpx;
    margin-top: 16rpx;

    .reward-main, .urgent-reward {
      background: #ffffff;
      border-radius: 12rpx;
      padding: 20rpx;
      margin-bottom: 16rpx;
      box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);

      &:last-child {
        margin-bottom: 0;
      }

      .reward-label {
        font-size: 26rpx;
        color: #666;
        margin-bottom: 12rpx;
      }

      .reward-value {
        display: flex;
        align-items: baseline;

        .currency {
          font-size: 32rpx;
          color: #ff9500;
          margin-right: 4rpx;
          font-weight: bold;
        }

        .amount {
          font-size: 48rpx;
          color: #ff9500;
          font-weight: bold;
          font-family: 'DIN Condensed', sans-serif;
        }
      }
    }

    .urgent-reward {
      .reward-value {
        .currency {
          color: #ff4d4f;
        }
        
        .amount {
          color: #ff4d4f;
        }

        .urgent-tag {
          font-size: 24rpx;
          color: #ff4d4f;
          background: #fff2f0;
          padding: 4rpx 16rpx;
          border-radius: 20rpx;
          margin-left: 16rpx;
        }
      }
    }
  }
}

.phone-number {
  color: #07c160;
  text-decoration: underline;
  font-weight: 600;
  
  &:active {
    opacity: 0.8;
  }
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
  padding: 20rpx;
  background: #f8f8f8;
  border-radius: 16rpx;
  
  .task-image {
    width: 100%;
    height: 200rpx;
    border-radius: 12rpx;
    box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
  }
}

.notes-content {
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
  padding: 24rpx;
  background: #f8f8f8;
  border-radius: 16rpx;
  border-left: 8rpx solid #07c160;
}

.message-card {
  .card-header {
    padding: 24rpx 30rpx;
    border-bottom: 2rpx solid #f5f7fa;
    
    .message-count {
      font-size: 26rpx;
      color: #999;
      margin-left: 12rpx;
      background: #f5f7fa;
      padding: 4rpx 12rpx;
      border-radius: 20rpx;
    }
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
  
  .message-input {
    flex: 1;
    height: 80rpx;
    background: #f5f7fa;
    border-radius: 40rpx;
    padding: 0 30rpx;
    font-size: 28rpx;
    margin-right: 20rpx;
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

// 添加跳动动画
@keyframes urgentBounce {
  0%, 100% {
    transform: scale(1) translateY(0);
  }
  20% {
    transform: scale(1.1) translateY(-4rpx);
  }
  40% {
    transform: scale(0.9) translateY(4rpx);
  }
  60% {
    transform: scale(1.05) translateY(-2rpx);
  }
  80% {
    transform: scale(0.95) translateY(2rpx);
  }
}

.tab-container {
  margin-top: 30rpx;
  background: #fff;
  
  .tab-header {
    display: flex;
    align-items: center;
    border-bottom: 2rpx solid #f5f7fa;
    padding: 0 30rpx;
    
    .tab-item {
      position: relative;
      padding: 24rpx 40rpx;
      font-size: 28rpx;
      color: #666;
      display: flex;
      align-items: center;
      
      .tab-count {
        font-size: 24rpx;
        color: #999;
        margin-left: 8rpx;
      }
      
      &.active {
        color: #07c160;
        font-weight: 600;
        
        &::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 40rpx;
          right: 40rpx;
          height: 4rpx;
          background: #07c160;
          border-radius: 4rpx;
        }
      }
    }
  }
  
  .tab-content {
    min-height: 400rpx;
  }
}
</style> 