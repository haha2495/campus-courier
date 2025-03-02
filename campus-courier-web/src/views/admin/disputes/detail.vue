<template>
  <div class="dispute-detail">
    <!-- 顶部返回区域 -->
    <el-card class="header-card" shadow="hover">
      <div class="header-wrapper">
        <div class="back-section" @click="handleBack">
          <el-icon class="back-icon">
            <ArrowLeft/>
          </el-icon>
          <span>返回列表</span>
        </div>
        <div class="title">{{ type === 'detail' ? '任务详情' : '纠纷详情' }}</div>
        <el-button
            v-if="type === 'detail' && (detail?.dispute?.status === 0 || detail?.dispute?.status === 1)"
            type="primary"
            size="small"
            class="handle-btn"
            @click="handleDisputeClick"
        >
          <el-icon class="btn-icon">
            <Tools/>
          </el-icon>
          处理纠纷
        </el-button>
      </div>
    </el-card>

    <!-- 纠纷大厅提示 -->
    <el-alert
        v-if="detail?.dispute?.status === 3"
        type="warning"
        :closable="false"
        class="dispute-hall-alert"
    >
      <template #title>
        <div class="alert-content">
          <el-icon class="alert-icon">
            <Warning/>
          </el-icon>
          <span>当前纠纷正在大厅公示中，请等待公示结束后进行处理</span>
          <el-tag
              size="small"
              effect="dark"
              type="warning"
              class="time-tag"
          >
            剩余时间：{{ formatRemainingTime(detail.dispute.deadline) }}
          </el-tag>
        </div>
      </template>
    </el-alert>

    <!-- 内容区域 -->
    <el-card class="content-card" shadow="hover">
      <div class="content-wrapper" v-loading="loading">
        <template v-if="detail">
          <!-- 任务基本信息 -->
          <div class="task-info-section">
            <h3 class="section-title">任务信息</h3>
            <div class="task-header">
              <div class="task-type">
                <img class="type-icon" :src="detail.taskType.icon" alt="任务类型">
                <span>{{ detail.taskType.typeName }}</span>
              </div>
              <el-tag
                  :type="getStatusType(detail.task.status)"
                  effect="light"
                  class="status-tag"
              >
                {{ getStatusText(detail.task.status) }}
              </el-tag>
            </div>
            <div class="task-content">
              <h4>{{ detail.task.title }}</h4>
              <p>{{ detail.task.description }}</p>
            </div>
          </div>

          <!-- 相关用户 -->
          <div class="users-section">
            <h3 class="section-title">相关用户</h3>
            <div class="user-cards">
              <div class="user-card" v-if="detail.publisher">
                <div class="user-role">发布者</div>
                <el-avatar
                    :src="detail.publisher.avatar || null"
                >
                  {{ detail.publisher.name?.[0]?.toUpperCase() || '?' }}
                </el-avatar>
                <div class="user-info">
                  <div class="user-name">{{ detail.publisher.name || '未知用户' }}</div>
                  <div class="user-no">{{ detail.publisher.studentNo || '无学号' }}</div>
                </div>
              </div>
              <div class="user-card" v-if="detail.runner">
                <div class="user-role">跑腿者</div>
                <el-avatar
                    :src="detail.runner.avatar || null"
                >
                  {{ detail.runner.name?.[0]?.toUpperCase() || '?' }}
                </el-avatar>
                <div class="user-info">
                  <div class="user-name">{{ detail.runner.name || '未知用户' }}</div>
                  <div class="user-no">{{ detail.runner.studentNo || '无学号' }}</div>
                </div>
              </div>
            </div>
          </div>

          <!-- 配送信息和时间信息并排布局 -->
          <div class="delivery-time-row">
            <!-- 配送信息 -->
            <div class="delivery-section">
              <h3 class="section-title">配送信息</h3>
              <div class="info-content">
                <div class="info-item">
                  <span class="info-label">取件地址</span>
                  <span class="info-value">{{ detail.task?.pickupAddress || '无' }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">送达地址</span>
                  <span class="info-value">{{ detail.task?.deliveryAddress || '无' }}</span>
                </div>
              </div>
            </div>

            <!-- 时间信息 -->
            <div class="time-section">
              <h3 class="section-title">时间信息</h3>
              <div class="info-content">
                <div class="info-item">
                  <span class="info-label">发布时间</span>
                  <span class="info-value">{{ formatTime(detail.task?.createTime) || '无' }}</span>
                </div>
                <div class="info-item">
                  <span class="info-label">期望送达</span>
                  <span class="info-value">{{ formatTime(detail.task?.expectedFinishTime) || '无' }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 备注说明 -->
          <div class="remark-section">
            <h3 class="section-title">备注说明</h3>
            <div class="info-content">
              <div class="remark-content">
                {{ detail.task?.notes || '暂无备注' }}
              </div>
            </div>
          </div>

          <!-- 物品信息和赏金信息区域 -->
          <div class="info-row">
            <!-- 物品信息 -->
            <div class="item-section">
              <h3 class="section-title">物品信息</h3>
              <div class="specs-grid">
                <div class="spec-item">
                  <span class="info-label">预估重量</span>
                  <span class="info-value">{{ detail.task?.estimatedWeight || '0' }}kg</span>
                </div>
                <div class="spec-item">
                  <span class="info-label">预估大小</span>
                  <span class="info-value">{{ detail.task?.estimatedSize || '未知' }}</span>
                </div>
              </div>
            </div>

            <!-- 赏金信息 -->
            <div class="reward-section">
              <h3 class="section-title">赏金信息</h3>
              <div class="reward-content">
                <div class="reward-row">
                  <div class="reward-item">
                    <span class="reward-label">赏金金额</span>
                    <div class="reward-value">
                      <span class="currency">¥</span>
                      <span class="amount">{{ detail.task?.reward || '0' }}</span>
                    </div>
                  </div>
                  <div v-if="detail.task?.urgent" class="reward-item urgent">
                    <span class="reward-label">加急奖励</span>
                    <div class="reward-value">
                      <span class="currency">+¥</span>
                      <span class="amount">{{ detail.task?.urgentReward || '0' }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 联系方式 -->
          <div class="contact-section">
            <h3 class="section-title">联系方式</h3>
            <div class="info-content">
              <div class="info-item">
                <span class="info-label">联系人</span>
                <span class="info-value">{{ detail.task?.contactName || '无' }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">联系电话</span>
                <span class="info-value">{{ detail.task?.contactPhone || '无' }}</span>
              </div>
            </div>
          </div>

          <!-- 评价信息 -->
          <div class="evaluation-section" v-if="detail.evaluation">
            <h3 class="section-title">
              <el-icon class="title-icon"><Star /></el-icon>
              评价信息
            </h3>
            <div class="evaluation-content">
              <div class="rating-area">
                <div class="rating-header">
                  <div class="score-display">
                    <div class="score-box">
                      <span class="score-number">{{ detail.evaluation.score }}</span>
                      <span class="score-text">分</span>
                    </div>
                    <div class="rating-score">
                      <el-rate
                        v-model="detail.evaluation.score"
                        disabled
                        show-score
                        text-color="#ff9900"
                        score-template=""
                        class="custom-rate"
                      />
                    </div>
                  </div>
                </div>
                <div class="rating-tags" v-if="detail.evaluation.tags?.length">
                  <el-tag
                    v-for="(tag, index) in detail.evaluation.tags"
                    :key="tag"
                    class="tag-item"
                    effect="light"
                    :style="{ animationDelay: index * 0.1 + 's' }"
                  >
                    <el-icon class="tag-icon"><Medal /></el-icon>
                    {{ tag }}
                  </el-tag>
                </div>
              </div>
              <div class="evaluation-remark" v-if="detail.evaluation.content">
                <div class="quote-icon">
                  <el-icon><ChatRound /></el-icon>
                </div>
                <div class="remark-content">{{ detail.evaluation.content }}</div>
              </div>
            </div>
          </div>

          <!-- 纠纷信息 -->
          <div class="dispute-section" v-if="detail.dispute">
            <h3 class="section-title">
              纠纷信息
            </h3>
            <div class="dispute-content">
              <div class="dispute-header">
                <div class="dispute-tags">
                  <el-tag
                      :type="getDisputeTypeStyle(detail.dispute.type)"
                      class="dispute-tag"
                      size="large"
                  >
                    <el-icon class="tag-icon">
                      <component :is="getDisputeTypeIcon(detail.dispute.type)"/>
                    </el-icon>
                    <span>{{ getDisputeType(detail.dispute.type) }}</span>
                  </el-tag>
                  <el-tag
                      :type="DisputeStatusEnum.getType(detail.dispute.status)"
                      class="dispute-tag"
                      size="large"
                  >
                    <el-icon class="tag-icon">
                      <component :is="DisputeStatusEnum.getIcon(detail.dispute.status)"/>
                    </el-icon>
                    <span>{{ DisputeStatusEnum.getDesc(detail.dispute.status) }}</span>
                  </el-tag>
                </div>
              </div>
              <div class="dispute-main">
                <h4>{{ detail.dispute.title }}</h4>
                <p>{{ detail.dispute.content }}</p>
                <!-- 证据图片 -->
                <div class="evidence-list" v-if="detail.dispute.evidence?.length">
                  <h5>证据图片</h5>
                  <div class="evidence-grid">
                    <el-image
                        v-for="(url, index) in detail.dispute.evidence"
                        :key="index"
                        :src="url"
                        :preview-src-list="detail.dispute.evidence"
                        :initial-index="index"
                        fit="cover"
                        class="evidence-image"
                        :hide-on-click-modal="false"
                    >
                      <template #error>
                        <div class="image-error">
                          <el-icon>
                            <Picture/>
                          </el-icon>
                          <span>加载失败</span>
                        </div>
                      </template>
                    </el-image>
                  </div>
                </div>

                <!-- 纠纷结果区域 -->
                <div v-if="detail.dispute.status === 2" class="result-section">
                  <div class="result-header">
                    <el-icon class="result-icon">
                      <Trophy/>
                    </el-icon>
                    <span class="result-title">处理结果</span>
                  </div>
                  <div class="result-content">
                    <div class="winner-info">
                      <el-tag type="success" effect="dark" class="winner-tag">
                        {{ detail.dispute.result === 1 ? '发布者胜诉' : '跑腿者胜诉' }}
                      </el-tag>
                    </div>
                    <div class="admin-remark">
                      <div class="remark-label">处理说明：</div>
                      <div class="remark-text">{{ detail.dispute.adminRemark || '无' }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 任务日志和留言记录 -->
          <el-tabs class="detail-tabs" type="card">
            <el-tab-pane>
              <template #label>
                <div class="custom-tab">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  <span>任务日志</span>
                </div>
              </template>
              <div class="logs-section">
                <el-timeline>
                  <el-timeline-item
                      v-for="log in detail.taskLogs"
                      :key="log.id"
                      :timestamp="formatTime(log.actionTime)"
                      :type="getLogType(log.action)"
                      placement="top"
                  >
                    <div class="log-content">
                      <div class="log-text">{{ log.remark }}</div>
                      <!-- 日志图片展示 -->
                      <div v-if="log.images?.length" class="log-images">
                        <el-image
                            v-for="(url, index) in log.images"
                            :key="index"
                            :src="url"
                            :preview-src-list="log.images"
                            :initial-index="index"
                            fit="cover"
                            class="log-image"
                        >
                          <template #error>
                            <div class="image-error">
                              <el-icon>
                                <Picture/>
                              </el-icon>
                            </div>
                          </template>
                        </el-image>
                      </div>
                    </div>
                  </el-timeline-item>
                </el-timeline>
              </div>
            </el-tab-pane>

            <el-tab-pane>
              <template #label>
                <div class="custom-tab">
                  <el-icon>
                    <ChatDotRound/>
                  </el-icon>
                  <span>留言记录</span>
                </div>
              </template>
              <div class="messages-section">
                <div class="message-list">
                  <div
                      v-for="msg in detail.messages"
                      :key="msg.message.id"
                      class="message-item"
                      :class="{ 'self-message': msg.student.id === detail.publisher.id }"
                  >
                    <el-avatar
                        :src="msg.student?.avatar || null"
                        size="small"
                    >
                      {{ msg.student?.name?.[0]?.toUpperCase() || '?' }}
                    </el-avatar>
                    <div class="message-content">
                      <div class="message-header">
                        <span class="sender-name">{{ msg.student?.name || '未知用户' }}</span>
                        <span class="message-time">{{ formatTime(msg.message.createTime) }}</span>
                      </div>
                      <div class="message-body">
                        <div class="message-text">{{ msg.message.content || '无内容' }}</div>
                        <!-- 消息图片展示 -->
                        <div v-if="msg.message.images?.length && msg.message.images[0]" class="message-images">
                          <el-image
                              v-for="(url, index) in msg.message.images"
                              :key="index"
                              :src="url"
                              :preview-src-list="msg.message.images"
                              :initial-index="index"
                              fit="cover"
                              class="message-image"
                          >
                            <template #error>
                              <div class="image-error">
                                <el-icon>
                                  <Picture/>
                                </el-icon>
                              </div>
                            </template>
                          </el-image>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <el-tab-pane v-if="detail.dispute">
              <template #label>
                <div class="custom-tab">
                  <el-icon>
                    <ChatLineSquare/>
                  </el-icon>
                  <span>支持信息</span>
                  <el-badge :value="detail.disputeSupports?.length || 0" class="support-badge"/>
                </div>
              </template>
              <div class="supports-section">
                <DisputeSupports :dispute-supports="detail.disputeSupports"/>
              </div>
            </el-tab-pane>
          </el-tabs>
        </template>
      </div>
    </el-card>

    <!-- 处理纠纷弹窗 -->
    <el-dialog
        v-model="dialogVisible"
        title="处理纠纷"
        width="900px"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        class="dispute-dialog"
    >
      <div class="handle-options">
        <el-radio-group v-model="handleResult" class="handle-radio-group">
          <div class="options-row">
            <el-radio label="PUBLISHER_WIN" border class="option-card">
              <div class="radio-content">
                <div class="radio-header">
                  <div class="radio-title">发布者胜</div>
                  <el-icon class="option-icon">
                    <UserFilled/>
                  </el-icon>
                </div>
                <div class="radio-desc">判定发布者胜诉，退还订单金额</div>
              </div>
            </el-radio>

            <el-radio label="RUNNER_WIN" border class="option-card">
              <div class="radio-content">
                <div class="radio-header">
                  <div class="radio-title">跑腿者胜</div>
                  <el-icon class="option-icon">
                    <Medal/>
                  </el-icon>
                </div>
                <div class="radio-desc">判定跑腿者胜诉，订单金额归跑腿者</div>
              </div>
            </el-radio>

            <el-tooltip
                content="该纠纷已经发布过纠纷大厅，不能重复发布"
                placement="top"
                :disabled="detail?.dispute?.status !== 1"
            >
              <div class="radio-wrapper">
                <el-radio
                    label="UNCERTAIN"
                    border
                    class="option-card"
                    :disabled="detail?.dispute?.status === 1"
                >
                  <div class="radio-content">
                    <div class="radio-header">
                      <div class="radio-title">存在疑问</div>
                      <el-icon class="option-icon">
                        <Warning/>
                      </el-icon>
                    </div>
                    <div class="radio-desc">发布到纠纷大厅进行筛选</div>
                  </div>
                </el-radio>
              </div>
            </el-tooltip>
          </div>
        </el-radio-group>

        <!-- 处理结果相关的输入区域 -->
        <div class="handle-details" v-if="handleResult">
          <div v-if="handleResult === 'PUBLISHER_WIN' || handleResult === 'RUNNER_WIN'" class="reason-section">
            <h4 class="details-title">
              <el-icon>
                <Document/>
              </el-icon>
              处理原因
            </h4>
            <el-input
                v-model="handleReason"
                type="textarea"
                :rows="4"
                :placeholder="handleResult === 'PUBLISHER_WIN' ? '请输入判定发布者胜诉的原因（必填）' : '请输入判定跑腿者胜诉的原因（必填）'"
                maxlength="200"
                show-word-limit
                resize="none"
                class="reason-input"
            />
          </div>

          <div v-if="handleResult === 'UNCERTAIN'" class="time-section">
            <h4 class="details-title">
              <el-icon>
                <Timer/>
              </el-icon>
              展示时长
            </h4>
            <div class="time-options">
              <el-radio-group v-model="displayHours" class="time-radio-group">
                <el-radio-button :label="24">24小时</el-radio-button>
                <el-radio-button :label="48">48小时</el-radio-button>
                <el-radio-button :label="72">72小时</el-radio-button>
              </el-radio-group>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn" plain>取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting" class="confirm-btn">
            <el-icon class="btn-icon"><Check/></el-icon>
            确认处理
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {
  ArrowLeft,
  ChatDotRound,
  ChatLineSquare,
  Check,
  Document,
  Medal,
  Picture,
  Timer,
  Tools,
  Trophy,
  UserFilled,
  Warning,
  Star,
  ChatRound
} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {handleDispute} from '@/api/dispute'
import {TaskStatusEnum} from '@/utils/enums/taskEnums'
import {DisputeStatusEnum} from '@/utils/enums/disputeEnums'
import {DisputeTypeEnum} from "@/utils/enums/disputeTypeEnums.js";
import {getTaskDetail} from "@/api/task.js";
import DisputeSupports from '@/components/DisputeSupports.vue'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const detail = ref(null)
const dialogVisible = ref(false)
const handleResult = ref('')
const submitting = ref(false)
const handleReason = ref('')
const displayHours = ref(24)

// 获取路由参数
const taskId = route.query.taskId
const type = route.query.type

// 返回列表页
const handleBack = () => {
  router.back()
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

// 获取任务状态类型
const getStatusType = (status) => {
  return status ? TaskStatusEnum.getType(status) : ''
}

// 获取任务状态文本
const getStatusText = (status) => {
  return status ? TaskStatusEnum.getDesc(status) : ''
}

// 获取纠纷类型
const getDisputeType = (type) => {
  return type ? DisputeTypeEnum.getDesc(type) : ''
}

// 获取纠纷类型样式
const getDisputeTypeStyle = (type) => {
  return type ? DisputeTypeEnum.getType(type) : ''
}

// 获取纠纷类型图标
const getDisputeTypeIcon = (type) => {
  return type ? DisputeTypeEnum.getIcon(type) : ''
}

// 获取日志类型
const getLogType = (action) => {
  if (!action) return 'info'
  switch (action) {
    case 'SUBMIT_DISPUTE':
      return 'danger'
    case 'DELIVER':
      return 'success'
    default:
      return 'primary'
  }
}

// 获取详情数据
const fetchDetail = async () => {
  if (!taskId) {
    ElMessage.error('参数错误')
    router.replace('/admin/disputes')
    return
  }

  loading.value = true
  try {
    detail.value = await getTaskDetail(taskId)
  } catch (error) {
    console.error('获取纠纷详情失败:', error)
    ElMessage.error('系统异常，请稍后重试')
    router.replace('/admin/disputes')
  } finally {
    loading.value = false
  }
}

// 生命周期钩子
onMounted(async () => {
  console.log('组件挂载，开始获取详情数据...')
  await fetchDetail()
})

onBeforeUnmount(() => {
  console.log('组件即将卸载，清理数据...')
  detail.value = null
  loading.value = false
})

// 点击处理纠纷按钮
const handleDisputeClick = () => {
  dialogVisible.value = true
  handleResult.value = ''
}

// 提交处理结果
const handleSubmit = async () => {
  if (!handleResult.value) {
    ElMessage.warning('请选择处理结果')
    return
  }

  // 验证必填项
  if ((handleResult.value === 'PUBLISHER_WIN' || handleResult.value === 'RUNNER_WIN')
      && !handleReason.value.trim()) {
    ElMessage.warning('请输入处理原因')
    return
  }

  if (handleResult.value === 'UNCERTAIN' && !displayHours.value) {
    ElMessage.warning('请选择展示时长')
    return
  }

  submitting.value = true
  try {
    // 将前端的枚举值转换为后端需要的数字
    const handleResultMap = {
      'PUBLISHER_WIN': 1,
      'RUNNER_WIN': 2,
      'UNCERTAIN': 3
    }

    await handleDispute({
      disputeId: detail.value.dispute.id,
      handleResult: handleResultMap[handleResult.value],
      handleRemark: handleReason.value,
      displayHours: handleResult.value === 'UNCERTAIN' ? displayHours.value : null
    })

    ElMessage.success('处理成功')
    dialogVisible.value = false
    // 重新加载详情
    await fetchDetail()
  } catch (error) {
    console.error('处理纠纷失败:', error)
    ElMessage.error(error.message || '处理失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 监听弹窗关闭，重置表单
watch(dialogVisible, (val) => {
  if (!val) {
    handleResult.value = ''
    handleReason.value = ''
    displayHours.value = 24
  }
})

// 格式化剩余时间
const formatRemainingTime = (endTimeString) => {
  if (!endTimeString) return '未知'

  const endTime = new Date(endTimeString).getTime()
  const now = new Date().getTime()
  const diff = endTime - now

  if (diff <= 0) return '已结束'

  const hours = Math.floor(diff / (1000 * 60 * 60))
  const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60))

  if (hours > 24) {
    const days = Math.floor(hours / 24)
    const remainingHours = hours % 24
    return `${days}天${remainingHours}小时`
  }

  return `${hours}小时${minutes}分钟`
}
</script>

<style scoped lang="scss">
.dispute-detail {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
  padding: 16px;
  box-sizing: border-box;
  overflow: hidden;
}

/* 头部卡片样式 */
.header-card {
  border-radius: 8px;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  margin-bottom: 16px;
  background: #fff;

  :deep(.el-card__body) {
    padding: 16px;
  }
}

.header-wrapper {
  display: flex;
  align-items: center;
  position: relative;
  height: 48px;
  padding: 0 130px;

  .back-section {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    color: #666;
    transition: all 0.3s;
    position: absolute;
    left: 24px;

    &:hover {
      color: #409EFF;
    }

    .back-icon {
      font-size: 16px;
    }
  }

  .title {
    flex: 1;
    text-align: center;
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }

  .handle-btn {
    position: absolute;
    right: 0;
    height: 40px;
    min-width: 120px;
    background: linear-gradient(45deg, #409EFF, #36cfc9);
    border: none;
    padding: 0 24px;
    display: flex;
    align-items: center;
    gap: 8px;
    transition: all 0.3s ease;
    box-shadow: 0 2px 6px rgba(64, 158, 255, 0.2);
    font-size: 15px;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
      background: linear-gradient(45deg, #66b1ff, #5cdbd3);
    }

    &:active {
      transform: translateY(0);
    }

    .btn-icon {
      font-size: 18px;
      animation: rotate 2s linear infinite;
    }

    @keyframes rotate {
      0% {
        transform: rotate(0deg);
      }
      25% {
        transform: rotate(-15deg);
      }
      75% {
        transform: rotate(15deg);
      }
      100% {
        transform: rotate(0deg);
      }
    }
  }
}

/* 内容卡片样式 */
.content-card {
  flex: 1;
  border-radius: 8px;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  background: #fff;

  :deep(.el-card__body) {
    height: 100%;
    padding: 0;
  }
}

.content-wrapper {
  height: 100%;
  padding: 24px;
  overflow-y: auto;
  box-sizing: border-box;

  &::-webkit-scrollbar {
    width: 8px;
  }

  &::-webkit-scrollbar-thumb {
    background: linear-gradient(to bottom, #409EFF, #36cfc9);
    border-radius: 4px;

    &:hover {
      background: linear-gradient(to bottom, #66b1ff, #5cdbd3);
    }
  }

  &::-webkit-scrollbar-track {
    background: #f1f5f9;
    border-radius: 4px;
  }
}

/* 各个区块通用样式 */
.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  align-items: center;

  &::before {
    content: '';
    width: 3px;
    height: 16px;
    background: #409EFF;
    margin-right: 8px;
    border-radius: 2px;
  }

  .handle-btn {
    margin-left: auto;
  }
}

/* 任务信息区域 */
.task-info-section {
  margin-bottom: 24px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  border: 1px solid #e5e7eb;

  .task-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    .task-type {
      display: flex;
      align-items: center;
      gap: 8px;

      .type-icon {
        width: 24px;
        height: 24px;
        border-radius: 4px;
      }

      span {
        font-size: 15px;
        color: #1f2937;
      }
    }

    .status-tag {
      font-size: 13px;
      padding: 4px 12px;
      border-radius: 4px;
    }
  }

  .task-content {
    background: #f9fafb;
    padding: 16px;
    border-radius: 8px;

    h4 {
      font-size: 15px;
      color: #1f2937;
      margin-bottom: 8px;
    }

    p {
      color: #4b5563;
      line-height: 1.6;
      font-size: 14px;
    }
  }
}

/* 用户信息区域 */
.users-section {
  margin-bottom: 24px;

  .user-cards {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }

  .user-card {
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    border: 1px solid #e5e7eb;
    display: flex;
    align-items: center;
    gap: 12px;

    .user-role {
      font-size: 12px;
      color: #409EFF;
      background: #e6f4ff;
      padding: 2px 8px;
      border-radius: 4px;
    }

    .user-info {
      .user-name {
        font-size: 14px;
        color: #1f2937;
        margin-bottom: 4px;
      }

      .user-no {
        font-size: 12px;
        color: #6b7280;
      }
    }
  }
}

/* 配送信息和时间信息并排布局 */
.delivery-time-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;

  .delivery-section,
  .time-section {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    border: 1px solid #e5e7eb;

    .info-content {
      .info-item {
        display: flex;
        padding: 12px 0;
        border-bottom: 1px solid #f3f4f6;

        &:first-child {
          padding-top: 0;
        }

        &:last-child {
          padding-bottom: 0;
          border-bottom: none;
        }

        .info-label {
          width: 80px;
          color: #6b7280;
          font-size: 14px;
        }

        .info-value {
          flex: 1;
          color: #1f2937;
          font-size: 14px;
        }
      }
    }
  }
}

/* 备注说明区域 */
.remark-section {
  margin-bottom: 24px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  border: 1px solid #e5e7eb;

  .remark-content {
    background: #f9fafb;
    padding: 16px;
    border-radius: 8px;
    color: #4b5563;
    font-size: 14px;
    line-height: 1.6;
    min-height: 60px;
  }
}

/* 物品信息和赏金信息区域 */
.info-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  margin-bottom: 24px;

  .item-section,
  .reward-section {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    border: 1px solid #e5e7eb;
    height: fit-content;
  }

  .item-section {
    .specs-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;
      margin-top: 8px;

      .spec-item {
        background: #f9fafb;
        padding: 20px;
        border-radius: 8px;
        text-align: center;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        min-height: 100px;
        border: 1px solid #e5e7eb;
        transition: all 0.2s ease;

        &:hover {
          background: #f3f4f6;
        }

        .info-label {
          color: #6b7280;
          font-size: 13px;
          margin-bottom: 12px;
        }

        .info-value {
          color: #1f2937;
          font-size: 18px;
          font-weight: 500;
        }
      }
    }
  }

  .reward-section {
    .reward-content {
      background: #fffaf0;
      border-radius: 8px;
      padding: 16px;

      .reward-row {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 16px;

        .reward-item {
          background: #fff;
          border-radius: 8px;
          padding: 16px;
          display: flex;
          justify-content: space-between;
          align-items: center;

          .reward-label {
            color: #6b7280;
            font-size: 13px;
          }

          .reward-value {
            display: flex;
            align-items: baseline;

            .currency {
              color: #f59e0b;
              font-size: 14px;
              margin-right: 2px;
            }

            .amount {
              color: #f59e0b;
              font-size: 20px;
              font-weight: 600;
            }
          }

          &.urgent {
            .reward-value {
              .currency, .amount {
                color: #ef4444;
              }
            }
          }
        }
      }
    }
  }
}

/* 联系方式区域 */
.contact-section {
  margin-bottom: 24px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  border: 1px solid #e5e7eb;

  .info-content {
    .info-item {
      display: flex;
      padding: 12px 0;
      border-bottom: 1px solid #f3f4f6;

      &:first-child {
        padding-top: 0;
      }

      &:last-child {
        padding-bottom: 0;
        border-bottom: none;
      }

      .info-label {
        width: 80px;
        color: #6b7280;
        font-size: 14px;
      }

      .info-value {
        flex: 1;
        color: #1f2937;
        font-size: 14px;
      }
    }
  }
}

/* 评价信息区域 */
.evaluation-section {
  margin-bottom: 24px;
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  border: 1px solid #e5e7eb;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  }

  .title-icon {
    color: #fbbf24;
    font-size: 18px;
    margin-right: 6px;
  }

  .evaluation-content {
    .rating-area {
      margin-bottom: 16px;

      .rating-header {
        margin-bottom: 16px;
        padding: 16px;
        background: #f8fafc;
        border-radius: 8px;

        .score-display {
          display: flex;
          flex-direction: column;
          gap: 8px;

          .score-box {
            display: flex;
            align-items: baseline;
            gap: 4px;
            
            .score-number {
              font-size: 32px;
              font-weight: 600;
              color: #f59e0b;
              line-height: 1;
            }

            .score-text {
              font-size: 14px;
              color: #f59e0b;
            }
          }

          .rating-score {
            :deep(.custom-rate) {
              .el-rate__icon {
                font-size: 20px;
                margin-right: 4px;
                
                &.el-rate__icon--on {
                  color: #f59e0b;
                }
              }
            }
          }
        }
      }

      .rating-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;

        .tag-item {
          padding: 4px 12px;
          border-radius: 16px;
          font-size: 13px;
          background: #fff7ed;
          border-color: #fed7aa;
          color: #c2410c;
          display: flex;
          align-items: center;
          gap: 4px;
          transition: all 0.3s ease;

          &:hover {
            transform: translateY(-1px);
            box-shadow: 0 2px 6px rgba(194, 65, 12, 0.1);
          }

          .tag-icon {
            font-size: 14px;
          }
        }
      }
    }

    .evaluation-remark {
      position: relative;
      background: #f8fafc;
      padding: 16px 20px;
      border-radius: 8px;
      margin-top: 20px;

      .quote-icon {
        position: absolute;
        top: -10px;
        left: 16px;
        width: 20px;
        height: 20px;
        background: #3b82f6;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
        font-size: 12px;
      }

      .remark-content {
        color: #4b5563;
        font-size: 14px;
        line-height: 1.6;
        font-style: italic;
        padding-top: 4px;
      }
    }
  }
}

/* 纠纷信息区域 */
.dispute-section {
  margin-bottom: 24px;

  .dispute-content {
    background: #fff1f0;
    border-radius: 8px;
    padding: 20px;
    border: 1px solid #ffa39e;

    .dispute-header {
      margin-bottom: 16px;

      .dispute-tags {
        display: flex;
        gap: 8px;

        :deep(.dispute-tag) {
          display: inline-flex;
          align-items: center;
          height: 28px;
          padding: 0 12px;
          font-size: 13px;
          border-radius: 4px;

          .tag-icon {
            margin-right: 4px;
            font-size: 14px;
          }
        }
      }
    }

    .dispute-main {
      h4 {
        font-size: 15px;
        color: #1f2937;
        margin-bottom: 12px;
      }

      p {
        color: #4b5563;
        line-height: 1.6;
        margin-bottom: 16px;
        font-size: 14px;
      }

      .evidence-list {
        h5 {
          font-size: 14px;
          color: #1f2937;
          margin-bottom: 12px;
        }

        .evidence-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
          gap: 12px;
        }
      }

      .dispute-result {
        margin-top: 20px;
        background: #f0fdf4;
        border-radius: 8px;
        padding: 16px;
        border: 1px solid #86efac;

        .result-header {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 16px;

          .result-icon {
            font-size: 20px;
            color: #059669;
            animation: bounce 1s ease infinite;
          }

          .result-title {
            font-size: 15px;
            font-weight: 600;
            color: #059669;
          }
        }

        .result-content {
          .winner-info {
            margin-bottom: 12px;

            .winner-tag {
              font-size: 14px;
              padding: 6px 12px;
              border-radius: 4px;
              background: linear-gradient(45deg, #059669, #10b981);
            }
          }

          .admin-remark {
            background: #fff;
            border-radius: 6px;
            padding: 12px;

            .remark-label {
              font-size: 13px;
              color: #374151;
              margin-bottom: 8px;
              font-weight: 500;
            }

            .remark-text {
              font-size: 14px;
              color: #1f2937;
              line-height: 1.6;
            }
          }
        }
      }
    }
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-3px);
  }
}

/* 标签页样式优化 */
.detail-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 0;
    padding: 0;
    background: linear-gradient(135deg, #f0f9ff 0%, #e6f4ff 100%);
    border-radius: 16px 16px 0 0;
    border-bottom: none;
    height: 64px;
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: url('data:image/svg+xml,<svg width="20" height="20" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path d="M1 1h2v2H1V1zm4 0h2v2H5V1zm4 0h2v2H9V1zm4 0h2v2h-2V1zm4 0h2v2h-2V1z" fill="rgba(147,197,253,0.1)"/></svg>') repeat;
      opacity: 0.5;
    }
  }

  :deep(.el-tabs__nav-wrap) {
    padding: 0 24px;
  }

  :deep(.el-tabs__nav) {
    border: none !important;
    height: 64px;
  }

  :deep(.el-tabs__item) {
    height: 64px;
    line-height: 64px;
    font-size: 15px;
    border: none !important;
    background: transparent !important;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    padding: 0 32px;
    margin: 0 4px;

    &::before {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 3px;
      background: #409EFF;
      transform: scaleX(0);
      transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      border-radius: 3px 3px 0 0;
    }

    &.is-active {
      color: #409EFF;
      font-weight: 600;
      background: rgba(255, 255, 255, 0.8) !important;
      box-shadow: 0 0 20px rgba(64, 158, 255, 0.1);

      &::before {
        transform: scaleX(1);
      }

      .custom-tab {
        transform: translateY(-2px);

        .el-icon {
          transform: scale(1.2) rotate(360deg);
          color: #409EFF;
        }
      }
    }

    &:hover:not(.is-active) {
      color: #409EFF;
      background: rgba(255, 255, 255, 0.5) !important;

      .custom-tab {
        transform: translateY(-2px);

        .el-icon {
          transform: scale(1.1) rotate(15deg);
          color: #409EFF;
        }
      }
    }
  }

  .custom-tab {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

    .el-icon {
      font-size: 20px;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }

    .support-badge {
      margin-left: 4px;
      transform: translateY(-8px);

      :deep(.el-badge__content) {
        background: linear-gradient(135deg, #10b981, #34d399);
        border: 2px solid #fff;
        box-shadow: 0 2px 6px rgba(16, 185, 129, 0.2);
        font-weight: 600;
        padding: 0 8px;
        min-width: 20px;
        height: 20px;
        border-radius: 10px;

        &.is-fixed {
          top: 0;
          right: 0;
        }
      }
    }
  }
}

/* 任务日志区域样式 */
.logs-section {
  padding: 0 12px;

  :deep(.el-timeline) {
    padding-left: 16px;
  }

  :deep(.el-timeline-item__node) {
    width: 12px;
    height: 12px;
    left: -1px;
  }

  :deep(.el-timeline-item__tail) {
    left: 5px;
    top: 0;
    height: calc(100% - 10px);
  }

  :deep(.el-timeline-item__wrapper) {
    padding-left: 28px;
    top: -4px;
  }

  :deep(.el-timeline-item__timestamp) {
    color: #6b7280;
    font-size: 13px;
    margin-bottom: 8px;
    display: block;
    line-height: 1.5;
    position: relative;
    top: -4px;

    &.is-top {
      display: block;
      margin-bottom: 8px;
    }
  }

  .log-content {
    background: #f9fafb;
    padding: 16px;
    border-radius: 8px;
    margin-bottom: 16px;
    border: 1px solid #e5e7eb;

    .log-text {
      color: #1f2937;
      line-height: 1.6;
      font-size: 14px;
    }
  }
}

/* 留言记录区域样式 */
.messages-section {
  padding: 0 12px;

  .message-item {
    display: flex;
    gap: 12px;
    margin-bottom: 20px;

    .message-content {
      flex: 1;

      .message-header {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 4px;

        .sender-name {
          font-size: 13px;
          color: #4b5563;
        }

        .message-time {
          font-size: 12px;
          color: #9ca3af;
        }
      }

      .message-body {
        background: #f9fafb;
        padding: 12px;
        border-radius: 8px;
        max-width: 80%;

        .message-text {
          color: #1f2937;
          line-height: 1.6;
          font-size: 14px;
          word-break: break-all;
        }
      }
    }
  }
}

/* 图片相关样式统一 */
.evidence-image,
.log-image,
.message-image {
  width: 100%;
  height: 100px;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;

  :deep(.el-image__inner) {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .image-error {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: #f3f4f6;
    color: #9ca3af;
    font-size: 12px;

    .el-icon {
      font-size: 20px;
      margin-bottom: 4px;
    }
  }
}

/* 图片网格布局统一 */
.evidence-grid,
.log-images,
.message-images {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 12px;
  margin-top: 12px;
}

.handle-options {
  padding: 0;

  .handle-radio-group {
    width: 100%;

    .options-row {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 12px;
      margin-bottom: 20px;
    }

    :deep(.el-radio) {
      margin: 0;
      height: auto;
      width: 100%;
      display: flex;
      align-items: flex-start;
      transition: all 0.3s ease;
      position: relative;
      overflow: hidden;

      &.option-card {
        padding: 16px;
        border-radius: 8px;
        height: 100px;
        background: #f9fafb;
        display: flex;
        align-items: flex-start;

        &:hover {
          background: #f0f9ff;
          border-color: #a3d0ff;
        }

        &.is-checked {
          background: #f0f9ff;
          border-color: #409EFF;
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);

          .option-icon {
            transform: scale(1.1);
          }
        }

        .el-radio__input {
          margin-top: 4px;
        }
      }

      .el-radio__input {
        margin-top: 4px;
      }
    }
  }
}

.radio-content {
  margin-left: 8px;
  flex: 1;

  .radio-header {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
    gap: 8px;

    .option-icon {
      font-size: 20px;
      color: #409EFF;
      order: -1;
    }

    .radio-title {
      font-size: 15px;
      font-weight: 500;
      color: #1f2937;
    }
  }

  .radio-desc {
    font-size: 13px;
    color: #666;
    line-height: 1.5;
  }
}

.handle-details {
  background: #f9fafb;
  border-radius: 8px;
  padding: 20px;
  margin-top: 12px;

  .details-title {
    font-size: 15px;
    margin-bottom: 12px;

    .el-icon {
      font-size: 16px;
    }
  }

  .reason-section {
    .reason-input {
      :deep(.el-textarea__inner) {
        padding: 12px;
        font-size: 13px;
        background: #fff;
      }
    }
  }

  .time-section {
    .details-title {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #1f2937;
      margin-bottom: 16px;

      .el-icon {
        font-size: 18px;
        color: #409EFF;
      }
    }

    .time-options {
      display: flex;
      justify-content: center;

      .time-radio-group {
        display: inline-flex;
        gap: 12px;

        :deep(.el-radio-button) {
          height: 36px;

          &:first-child .el-radio-button__inner {
            border-radius: 8px 0 0 8px;
          }

          &:last-child .el-radio-button__inner {
            border-radius: 0 8px 8px 0;
          }

          .el-radio-button__inner {
            height: 36px;
            padding: 0 24px;
            font-size: 14px;
            border: 1px solid #dcdfe6;
            background: #fff;
            color: #606266;
            transition: all 0.3s;
            display: flex;
            align-items: center;
            justify-content: center;

            &:hover {
              color: #409EFF;
              border-color: #409EFF;
              background: #f0f9ff;
            }
          }

          &.is-active {
            .el-radio-button__inner {
              color: #fff;
              background: #409EFF;
              border-color: #409EFF;
              box-shadow: 0 2px 6px rgba(64, 158, 255, 0.2);
            }
          }
        }
      }
    }
  }
}

.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 12px;

  .cancel-btn {
    min-width: 100px;
    height: 36px;
    border-color: #dcdfe6;

    &:hover {
      border-color: #409EFF;
      color: #409EFF;
      background: #f0f9ff;
    }
  }

  .confirm-btn {
    min-width: 120px;
    height: 36px;
    font-size: 14px;
    background: linear-gradient(45deg, #409EFF, #36cfc9);
    border: none;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
    }

    &:active {
      transform: translateY(0);
    }

    .btn-icon {
      font-size: 14px;
      margin-right: 4px;
    }
  }
}

/* 处理纠纷弹窗样式优化 */
:deep(.dispute-dialog) {
  .el-dialog__header {
    margin: 0;
    padding: 20px 24px;
    border-bottom: 1px solid #e5e7eb;
    background: linear-gradient(135deg, #e6f4ff 0%, #f0f9ff 100%);

    .el-dialog__title {
      font-size: 18px;
      font-weight: 600;
      color: #1f2937;
      display: flex;
      align-items: center;
      gap: 8px;

      &::before {
        content: '';
        width: 3px;
        height: 18px;
        background: #409EFF;
        border-radius: 2px;
      }
    }
  }

  .el-dialog__body {
    padding: 20px;
    background: #fff;
  }

  .el-dialog__footer {
    padding: 16px 24px;
    border-top: 1px solid #e5e7eb;
    background: #f9fafb;
  }

  .radio-wrapper {
    width: 100%;
  }

  :deep(.el-radio.is-disabled) {
    &.option-card {
      opacity: 0.6;
      cursor: not-allowed;

      &:hover {
        background: #f9fafb;
        border-color: #dcdfe6;
        transform: none;
      }
    }
  }
}

/* 纠纷大厅提示样式 */
.dispute-hall-alert {
  margin: 16px;
  border-radius: 8px;
  border: none;

  :deep(.el-alert__title) {
    font-size: 14px;
  }

  .alert-content {
    display: flex;
    align-items: center;
    gap: 8px;

    .alert-icon {
      font-size: 16px;
      animation: pulse 2s infinite;
    }

    .time-tag {
      margin-left: auto;
      font-family: 'DIN Condensed', sans-serif;
    }
  }
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

/* 支持信息区域样式 */
.supports-section {
  margin-top: 24px;

  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;
    margin-bottom: 16px;
    display: flex;
    align-items: center;
    gap: 8px;

    &::before {
      content: '';
      width: 3px;
      height: 16px;
      background: #409EFF;
      border-radius: 2px;
    }
  }
}

:deep(.el-tabs__content) {
  background: #fff;
  border-radius: 0 0 16px 16px;
  padding: 32px;
  border: 1px solid #e5e7eb;
  border-top: none;
  min-height: 400px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

:deep(.el-tabs__active-bar) {
  display: none;
}

/* 纠纷结果区域样式 */
.result-section {
  margin-top: 20px;
  background: #f0fdf4;
  border-radius: 8px;
  padding: 16px;
  border: 1px solid #86efac;

  .result-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 16px;

    .result-icon {
      font-size: 20px;
      color: #059669;
      animation: bounce 1s ease infinite;
    }

    .result-title {
      font-size: 15px;
      font-weight: 600;
      color: #059669;
    }
  }

  .result-content {
    .winner-info {
      margin-bottom: 12px;

      .winner-tag {
        font-size: 14px;
        padding: 6px 12px;
        border-radius: 4px;
        background: linear-gradient(45deg, #059669, #10b981);
      }
    }

    .admin-remark {
      background: #fff;
      border-radius: 6px;
      padding: 12px;

      .remark-label {
        font-size: 13px;
        color: #374151;
        margin-bottom: 8px;
        font-weight: 500;
      }

      .remark-text {
        font-size: 14px;
        color: #1f2937;
        line-height: 1.6;
      }
    }
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-3px);
  }
}
</style> 