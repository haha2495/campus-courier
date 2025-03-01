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

        <!-- 纠纷信息 -->
        <view v-if="taskDetail.dispute" class="info-section dispute-section">
          <view class="section-title">纠纷信息</view>
          <view class="dispute-content">
            <!-- 纠纷标题 -->
            <view class="dispute-title">{{ taskDetail.dispute.title }}</view>
            
            <!-- 纠纷内容 -->
            <view class="dispute-desc">{{ taskDetail.dispute.content }}</view>
            
            <!-- 纠纷图片 -->
            <view v-if="taskDetail.dispute.images && taskDetail.dispute.images.length > 0" class="dispute-images">
              <image
                v-for="(image, index) in taskDetail.dispute.images"
                :key="index"
                :src="image"
                mode="aspectFill"
                @click="previewImage(image, taskDetail.dispute.images)"
                class="dispute-image"
              ></image>
            </view>

            <!-- 证据图片 -->
            <view v-if="taskDetail.dispute.evidence && taskDetail.dispute.evidence.length > 0" class="evidence-section">
              <view class="evidence-title">证据图片</view>
              <view class="evidence-images">
                <image
                  v-for="(image, index) in taskDetail.dispute.evidence"
                  :key="index"
                  :src="image"
                  mode="aspectFill"
                  @click="previewImage(image, taskDetail.dispute.evidence)"
                  class="evidence-image"
                ></image>
              </view>
            </view>

            <!-- 任务相关者提示 -->
            <view v-if="isTaskParticipant" class="notice-section">
              <u-icon name="info-circle-fill" size="32" color="#FFA940"></u-icon>
              <text class="notice-text">您是任务相关者，无法对此纠纷进行审判</text>
            </view>

            <!-- 审判按钮 -->
            <view v-if="!taskDetail.currentUserSupport && !isTaskParticipant" class="judge-section">
              <button class="judge-btn" @click="showJudgePopup">
                <u-icon name="error-circle-fill" size="32" color="#FFFFFF"></u-icon>
                <text>进行审判</text>
              </button>
            </view>
          </view>
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
            <view class="tab-content-wrapper">
              <view class="tab-inner">
                <u-icon :name="tab.icon" size="32" :color="activeTab === index ? '#3B82F6' : '#6B7280'"></u-icon>
                <text class="tab-text">{{ tab.name }}</text>
              </view>
            </view>
            <view class="tab-line" :class="{ 'tab-line-active': activeTab === index }"></view>
          </view>
        </view>

        <!-- 标签页内容 -->
        <view class="tab-content">
          <!-- 任务进度 -->
          <TaskTimeline 
            v-if="activeTab === 0" 
            :taskLogs="taskDetail.taskLogs"
          />

          <!-- 任务留言 -->
          <TaskMessages
            v-else-if="activeTab === 1"
            :taskId="taskId"
            :messages="taskDetail.messages"
            @refresh="fetchTaskDetail"
          />

          <!-- 支持信息 -->
          <DisputeSupports
            v-else-if="activeTab === 2 && taskDetail.dispute"
            :dispute="taskDetail.dispute"
            :disputeSupports="taskDetail.disputeSupports"
          />

          <!-- 评价信息 -->
          <view v-else-if="activeTab === 3" class="evaluation-section">
            <template v-if="taskDetail.evaluation">
              <view class="evaluation-card">
                <!-- 评分展示 -->
                <view class="score-section">
                  <view class="score-value">
                    <text class="score-number">{{ taskDetail.evaluation.score }}</text>
                    <text class="score-unit">分</text>
                  </view>
                  <view class="star-container">
                    <u-rate
                      v-model="taskDetail.evaluation.score"
                      :readonly="true"
                      :count="5"
                      active-color="#FFB800"
                      inactive-color="#E0E0E0"
                      size="40"
                    ></u-rate>
                  </view>
                </view>

                <!-- 评价标签 -->
                <view v-if="taskDetail.evaluation.tags && taskDetail.evaluation.tags.length > 0" class="tags-section">
                  <view 
                    v-for="(tag, index) in taskDetail.evaluation.tags" 
                    :key="index"
                    class="tag-item"
                  >
                    {{ tag }}
                  </view>
                </view>

                <!-- 评价内容 -->
                <view v-if="taskDetail.evaluation.content" class="content-section">
                  <text class="content-text">{{ taskDetail.evaluation.content }}</text>
                </view>

                <!-- 评价信息 -->
                <view class="evaluation-info">
                  <view class="evaluator">
                    <text v-if="!taskDetail.evaluation.anonymous">{{ taskDetail.evaluation.evaluatorName }}</text>
                    <text v-else>匿名用户</text>
                  </view>
                  <text class="evaluation-time">{{ formatTime(taskDetail.evaluation.createTime) }}</text>
                </view>
              </view>
            </template>
            <view v-else class="no-evaluation">
              <u-empty
                mode="data"
                text="暂无评价信息"
                margin-top="40"
              ></u-empty>
            </view>
          </view>
        </view>
      </view>
    </template>

    <!-- 审判弹窗 -->
    <u-popup :show="showPopup" mode="center" round="12" @close="showPopup = false">
      <view class="judge-popup">
        <view class="popup-title">
          <text>选择支持方</text>
          <u-icon name="close" size="32" color="#999" @click="showPopup = false"></u-icon>
        </view>
        
        <view class="popup-content">
          <view class="judge-options">
            <view 
              class="judge-option publisher"
              :class="{ active: selectedParty === 'publisher' }"
              @click="selectedParty = 'publisher'"
            >
              <u-icon name="account" size="48" color="#3B82F6"></u-icon>
              <text>支持发布者</text>
              <text class="sub-text">{{ taskDetail?.publisher?.name }}</text>
            </view>
            
            <view 
              class="judge-option runner"
              :class="{ active: selectedParty === 'runner' }"
              @click="selectedParty = 'runner'"
            >
              <u-icon name="car" size="48" color="#10B981"></u-icon>
              <text>支持跑腿者</text>
              <text class="sub-text">{{ taskDetail?.runner?.name }}</text>
            </view>
          </view>
          
          <view class="judge-remark">
            <u-input
              v-model="judgeRemark"
              type="textarea"
              placeholder="请输入审判理由（选填）"
              height="160"
              :border="true"
            ></u-input>
          </view>
        </view>
        
        <view class="popup-footer">
          <button class="cancel-btn" @click="showPopup = false">取消</button>
          <button class="confirm-btn" @click="handleJudge" :disabled="!selectedParty">确认</button>
        </view>
      </view>
    </u-popup>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getTaskDetail } from '@/api/task'
