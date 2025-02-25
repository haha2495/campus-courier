<template>
  <div class="dispute-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <div class="search-area">
            <el-input
              v-model="searchKeyword"
              placeholder="请输入纠纷标题或描述搜索"
              style="width: 300px"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button @click="handleSearch">搜索</el-button>
              </template>
            </el-input>
            
            <el-select v-model="searchStatus" placeholder="纠纷状态" clearable>
              <el-option label="待处理" :value="0" />
              <el-option label="处理中" :value="1" />
              <el-option label="已完成" :value="2" />
              <el-option label="已转发" :value="3" />
            </el-select>
            
            <el-select v-model="searchTypeId" placeholder="纠纷类型" clearable>
              <el-option
                v-for="type in disputeTypes"
                :key="type.id"
                :label="type.name"
                :value="type.id"
              />
            </el-select>
          </div>
        </div>
      </template>
      
      <el-table 
        :data="disputeList" 
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="纠纷标题" />
        <el-table-column prop="typeName" label="纠纷类型" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="appealerName" label="申诉人" />
        <el-table-column prop="createTime" label="创建时间">
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="scope">
            <el-button size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button 
              size="small" 
              type="primary" 
              @click="handleApprove(scope.row)"
              v-if="scope.row.status === 0"
            >审批</el-button>
            <el-button 
              size="small" 
              type="warning" 
              @click="handleForward(scope.row)"
              v-if="scope.row.status === 0"
            >转发</el-button>
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
      v-model="detailVisible" 
      title="纠纷详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="纠纷标题">{{ detail.title }}</el-descriptions-item>
        <el-descriptions-item label="纠纷类型">{{ detail.typeName }}</el-descriptions-item>
        <el-descriptions-item label="相关任务">{{ detail.taskTitle }}</el-descriptions-item>
        <el-descriptions-item label="申诉人">{{ detail.appealerName }}</el-descriptions-item>
        <el-descriptions-item label="被申诉人">{{ detail.defendantName }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ detail.handlerName || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="纠纷状态">
          <el-tag :type="getStatusType(detail.status)">
            {{ getStatusText(detail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(detail.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="纠纷描述" :span="2">{{ detail.description }}</el-descriptions-item>
        <el-descriptions-item label="处理结果" :span="2" v-if="detail.result">
          {{ detail.result }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog 
      v-model="approvalVisible" 
      title="审批纠纷"
      width="500px"
    >
      <el-form 
        :model="approvalForm" 
        :rules="approvalRules" 
        ref="approvalFormRef" 
        label-width="100px"
      >
        <el-form-item label="处理结果" prop="result">
          <el-input 
            v-model="approvalForm.result" 
            type="textarea" 
            :rows="4"
            placeholder="请输入处理结果"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="approvalVisible = false">取消</el-button>
          <el-button type="primary" @click="submitApproval">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

// 数据列表相关
const disputeList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKeyword = ref('')
const searchStatus = ref(null)
const searchTypeId = ref(null)
const disputeTypes = ref([])

// 详情相关
const detailVisible = ref(false)
const detail = ref({})

// 审批相关
const approvalVisible = ref(false)
const approvalFormRef = ref(null)
const approvalForm = ref({
  disputeId: null,
  result: ''
})
const approvalRules = {
  result: [
    { required: true, message: '请输入处理结果', trigger: 'blur' },
    { min: 5, max: 500, message: '长度在 5 到 500 个字符', trigger: 'blur' }
  ]
}

// 获取纠纷类型列表
const loadDisputeTypes = async () => {
  try {
    const response = await axios.get('/api/dispute-types')
    disputeTypes.value = response.data.data.records
  } catch (error) {
    console.error('加载纠纷类型失败:', error)
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
    0: '待处理',
    1: '处理中',
    2: '已完成',
    3: '已转发'
  }
  return map[status]
}

// 加载数据
const loadData = async () => {
  try {
    loading.value = true
    const response = await axios.get('/api/disputes', {
      params: {
        keyword: searchKeyword.value,
        status: searchStatus.value,
        typeId: searchTypeId.value,
        pageNum: currentPage.value,
        pageSize: pageSize.value
      }
    })
    const { data } = response.data
    disputeList.value = data.records
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
    const response = await axios.get(`/api/disputes/${row.id}`)
    detail.value = response.data.data
    detailVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
    console.error('获取详情失败:', error)
  }
}

// 审批
const handleApprove = (row) => {
  approvalForm.value = {
    disputeId: row.id,
    result: ''
  }
  approvalVisible.value = true
}

// 提交审批
const submitApproval = async () => {
  if (!approvalFormRef.value) return
  
  await approvalFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await axios.post('/api/disputes/approve', approvalForm.value)
        ElMessage.success('审批成功')
        approvalVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error('审批失败')
        console.error('审批失败:', error)
      }
    }
  })
}

// 转发
const handleForward = (row) => {
  ElMessageBox.confirm(
    '确定要将该纠纷转发到纠纷大厅吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await axios.post(`/api/disputes/${row.id}/forward`)
      ElMessage.success('转发成功')
      loadData()
    } catch (error) {
      ElMessage.error('转发失败')
      console.error('转发失败:', error)
    }
  })
}

// 页面加载时获取数据
onMounted(() => {
  loadDisputeTypes()
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
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 