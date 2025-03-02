<template>
  <div class="task-manage">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="任务状态">
          <el-select v-model="searchForm.status" placeholder="请选择任务状态" clearable>
            <el-option
              v-for="item in taskStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 任务列表 -->
    <el-card class="content-card">
      <div class="task-list">
        <el-table
          v-loading="loading"
          :data="taskList"
          border
          style="width: 100%"
        >
          <el-table-column prop="id" label="任务ID" width="80" />
          <el-table-column prop="title" label="任务标题" min-width="200" />
          <el-table-column label="任务状态" width="120">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-button
                link
                type="primary"
                @click="handleViewDetail(row)"
              >
                查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 任务状态选项
const taskStatusOptions = [
  { label: '待接单', value: 0 },
  { label: '已接单', value: 1 },
  { label: '已完成', value: 2 },
  { label: '已取消', value: 3 }
]

// 搜索表单
const searchForm = ref({
  status: ''
})

// 表格数据
const loading = ref(false)
const taskList = ref([]) // 暂时使用空数组
const page = ref(1)
const pageSize = ref(10)
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

// 搜索
const handleSearch = () => {
  page.value = 1
  // 暂时不执行实际搜索
}

// 重置
const handleReset = () => {
  searchForm.value = {
    status: ''
  }
  handleSearch()
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
.task-manage {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
  }

  .content-card {
    .task-list {
      margin-bottom: 20px;
    }
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }
}
</style> 