<template>
  <div class="task-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <div class="search-area">
            <el-input
              v-model="searchKeyword"
              placeholder="请输入任务标题或描述搜索"
              style="width: 300px"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button @click="handleSearch">搜索</el-button>
              </template>
            </el-input>
            
            <el-select v-model="searchStatus" placeholder="任务状态" clearable>
              <el-option label="待接单" :value="0" />
              <el-option label="进行中" :value="1" />
              <el-option label="已完成" :value="2" />
              <el-option label="已取消" :value="3" />
            </el-select>
            
            <el-select v-model="searchTypeId" placeholder="任务类型" clearable>
              <el-option
                v-for="type in taskTypes"
                :key="type.id"
                :label="type.name"
                :value="type.id"
              />
            </el-select>
          </div>
        </div>
      </template>
      
      <el-table 
        :data="taskList" 
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="任务标题" />
        <el-table-column prop="typeName" label="任务类型" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reward" label="报酬" />
        <el-table-column prop="deadline" label="截止时间">
          <template #default="scope">
            {{ formatDateTime(scope.row.deadline) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间">
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      title="任务详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务标题">{{ detail.title }}</el-descriptions-item>
        <el-descriptions-item label="任务类型">{{ detail.typeName }}</el-descriptions-item>
        <el-descriptions-item label="发布者">{{ detail.publisherName }}</el-descriptions-item>
        <el-descriptions-item label="接单者">{{ detail.receiverName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="任务状态">
          <el-tag :type="getStatusType(detail.status)">
            {{ getStatusText(detail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="报酬金额">{{ detail.reward }}</el-descriptions-item>
        <el-descriptions-item label="截止时间">{{ formatDateTime(detail.deadline) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(detail.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="任务描述" :span="2">{{ detail.description }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 数据列表
const taskList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKeyword = ref('')
const searchStatus = ref(null)
const searchTypeId = ref(null)
const taskTypes = ref([])

// 详情相关
const dialogVisible = ref(false)
const detail = ref({})

// 获取任务类型列表
const loadTaskTypes = async () => {
  try {
    const response = await axios.get('/api/task-types')
    taskTypes.value = response.data.data.records
  } catch (error) {
    console.error('加载任务类型失败:', error)
  }
}

// 格式化日期时间
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return ''
  const date = new Date(dateTimeStr)
  return date.toLocaleString()
}

// 获取状态样式
const getStatusType = (status) => {
  const map = {
    0: '',
    1: 'warning',
    2: 'success',
    3: 'info'
  }
  return map[status]
}

// 获取状态文本
const getStatusText = (status) => {
  const map = {
    0: '待接单',
    1: '进行中',
    2: '已完成',
    3: '已取消'
  }
  return map[status]
}

// 加载数据
const loadData = async () => {
  try {
    loading.value = true
    const response = await axios.get('/api/tasks', {
      params: {
        keyword: searchKeyword.value,
        status: searchStatus.value,
        typeId: searchTypeId.value,
        pageNum: currentPage.value,
        pageSize: pageSize.value
      }
    })
    const { data } = response.data
    taskList.value = data.records
    total.value = data.total
  } catch (error) {
    ElMessage.error('加载数据失败')
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadData()
}

// 分页
const handleSizeChange = (val) => {
  pageSize.value = val
  loadData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadData()
}

// 查看详情
const handleView = async (row) => {
  try {
    const response = await axios.get(`/api/tasks/${row.id}`)
    detail.value = response.data.data
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
    console.error('获取详情失败:', error)
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadTaskTypes()
  loadData()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.search-area {
  display: flex;
  align-items: center;
  gap: 12px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 