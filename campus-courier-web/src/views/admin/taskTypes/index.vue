<template>
  <div class="task-type-manage">
    <!-- 顶部搜索区域 -->
    <el-card class="search-card" shadow="hover">
      <div class="search-area">
        <div class="filter-wrapper">
          <!-- 自定义状态筛选器 - 改为分段选择器样式 -->
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
                  <component :is="option.icon" />
                </el-icon>
                <span>{{ option.label }}</span>
              </div>
              <div class="active-indicator"></div>
            </div>
          </div>
        </div>
        
        <!-- 改进的新增按钮 -->
        <el-button 
          type="primary" 
          @click="handleAdd" 
          class="add-btn pulse-on-hover"
        >
          <div class="btn-content">
            <div class="icon-wrapper">
              <el-icon><Plus /></el-icon>
            </div>
            <span>新增任务类型</span>
          </div>
        </el-button>
      </div>
    </el-card>

    <!-- 中间表格区域 -->
    <el-card class="table-card" shadow="hover">
      <el-table
          :data="tableData"
          :height="tableHeight"
          v-loading="loading"
          :header-cell-style="{
            background: '#f8fafc',
            color: '#475569',
            fontWeight: '600',
            fontSize: '14px',
            height: '48px',
          }"
          :cell-style="{
            fontSize: '14px',
            color: '#1f2937',
            padding: '12px 0',
          }"
          row-class-name="table-row"
      >
        <!-- ID列 -->
        <el-table-column prop="id" label="ID" width="80" align="center"/>

        <!-- 图标列 -->
        <el-table-column label="图标" width="80" align="center">
          <template #default="{ row }">
            <el-avatar
                :size="36"
                :src="row.icon"
                class="type-icon"
            >
              <el-icon>
                <Document/>
              </el-icon>
            </el-avatar>
          </template>
        </el-table-column>

        <!-- 类型名称列 -->
        <el-table-column prop="typeName" label="类型名称" min-width="120"/>

        <!-- 描述列 -->
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip/>

        <!-- 优先级列 -->
        <el-table-column prop="priority" label="优先级" width="100" align="center"/>

        <!-- 状态列 -->
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
                :type="row.status === 1 ? 'success' : 'danger'" 
                size="small"
                effect="light"
                class="status-tag"
            >
              <el-icon v-if="row.status === 1" class="status-icon"><Check /></el-icon>
              <el-icon v-else class="status-icon"><Close /></el-icon>
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 创建时间列 -->
        <el-table-column prop="createTime" label="创建时间" width="180"/>

        <!-- 操作列 -->
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <div class="operation-buttons">
              <el-button size="small" type="primary" text @click="handleEdit(row)" class="op-btn">
                <el-icon><Edit /></el-icon>
                <span>编辑</span>
              </el-button>
              <el-button
                  v-if="row.status === 0"
                  size="small"
                  type="success"
                  text
                  @click="handleEnable(row)"
                  class="op-btn"
              >
                <el-icon><Check /></el-icon>
                <span>启用</span>
              </el-button>
              <el-button
                  v-else
                  size="small"
                  type="warning"
                  text
                  @click="handleDisable(row)"
                  class="op-btn"
              >
                <el-icon><Close /></el-icon>
                <span>禁用</span>
              </el-button>
              <el-button
                  size="small"
                  type="danger"
                  text
                  @click="handleDelete(row)"
                  class="op-btn"
              >
                <el-icon><Delete /></el-icon>
                <span>删除</span>
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 底部分页区域 - 独立卡片 -->
    <el-card class="pagination-card" shadow="hover">
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
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
import {ref, reactive, onMounted, computed} from 'vue'
import {useRouter} from 'vue-router'
import {Document, Plus, Check, Close, Edit, Delete, View, Filter, List} from '@element-plus/icons-vue'
import {pageTaskTypes} from '@/api/taskType'
import {ElMessage, ElMessageBox} from 'element-plus'

const router = useRouter()

