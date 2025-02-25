<template>
  <div class="task-type-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <div class="search-area">
            <el-input
              v-model="searchKeyword"
              placeholder="请输入类型名称或描述搜索"
              style="width: 300px"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button @click="handleSearch">搜索</el-button>
              </template>
            </el-input>
          </div>
          <el-button type="primary" @click="handleAdd">新增类型</el-button>
        </div>
      </template>
      
      <el-table 
        :data="typeList" 
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="类型名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="createTime" label="创建时间">
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogType === 'add' ? '新增任务类型' : '编辑任务类型'"
      width="500px"
    >
      <el-form 
        :model="form" 
        :rules="rules" 
        ref="formRef" 
        label-width="120px"
        @submit.prevent
      >
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入类型名称"/>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit(formRef)">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

// 数据列表
const typeList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKeyword = ref('')

// 表单相关
const dialogVisible = ref(false)
const dialogType = ref('add')
const formRef = ref(null)
const form = ref({
  name: '',
  description: ''
})

// 表单校验规则
const rules = {
  name: [
    { required: true, message: '请输入类型名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入描述', trigger: 'blur' },
    { max: 200, message: '最大长度为200个字符', trigger: 'blur' }
  ]
}

// 格式化日期时间
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return ''
  const date = new Date(dateTimeStr)
  return date.toLocaleString()
}

// 加载数据
const loadData = async () => {
  try {
    loading.value = true
    const response = await axios.get('/api/task-types', {
      params: {
        keyword: searchKeyword.value,
        pageNum: currentPage.value,
        pageSize: pageSize.value
      }
    })
    const { data } = response.data
    typeList.value = data.records
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

// 新增
const handleAdd = () => {
  dialogType.value = 'add'
  form.value = { 
    name: '', 
    description: ''
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogType.value = 'edit'
  form.value = { ...row }
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除该任务类型吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await axios.delete(`/api/task-types/${row.id}`)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  })
}

// 提交表单
const handleSubmit = async (formEl) => {
  if (!formEl) return
  
  await formEl.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          await axios.post('/api/task-types', form.value)
          ElMessage.success('添加成功')
        } else {
          await axios.put(`/api/task-types/${form.value.id}`, form.value)
          ElMessage.success('编辑成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        ElMessage.error(dialogType.value === 'add' ? '添加失败' : '编辑失败')
        console.error('操作失败:', error)
      }
    }
  })
}

// 页面加载时获取数据
onMounted(() => {
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