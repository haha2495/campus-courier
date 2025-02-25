<template>
  <div class="task-type-manage">
    <!-- 顶部搜索区域 -->
    <el-card class="search-card">
      <div class="search-area">
        <div class="search-input-wrapper">
          <!-- 类型名称搜索 -->
          <el-input
              v-model="searchForm.typeName"
              placeholder="搜索任务类型名称"
              clearable
              class="search-input"
          >
            <template #prefix>
              <el-icon class="search-icon">
                <Document/>
              </el-icon>
            </template>
          </el-input>

          <!-- 状态筛选 -->
          <el-select
              v-model="searchForm.status"
              placeholder="选择状态"
              clearable
              class="search-select"
          >
            <el-option :value="1" label="启用"/>
            <el-option :value="0" label="禁用"/>
          </el-select>

          <div class="search-actions">
            <el-button type="primary" @click="handleSearch" class="search-btn">
              <el-icon>
                <Search/>
              </el-icon>
              <span>搜索</span>
            </el-button>
            <el-button @click="handleReset" class="reset-btn">
              <el-icon>
                <RefreshRight/>
              </el-icon>
              <span>重置</span>
            </el-button>
          </div>
        </div>
        <el-button type="primary" @click="handleAdd" class="add-btn">
          <el-icon>
            <Plus/>
          </el-icon>
          <span>新增任务类型</span>
        </el-button>
      </div>
    </el-card>

    <!-- 中间表格区域 -->
    <el-card class="table-card">
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
        <el-table-column prop="id" label="ID" width="80" align="center" />
        
        <!-- 图标列 -->
        <el-table-column label="图标" width="80" align="center">
          <template #default="{ row }">
            <el-avatar 
                :size="36" 
                :src="row.icon" 
                class="type-icon"
            >
              <el-icon><Document /></el-icon>
            </el-avatar>
          </template>
        </el-table-column>
        
        <!-- 类型名称列 -->
        <el-table-column prop="typeName" label="类型名称" min-width="120" />
        
        <!-- 描述列 -->
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        
        <!-- 优先级列 -->
        <el-table-column prop="priority" label="优先级" width="100" align="center" />
        
        <!-- 状态列 -->
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <!-- 创建时间列 -->
        <el-table-column prop="createTime" label="创建时间" width="180" />
        
        <!-- 操作列 -->
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="primary" text @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button 
              v-if="row.status === 0" 
              size="small" 
              type="success" 
              text
              @click="handleEnable(row)"
            >
              启用
            </el-button>
            <el-button 
              v-else 
              size="small" 
              type="warning" 
              text
              @click="handleDisable(row)"
            >
              禁用
            </el-button>
            <el-button 
              size="small" 
              type="danger" 
              text
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 底部分页区域 - 独立卡片 -->
    <el-card class="pagination-card">
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
import { ref, reactive, onMounted, computed } from 'vue'
import { Document, Search, RefreshRight, Plus } from '@element-plus/icons-vue'

// 搜索表单
const searchForm = reactive({
  typeName: '',
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

// 生命周期钩子
onMounted(() => {
  // 这里暂时不加载数据，只是创建界面结构
  // 后续可以添加数据加载逻辑
})

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  // 这里暂时不实现搜索逻辑
}

// 重置搜索
const handleReset = () => {
  searchForm.typeName = ''
  searchForm.status = null
  handleSearch()
}

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  // 这里暂时不实现分页逻辑
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  // 这里暂时不实现分页逻辑
}

// 添加任务类型
const handleAdd = () => {
  // 这里暂时不实现添加逻辑
}

// 编辑任务类型
const handleEdit = (row) => {
  // 这里暂时不实现编辑逻辑
}

// 启用任务类型
const handleEnable = (row) => {
  // 这里暂时不实现启用逻辑
}

// 禁用任务类型
const handleDisable = (row) => {
  // 这里暂时不实现禁用逻辑
}

// 删除任务类型
const handleDelete = (row) => {
  // 这里暂时不实现删除逻辑
}
</script>

<style scoped>
.task-type-manage {
  padding: 16px;
}

.search-card {
  margin-bottom: 16px;
}

.search-area {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-input-wrapper {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.search-input {
  width: 220px;
}

.search-select {
  width: 120px;
}

.search-actions {
  display: flex;
  gap: 8px;
}

.table-card {
  margin-bottom: 16px;
}

.table-row {
  cursor: pointer;
  transition: background-color 0.3s;
}

.table-row:hover {
  background-color: #f8fafc;
}

.type-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f3f4f6;
  color: #6b7280;
}

/* 分页卡片样式 */
.pagination-card {
  margin-bottom: 16px;
}

.pagination-container {
  display: flex;
  justify-content: center; /* 居中显示 */
  padding: 8px 0;
}
</style> 