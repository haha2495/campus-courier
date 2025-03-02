<template>
  <div class="tasks-manage">
    <!-- 顶部筛选区域 -->
    <el-card class="search-card" shadow="hover">
      <div class="search-area">
        <div class="filter-wrapper">
          <!-- 状态筛选器 -->
          <div class="custom-filter-group">
            <div
              v-for="option in taskStatusOptions"
              :key="option.value"
              class="filter-option"
              :class="{ active: searchForm.status === option.value }"
              @click="selectStatus(option.value)"
            >
              <div class="option-content">
                <el-icon v-if="option.icon" class="option-icon">
                  <component :is="option.icon"/>
                </el-icon>
                <span>{{ option.label }}</span>
              </div>
              <div class="active-indicator"></div>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 内容区域 -->
    <el-card class="content-card" shadow="hover">
      <div class="content-area" v-loading="loading">
        <el-empty v-if="taskList.length === 0" description="暂无任务数据"/>
        <div v-else class="tasks-list">
          <div v-for="item in taskList" :key="item.id" class="task-card" @click="handleViewDetail(item)">
            <!-- 卡片头部 - 任务编号和状态 -->
            <div class="card-header">
              <div class="left-section">
                <span class="task-no">{{ item.taskNo }}</span>
                <el-tag
                  :type="getStatusType(item.status)"
                  effect="light"
                  class="status-tag"
                >
                  {{ getStatusText(item.status) }}
                </el-tag>
              </div>
              <div v-if="item.urgent" class="urgent-tag">加急</div>
            </div>

            <!-- 卡片内容 -->
            <div class="card-body">
              <!-- 任务类型 -->
              <div class="task-type">
                <img class="type-icon" :src="item.type?.icon" alt="任务类型">
                <span>{{ item.type?.name }}</span>
              </div>

              <!-- 任务标题和描述 -->
              <div class="task-info">
                <div class="task-title">{{ item.title }}</div>
                <div class="task-desc">{{ item.description }}</div>
              </div>

              <!-- 地址信息 -->
              <div class="address-info">
                <div class="address-item" v-if="item.pickupAddress">
                  <div class="address-icon pickup">取</div>
                  <div class="address-content">
                    <div class="address-label">取件地址</div>
                    <div class="address-value">{{ item.pickupAddress }}</div>
                  </div>
                </div>
                <div class="address-item" v-if="item.deliveryAddress">
                  <div class="address-icon delivery">送</div>
                  <div class="address-content">
                    <div class="address-label">送达地址</div>
                    <div class="address-value">{{ item.deliveryAddress }}</div>
                  </div>
                </div>
              </div>

              <!-- 规格信息 -->
              <div class="specs-info">
                <div class="spec-item">
                  <span class="spec-label">重量</span>
                  <span class="spec-value">{{ item.estimatedWeight }}kg</span>
                </div>
                <div class="spec-item">
                  <span class="spec-label">大小</span>
                  <span class="spec-value">{{ item.estimatedSize }}</span>
                </div>
              </div>
            </div>

            <!-- 卡片底部 -->
            <div class="card-footer">
              <div class="left-info">
                <div class="price-info">
                  <span class="amount">{{ item.reward }}</span>
                  <span v-if="item.urgent" class="urgent-price">+{{ item.urgentReward }}</span>
                </div>
                <div class="time-info">
                  <el-icon><Timer /></el-icon>
                  <span>{{ formatDateTime(item.createTime) }}</span>
                </div>
              </div>
              <el-button type="primary" plain @click.stop="handleViewDetail(item)">
                查看详情
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 分页区域 -->
    <el-card class="pagination-card" shadow="hover">
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :page-sizes="[4, 8, 12, 16, 20]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Timer } from '@element-plus/icons-vue'

const router = useRouter()

// 任务状态选项
const taskStatusOptions = [
  { label: '待接单', value: 0, icon: 'Clock' },
  { label: '已接单', value: 1, icon: 'Loading' },
  { label: '已完成', value: 2, icon: 'Select' },
  { label: '已取消', value: 3, icon: 'Close' }
]

// 搜索表单
const searchForm = ref({
  status: ''
})

// 表格数据
const loading = ref(false)
const taskList = ref([]) // 暂时使用空数组
const page = ref(1)
const pageSize = ref(4)
const total = ref(0)

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    0: 'info',
    1: 'warning',
    2: 'success',
    3: 'danger'
  }
  return statusMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待接单',
    1: '已接单',
    2: '已完成',
    3: '已取消'
  }
  return statusMap[status] || '未知'
}

// 选择状态
const selectStatus = (status) => {
  searchForm.value.status = status
  page.value = 1
  // 暂时不执行实际搜索
}

// 格式化日期时间
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return ''
  const date = new Date(dateTimeStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  }).replace(/\//g, '-')
}

// 查看详情
const handleViewDetail = (row) => {
  router.push(`/admin/tasks/detail?id=${row.id}`)
}

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val
  // 暂时不执行实际加载
}

// 页码改变
const handleCurrentChange = (val) => {
  page.value = val
  // 暂时不执行实际加载
}
</script>

