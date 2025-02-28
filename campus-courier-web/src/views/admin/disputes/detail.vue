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
        <div class="title">纠纷详情</div>
      </div>
    </el-card>

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

          <!-- 纠纷信息 -->
          <div class="dispute-section" v-if="detail.dispute">
            <h3 class="section-title">纠纷信息</h3>
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
                    <span>{{ getDisputeStatus(detail.dispute.status) }}</span>
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
                          <el-icon><Picture /></el-icon>
                          <span>加载失败</span>
                        </div>
                      </template>
                    </el-image>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 任务日志和留言记录 -->
          <el-tabs class="detail-tabs">
            <el-tab-pane label="任务日志">
              <div class="logs-section">
                <el-timeline>
                  <el-timeline-item
                      v-for="log in detail.taskLogs"
                      :key="log.id"
                      :timestamp="formatTime(log.actionTime)"
                      :type="getLogType(log.action)"
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
                              <el-icon><Picture /></el-icon>
                            </div>
                          </template>
                        </el-image>
                      </div>
                    </div>
                  </el-timeline-item>
                </el-timeline>
              </div>
            </el-tab-pane>

            <el-tab-pane label="留言记录">
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
                                <el-icon><Picture /></el-icon>
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
          </el-tabs>
        </template>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ArrowLeft, Phone, Picture} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {getDisputeDetail} from '@/api/dispute'
import {TaskStatusEnum} from '@/utils/enums/taskEnums'
import {DisputeStatusEnum} from '@/utils/enums/disputeEnums'
import {DisputeTypeEnum} from "@/utils/enums/disputeTypeEnums.js";
import {getTaskDetail} from "@/api/task.js";

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const detail = ref(null)

// 获取路由参数
const taskId = route.query.taskId

// 返回列表页
const handleBack = () => {
  router.back()
}

// 拨打电话
const handleCall = (phone) => {
  window.location.href = `tel:${phone}`
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

// 获取纠纷状态
const getDisputeStatus = (status) => {
  return status ? DisputeStatusEnum.getDesc(status) : ''
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
</script>

<style scoped lang="scss">
.dispute-detail {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
  padding: 16px;
  box-sizing: border-box;
  overflow: hidden;
}

/* 头部卡片样式 */
.header-card {
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 16px;
  flex-shrink: 0;

  :deep(.el-card__body) {
    padding: 16px 20px;
  }
}

.header-wrapper {
  display: flex;
  align-items: center;
  position: relative;
  height: 32px;

  .back-section {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    color: #666;
    transition: all 0.3s;
    position: absolute;
    left: 0;

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
}

/* 内容卡片样式 */
.content-card {
  flex: 1;
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;

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
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background-color: #dcdfe6;
    border-radius: 3px;
  }

  &::-webkit-scrollbar-track {
    background-color: #f5f7fa;
  }
}

/* 各个区块通用样式 */
.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  align-items: center;

  &::before {
    content: '';
    width: 4px;
    height: 18px;
    background-color: #409EFF;
    margin-right: 8px;
    border-radius: 2px;
  }
}

/* 任务信息区域 */
.task-info-section {
  margin-bottom: 24px;

  .task-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    .task-type {
      display: flex;
      align-items: center;
      gap: 12px;

      .urgent-tag {
        padding: 2px 8px;
        border-radius: 4px;
        font-size: 12px;
      }

      .type-icon {
        width: 24px;
        height: 24px;
        border-radius: 4px;
      }

      span {
        font-size: 16px;
        font-weight: 500;
        color: #333;
      }
    }

    .status-tag {
      font-size: 12px;
      padding: 2px 10px;
      border-radius: 12px;
    }
  }

  .task-content {
    background: #f9fafb;
    padding: 16px;
    border-radius: 8px;

    h4 {
      font-size: 16px;
      color: #333;
      margin-bottom: 8px;
    }

    p {
      color: #666;
      line-height: 1.6;
    }
  }
}

/* 配送信息区域 */
.delivery-section, .contact-section {
  margin-bottom: 24px;

  .info-content {
    background: #f9fafb;
    border-radius: 8px;
    overflow: hidden;

    .info-item {
      display: flex;
      padding: 16px;
      border-bottom: 1px solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .info-label {
        width: 100px;
        color: #666;
        font-size: 14px;
      }

      .info-value {
        flex: 1;
        color: #333;
        font-size: 14px;

        &.phone-wrapper {
          display: flex;
          align-items: center;
          justify-content: space-between;
        }
      }
    }
  }
}

