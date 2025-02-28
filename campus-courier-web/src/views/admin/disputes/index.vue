<template>
  <div class="disputes-manage">
    <!-- 顶部搜索区域 -->
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

    <!-- 中间表格区域 -->
    <el-card class="table-card" shadow="hover">
      <el-table
          :data="tableData"
          v-loading="loading"
          :header-cell-style="headerCellStyle"
          :cell-style="cellStyle"
          row-class-name="table-row"
          border
          stripe
          :height="tableHeight"
      >
        <!-- 任务编号列 -->
        <el-table-column prop="taskNo" label="任务编号" min-width="120" align="left">
          <template v-slot:header>
            <div class="column-header">
              <el-icon class="header-icon">
                <Document/>
              </el-icon>
              <span class="header-text">任务编号</span>
            </div>
          </template>
        </el-table-column>

        <!-- 任务标题列 -->
        <el-table-column prop="title" label="任务标题" min-width="180" align="left">
          <template v-slot:header>
            <div class="column-header">
              <el-icon class="header-icon">
                <InfoFilled/>
              </el-icon>
              <span class="header-text">任务标题</span>
            </div>
          </template>
        </el-table-column>

        <!-- 纠纷状态列 -->
        <el-table-column prop="status" label="纠纷状态" width="100" align="center">
          <template v-slot:header>
            <div class="column-header">
              <el-icon class="header-icon">
                <Warning/>
              </el-icon>
              <span class="header-text">纠纷状态</span>
            </div>
          </template>
          <template #default="scope">
            <el-tag
                :type="getStatusType(scope.row.status)"
                effect="light"
                class="status-tag"
            >
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- 发布者列 -->
        <el-table-column prop="publisherName" label="发布者" width="120" align="center">
          <template v-slot:header>
            <div class="column-header">
              <el-icon class="header-icon">
                <User/>
              </el-icon>
              <span class="header-text">发布者</span>
            </div>
          </template>
        </el-table-column>

        <!-- 接单者列 -->
        <el-table-column prop="runnerName" label="接单者" width="120" align="center">
          <template v-slot:header>
            <div class="column-header">
              <el-icon class="header-icon">
                <User/>
              </el-icon>
              <span class="header-text">接单者</span>
            </div>
          </template>
        </el-table-column>

        <!-- 创建时间列 -->
        <el-table-column prop="createTime" label="创建时间" width="180" align="center">
          <template v-slot:header>
            <div class="column-header">
              <el-icon class="header-icon">
                <Calendar/>
              </el-icon>
              <span class="header-text">创建时间</span>
            </div>
          </template>
          <template #default="scope">
            <div class="time-cell">
              <el-icon>
                <Timer/>
              </el-icon>
              <span>{{ scope.row.createTime }}</span>
            </div>
          </template>
        </el-table-column>

        <!-- 操作列 -->
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template v-slot:header>
            <div class="column-header">
              <el-icon class="header-icon">
                <SetUp/>
              </el-icon>
              <span class="header-text">操作</span>
            </div>
          </template>
          <template #default="scope">
            <div class="operation-cell">
              <el-tooltip content="查看详情" placement="top" :effect="tooltipEffect">
                <el-button
                    circle
                    type="primary"
                    :icon="View"
                    @click="handleView(scope.row)"
                    class="op-icon-btn"
                />
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 底部分页区域 -->
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
import {onMounted, onUnmounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {Calendar, Document, InfoFilled, SetUp, Timer, User, View, Warning} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {DisputeStatusEnum} from '@/utils/enums/disputeEnums'
import {pageDisputes} from '@/api/dispute'

const router = useRouter()
const tooltipEffect = 'dark'

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
const pageSize = ref(10)
const tableHeight = ref(500)

// 表格样式
const headerCellStyle = () => {
  return {
    backgroundColor: '#f8fafc',
    color: '#64748b',
    fontWeight: '400',
    padding: '14px 8px',
    height: '52px',
    fontSize: '14px',
    transition: 'all 0.3s',
    borderBottom: '1px solid #e2e8f0',
    overflow: 'visible'
  }
}

const cellStyle = () => {
  return {
    padding: '14px 8px',
    height: '65px',
    transition: 'all 0.2s',
    overflow: 'visible'
  }
}

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


// 计算表格高度
const calcTableHeight = () => {
  setTimeout(() => {
    const viewHeight = window.innerHeight
    const searchCard = document.querySelector('.search-card')
    const paginationCard = document.querySelector('.pagination-card')

    let searchHeight = 0
    let paginationHeight = 0

    if (searchCard) searchHeight = searchCard.offsetHeight
    if (paginationCard) paginationHeight = paginationCard.offsetHeight

    tableHeight.value = viewHeight - 50 - searchHeight - paginationHeight - 60
  }, 100)
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

// 生命周期钩子
onMounted(() => {
  fetchData()
  calcTableHeight()
  window.addEventListener('resize', calcTableHeight)
})

onUnmounted(() => {
  window.removeEventListener('resize', calcTableHeight)
})
</script>

<style scoped>
.disputes-manage {
  padding: 16px;
  height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
}

/* 搜索卡片样式 */
.search-card {
  margin-bottom: 16px;
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.search-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
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

/* 表格卡片样式 */
.table-card {
  margin-bottom: 20px;
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.table-card:hover {
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

/* 表格样式 */
.table-row {
  cursor: pointer;
  transition: all 0.3s;
}

.table-row:hover {
  background-color: #f0f7ff !important;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
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
  min-width: 60px;
}

.status-tag:hover {
  transform: scale(1.05);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 时间单元格样式 */
.time-cell {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #64748b;
  font-size: 13px;
  transition: all 0.3s;
}

.time-cell .el-icon {
  color: #94a3b8;
  transition: all 0.3s;
}

.table-row:hover .time-cell {
  color: #334155;
}

.table-row:hover .time-cell .el-icon {
  color: #409eff;
  transform: scale(1.2);
}

/* 操作按钮样式 */
.operation-cell {
  display: flex;
  justify-content: center;
  gap: 12px;
  align-items: center;
  overflow: visible;
  padding: 0 6px;
  height: 100%;
}

.op-icon-btn {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: 1px solid #e2e8f0;
  color: #64748b;
  transition: all 0.3s ease;
  overflow: visible;
  position: relative;
  background: transparent !important;
  z-index: 1;
}

.op-icon-btn:hover {
  border-color: #5e81ac;
  color: #5e81ac;
  transform: scale(1.03);
  box-shadow: 0 2px 8px rgba(94, 129, 172, 0.15);
  z-index: 9;
}

/* 分页卡片样式 */
.pagination-card {
  margin-bottom: 16px;
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

/* 表头样式 */
.column-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  white-space: nowrap;
  line-height: 1;
  overflow: visible;
  padding: 2px 0;
  position: relative;
  z-index: 1;
}

.header-icon {
  font-size: 16px;
  color: #5e81ac;
}

.header-text {
  font-weight: 400;
  color: #64748b;
}
</style> 