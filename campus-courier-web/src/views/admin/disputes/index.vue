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
          <task-card
            v-for="item in tableData"
            :key="item.id"
            :task="item.task"
            :task-type="item.taskType"
            action-text="前去处理"
            @click="handleView"
            @action="handleView"
          />
        </div>
      </div>
    </el-card>

    <!-- 分页区域 -->
    <el-card class="pagination-card" shadow="hover">
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="currentPage"
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
import {onMounted, onUnmounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {
  Timer, User, UserFilled, View, Warning
} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {DisputeStatusEnum} from '@/utils/enums/disputeEnums'
import {pageDisputes} from '@/api/dispute'
import { TaskStatusEnum } from '@/utils/enums/taskEnums'
import TaskCard from '@/components/task-card/index.vue'

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
const pageSize = ref(4)  // 默认每页显示4条

// 获取状态类型
const getStatusType = (status) => {
  return TaskStatusEnum.getType(status)
}

// 获取状态文本
const getStatusText = (status) => {
  return TaskStatusEnum.getDesc(status)
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
  console.log(row)
  router.push({
    path: '/admin/disputes/detail',
    query: {
      taskId: row.id
    }
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
  display: flex;
  flex-direction: column;
  overflow: hidden;
  
  &:hover {
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  }

  :deep(.el-card__body) {
    padding: 20px;
    height: 100%;
    display: flex;
    flex-direction: column;
    overflow: hidden;
  }
}

.content-area {
  height: calc(100vh - 250px);
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .disputes-list {
    height: 100%;
    overflow-y: auto;
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 24px;
    padding: 16px;
    align-content: start;
    grid-auto-rows: min-content;

    /* 美化滚动条 */
    &::-webkit-scrollbar {
      width: 8px;
      height: 8px;
    }

    &::-webkit-scrollbar-track {
      background: #f1f1f1;
      border-radius: 4px;
    }

    &::-webkit-scrollbar-thumb {
      background: #ccc;
      border-radius: 4px;
    }

    &::-webkit-scrollbar-thumb:hover {
      background: #999;
    }
  }
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
        width: 110px;  // 调整选择器宽度
      }
    }
  }
}
</style> 