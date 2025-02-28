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

          <!-- 用户信息 -->
          <div class="users-section">
            <h3 class="section-title">相关用户</h3>
            <div class="user-cards">
              <div class="user-card">
                <div class="user-role">发布者</div>
                <el-avatar :src="detail.publisher.avatar"/>
                <div class="user-info">
                  <div class="user-name">{{ detail.publisher.name }}</div>
                  <div class="user-no">{{ detail.publisher.studentNo }}</div>
                </div>
              </div>
              <div class="user-card" v-if="detail.runner">
                <div class="user-role">跑腿者</div>
                <el-avatar :src="detail.runner.avatar"/>
                <div class="user-info">
                  <div class="user-name">{{ detail.runner.name }}</div>
                  <div class="user-no">{{ detail.runner.studentNo }}</div>
                </div>
              </div>
            </div>
          </div>

          <!-- 纠纷信息 -->
          <div class="dispute-section" v-if="detail.dispute">
            <h3 class="section-title">纠纷信息</h3>
            <div class="dispute-content">
              <div class="dispute-header">
                <el-tag :type="getDisputeTypeStyle(detail.dispute.type)">
                  <el-icon>
                    <component :is="getDisputeTypeIcon(detail.dispute.type)"/>
                  </el-icon>
                  {{ getDisputeType(detail.dispute.type) }}
                </el-tag>
                <el-tag :type="DisputeStatusEnum.getType(detail.dispute.status)">
                  <el-icon>
                    <component :is="DisputeStatusEnum.getIcon(detail.dispute.status)"/>
                  </el-icon>
                  {{ getDisputeStatus(detail.dispute.status) }}
                </el-tag>
              </div>
              <h4>{{ detail.dispute.title }}</h4>
              <p>{{ detail.dispute.content }}</p>
              <!-- 证据图片 -->
              <div class="evidence-list" v-if="detail.dispute.evidence?.length">
                <h5>证据图片</h5>
                <el-image-group>
                  <el-image
                      v-for="(url, index) in detail.dispute.evidence"
                      :key="index"
                      :src="url"
                      :preview-src-list="detail.dispute.evidence"
                      fit="cover"
                      class="evidence-image"
                  />
                </el-image-group>
              </div>
            </div>
          </div>

          <!-- 任务日志 -->
          <div class="logs-section">
            <h3 class="section-title">任务日志</h3>
            <el-timeline>
              <el-timeline-item
                  v-for="log in detail.taskLogs"
                  :key="log.id"
                  :timestamp="log.createTime"
                  :type="getLogType(log.action)"
              >
                {{ log.content }}
              </el-timeline-item>
            </el-timeline>
          </div>

          <!-- 留言记录 -->
          <div class="messages-section">
            <h3 class="section-title">留言记录</h3>
            <div class="message-list">
              <div
                  v-for="msg in detail.messages"
                  :key="msg.id"
                  class="message-item"
              >
                <el-avatar :src="msg.sender.avatar" size="small"/>
                <div class="message-content">
                  <div class="message-header">
                    <span class="sender-name">{{ msg.sender.name }}</span>
                    <span class="message-time">{{ msg.createTime }}</span>
                  </div>
                  <div class="message-text">{{ msg.content }}</div>
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ArrowLeft} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {getTaskDetail} from '@/api/task'
import {TaskStatusEnum} from '@/utils/enums/taskEnums'
import {DisputeStatusEnum} from '@/utils/enums/disputeEnums'
import {DisputeTypeEnum} from "@/utils/enums/disputeTypeEnums.js";

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

// 获取任务状态类型
const getStatusType = (status) => {
  return TaskStatusEnum.getType(status)
}

// 获取任务状态文本
const getStatusText = (status) => {
  return TaskStatusEnum.getDesc(status)
}

// 获取纠纷状态
const getDisputeStatus = (status) => {
  return DisputeStatusEnum.getDesc(status)
}

// 获取纠纷类型
const getDisputeType = (type) => {
  return DisputeTypeEnum.getDesc(type)
}

// 获取纠纷类型样式
const getDisputeTypeStyle = (type) => {
  return DisputeTypeEnum.getType(type)
}

// 获取纠纷类型图标
const getDisputeTypeIcon = (type) => {
  return DisputeTypeEnum.getIcon(type)
}

// 获取日志类型
const getLogType = (action) => {
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
  padding: 16px;
  min-height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
  gap: 16px;
}

/* 头部卡片样式 */
.header-card {
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

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
  background: #fff;

  :deep(.el-card__body) {
    padding: 24px;
  }
}

.content-wrapper {
  min-height: inherit;
}

/* 各个区块通用样式 */
.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
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
      gap: 8px;

      .type-icon {
        width: 24px;
        height: 24px;
        border-radius: 4px;
      }
    }

    .status-tag {
      font-size: 12px;
      padding: 2px 10px;
      border-radius: 12px;
    }
  }

  .task-content {
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
    padding: 16px;

    .dispute-header {
      display: flex;
      gap: 10px;
      margin-bottom: 15px;

      .el-tag {
        display: flex;
        align-items: center;
        gap: 5px;
        padding: 6px 12px;

        .el-icon {
          font-size: 16px;
        }
      }
    }

    h4 {
      font-size: 16px;
      color: #333;
      margin-bottom: 8px;
    }

    p {
      color: #666;
      line-height: 1.6;
      margin-bottom: 16px;
    }

    .evidence-list {
      h5 {
        font-size: 14px;
        color: #333;
        margin-bottom: 12px;
      }

      .evidence-image {
        width: 100px;
        height: 100px;
        border-radius: 4px;
        margin-right: 8px;
        margin-bottom: 8px;
        cursor: pointer;
      }
    }
  }
}

/* 任务日志区域 */
.logs-section {
  margin-bottom: 24px;
}

/* 留言记录区域 */
.messages-section {
  .message-list {
    .message-item {
      display: flex;
      gap: 12px;
      margin-bottom: 16px;

      .message-content {
        flex: 1;

        .message-header {
          display: flex;
          justify-content: space-between;
          margin-bottom: 4px;

          .sender-name {
            font-size: 14px;
            font-weight: 500;
            color: #333;
          }

          .message-time {
            font-size: 12px;
            color: #999;
          }
        }

        .message-text {
          font-size: 14px;
          color: #666;
          line-height: 1.6;
          background: #f9fafb;
          padding: 8px 12px;
          border-radius: 4px;
        }
      }
    }
  }
}
</style> 