import { supportDispute } from '@/api/dispute'
import { TASK_STATUS } from '@/constants/taskStatus'
import { userApi } from '@/api/user'
import TaskTimeline from './components/TaskTimeline.vue'
import TaskMessages from './components/TaskMessages.vue'
import DisputeSupports from './components/DisputeSupports.vue'

const loading = ref(true)
const taskDetail = ref(null)
const activeTab = ref(0)
const taskId = ref(null)
const showPopup = ref(false)
const selectedParty = ref(null)
const judgeRemark = ref('')
const currentUser = ref(null)

// 标签页配置
const tabs = computed(() => {
  const baseTabs = [
    {
      name: '任务进度',
      icon: 'clock-fill'
    },
    {
      name: '任务留言',
      icon: 'chat-fill'
    }
  ]
  
  // 如果有纠纷信息且用户有权限查看支持信息，添加支持信息标签
  if (taskDetail.value?.dispute && (isTaskParticipant.value || taskDetail.value?.currentUserSupport)) {
    baseTabs.push({
      name: '支持信息',
      icon: 'thumb-up-fill'
    })
  }
  
  // 只有当任务有评价信息时，才添加评价标签页
  if (taskDetail.value?.evaluation) {
    baseTabs.push({
      name: '评价信息',
      icon: 'star-fill'
    })
  }
  
  return baseTabs
})