/* 时间信息区域 */
.time-section {
  margin-bottom: 24px;

  .info-content {
    background: #f9fafb;
    border-radius: 8px;
    overflow: hidden;

    .info-item {
      display: flex;
      padding: 16px;
      border-bottom: 1px solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .info-label {
        width: 100px;
        color: #666;
        font-size: 14px;
      }

      .info-value {
        flex: 1;
        color: #333;
        font-size: 14px;
      }
    }
  }
}

/* 备注说明区域 */
.remark-section {
  margin-bottom: 24px;

  .info-content {
    background: #f9fafb;
    border-radius: 8px;
    overflow: hidden;

    .remark-content {
      padding: 16px;
      color: #666;
      font-size: 14px;
      line-height: 1.6;
      min-height: 60px;
    }
  }
}

/* 物品信息和赏金信息并排布局 */
.info-row {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;

  .item-section,
  .reward-section {
    flex: 1;
    margin-bottom: 0;
  }
}

/* 物品信息区域 */
.item-section {
  .specs-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;

    .spec-item {
      background: #f9fafb;
      padding: 16px;
      border-radius: 8px;
      text-align: center;

      .info-label {
        display: block;
        color: #666;
        font-size: 14px;
        margin-bottom: 8px;
      }

      .info-value {
        color: #333;
        font-size: 16px;
        font-weight: 500;
      }
    }
  }
}

/* 赏金信息区域 */
.reward-section {
  .reward-content {
    background: linear-gradient(135deg, #fff9f0, #fff5f5);
    border-radius: 8px;
    padding: 16px;

    .reward-row {
      display: flex;
      gap: 24px;

      .reward-item {
        flex: 1;
        background: #fff;
        border-radius: 8px;
        padding: 16px;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .reward-label {
          color: #666;
          font-size: 14px;
        }

        .reward-value {
          display: flex;
          align-items: baseline;

          .currency {
            color: #ff9500;
            font-size: 16px;
            margin-right: 4px;
          }

          .amount {
            color: #ff9500;
            font-size: 24px;
            font-weight: bold;
          }
        }

        &.urgent {
          .reward-value {
            .currency, .amount {
              color: #ff4d4f;
            }
          }
        }
      }
    }
  }
}

/* 用户信息区域 */
.users-section {
  margin-bottom: 24px;

  .user-cards {
    display: flex;
    gap: 24px;
  }

  .user-card {
    flex: 1;
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 16px;
    background: #f9fafb;
    border-radius: 8px;

    .user-role {
      font-size: 12px;
      color: #999;
      background: #fff;
      padding: 2px 8px;
      border-radius: 10px;
    }

    .user-info {
      .user-name {
        font-size: 14px;
        font-weight: 500;
        color: #333;
      }

      .user-no {
        font-size: 12px;
        color: #999;
      }
    }
  }
}

/* 纠纷信息区域 */
.dispute-section {
  margin-bottom: 24px;

  .dispute-content {
    background: #fff5f5;
    border-radius: 8px;
    padding: 20px;

    .dispute-header {
      margin-bottom: 20px;

      .dispute-tags {
        display: flex;
        gap: 12px;

        :deep(.dispute-tag) {
          display: inline-flex;
          align-items: center;
          height: 32px;
          padding: 0 12px;
          font-size: 14px;
          border-radius: 16px;
          border: none;

          .tag-icon {
            margin-right: 6px;
            font-size: 16px;
          }

          &.el-tag--warning {
            background-color: #fff7e6;
            color: #d46b08;
          }

          &.el-tag--danger {
            background-color: #fff1f0;
            color: #cf1322;
          }

          &.el-tag--success {
            background-color: #f6ffed;
            color: #389e0d;
          }
        }
      }
    }

    .dispute-main {
      h4 {
        font-size: 16px;
        color: #333;
        margin-bottom: 12px;
        font-weight: 500;
      }

      p {
        color: #666;
        line-height: 1.6;
        margin-bottom: 20px;
        font-size: 14px;
      }

      .evidence-list {
        h5 {
          font-size: 14px;
          color: #333;
          margin-bottom: 12px;
          font-weight: 500;
        }

        .evidence-grid {
          display: grid;
          grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
          gap: 8px;
          margin-top: 12px;
        }

        .evidence-image {
          width: 100%;
          height: 80px;
          border-radius: 6px;
          overflow: hidden;
          cursor: pointer;
          
          .el-image__inner {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s ease;
          }
          
          &:hover .el-image__inner {
            transform: scale(1.05);
          }
          
          .image-error {
            height: 100%;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            color: #909399;
            font-size: 12px;
            background: #f5f7fa;
            
            .el-icon {
              font-size: 20px;
              margin-bottom: 4px;
            }
          }
        }
      }
    }
  }
}

/* 标签页样式 */
.detail-tabs {
  margin-top: 24px;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);

  :deep(.el-tabs__header) {
    margin-bottom: 20px;
  }

  :deep(.el-tabs__nav-wrap::after) {
    height: 1px;
    background-color: #f0f0f0;
  }

  :deep(.el-tabs__item) {
    font-size: 15px;
    color: #666;
    padding: 0 20px;
    height: 40px;
    line-height: 40px;

    &.is-active {
      color: #409EFF;
      font-weight: 500;
    }

    &:hover {
      color: #409EFF;
    }
  }

  :deep(.el-tabs__active-bar) {
    height: 3px;
    border-radius: 1.5px;
  }
}