<style scoped lang="scss">
.tasks-manage {
  padding: 16px;
  min-height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
  gap: 16px;
}

/* 搜索卡片样式 */
.search-card {
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  }
}

/* 内容卡片样式 */
.content-card {
  flex: 1;
  border-radius: 12px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  background: #fff;
  
  &:hover {
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  }

  :deep(.el-card__body) {
    padding: 20px;
  }
}

.content-area {
  min-height: 300px;
}

/* 自定义状态筛选器样式 */
.custom-filter-group {
  display: flex;
  background-color: #f0f2f5;
  border-radius: 12px;
  padding: 3px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.filter-option {
  position: relative;
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 10px;
  margin: 2px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  overflow: hidden;
}

.option-content {
  display: flex;
  align-items: center;
  gap: 6px;
  z-index: 2;
  position: relative;
}

.option-icon {
  font-size: 14px;
  color: inherit;
}

.filter-option:hover {
  background-color: rgba(255, 255, 255, 0.6);
}

.filter-option.active {
  background-color: white;
  color: #409EFF;
  font-weight: 500;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
  transform: translateY(-1px);
}

.active-indicator {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 3px;
  background-color: #409EFF;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 3px;
  transform: translateX(-50%);
}

.filter-option.active .active-indicator {
  width: 60%;
}

/* 卡片网格布局 */
.tasks-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  padding: 16px;
}

/* 卡片基础样式 */
.task-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #ebeef5;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  min-width: 320px;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  }

  /* 卡片头部 */
  .card-header {
    padding: 12px 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #f0f0f0;
    background: #fafafa;

    .left-section {
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .task-no {
      font-family: monospace;
      color: #666;
      font-size: 13px;
    }

    .status-tag {
      font-size: 12px;
      padding: 2px 10px;
      border-radius: 12px;
    }

    .urgent-tag {
      background: #fff2f0;
      color: #ff4d4f;
      padding: 2px 8px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 500;
      display: flex;
      align-items: center;
      gap: 4px;

      &::before {
        content: '🔥';
        font-size: 14px;
      }
    }
  }

  /* 卡片内容 */
  .card-body {
    padding: 20px;
    display: flex;
    flex-direction: column;
    gap: 16px;

    .task-type {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 12px;

      .type-icon {
        width: 24px;
        height: 24px;
        border-radius: 4px;
      }

      span {
        font-size: 14px;
        color: #333;
        font-weight: 500;
      }
    }

    .task-info {
      margin-bottom: 16px;

      .task-title {
        font-size: 16px;
        font-weight: 600;
        color: #333;
        margin-bottom: 8px;
        line-height: 1.4;
      }

      .task-desc {
        font-size: 14px;
        color: #666;
        line-height: 1.6;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }
    }

    .address-info {
      margin-bottom: 20px;

      .address-item {
        display: flex;
        align-items: flex-start;
        gap: 16px;
        margin-bottom: 16px;

        &:last-child {
          margin-bottom: 0;
        }

        .address-icon {
          width: 28px;
          height: 28px;
          border-radius: 6px;
          display: flex;
          align-items: center;
          justify-content: center;
          color: white;
          font-size: 12px;
          flex-shrink: 0;

          &.pickup {
            background: #409eff;
          }

          &.delivery {
            background: #67c23a;
          }
        }

        .address-content {
          flex: 1;

          .address-label {
            font-size: 12px;
            color: #999;
            margin-bottom: 4px;
          }

          .address-value {
            font-size: 14px;
            color: #333;
            line-height: 1.4;
          }
        }
      }
    }

    .specs-info {
      padding: 16px 20px;
      margin-bottom: 20px;
      display: flex;
      gap: 24px;
      background: #f9fafb;
      border-radius: 8px;

      .spec-item {
        display: flex;
        align-items: center;
        gap: 8px;

        .spec-label {
          font-size: 13px;
          color: #999;
        }

        .spec-value {
          font-size: 14px;
          color: #333;
          font-weight: 500;
        }
      }
    }
  }

  /* 卡片底部 */
  .card-footer {
    padding: 16px 20px;
    border-top: 1px solid #f0f0f0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #fafafa;

    .left-info {
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    .price-info {
      display: flex;
      align-items: baseline;
      gap: 4px;

      .amount {
        font-size: 20px;
        font-weight: 600;
        color: #ff9500;

        &::before {
          content: '¥';
          font-size: 14px;
          margin-right: 2px;
        }
      }

      .urgent-price {
        font-size: 14px;
        color: #ff4d4f;
        font-weight: 500;
        margin-left: 4px;
      }
    }

    .time-info {
      display: flex;
      align-items: center;
      gap: 4px;
      color: #999;
      font-size: 12px;

      .el-icon {
        font-size: 14px;
      }
    }

    .el-button {
      padding: 8px 20px;
      border-radius: 20px;
      font-size: 14px;
    }
  }
}

/* 分页卡片样式 */
.pagination-card {
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 8px 0;
}

/* 调整分页选项 */
:deep(.el-pagination) {
  .el-pagination__sizes {
    .el-select {
      .el-input {
        width: 110px;
      }
    }
  }
}
</style> 