// 切换标签页
const handleTabChange = (index) => {
  // 确保切换到的标签页存在
  if (index < tabs.value.length) {
    activeTab.value = index
  }
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
      // 如果当前是评价标签页，但没有评价信息，则切换到第一个标签
      if (activeTab.value === 3 && !taskDetail.value.evaluation) {
        activeTab.value = 0
      }
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

// 显示审判弹窗
const showJudgePopup = () => {
  selectedParty.value = ''
  judgeRemark.value = ''
  showPopup.value = true
}

// 处理审判提交
const handleJudge = async () => {
  if (!selectedParty.value) {
    uni.showToast({
      title: '请选择支持方',
      icon: 'none'
    })
    return
  }

  try {
    // 显示加载提示
    uni.showLoading({
      title: '提交中...'
    })

    // 构造请求参数
    const params = {
      disputeId: taskDetail.value.dispute.id,
      supportType: selectedParty.value === 'publisher' ? taskDetail.value.publisher.id : taskDetail.value.runner.id,
      remark: judgeRemark.value || '用户支持'
    }
    
    // 调用支持纠纷API
    await supportDispute(params)
    
    // 清空表单数据
    selectedParty.value = ''
    judgeRemark.value = ''
    
    // 关闭弹窗
    showPopup.value = false
    
    // 刷新页面数据
    await fetchTaskDetail()
    
    // 显示成功提示
    uni.showToast({
      title: '支持成功',
      icon: 'success'
    })
  } catch (error) {
    console.error('支持失败：', error)
    uni.showToast({
      title: error.message || '支持失败，请重试',
      icon: 'none'
    })
  } finally {
    uni.hideLoading()
  }
}

// 判断当前用户是否是任务相关者（发布者或跑腿者）
const isTaskParticipant = computed(() => {
  if (!taskDetail.value || !currentUser.value) return false
  return currentUser.value.id === taskDetail.value.publisher?.id || currentUser.value.id === taskDetail.value.runner?.id
})

// 获取当前用户信息
const fetchCurrentUser = async () => {
  try {
    const res = await userApi.getCurrentUser()
    if (res.data) {
      currentUser.value = res.data
    }
  } catch (error) {
    console.error('获取当前用户信息失败：', error)
  }
}

onMounted(async () => {
  await fetchCurrentUser()
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

/* 标签切换样式 */
.tab-container {
  position: relative;
  background-color: #ffffff;
  margin: 30rpx 20rpx;
  border-radius: 24rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.tab-header {
  display: flex;
  align-items: stretch;
  position: relative;
  height: 88rpx;
  background: linear-gradient(to bottom, #ffffff, #f8fafc);
  
  &::after {
    content: '';
    position: absolute;
    left: 15%;
    right: 15%;
    bottom: 0;
    height: 2rpx;
    background: linear-gradient(to right, transparent, #E5E7EB, transparent);
    transform: scaleY(0.5);
  }
}

.tab-item {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12rpx 0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  
  .tab-content-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    transform: translateY(0);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }

  .tab-inner {
    display: flex;
    align-items: center;
    gap: 8rpx;
  }
  
  .tab-text {
    font-size: 28rpx;
    color: #6B7280;
    font-weight: 500;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }
  
  .tab-line {
    position: absolute;
    bottom: 0;
    width: 32rpx;
    height: 4rpx;
    background: linear-gradient(to right, #3B82F6, #60A5FA);
    border-radius: 4rpx;
    opacity: 0;
    transform: translateY(4rpx);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }
  
  &.active {
    .tab-content-wrapper {
      transform: translateY(-2rpx);
    }
    
    .tab-text {
      color: #3B82F6;
      font-weight: 600;
    }
    
    .tab-line {
      opacity: 1;
      transform: translateY(0);
      width: 48rpx;
    }
  }
  
  &:active {
    .tab-content-wrapper {
      transform: scale(0.98);
    }
  }
}

.tab-content {
  background-color: #ffffff;
  min-height: 300rpx;
  padding: 30rpx;
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 20rpx;
    right: 20rpx;
    height: 30rpx;
    background: linear-gradient(to bottom, rgba(255,255,255,0.95), transparent);
    pointer-events: none;
  }
}

.evaluation-section {
  padding: 20rpx;
  
  .evaluation-card {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 30rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
    
    .score-section {
      display: flex;
      flex-direction: column;
      align-items: center;
      margin-bottom: 30rpx;
      
      .score-value {
        margin-bottom: 20rpx;
        display: flex;
        align-items: flex-end;
        
        .score-number {
          font-size: 64rpx;
          font-weight: bold;
          color: #FFB800;
          font-family: 'DIN Condensed', sans-serif;
          line-height: 1;
        }

        .score-unit {
          font-size: 24rpx;
          color: #FFB800;
          margin-left: 4rpx;
          margin-bottom: 12rpx;
        }
      }
      
      .star-container {
        display: flex;
        justify-content: center;
      }
    }
    
    .tags-section {
      display: flex;
      flex-wrap: wrap;
      gap: 16rpx;
      margin-bottom: 30rpx;
      
      .tag-item {
        padding: 8rpx 20rpx;
        background: #F0F7FF;
        color: #3B82F6;
        border-radius: 30rpx;
        font-size: 24rpx;
      }
    }
    
    .content-section {
      margin-bottom: 30rpx;
      padding: 20rpx;
      background: #F8F9FA;
      border-radius: 12rpx;
      
      .content-text {
        font-size: 28rpx;
        color: #333;
        line-height: 1.6;
      }
    }
    
    .evaluation-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 20rpx;
      border-top: 2rpx solid #F0F0F0;
      
      .evaluator {
        font-size: 26rpx;
        color: #666;
      }
      
      .evaluation-time {
        font-size: 24rpx;
        color: #999;
      }
    }
  }
  
  .no-evaluation {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 300rpx;
  }
}

.dispute-section {
  .dispute-content {
    background: #FFF7ED;
    border-radius: 16rpx;
    padding: 24rpx;
    border: 2rpx solid #FDBA74;

    .dispute-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #1F2937;
      margin-bottom: 16rpx;
    }

    .dispute-desc {
      font-size: 28rpx;
      color: #4B5563;
      line-height: 1.6;
      margin-bottom: 20rpx;
    }

    .dispute-images {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 16rpx;
      margin-bottom: 24rpx;
      
      .dispute-image {
        width: 100%;
        height: 180rpx;
        border-radius: 12rpx;
        box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
      }
    }

    .evidence-section {
      margin-top: 24rpx;
      padding-top: 24rpx;
      border-top: 2rpx dashed #FDBA74;

      .evidence-title {
        font-size: 28rpx;
        font-weight: 500;
        color: #F97316;
        margin-bottom: 16rpx;
        display: flex;
        align-items: center;
        
        &::before {
          content: '';
          width: 6rpx;
          height: 24rpx;
          background: #F97316;
          margin-right: 12rpx;
          border-radius: 3rpx;
        }
      }

      .evidence-images {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 16rpx;
        
        .evidence-image {
          width: 100%;
          height: 180rpx;
          border-radius: 12rpx;
          box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
        }
      }
    }

    .judge-section {
      margin-top: 32rpx;
      padding-top: 24rpx;
      border-top: 2rpx dashed #FDBA74;
      display: flex;
      justify-content: center;

      .judge-btn {
        background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
        color: #FFFFFF;
        padding: 24rpx 64rpx;
        border-radius: 40rpx;
        font-size: 32rpx;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 12rpx;
        box-shadow: 0 8rpx 16rpx rgba(255, 77, 79, 0.2);
        transition: all 0.3s ease;
        width: 60%;

        &:active {
          transform: scale(0.95);
          box-shadow: 0 4rpx 8rpx rgba(255, 77, 79, 0.15);
        }

        .u-icon {
          animation: pulse 2s infinite;
        }
      }
    }
  }
}

// 添加动画
@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.judge-popup {
  background: #FFFFFF;
  width: 600rpx;
  border-radius: 24rpx;
  overflow: hidden;
  
  .popup-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 32rpx;
    border-bottom: 2rpx solid #F3F4F6;

    text {
      font-size: 36rpx;
      font-weight: 600;
      color: #1F2937;
    }
  }
  
  .popup-content {
    padding: 32rpx;

    .judge-options {
      display: flex;
      justify-content: space-between;
      gap: 24rpx;
      margin-bottom: 32rpx;

      .judge-option {
        flex: 1;
        padding: 32rpx 24rpx;
        border: 2rpx solid #E5E7EB;
        border-radius: 16rpx;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 12rpx;
        transition: all 0.3s ease;

        text {
          font-size: 28rpx;
          color: #4B5563;
          font-weight: 500;
        }

        .sub-text {
          font-size: 24rpx;
          color: #9CA3AF;
        }

        &.active {
          &.publisher {
            border-color: #3B82F6;
            background: #F0F7FF;
          }

          &.runner {
            border-color: #10B981;
            background: #ECFDF5;
          }
        }

        &:active {
          transform: scale(0.98);
        }
      }
    }

    .judge-remark {
      margin-top: 24rpx;
    }
  }
  
  .popup-footer {
    display: flex;
    gap: 24rpx;
    padding: 32rpx;
    border-top: 2rpx solid #F3F4F6;

    button {
      flex: 1;
      height: 88rpx;
      border-radius: 44rpx;
      font-size: 32rpx;
      font-weight: 600;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;

      &.cancel-btn {
        background: #F3F4F6;
        color: #6B7280;

        &:active {
          background: #E5E7EB;
        }
      }

      &.confirm-btn {
        background: linear-gradient(135deg, #3B82F6 0%, #60A5FA 100%);
        color: #FFFFFF;

        &:active {
          transform: scale(0.98);
        }

        &:disabled {
          background: linear-gradient(135deg, #93C5FD 0%, #BFDBFE 100%);
          color: #FFFFFF;
        }
      }
    }
  }
}

.notice-section {
  margin-top: 32rpx;
  padding: 24rpx;
  background: #FFF7E6;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  gap: 16rpx;
  border: 2rpx solid #FFD591;
  
  .notice-text {
    font-size: 28rpx;
    color: #D46B08;
    flex: 1;
  }
}
</style> 