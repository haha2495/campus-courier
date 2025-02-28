<template>
  <div class="disputes-manage">
    <!-- 顶部筛选区域 -->
    <el-card class="search-card" shadow="hover">
      <div class="search-area">
        <div class="filter-wrapper">
          <!-- 状态筛选器 -->
          <div class="custom-filter-group">
            <div
                v-for="option in filterOptions"
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
        <el-empty v-if="tableData.length === 0" description="暂无纠纷数据"/>
        <div v-else class="disputes-list">
          <div v-for="item in tableData" :key="item.id" class="dispute-card" @click="handleView(item)">
            <!-- 卡片内容保持不变 -->
            <div class="card-header">
              <div class="task-type">
                <img class="type-icon" :src="item.taskType.icon" alt="任务类型图标">
                <span>{{ item.taskType.typeName }}</span>
              </div>
              <div class="task-no">{{ item.task.taskNo }}</div>
              <el-tag
                  :type="getStatusType(item.task.status)"
                  effect="light"
                  class="status-tag"
              >
                {{ getStatusText(item.task.status) }}
              </el-tag>
            </div>

            <div class="card-body">
              <div class="task-title">{{ item.task.title }}</div>
              <div class="task-desc">{{ item.task.description }}</div>
              <div class="user-info">
                <div class="user-item">
                  <el-icon><UserFilled /></el-icon>
                  <span class="label">发布者：</span>
                  <span class="value">{{ item.publisherName }}</span>
                </div>
                <div class="user-item">
                  <el-icon><User /></el-icon>
                  <span class="label">接单者：</span>
                  <span class="value">{{ item.runnerName }}</span>
                </div>
              </div>
              <div class="time-info">
                <el-icon><Timer /></el-icon>
                <span>{{ formatDateTime(item.task.createTime) }}</span>
              </div>
            </div>

            <div class="card-footer">
              <el-button type="primary" @click.stop="handleView(item)">
                <el-icon><View /></el-icon>
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
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[8, 12, 16, 20]"
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
import {onMounted, onUnmounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {
  Timer, User, UserFilled, View, Warning
} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {DisputeStatusEnum} from '@/utils/enums/disputeEnums'
import {pageDisputes} from '@/api/dispute'

const router = useRouter()

// 筛选选项配置
const filterOptions = DisputeStatusEnum.getOptions()

// 搜索表单
const searchForm = reactive({
  status: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(8)  // 默认每页显示8条

// 获取状态类型
const getStatusType = (status) => {
  return DisputeStatusEnum.getType(status)
}

// 获取状态文本
const getStatusText = (status) => {
  return DisputeStatusEnum.getDesc(status)
}

// 选择状态
const selectStatus = (status) => {
  searchForm.status = status
  currentPage.value = 1
  fetchData()
}

// 查询数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      status: searchForm.status
    }

    const res = await pageDisputes(params)
    // 处理返回的数据
    tableData.value = res.records
    total.value = res.total
  } catch (error) {
    console.error('查询纠纷列表出错:', error)
    ElMessage.error('系统异常，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

// 查看详情
const handleView = (row) => {
  router.push({
    path: '/admin/disputes/detail',
    query: {id: row.id}
  })
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

// 生命周期钩子
onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.disputes-manage {
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
.disputes-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
  padding: 8px;
}

/* 纠纷卡片样式 */
.dispute-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
  }

  .card-header {
    padding: 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #f0f0f0;
    background: linear-gradient(to right, #f8fafc, #ffffff);

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
        font-size: 14px;
        color: #333;
        font-weight: 500;
      }
    }

    .task-no {
      font-size: 14px;
      color: #666;
      font-family: monospace;
    }

    .status-tag {
      border-radius: 20px;
      padding: 2px 12px;
      font-weight: 500;
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }

  .card-body {
    padding: 16px;
    flex: 1;

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
      margin-bottom: 16px;
      line-height: 1.5;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .user-info {
      margin-bottom: 16px;

      .user-item {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 8px;
        color: #666;
        font-size: 14px;

        .el-icon {
          font-size: 16px;
          color: #409EFF;
        }

        .label {
          color: #999;
        }

        .value {
          color: #333;
          font-weight: 500;
        }
      }
    }

    .time-info {
      display: flex;
      align-items: center;
      gap: 6px;
      color: #999;
      font-size: 13px;

      .el-icon {
        font-size: 14px;
      }
    }
  }

  .card-footer {
    padding: 12px 16px;
    border-top: 1px solid #f0f0f0;
    display: flex;
    justify-content: flex-end;
    background: #fafafa;

    .el-button {
      padding: 8px 16px;
      border-radius: 20px;
      
      .el-icon {
        margin-right: 4px;
      }
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
</style> 