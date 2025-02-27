<template>
  <view class="my-tasks-container">
    <!-- 状态筛选器 -->
    <view class="status-filter-wrapper">
      <scroll-view class="status-filter" scroll-x show-scrollbar="false">
        <view
            v-for="(status, index) in statusOptions"
            :key="index"
            class="status-item"
            :class="{ active: currentStatus === status.value }"
            @click="filterByStatus(status.value)"
        >
          {{ status.label }}
        </view>
      </scroll-view>
    </view>

    <!-- 任务列表 -->
    <scroll-view
        scroll-y
        class="task-list-scroll"
        @scrolltolower="loadMoreTasks"
        @refresherrefresh="refreshTasks"
        refresher-enabled
        :refresher-triggered="isRefreshing"
        refresher-background="#f5f7fa"
        :show-scrollbar="false"
        enhanced
        :bounces="true"
        :scroll-anchoring="true"
    >
      <!-- 暂无数据提示 -->
      <view v-if="taskList.length === 0 && !isLoading" class="empty-container">
        <view class="empty-animation">
          <view class="empty-courier-icon">🛵</view>
          <view class="empty-dots">
            <view class="dot"></view>
            <view class="dot"></view>
            <view class="dot"></view>
          </view>
        </view>
        <text class="empty-text">还没有任务哦！</text>
        <text class="empty-subtext">快去发布一个新任务吧～</text>
        <view class="empty-action" @click="navigateToPublish">
          <text class="action-text">去发布</text>
          <text class="action-arrow">→</text>
        </view>
      </view>

      <!-- 加载中状态 -->
      <view v-if="isLoading && taskList.length === 0" class="loading-container">
        <u-loading-icon mode="circle" size="36"></u-loading-icon>
        <text class="loading-text">加载中...</text>
      </view>

      <!-- 任务卡片列表 -->
      <view v-else-if="taskList.length > 0" class="task-list">
        <!-- 任务卡片 -->
        <task-card
            v-for="task in taskList"
            :key="task.task.id"
            :task="task"
            @click="navigateToTaskDetail(task.task.id)"
        >
          <template #actions>
            <!-- 已完成状态显示评价按钮 -->
            <template v-if="task.task.status === TASK_STATUS.COMPLETED">
              <view class="action-buttons">
                <view class="action-btn evaluate-btn" @tap.stop="showEvaluateModal(task)">
                  评价跑腿者
                </view>
              </view>
            </template>

            <!-- 待确认状态 -->
            <template v-else-if="task.task.status === TASK_STATUS.AWAITING_CONFIRMATION">
              <view class="action-buttons">
                <view class="action-btn confirm-btn" @click.stop="confirmTask(task.task.id)">
                  确认完成
                </view>
                <view class="action-btn dispute-btn" @click.stop="submitDispute(task.task.id)">
                  提交纠纷
                </view>
              </view>
            </template>

            <!-- 待接单状态可取消 -->
            <view
                v-else-if="canCancelTask(task.task.status)"
                class="action-btn cancel-btn"
                @click.stop="cancelTask(task.task.id)"
            >
              取消任务
            </view>
          </template>
        </task-card>
      </view>

      <!-- 加载更多 -->
      <view v-if="taskList.length > 0" class="load-more">
        <u-loadmore v-if="isLoadingMore" status="loading"/>
        <u-loadmore v-else-if="!hasMoreData" status="nomore"/>
      </view>
    </scroll-view>

    <!-- 评价弹窗 -->
    <task-evaluate
      v-model:show="showEvaluate"
      :taskId="currentTask?.task?.id"
      @submit="handleEvaluateSubmit"
    />
  </view>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue'
import {
  cancelTask as apiCancelTask,
  getMyPublishedTasks,
  confirmTask as apiConfirmTask,
  submitDispute as apiSubmitDispute,
  addTaskEvaluation
} from '@/api/task'
import {TASK_STATUS} from '@/constants/taskStatus'
import TaskCard from '@/components/task-card/index.vue'

