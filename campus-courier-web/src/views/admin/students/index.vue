<template>
  <div class="student-manage">
    <!-- 顶部搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-area">
        <div class="search-input-wrapper">
          <!-- 姓名搜索 -->
          <el-input
            v-model="searchForm.name"
            placeholder="请输入姓名搜索"
            class="search-input"
            clearable
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>

          <!-- 状态选择 -->
          <el-select
            v-model="searchForm.status"
            placeholder="选择状态"
            clearable
            class="search-select"
          >
            <el-option :value="true" label="正常"/>
            <el-option :value="false" label="禁用"/>
          </el-select>

          <div class="search-actions">
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              <span>搜索</span>
            </el-button>
            <el-button @click="resetSearch">
              <el-icon><Refresh /></el-icon>
              <span>重置</span>
            </el-button>
          </div>
        </div>
        <div class="action-area">
          <div class="import-button-group">
            <el-upload
              class="import-upload"
              action="/api/student/import"
              :show-file-list="false"
              :on-success="handleImportSuccess"
              :on-error="handleImportError"
              :before-upload="beforeImportUpload"
              accept=".xlsx,.xls"
            >
              <el-button type="success" class="import-btn">
                <el-icon><Upload /></el-icon>
                <span>导入学生</span>
              </el-button>
            </el-upload>
            <el-dropdown @command="handleImportCommand" trigger="click">
              <el-button type="success" class="dropdown-btn">
                <el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="download">
                    <el-icon><Download /></el-icon>
                    下载导入模板
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <el-button type="primary" @click="handleAdd" class="add-btn">
            <el-icon><Plus /></el-icon>
            <span>新增学生</span>
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 中间表格区域 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        :header-cell-style="{
          background: '#F5F7FA',
          color: '#303133',
          fontWeight: '600',
          fontSize: '14px',
          height: '56px',
          borderBottom: '1px solid #EBEEF5'
        }"
        :cell-style="{
          fontSize: '14px',
          color: '#606266',
          padding: '16px 0',
          borderBottom: '1px solid #EBEEF5'
        }"
        row-class-name="table-row"
        @row-click="handleRowClick"
        highlight-current-row
      >
        <el-table-column prop="studentNo" label="学号" width="120" align="center" fixed>
          <template #default="{ row }">
            <span class="student-no">{{ row.studentNo }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120" align="center" fixed>
          <template #default="{ row }">
            <template v-if="row.avatar">
              <el-avatar :size="28" :src="row.avatar" class="student-avatar" />
            </template>
            <template v-else>
              <el-avatar :size="28" class="student-avatar" :style="{ backgroundColor: getAvatarColor(row.name) }">
                {{ getNameInitial(row.name) }}
              </el-avatar>
            </template>
            <span class="student-name">{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="80" align="center">
          <template #default="{ row }">
            <el-tag
              :type="row.gender === 1 ? 'primary' : 'success'"
              size="small"
              effect="plain"
            >
              {{ row.gender === 1 ? '男' : '女' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="班级信息" min-width="280" align="center">
          <template #default="{ row }">
            <div class="class-info">
              <el-tag size="small" effect="plain" class="info-tag">{{ row.department }}</el-tag>
              <el-icon><ArrowRight /></el-icon>
              <el-tag size="small" effect="plain" class="info-tag">{{ row.major }}</el-tag>
              <el-icon><ArrowRight /></el-icon>
              <el-tag size="small" effect="plain" class="info-tag">{{ row.grade }}</el-tag>
              <el-icon><ArrowRight /></el-icon>
              <el-tag size="small" effect="plain" class="info-tag">{{ row.className }}</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag
              :type="row.status ? 'success' : 'danger'"
              size="small"
              effect="light"
            >
              {{ row.status ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="primary" link @click="handleEdit(row)">
                <el-icon><Edit /></el-icon>
                <span>编辑</span>
              </el-button>
              <el-dropdown @command="(command) => handleCommand(command, row)">
                <el-button type="primary" link>
                  更多<el-icon class="el-icon--right"><ArrowDown /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="resetPassword">
                      <el-icon><Key /></el-icon>
                      重置密码
                    </el-dropdown-item>
                    <el-dropdown-item command="viewRecords">
                      <el-icon><Document /></el-icon>
                      查看记录
                    </el-dropdown-item>
                    <el-dropdown-item command="delete" divided>
                      <el-icon><Delete /></el-icon>
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 底部分页区域 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        :layout="'total, sizes, prev, pager, next, jumper'"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        background
      />
    </div>

    <!-- 新增/编辑学生抽屉 -->
    <student-form
      v-model="drawerVisible"
      :is-edit="isEdit"
      :edit-data="form"
      @success="handleFormSuccess"
    />

    <!-- 导入错误信息对话框 -->
    <el-dialog
      v-model="importErrorDialogVisible"
      :title="importResult.failCount === 0 ? '导入成功' : '导入结果'"
      width="680px"
      :close-on-click-modal="false"
      class="import-dialog"
      destroy-on-close
    >
      <div class="import-result">
        <!-- 导入结果统计卡片 -->
        <div class="result-cards">
          <div class="result-card">
            <div class="card-title">总数据</div>
            <div class="card-value">{{ importResult.total }}</div>
          </div>
          <div class="result-card">
            <div class="card-title">成功数</div>
            <div class="card-value success">{{ importResult.successCount }}</div>
          </div>
          <div class="result-card">
            <div class="card-title">失败数</div>
            <div class="card-value error">{{ importResult.failCount }}</div>
          </div>
        </div>
        
        <!-- 错误列表 -->
        <div v-if="importResult.errors && importResult.errors.length > 0" class="error-list">
          <div class="error-title">
            <el-icon><Warning /></el-icon>
            <span>错误详情</span>
          </div>
          <el-table
            :data="importResult.errors"
            style="width: 100%"
            :border="false"
            stripe
            max-height="300"
          >
            <el-table-column
              prop="rowIndex"
              label="行号"
              width="80"
              align="center"
            >
              <template #default="{ row }">
                <span class="row-index">{{ row.rowIndex }}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="studentNo"
              label="学号"
              width="120"
              align="center"
            />
            <el-table-column
              prop="message"
              label="错误信息"
            >
              <template #default="{ row }">
                <div class="error-message">
                  <el-icon><CircleClose /></el-icon>
                  <span>{{ row.message }}</span>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="importErrorDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="handleDownloadTemplate">
            <el-icon><Download /></el-icon>
            <span>下载模板</span>
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 记录抽屉 -->
    <student-record-drawer
      v-model="recordDrawerVisible"
      :student-id="currentStudent?.id"
      :student-name="currentStudent?.name"
    />
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {
  ArrowDown,
  ArrowRight,
  CircleClose,
  Delete,
  Download,
  Edit,
  Key,
  Plus,
  Refresh,
  Search,
  Upload,
  Warning,
  Document,
  User
} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {
  addStudent,
  deleteStudent,
  downloadTemplate,
  getDepartments,
  getStudentById,
  pageStudent,
  resetStudentPassword,
  updateStudent
} from '@/api/student'
import StudentForm from './components/StudentForm.vue'
import StudentRecordDrawer from './components/StudentRecordDrawer.vue'

// 搜索表单
const searchForm = ref({
  name: '',
  status: ''
})

// 表格数据相关
const tableData = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 抽屉相关
const drawerVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  studentNo: '',
  name: '',
  gender: 1,
  avatar: '',
  email: '',
  department: '',
  major: '',
  grade: '',
  className: '',
  enrollmentYear: '',
  identityType: 4,
  status: true
})

// 表单数据
const formRef = ref(null)

// 表单校验规则
const rules = {
  studentNo: [
    {required: true, message: '请输入学号', trigger: 'blur'},
    {pattern: /^\d{8,12}$/, message: '学号格式不正确', trigger: 'blur'}
  ],
  name: [
    {required: true, message: '请输入姓名', trigger: 'blur'},
    {min: 2, max: 20, message: '姓名长度在2-20个字符之间', trigger: 'blur'}
  ],
  gender: [
    {required: true, message: '请选择性别', trigger: 'change'}
  ],
  email: [
    {required: true, message: '请输入邮箱', trigger: 'blur'},
    {pattern: /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/, message: '邮箱格式不正确', trigger: 'blur'}
  ],
  department: [
    {required: true, message: '请选择院系', trigger: 'change'}
  ],
  major: [
    {required: true, message: '请选择专业', trigger: 'change'}
  ],
  grade: [
    {required: true, message: '请选择年级', trigger: 'change'}
  ],
  className: [
    {required: true, message: '请选择班级', trigger: 'change'}
  ],
  identityType: [
    {required: true, message: '请选择身份类型', trigger: 'change'}
  ]
}

// 级联选择相关数据
const departmentOptions = ref([])
const majorOptions = ref([])
const gradeOptions = ref([])
const classOptions = ref([])

// 获取院系数据
const fetchDepartments = async () => {
  try {
    loading.value = true
    const data = await getDepartments()
    if (data) {
      departmentOptions.value = data
      console.log('院系数据:', data)
    }
  } catch (error) {
    ElMessage.error('获取院系信息失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 处理院系变化
const handleDepartmentChange = (val) => {
  // 重置后续选项
  form.value.major = ''
  form.value.grade = ''
  form.value.className = ''
  majorOptions.value = []
  gradeOptions.value = []
  classOptions.value = []

  // 获取选中院系的专业列表
  const selectedDept = departmentOptions.value.find(dept => dept.name === val)
  if (selectedDept) {
    majorOptions.value = selectedDept.majors
    console.log('专业列表:', majorOptions.value)
  }
}

// 处理专业变化
const handleMajorChange = (val) => {
  // 重置后续选项
  form.value.grade = ''
  form.value.className = ''
  gradeOptions.value = []
  classOptions.value = []

  // 获取选中专业的年级列表
  const selectedMajor = majorOptions.value.find(major => major.name === val)
  if (selectedMajor) {
    gradeOptions.value = selectedMajor.grades
    console.log('年级列表:', gradeOptions.value)
  }
}

// 处理年级变化
const handleGradeChange = (val) => {
  // 重置班级选项
  form.value.className = ''
  classOptions.value = []
  
  // 获取选中年级的班级列表
  const selectedGrade = gradeOptions.value.find(grade => grade.name === val)
  if (selectedGrade) {
    classOptions.value = selectedGrade.classes
    console.log('班级列表:', classOptions.value)
    
    // 从年级名称中提取入学年份(例如: "2020级" -> 2020)
    form.value.enrollmentYear = parseInt(selectedGrade.name)
  }
}

// 新增学生
const handleAdd = () => {
  isEdit.value = false
  form.value = {
    studentNo: '',
    name: '',
    gender: 1,
    avatar: '',
    email: '',
    department: '',
    major: '',
    grade: '',
    className: '',
    enrollmentYear: '',
    identityType: 4,
    status: true
  }
  drawerVisible.value = true
}

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val
  loadData()
}

// 页码改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  loadData()
}

// 头像上传相关方法
const handleAvatarSuccess = (response) => {
  form.value.avatar = response.data
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('上传头像图片只能是图片格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
  }

  return isImage && isLt2M
}

// 加载表格数据
const loadData = async () => {
  try {
    loading.value = true
    const res = await pageStudent({
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      name: searchForm.value.name,
      status: searchForm.value.status
    })
    tableData.value = res.records
    total.value = res.total
  } catch (error) {
    ElMessage.error('获取学生列表失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 处理删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该学生吗？', '提示', {
      type: 'warning'
    })
    await deleteStudent(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败：' + error.message)
    }
  }
}

// 处理重置密码
const handleResetPassword = async (row) => {
  try {
    await ElMessageBox.confirm('确定要重置该学生的密码吗？', '提示', {
      type: 'warning'
    })
    await resetStudentPassword(row.id)
    ElMessage.success('密码重置成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('密码重置失败：' + error.message)
    }
  }
}

// 表格列配置
const columns = [
  {
    prop: 'studentNo',
    label: '学号',
    width: 120
  },
  {
    prop: 'name',
    label: '姓名',
    width: 100
  },
  {
    prop: 'gender',
    label: '性别',
    width: 80,
    formatter: (row) => row.gender === 1 ? '男' : '女'
  },
  {
    prop: 'department',
    label: '院系'
  },
  {
    prop: 'major',
    label: '专业'
  },
  {
    prop: 'grade',
    label: '年级',
    width: 100
  },
  {
    prop: 'className',
    label: '班级',
    width: 120
  },
  {
    prop: 'identityType',
    label: '身份',
    width: 100,
    formatter: (row) => {
      const types = {
        4: '学生'
      }
      return types[row.identityType] || '未知'
    }
  },
  {
    prop: 'status',
    label: '状态',
    width: 100,
    formatter: (row) => row.status ? '正常' : '禁用'
  }
]

// 处理表单提交成功
const handleFormSuccess = async (formData) => {
  try {
    if (isEdit.value) {
      await updateStudent(formData)
      ElMessage.success('更新学生成功')
    } else {
      await addStudent(formData)
      ElMessage.success('新增学生成功')
    }
    drawerVisible.value = false
    loadData() // 刷新表格数据
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新学生失败：' : '新增学生失败：' + error.message)
  }
}

// 导入错误相关
const importErrorDialogVisible = ref(false)
const importResult = ref({
  success: false,
  total: 0,
  successCount: 0,
  failCount: 0,
  errors: []
})

// 获取导入结果摘要
const getResultSummary = () => {
  const { total, successCount, failCount } = importResult.value
  if (failCount === 0) {
    return `导入成功！共导入 ${total} 条数据`
  }
  return `导入完成，共 ${total} 条数据，成功 ${successCount} 条，失败 ${failCount} 条`
}

// 处理导入成功
const handleImportSuccess = (response) => {
  importResult.value = response.data
  importErrorDialogVisible.value = true
  
  if (response.data.success) {
    loadData() // 刷新表格数据
  }
}

// 处理导入失败
const handleImportError = (error) => {
  ElMessage.error('导入失败：' + error.message)
}

// 导入前校验
const beforeImportUpload = (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || 
                 file.type === 'application/vnd.ms-excel'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isExcel) {
    ElMessage.error('只能上传Excel文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('文件大小不能超过 2MB!')
    return false
  }
  return true
}

// 处理导入相关的下拉菜单命令
const handleImportCommand = async (command) => {
  if (command === 'download') {
    try {
      await downloadTemplate()
    } catch (error) {
      ElMessage.error('下载模板失败：' + error.message)
    }
  }
}

// 处理更多菜单命令
const handleCommand = async (command, row) => {
  switch (command) {
    case 'resetPassword':
      await handleResetPassword(row)
      break
    case 'viewRecords':
      handleViewRecords(row)
      break
    case 'delete':
      await handleDelete(row)
      break
  }
}

// 处理下载模板
const handleDownloadTemplate = () => {
  downloadTemplate()
  importErrorDialogVisible.value = false
}

// 获取身份标签类型
const getIdentityTagType = (type) => {
  const types = {
    2: 'danger',
    3: 'warning',
    4: 'info'
  }
  return types[type] || 'info'
}

// 获取身份标签文本
const getIdentityLabel = (type) => {
  const types = {
    4: '学生'
  }
  return types[type] || '未知'
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  loadData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    name: '',
    status: ''
  }
  handleSearch()
}

// 处理状态变更
const handleStatusChange = async (row) => {
  try {
    // 这里添加更新状态的API调用
    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('状态更新失败：' + error.message)
    row.status = !row.status // 恢复状态
  }
}

// 获取姓名首字母
const getNameInitial = (name) => {
  return name ? name.charAt(0).toUpperCase() : '?'
}

// 根据姓名生成随机颜色
const getAvatarColor = (name) => {
  const colors = [
    '#1abc9c', '#2ecc71', '#3498db', '#9b59b6', '#34495e',
    '#16a085', '#27ae60', '#2980b9', '#8e44ad', '#2c3e50',
    '#f1c40f', '#e67e22', '#e74c3c', '#95a5a6', '#f39c12',
    '#d35400', '#c0392b', '#7f8c8d'
  ]
  
  // 使用姓名生成一个固定的索引，这样同一个名字总是得到相同的颜色
  let hash = 0
  for (let i = 0; i < name.length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  
  return colors[Math.abs(hash) % colors.length]
}

// 处理编辑
const handleEdit = async (row) => {
  try {
    form.value = await getStudentById(row.id)
    isEdit.value = true
    drawerVisible.value = true
  } catch (error) {
    ElMessage.error('获取学生信息失败：' + error.message)
  }
}

// 记录抽屉相关
const recordDrawerVisible = ref(false)
const currentStudent = ref(null)

// 打开记录抽屉
const handleViewRecords = (row) => {
  currentStudent.value = row
  recordDrawerVisible.value = true
}

onMounted(() => {
  loadData()
  fetchDepartments()
})
</script>

<style lang="scss" scoped>
.student-manage {
  padding: 20px;
  background: #F5F7FA;
  height: calc(100vh - 84px);
  display: flex;
  flex-direction: column;
  gap: 16px;

  .search-card {
    flex-shrink: 0;
    margin-bottom: 0;
    border-radius: 8px;
    
    .search-area {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 16px;

      .search-input-wrapper {
        display: flex;
        align-items: center;
        gap: 16px;
        flex: 1;

        .search-input,
        .search-select {
          width: 200px;
        }

        .search-actions {
          margin-left: 16px;
          display: flex;
          gap: 8px;

          .el-button {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 4px;
            padding: 8px 16px;
            
            .el-icon {
              margin-right: 0;
            }
          }
        }
      }

      .action-area {
        display: flex;
        gap: 12px;

        .import-button-group {
          display: inline-flex;
          align-items: stretch;

          .import-btn {
            border-top-right-radius: 0;
            border-bottom-right-radius: 0;
            margin-right: 1px;
          }

          .dropdown-btn {
            border-top-left-radius: 0;
            border-bottom-left-radius: 0;
            padding: 8px;
            margin-left: -1px;
          }
        }

        .add-btn {
          display: inline-flex;
          align-items: center;
          gap: 4px;
          padding: 8px 16px;
        }
      }
    }
  }

  .table-card {
    flex: 1;
    display: flex;
    flex-direction: column;
    margin-bottom: 0;
    border-radius: 8px;
    overflow: hidden;
    height: 0;

    :deep(.el-card__body) {
      height: 100%;
      padding: 0;
      display: flex;
      flex-direction: column;
    }

    :deep(.el-table) {
      flex: 1;
      overflow: hidden;
      
      .el-table__inner-wrapper {
        height: 100%;
      }
      
      .el-table__body-wrapper {
        overflow-y: auto;
        height: calc(100% - 56px) !important;
      }
    }

    .student-no {
      font-family: monospace;
      color: #409EFF;
    }

    .student-avatar {
      margin-right: 8px;
      vertical-align: middle;
    }

    .student-name {
      vertical-align: middle;
    }

    .text-red {
      color: #F56C6C;
    }

    :deep(.el-table) {
      // 添加水平滚动条样式
      .el-table__body-wrapper::-webkit-scrollbar {
        height: 6px;
      }
      
      .el-table__body-wrapper::-webkit-scrollbar-thumb {
        border-radius: 3px;
        background: #cbd5e1;
        
        &:hover {
          background: #94a3b8;
        }
      }
      
      .el-table__body-wrapper::-webkit-scrollbar-track {
        background: #f1f5f9;
      }
    }
  }

  .pagination-container {
    flex-shrink: 0;
    margin: 0;
    padding: 16px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
    
    :deep(.el-pagination) {
      justify-content: center;
      width: 100%;
      
      .el-pagination__total,
      .el-pagination__sizes,
      .el-pagination__jump {
        margin-right: 16px;
      }
      
      .el-pager {
        margin: 0 16px;
      }
    }
  }
}

.drawer-form {
  padding: 20px;

  .form-section {
    margin-bottom: 24px;
    padding: 16px;
    background: #f8fafc;
    border-radius: 8px;

    .section-title {
      font-size: 16px;
      font-weight: 600;
      color: #1a1a1a;
      margin-bottom: 16px;
      padding-left: 8px;
      border-left: 4px solid #2563eb;
    }
  }
}

.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: all 0.3s;

    &:hover {
      border-color: #2563eb;
    }
  }
}

.avatar-placeholder {
  width: 120px;
  height: 120px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #8c939d;
  font-size: 14px;
  gap: 8px;
}

.avatar-uploader-icon {
  font-size: 24px;
}

.avatar {
  width: 120px;
  height: 120px;
  display: block;
  object-fit: cover;
}

.drawer-footer {
  padding: 16px 20px;
  text-align: right;
}

// 添加级联选择相关样式
:deep(.el-select) {
  width: 100%;
}

:deep(.el-form-item) {
  margin-bottom: 20px;

  &:last-child {
    margin-bottom: 0;
  }
}

.import-result {
  .result-cards {
    display: flex;
    gap: 16px;
    margin-bottom: 24px;

    .result-card {
      flex: 1;
      padding: 16px;
      background: #f9fafb;
      border-radius: 8px;
      text-align: center;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
      }

      .card-title {
        color: #6b7280;
        font-size: 14px;
        margin-bottom: 8px;
      }

      .card-value {
        font-size: 24px;
        font-weight: 600;
        color: #111827;

        &.success {
          color: #059669;
        }

        &.error {
          color: #dc2626;
        }
      }
    }
  }

  .error-list {
    background: #fff;
    border-radius: 8px;
    
    .error-title {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 12px 0;
      color: #dc2626;
      font-weight: 500;
      
      .el-icon {
        font-size: 18px;
      }
    }

    .el-table {
      --el-table-border-color: #f3f4f6;
      --el-table-header-bg-color: #f9fafb;
      
      :deep(.el-table__header-wrapper) {
        th {
          font-weight: 600;
          color: #374151;
          background: #f9fafb;
        }
      }

      .row-index {
        display: inline-block;
        padding: 2px 8px;
        background: #f3f4f6;
        border-radius: 4px;
        color: #4b5563;
        font-family: monospace;
      }

      .error-message {
        display: flex;
        align-items: center;
        gap: 6px;
        color: #dc2626;

        .el-icon {
          font-size: 16px;
        }
      }
    }
  }
}

.dialog-footer {
  border-top: 1px solid #e5e7eb;
  padding: 16px 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;

  .el-button {
    padding: 8px 20px;
    
    .el-icon {
      margin-right: 4px;
    }
  }
}

.import-dialog {
  :deep(.el-dialog__header) {
    margin: 0;
    padding: 20px 24px;
    border-bottom: 1px solid #e5e7eb;
    
    .el-dialog__title {
      font-size: 18px;
      font-weight: 600;
      color: #111827;
    }
  }

  .import-result {
    .result-cards {
      display: flex;
      gap: 16px;
      margin-bottom: 24px;

      .result-card {
        flex: 1;
        padding: 16px;
        background: #f9fafb;
        border-radius: 8px;
        text-align: center;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }

        .card-title {
          color: #6b7280;
          font-size: 14px;
          margin-bottom: 8px;
        }

        .card-value {
          font-size: 24px;
          font-weight: 600;
          color: #111827;

          &.success {
            color: #059669;
          }

          &.error {
            color: #dc2626;
          }
        }
      }
    }

    .error-list {
      background: #fff;
      border-radius: 8px;
      
      .error-title {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 12px 0;
        color: #dc2626;
        font-weight: 500;
        
        .el-icon {
          font-size: 18px;
        }
      }

      .el-table {
        --el-table-border-color: #f3f4f6;
        --el-table-header-bg-color: #f9fafb;
        
        :deep(.el-table__header-wrapper) {
          th {
            font-weight: 600;
            color: #374151;
            background: #f9fafb;
          }
        }

        .row-index {
          display: inline-block;
          padding: 2px 8px;
          background: #f3f4f6;
          border-radius: 4px;
          color: #4b5563;
          font-family: monospace;
        }

        .error-message {
          display: flex;
          align-items: center;
          gap: 6px;
          color: #dc2626;

          .el-icon {
            font-size: 16px;
          }
        }
      }
    }
  }

  .dialog-footer {
    border-top: 1px solid #e5e7eb;
    padding: 16px 24px;
    display: flex;
    justify-content: flex-end;
    gap: 12px;

    .el-button {
      padding: 8px 20px;
      
      .el-icon {
        margin-right: 4px;
      }
    }
  }
}

.class-info {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;

  .info-tag {
    margin: 0;
  }

  .el-icon {
    color: #909399;
    font-size: 12px;
  }
}

.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 4px;

  .el-button {
    display: inline-flex;
    align-items: center;
    gap: 2px;
    padding: 6px 8px;
    font-size: 14px;
    white-space: nowrap;
    
    .el-icon {
      font-size: 14px;
    }
    
    &:hover {
      opacity: 0.8;
    }
  }
}
</style> 