/* 任务日志区域样式优化 */
.logs-section {
  padding: 0 10px;

  :deep(.el-timeline-item__node) {
    width: 12px;
    height: 12px;
  }

  :deep(.el-timeline-item__timestamp) {
    color: #999;
    font-size: 13px;
  }

  .log-content {
    background: #f9fafb;
    padding: 12px 16px;
    border-radius: 8px;
    margin-bottom: 8px;

    .log-text {
      color: #333;
      line-height: 1.6;
      font-size: 14px;
    }
  }
}

/* 留言记录区域样式优化 */
.messages-section {
  padding: 0 10px;

  .message-item {
    margin-bottom: 20px;
    
    &.self-message {
      flex-direction: row-reverse;
      
      .message-content {
        align-items: flex-end;
        
        .message-header {
          flex-direction: row-reverse;
        }
        
        .message-body {
          background: #e6f3ff;
          border-radius: 8px 2px 8px 8px;
        }
      }
    }
  }

  .message-content {
    .message-body {
      background: #f9fafb;
      padding: 12px;
      border-radius: 2px 8px 8px 8px;
      margin-top: 4px;
    }
  }
}

/* 纠纷信息区域的图片样式 */
.evidence-list {
  .evidence-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
    gap: 8px;
    margin-top: 12px;
  }

  :deep(.evidence-image) {
    width: 100%;
    height: 80px;
    border-radius: 6px;
    overflow: hidden;
    cursor: pointer;
    
    .el-image__inner {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }
    
    &:hover .el-image__inner {
      transform: scale(1.05);
    }
    
    .image-error {
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #909399;
      font-size: 12px;
      background: #f5f7fa;
      
      .el-icon {
        font-size: 20px;
        margin-bottom: 4px;
      }
    }
  }
}

/* 任务日志图片样式 */
.log-images {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 8px;
  margin-top: 8px;

  .log-image {
    width: 100%;
    height: 80px;
    border-radius: 6px;
    overflow: hidden;
    cursor: pointer;

    .el-image__inner {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    &:hover .el-image__inner {
      transform: scale(1.05);
    }
  }
}

/* 消息图片样式 */
.message-images {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 8px;
  margin-top: 8px;

  .message-image {
    width: 100%;
    height: 80px;
    border-radius: 6px;
    overflow: hidden;
    cursor: pointer;

    .el-image__inner {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    &:hover .el-image__inner {
      transform: scale(1.05);
    }
  }
}

/* 图片预览样式优化 */
:deep(.el-image-viewer__wrapper) {
  .el-image-viewer__mask {
    background-color: rgba(0, 0, 0, 0.9);
  }
  
  .el-image-viewer__close {
    color: #fff;
    font-size: 24px;
    
    &:hover {
      color: #409EFF;
    }
  }
  
  .el-image-viewer__prev,
  .el-image-viewer__next {
    background-color: rgba(255, 255, 255, 0.1);
    color: #fff;
    font-size: 24px;
    
    &:hover {
      background-color: rgba(255, 255, 255, 0.2);
    }
  }

  .el-image-viewer__actions {
    opacity: 0.9;
    
    &:hover {
      opacity: 1;
    }
  }
}
</style> 