// 筛选选项配置
const filterOptions = [
  { value: null, label: '全部', icon: 'List' },
  { value: 1, label: '启用', icon: 'Check' },
  { value: 0, label: '禁用', icon: 'Close' }
];

// 搜索表单 - 移除typeName，只保留status
const searchForm = reactive({
  status: null
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 计算表格高度
const tableHeight = computed(() => {
  return window.innerHeight - 360 // 调整高度以适应独立的分页卡片
})

// 查询任务类型数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      status: searchForm.status
    }

    const res = await pageTaskTypes(params)
    tableData.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('查询任务类型出错:', error)
    ElMessage.error('系统异常，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 选择状态
const selectStatus = (status) => {
  searchForm.status = status
  currentPage.value = 1
  fetchData()
}

// 生命周期钩子
onMounted(() => {
  fetchData()
})

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

// 添加任务类型
const handleAdd = () => {
  router.push('/admin/task-types/form')
}

// 编辑任务类型
const handleEdit = (row) => {
  router.push({
    path: '/admin/task-types/form',
    query: {id: row.id}
  })
}

// 启用任务类型
const handleEnable = (row) => {
  // 这里暂时不实现启用逻辑
  ElMessage({
    message: '启用功能待实现',
    type: 'success',
    duration: 2000
  })
}

// 禁用任务类型
const handleDisable = (row) => {
  // 这里暂时不实现禁用逻辑
  ElMessage({
    message: '禁用功能待实现',
    type: 'warning',
    duration: 2000
  })
}

// 删除任务类型
const handleDelete = (row) => {
  ElMessageBox.confirm(
      `确定要删除任务类型 "${row.typeName}" 吗?`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        // 这里暂时不实现删除逻辑
        ElMessage({
          message: '删除功能待实现',
          type: 'success',
          duration: 2000
        })
      })
      .catch(() => {
        // 用户取消删除操作
      })
}
</script>

<style scoped>
.task-type-manage {
  padding: 16px;
}

.search-card {
  margin-bottom: 16px;
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
}

.search-card:hover {
  transform: translateY(-2px);
}

.search-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-wrapper {
  display: flex;
  align-items: center;
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

/* 新增按钮样式 */
.add-btn {
  border-radius: 10px;
  height: 40px;
  transition: all 0.3s ease;
  border: none;
  background: linear-gradient(90deg, #409EFF, #67c23a);
  box-shadow: 0 3px 8px rgba(64, 158, 255, 0.2);
  overflow: hidden;
  position: relative;
}

.btn-content {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  z-index: 2;
  position: relative;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  width: 20px;
  height: 20px;
  transition: all 0.3s ease;
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(64, 158, 255, 0.3);
}

.add-btn:hover .icon-wrapper {
  transform: rotate(90deg);
}

.pulse-on-hover:hover:before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 120%;
  height: 120%;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  transform: translate(-50%, -50%) scale(0);
  animation: pulse-effect 0.6s ease-out;
}

@keyframes pulse-effect {
  0% {
    transform: translate(-50%, -50%) scale(0);
    opacity: 0.5;
  }
  100% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0;
  }
}

.table-card {
  margin-bottom: 16px;
  border-radius: 12px;
  border: none;
  overflow: hidden;
  transition: all 0.3s ease;
}

.table-row {
  cursor: pointer;
  transition: all 0.3s;
}

.table-row:hover {
  background-color: #f0f7ff !important;
  transform: translateY(-1px);
}

.type-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f3f4f6;
  color: #6b7280;
  border-radius: 50%;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.type-icon:hover {
  transform: scale(1.1);
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
}

/* 状态标签样式 */
.status-tag {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  border-radius: 20px;
  padding: 2px 10px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.status-tag:hover {
  transform: scale(1.05);
}

.status-icon {
  font-size: 12px;
}

/* 操作按钮样式 */
.operation-buttons {
  display: flex;
  gap: 4px;
}

.op-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  transition: all 0.3s ease;
}

.op-btn:hover {
  transform: translateY(-2px);
}

/* 分页卡片样式 */
.pagination-card {
  margin-bottom: 16px;
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 8px 0;
}
</style> 