// 引入 uview-plus 组件
import uPopup from 'uview-plus/components/u-popup/u-popup.vue'
import uIcon from 'uview-plus/components/u-icon/u-icon.vue'
import uRate from 'uview-plus/components/u-rate/u-rate.vue'
import uSwitch from 'uview-plus/components/u-switch/u-switch.vue'
import uLoadingIcon from 'uview-plus/components/u-loading-icon/u-loading-icon.vue'
import uLoadmore from 'uview-plus/components/u-loadmore/u-loadmore.vue'

// 导入评价组件
import TaskEvaluate from '@/components/task-evaluate/index.vue'

// 状态筛选选项
const statusOptions = TASK_STATUS.getStatusOptions()
const currentStatus = ref(null) // 使用null表示全部

// 任务列表数据
const taskList = ref([])
const isRefreshing = ref(false)
const isLoading = ref(false)
const isLoadingMore = ref(false)
const hasMoreData = ref(true)
const currentPage = ref(1)
const pageSize = 10
const total = ref(0)

// 评价相关数据
const showEvaluate = ref(false)
const currentTask = ref(null)
const submitting = ref(false)

// 评价表单
const evaluateForm = ref({
  taskId: null,
  score: 5,
  content: '',
  tags: [],
  anonymous: false
})

// 评价标签选项
const evaluateTags = [
  '准时送达',
  '态度友好',
  '服务周到',
  '包装完好',
  '沟通顺畅'
]

// 格式化时间显示 (YYYY-MM-DD HH:MM)
const formatTime = (timeString) => {
  if (!timeString) return '';
  const date = new Date(timeString);

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}`;
}

// 格式化相对时间 (例如：刚刚、5分钟前、1小时前)
const formatRelativeTime = (timeString) => {
  if (!timeString) return '';

  const now = new Date();
  const date = new Date(timeString);
  const diff = Math.floor((now - date) / 1000); // 差值（秒）

  if (diff < 60) {
    return '刚刚';
  } else if (diff < 3600) {
    return `${Math.floor(diff / 60)}分钟前`;
  } else if (diff < 86400) {
    return `${Math.floor(diff / 3600)}小时前`;
  } else if (diff < 604800) { // 7天内
    return `${Math.floor(diff / 86400)}天前`;
  } else {
    // 超过7天显示具体日期
    return formatTime(timeString);
  }
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

// 判断任务是否可以取消
const canCancelTask = (status) => {
  // 只有待接单状态的任务可以取消
  return status === TASK_STATUS.PENDING
}

// 状态筛选
const filterByStatus = (status) => {
  if (currentStatus.value === status) return

  currentStatus.value = status
  refreshTasks()
}

// 获取任务列表数据
const fetchTaskList = async () => {
  try {
    isLoading.value = currentPage.value === 1 && !isRefreshing.value
    isLoadingMore.value = currentPage.value > 1

    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize
    }

    // 如果状态不为空，添加状态筛选条件
    if (currentStatus.value !== null) {
      params.status = currentStatus.value
    }

    // 调用后端API获取任务列表
    const res = await getMyPublishedTasks(params)

    console.log('任务列表接口返回：', res)

    // 处理响应数据
    if (res.data && res.data.records) {
      // 如果是刷新或第一页，直接替换数据
      if (currentPage.value === 1) {
        taskList.value = res.data.records
      } else {
        // 否则追加数据
        taskList.value = [...taskList.value, ...res.data.records]
      }

      // 更新总数
      total.value = res.data.total

      // 判断是否有更多数据
      hasMoreData.value = taskList.value.length < total.value
    } else {
      if (currentPage.value === 1) {
        taskList.value = []
      }
      hasMoreData.value = false
    }
  } catch (error) {
    console.error('获取任务列表失败：', error)
    uni.showToast({
      title: '获取任务列表失败',
      icon: 'none'
    })
  } finally {
    isLoading.value = false
    isRefreshing.value = false
    isLoadingMore.value = false
  }
}

// 刷新任务列表
const refreshTasks = async () => {
  isRefreshing.value = true
  currentPage.value = 1
  await fetchTaskList()

  // 修复滑动问题：确保刷新状态正确重置
  setTimeout(() => {
    // 延迟重置刷新状态，确保UI有时间更新
    isRefreshing.value = false
  }, 300)
}

// 加载更多任务
const loadMoreTasks = async () => {
  if (!hasMoreData.value || isLoadingMore.value) return

  currentPage.value++
  await fetchTaskList()
}

// 取消任务
const cancelTask = async (taskId) => {
  try {
    uni.showModal({
      title: '确认取消',
      content: '确定要取消这个任务吗？取消后无法恢复。',
      async success(res) {
        if (res.confirm) {
          uni.showLoading({title: '取消中...'})

          try {
            // 调用取消任务API
            const res = await apiCancelTask(taskId)

            // 先显示成功提示
            await uni.showToast({
              title: '任务已取消',
              icon: 'success',
              duration: 1500
            })

            // 等待提示显示完毕后再刷新列表
            setTimeout(async () => {
              // 重置页码和列表数据
              currentPage.value = 1
              taskList.value = []
              // 重新获取列表数据
              await fetchTaskList()
            }, 500)
          } catch (error) {
            console.error('取消任务失败：', error)
            uni.showToast({
              title: error.message || '取消任务失败',
              icon: 'none'
            })
          } finally {
            uni.hideLoading()
          }
        }
      }
    })
  } catch (error) {
    console.error('取消任务失败：', error)
    uni.showToast({
      title: '取消任务失败',
      icon: 'none'
    })
  }
}

// 跳转到任务详情
const navigateToTaskDetail = (taskId) => {
  uni.navigateTo({
    url: `/pages/task-detail/index?id=${taskId}`
  })
}

// 跳转到发布任务页面
const navigateToPublish = () => {
  uni.navigateTo({
    url: '/pages/publish/index'
  })
}

// 确认完成任务
const confirmTask = async (taskId) => {
  uni.showModal({
    title: '确认完成',
    content: '请仔细核对后，进行确认。一旦确认，将无法发起纠纷，请认真处理。',
    confirmText: '确认完成',
    confirmColor: '#07c160',
    cancelText: '再检查下',
    cancelColor: '#999999',
    async success(res) {
      if (res.confirm) {
        // 二次确认
        uni.showModal({
          title: '二次确认',
          content: '您确定要标记此任务为已完成吗？\n此操作不可撤销！',
          confirmText: '确定完成',
          confirmColor: '#07c160',
          cancelText: '返回检查',
          cancelColor: '#999999',
          async success(res) {
            if (res.confirm) {
              uni.showLoading({title: '处理中...'})
              try {
                await apiConfirmTask(taskId)
                await uni.showToast({
                  title: '已确认完成',
                  icon: 'success'
                })
                await refreshTasks()
              } catch (error) {
                console.error('确认任务失败：', error)
                uni.showToast({
                  title: error.message || '操作失败',
                  icon: 'none'
                })
              } finally {
                uni.hideLoading()
              }
            }
          }
        })
      }
    }
  })
}

// 提交纠纷
const submitDispute = async (taskId) => {
  uni.showModal({
    title: '提交纠纷',
    content: '确定要提交纠纷吗？提交后客服会介入处理',
    async success(res) {
      if (res.confirm) {
        uni.showLoading({title: '处理中...'})
        try {
          await apiSubmitDispute(taskId)
          await uni.showToast({
            title: '已提交纠纷',
            icon: 'success'
          })
          await refreshTasks()
        } catch (error) {
          console.error('提交纠纷失败：', error)
          uni.showToast({
            title: error.message || '操作失败',
            icon: 'none'
          })
        } finally {
          uni.hideLoading()
        }
      }
    }
  })
}

// 显示评价弹窗
const showEvaluateModal = (task) => {
  currentTask.value = task
  showEvaluate.value = true
}

// 处理评价提交
const handleEvaluateSubmit = async (formData) => {
  try {
    await addTaskEvaluation(formData)
    uni.showToast({
      title: '评价成功',
      icon: 'success'
    })
    showEvaluate.value = false
    refreshTasks()
  } catch (error) {
    console.error('评价失败：', error)
    uni.showToast({
      title: error.message || '评价失败',
      icon: 'none'
    })
  }
}

// 切换标签选择
const toggleTag = (tag) => {
  const index = evaluateForm.value.tags.indexOf(tag)
  if (index > -1) {
    evaluateForm.value.tags.splice(index, 1)
  } else if (evaluateForm.value.tags.length < 5) {
    evaluateForm.value.tags.push(tag)
  }
}

// 是否可以提交
const canSubmit = computed(() => {
  return evaluateForm.value.score > 0
})

// 获取评分对应的文本和样式
const getScoreText = (score) => {
  if (score >= 4.5) return '非常满意';
  if (score >= 4) return '满意';
  if (score >= 3) return '一般';
  return '不满意';
}

const getScoreClass = (score) => {
  if (score >= 4.5) return 'score-excellent';
  if (score >= 4) return 'score-good';
  if (score >= 3) return 'score-normal';
  return 'score-bad';
}

// 页面加载时获取任务列表
onMounted(() => {
  refreshTasks()
})
</script>

<style lang="scss" scoped>
.my-tasks-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f7fa;
}

.status-filter-wrapper {
  background-color: #ffffff;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  padding: 10rpx 0;
  /* 添加安全区域，确保适配刘海屏 */
  padding-top: constant(safe-area-inset-top); /* iOS 11.0及以下 */
  padding-top: env(safe-area-inset-top); /* iOS 11.2+ */
}

.status-filter {
  display: flex;
  white-space: nowrap;
  padding: 10rpx 30rpx;
  height: 70rpx;

  &::-webkit-scrollbar {
    display: none;
  }

  .status-item {
    padding: 6rpx 30rpx;
    margin-right: 20rpx;
    font-size: 26rpx;
    color: #666;
    background-color: #f5f7fa;
    border-radius: 30rpx;
    transition: all 0.3s;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    height: 56rpx;
    flex-shrink: 0;

    &.active {
      background-color: #fff3e0;
      color: #ff9500;
      font-weight: 500;
    }

    &:last-child {
      margin-right: 0;
    }
  }
}

.task-list-scroll {
  flex: 1;
  padding: 20rpx 30rpx 30rpx;
  height: 100%;
  box-sizing: border-box;
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

.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding-top: 150rpx;

  .empty-animation {
    position: relative;
    width: 200rpx;
    height: 200rpx;
    margin-bottom: 40rpx;

    .empty-courier-icon {
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      font-size: 60rpx;
      animation: drive 3s infinite;
    }

    .empty-dots {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      display: flex;
      justify-content: center;

      .dot {
        width: 16rpx;
        height: 16rpx;
        border-radius: 50%;
        background-color: #cccccc;
        margin: 0 10rpx;
        opacity: 0.6;

        &:nth-child(1) {
          animation: pulse 1.5s infinite 0.2s;
        }

        &:nth-child(2) {
          animation: pulse 1.5s infinite 0.4s;
        }

        &:nth-child(3) {
          animation: pulse 1.5s infinite 0.6s;
        }
      }
    }
  }

  .empty-text {
    font-size: 34rpx;
    color: #666;
    margin-bottom: 15rpx;
    font-weight: 500;
  }

  .empty-subtext {
    font-size: 28rpx;
    color: #999;
    margin-bottom: 40rpx;
  }

  .empty-action {
    background: linear-gradient(to right, #ff9500, #ffbd5c);
    padding: 18rpx 40rpx;
    border-radius: 40rpx;
    display: flex;
    align-items: center;
    box-shadow: 0 6rpx 16rpx rgba(255, 149, 0, 0.3);

    .action-text {
      color: #ffffff;
      font-size: 28rpx;
      font-weight: 500;
    }

    .action-arrow {
      color: #ffffff;
      font-size: 28rpx;
      margin-left: 10rpx;
      font-weight: 500;
    }
  }
}

.task-list {
  padding-bottom: 30rpx;
}

.load-more {
  padding: 20rpx 0;
}

.action-buttons {
  display: flex;
  gap: 16rpx;
}

.action-btn {
  font-size: 24rpx;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  transition: all 0.2s;

  &:active {
    transform: scale(0.95);
  }

  &.confirm-btn {
    color: #07c160;
    background-color: #e8f8f1;

    &:active {
      background-color: #d7f6e6;
    }
  }

  &.dispute-btn {
    color: #ff9500;
    background-color: #fff7e6;

    &:active {
      background-color: #fff0d9;
    }
  }

  &.cancel-btn {
    color: #ff4d4f;
    background-color: #fff2f0;

    &:active {
      background-color: #ffe7e5;
    }
  }

  &.evaluate-btn {
    color: #ff9500;
    background-color: #fff7e6;

    &:active {
      background-color: #fff0d9;
    }
  }
}

@keyframes drive {
  0% {
    left: -60rpx;
  }
  50% {
    left: 200rpx;
  }
  100% {
    left: -60rpx;
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.2;
    transform: scale(0.8);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.2);
  }
}

/* 修复刷新时的滑动问题 */
@keyframes refreshCompleted {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(0);
  }
}

/* 刷新完成后应用此动画，平滑过渡 */
.refresh-completed {
  animation: refreshCompleted 0.3s ease;
}

.evaluate-popup {
  background: linear-gradient(to bottom, #ffffff, #f8fafc);
  border-radius: 24rpx 24rpx 0 0;
  padding-bottom: env(safe-area-inset-bottom);
  max-height: 85vh;
  display: flex;
  flex-direction: column;

  .evaluate-header {
    flex-shrink: 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx;
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

  .evaluate-content {
    flex: 1;
    overflow-y: auto;
    padding: 30rpx;
    -webkit-overflow-scrolling: touch;
    padding-bottom: 120rpx;

    .section-title {
      font-size: 30rpx;
      font-weight: 600;
      color: #333;
      margin-bottom: 24rpx;
      display: flex;
      align-items: center;
      gap: 10rpx;

      .tag-tip {
        font-size: 24rpx;
        color: #999;
        font-weight: normal;
      }
    }

    .score-section {
      margin-bottom: 30rpx !important;
      padding: 20rpx !important;
    }

    .score-wrapper {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 16rpx;
      margin: 20rpx 0;
    }

    .score-text {
      font-size: 32rpx;
      font-weight: 600;
      margin-top: 16rpx;
      
      &.score-excellent {
        color: #ff9500;
        animation: pulse 1s infinite;
      }
      
      &.score-good {
        color: #07c160;
      }
      
      &.score-normal {
        color: #2b85e4;
      }
      
      &.score-bad {
        color: #ff4d4f;
      }
    }

    .score-value {
      font-size: 48rpx;
      font-weight: 700;
      color: #ff9500;
      margin-top: 16rpx;
      font-family: 'DIN Condensed', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
    }

    .tags-section {
      margin-bottom: 30rpx !important;
    }

    .tags-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 16rpx;

      .tag-item {
        padding: 16rpx 24rpx;
        border-radius: 30rpx;
        font-size: 28rpx;
        color: #666;
        background: #f5f5f5;
        transition: all 0.3s;
        display: flex;
        align-items: center;
        gap: 8rpx;
        border: 2rpx solid transparent;

        .tag-text {
          transition: all 0.3s;
        }

        .tag-icon {
          opacity: 0;
          transform: scale(0);
          transition: all 0.3s;
        }

        &.active {
          color: #ff9500;
          background: #fff7e6;
          border-color: #ff9500;
          font-weight: 500;

          .tag-icon {
            opacity: 1;
            transform: scale(1);
          }
        }

        &:active {
          transform: scale(0.95);
        }
      }
    }

    .content-section {
      margin-bottom: 30rpx !important;
    }

    .textarea-wrapper {
      position: relative;
      background: #fff;
      border-radius: 16rpx;
      padding: 20rpx;
      box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);

      .evaluate-textarea {
        width: 100%;
        height: 200rpx;
        font-size: 28rpx;
        line-height: 1.6;
        color: #333;
        padding: 0;
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

    .anonymous-section {
      margin-bottom: 100rpx !important;
      padding: 20rpx !important;
    }
  }

  .evaluate-footer {
    flex-shrink: 0;
    padding: 30rpx;
    border-top: 2rpx solid rgba(0, 0, 0, 0.05);
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 1;
  }
}

@keyframes shine {
  0% {
    transform: translateX(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) rotate(45deg);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

/* 优化滚动条样式 */
::-webkit-scrollbar {
  width: 0;
  height: 0;
  background: transparent;
}
</style> 