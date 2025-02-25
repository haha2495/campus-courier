<template>
  <div class="admin-manage">
    <!-- 顶部搜索区域 -->
    <el-card class="search-card">
      <div class="search-area">
        <div class="search-input-wrapper">
          <!-- 用户名搜索 -->
          <el-input
              v-model="searchForm.username"
              placeholder="搜索管理员账号"
              clearable
              class="search-input"
          >
            <template #prefix>
              <el-icon class="search-icon">
                <User/>
              </el-icon>
            </template>
          </el-input>

          <!-- 昵称搜索 -->
          <el-input
              v-model="searchForm.nickname"
              placeholder="搜索管理员昵称"
              clearable
              class="search-input"
          >
            <template #prefix>
              <el-icon class="search-icon">
                <UserFilled/>
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
          <span>新增管理员</span>
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
        <!-- 头像列 -->
        <el-table-column label="头像" width="70" align="center">
          <template #default="{ row }">
            <el-avatar 
                :size="36" 
                :src="row.avatar" 
                class="user-avatar"
                @click="handleAvatarPreview(row)"
                style="cursor: pointer"
            >
              {{ row.username.charAt(0).toUpperCase() }}
            </el-avatar>
          </template>
        </el-table-column>
        
        <!-- 账号列 -->
        <el-table-column prop="username" label="账号" min-width="140">
          <template #default="{ row }">
            <span class="username">{{ row.username }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="nickname" label="昵称" min-width="140"/>
        
        <el-table-column prop="createTime" label="创建时间" min-width="160" align="center">
          <template #default="{ row }">
            <span class="time-cell">{{ formatTime(row.createTime) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag
                :type="row.status ? 'success' : 'danger'"
                effect="light"
                class="status-tag"
                :class="{ 'is-active': row.status }"
            >
              <el-icon class="status-icon">
                <component :is="row.status ? 'CircleCheck' : 'CircleClose'" />
              </el-icon>
              <span>{{ row.status ? '启用' : '禁用' }}</span>
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" min-width="200" align="center">
          <template #default="{ row }">
            <div class="action-buttons">
              <span v-if="isCurrentUser(row)" class="current-user-tag">当前登录账号</span>
              <template v-else>
                <el-button type="primary" link @click="handleEdit(row)">
                  <el-icon><Edit /></el-icon>
                  <span>编辑</span>
                </el-button>
                <el-button type="warning" link @click="handleResetPassword(row)">
                  <el-icon><Key /></el-icon>
                  <span>重置密码</span>
                </el-button>
                <el-button type="danger" link @click="handleDelete(row)">
                  <el-icon><Delete /></el-icon>
                  <span>删除</span>
                </el-button>
              </template>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 底部分页区域 -->
    <el-card class="pagination-card">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
      />
    </el-card>

    <!-- 抽屉表单 -->
    <el-drawer
        v-model="drawerVisible"
        :title="isEdit ? '编辑管理员' : '新增管理员'"
        size="40%"
        :show-close="true"
        :close-on-click-modal="false"
        destroy-on-close
    >
      <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-width="80px"
          class="drawer-form"
      >
        <!-- 头像上传 -->
        <el-form-item label="头像" prop="avatar">
          <div class="avatar-uploader">
            <div class="avatar-upload-wrapper">
              <div class="avatar-tip">点击下方区域上传头像</div>
              <el-upload
                  class="avatar-uploader-box"
                  action="#"
                  :show-file-list="false"
                  :auto-upload="false"
                  :on-change="handleAvatarChange"
                  :before-upload="beforeAvatarUpload"
              >
                <div class="avatar-wrapper">
                  <img
                      v-if="formData.avatar"
                      :src="formData.avatar"
                      class="avatar"
                  />
                  <el-icon v-else class="avatar-icon">
                    <Plus/>
                  </el-icon>
                </div>
              </el-upload>
              <div class="avatar-format-tip">支持 JPG、PNG、GIF 格式，小于 2MB</div>
            </div>
          </div>
        </el-form-item>

        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username">
          <el-input
              v-model="formData.username"
              placeholder="请输入用户名（4-20个字符）"
              :disabled="isEdit"
              clearable
          >
            <template #prefix>
              <el-icon class="input-icon">
                <User/>
              </el-icon>
            </template>
          </el-input>
          <div class="form-item-tip">用户名将作为登录账号，创建后不可修改</div>
        </el-form-item>

        <!-- 密码输入框，仅在新增时显示 -->
        <el-form-item
            v-if="!isEdit"
            label="密码"
            prop="password"
        >
          <el-input
              v-model="formData.password"
              type="password"
              placeholder="请输入密码（6-20个字符）"
              show-password
              clearable
          >
            <template #prefix>
              <el-icon class="input-icon">
                <Lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 昵称 -->
        <el-form-item label="昵称" prop="nickname">
          <el-input
              v-model="formData.nickname"
              placeholder="请输入管理员昵称"
              clearable
          >
            <template #prefix>
              <el-icon class="input-icon">
                <UserFilled/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 状态 -->
        <el-form-item label="状态" prop="status">
          <div class="status-wrapper">
            <el-switch
                v-model="formData.status"
                :active-value="1"
                :inactive-value="0"
                class="status-switch"
                inline-prompt
                :active-text="'启用'"
                :inactive-text="'禁用'"
            />
            <span class="status-text">{{
                formData.status === 1 ? '启用后可正常登录系统' : '禁用后将无法登录系统'
              }}</span>
          </div>
        </el-form-item>
      </el-form>

      <!-- 抽屉底部按钮 -->
      <template #footer>
        <div class="drawer-footer">
          <el-button @click="drawerVisible = false">取 消</el-button>
          <el-button
              type="primary"
              :loading="submitLoading"
              @click="handleSubmit"
          >
            确 定
          </el-button>
        </div>
      </template>
    </el-drawer>

    <!-- 图片预览组件 -->
    <component :is="renderImageViewer()" />
  </div>
</template>

<script setup>
import {onMounted, reactive, ref, onUnmounted, nextTick, watch} from 'vue'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Lock, Plus, RefreshRight, Search, User, UserFilled, CircleCheck, CircleClose, Edit, Delete, Key, ArrowDown} from '@element-plus/icons-vue'
import {uploadFile} from '@/api/file'
import {addAdmin, pageAdmin, getCurrentAdmin, getAdminById, updateAdmin, deleteAdmin, resetAdminPassword} from '@/api/admin'
import { ElImageViewer } from 'element-plus'
import { h } from 'vue'

// 设置 Element Plus 的语言为中文
const locale = zhCn

// 搜索表单
const searchForm = ref({
  username: '',
  nickname: '',
  status: null
})

// 表格数据
const tableData = ref([
  {
    username: 'admin',
    name: '超级管理员',
    createTime: '2024-02-14',
    status: 1
  }
])

const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

// 抽屉相关
const drawerVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

// 表单数据
const formData = reactive({
  avatar: '',
  username: '',
  password: '',
  nickname: '',
  status: 1
})

// 表单校验规则
const formRules = {
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 4, max: 20, message: '长度在 4 到 20 个字符', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
  ],
  nickname: [
    {required: true, message: '请输入昵称', trigger: 'blur'}
  ]
}

// 图片预览相关
const showImageViewer = ref(false)
const previewUrl = ref('')

// 当前登录用户ID
const currentAdminId = ref(null)

// 获取当前登录用户信息
const fetchCurrentAdmin = async () => {
  try {
    const res = await getCurrentAdmin()
    currentAdminId.value = res.id
  } catch (error) {
    console.error('获取当前用户信息失败:', error)
    ElMessage.error('获取当前用户信息失败：' + (error.message || '未知错误'))
  }
}

// 判断是否为当前登录用户
const isCurrentUser = (row) => {
  return row.id === currentAdminId.value
}

// 处理头像上传前的校验
const beforeAvatarUpload = (file) => {
  // 校验文件类型
  const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif']
  if (!allowedTypes.includes(file.type)) {
    ElMessage.error('只能上传 JPG/PNG/GIF 格式的图片！')
    return false
  }

  // 校验文件大小（限制为 2MB）
  const maxSize = 2 * 1024 * 1024
  if (file.size > maxSize) {
    ElMessage.error('图片大小不能超过 2MB！')
    return false
  }

  return true
}

// 处理头像上传
const handleAvatarChange = async (file) => {
  if (!beforeAvatarUpload(file.raw)) {
    return
  }

  try {
    formData.avatar = await uploadFile(file.raw)
    ElMessage.success('头像上传成功')
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('头像上传失败：' + (error.message || '未知错误'))
  }
}

// 搜索
const handleSearch = async () => {
  try {
    loading.value = true
    const res = await pageAdmin({
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      username: searchForm.value.username,
      nickname: searchForm.value.nickname,
      status: searchForm.value.status
    })
    tableData.value = res.records
    total.value = res.total
  } catch (error) {
    console.error('查询失败:', error)
    ElMessage.error('查询失败：' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

// 重置
const handleReset = () => {
  searchForm.value = {
    username: '',
    nickname: '',
    status: null
  }
  // 重置后自动查询
  handleSearch()
}

// 打开新增抽屉
const handleAdd = () => {
  isEdit.value = false
  drawerVisible.value = true
  // 重置表单
  formData.avatar = ''
  formData.username = ''
  formData.password = ''
  formData.nickname = ''
  formData.status = 1
}

// 打开编辑抽屉
const handleEdit = async (row) => {
  try {
    isEdit.value = true
    drawerVisible.value = true
    loading.value = true
    
    // 获取完整的管理员信息
    const adminInfo = await getAdminById(row.id)
    
    // 填充表单数据
    formData.id = adminInfo.id
    formData.avatar = adminInfo.avatar || ''
    formData.username = adminInfo.username
    formData.nickname = adminInfo.nickname
    formData.status = adminInfo.status ? 1 : 0  // 确保状态值为 1 或 0
  } catch (error) {
    console.error('获取管理员信息失败:', error)
    ElMessage.error('获取管理员信息失败：' + (error.message || '未知错误'))
    drawerVisible.value = false
  } finally {
    loading.value = false
  }
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除该管理员吗？删除后将无法恢复，且如果该账号在线会被强制下线。',
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      loading.value = true
      await deleteAdmin(row.id)
      ElMessage.success('删除成功')
      // 刷新表格数据
      handleSearch()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败：' + (error.message || '未知错误'))
    } finally {
      loading.value = false
    }
  }).catch(() => {
    // 取消删除，不做任何操作
  })
}

// 分页大小改变
const handleSizeChange = (val) => {
  pageSize.value = val
  handleSearch()
}

// 页码改变
const handleCurrentChange = (val) => {
  currentPage.value = val
  handleSearch()
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    // 表单校验
    await formRef.value.validate()
    submitLoading.value = true

    if (isEdit.value) {
      // 编辑模式
      await updateAdmin({
        id: formData.id,
        nickname: formData.nickname,
        status: formData.status === 1  // 转换为布尔值
      })
    } else {
      // 新增模式
      await addAdmin({
        username: formData.username,
        password: formData.password,
        nickname: formData.nickname,
        avatar: formData.avatar,
        status: formData.status === 1  // 转换为布尔值
      })
    }

    ElMessage.success(isEdit.value ? '编辑成功' : '添加成功')
    drawerVisible.value = false

    // 刷新表格数据
    handleSearch()
  } finally {
    submitLoading.value = false
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

// 处理头像预览
const handleAvatarPreview = (row) => {
  if (row.avatar) {
    previewUrl.value = row.avatar
    showImageViewer.value = true
  }
}

// 监听图片预览关闭
const handleCloseViewer = () => {
  showImageViewer.value = false
  previewUrl.value = ''
}

// 渲染图片预览组件
const renderImageViewer = () => {
  if (showImageViewer.value) {
    return h(ElImageViewer, {
      urlList: [previewUrl.value],
      onClose: handleCloseViewer,
      teleported: true,
      zIndex: 3000,
      initialIndex: 0,
      infinite: false,
      hideOnClickModal: true
    })
  }
  return null
}

// 添加重置密码的处理方法
const handleResetPassword = (row) => {
  ElMessageBox.confirm(
    '确定要重置该管理员的密码吗？重置后密码将变为：123456，且该账号会被强制下线。',
    '重置密码确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      loading.value = true
      await resetAdminPassword(row.id)
      ElMessage.success('密码重置成功')
    } catch (error) {
      console.error('重置密码失败:', error)
      ElMessage.error('重置密码失败：' + (error.message || '未知错误'))
    } finally {
      loading.value = false
    }
  }).catch(() => {
    // 取消重置，不做任何操作
  })
}

// 修改计算表格高度的逻辑
const tableHeight = ref(0)

const calculateTableHeight = () => {
  nextTick(() => {
    const adminManage = document.querySelector('.admin-manage')
    const searchCard = document.querySelector('.search-card')
    const paginationCard = document.querySelector('.pagination-card')
    const tableCard = document.querySelector('.table-card')
    
    if (adminManage && searchCard && paginationCard && tableCard) {
      const availableHeight = adminManage.clientHeight - searchCard.offsetHeight - paginationCard.offsetHeight - 32 // 32px for gaps
      const tableCardPadding = 40 // top and bottom padding of table card
      tableHeight.value = availableHeight - tableCardPadding
    }
  })
}

// 在窗口大小改变和数据变化时重新计算高度
watch(() => tableData.value.length, calculateTableHeight)
onMounted(() => {
  calculateTableHeight()
  window.addEventListener('resize', calculateTableHeight)
  // 加载初始数据
  handleSearch()
  // 获取当前登录用户信息
  fetchCurrentAdmin()
})

onUnmounted(() => {
  window.removeEventListener('resize', calculateTableHeight)
})
</script>

<style scoped lang="scss">
.admin-manage {
  height: 100%;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  background-color: #f1f5f9;
  box-sizing: border-box;
  overflow: hidden;

  .search-card {
    flex: 0 0 auto;
  }

  .table-card {
    flex: 1 1 0%;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    
    :deep(.el-card__body) {
      height: 100%;
      padding: 20px;
      overflow: hidden;
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
        
        &::-webkit-scrollbar {
          width: 6px;
        }
        
        &::-webkit-scrollbar-thumb {
          border-radius: 3px;
          background: #cbd5e1;
          
          &:hover {
            background: #94a3b8;
          }
        }
        
        &::-webkit-scrollbar-track {
          background: #f1f5f9;
        }
      }
    }
  }

  .pagination-card {
    flex: 0 0 auto;
    
    :deep(.el-card__body) {
      padding: 12px 20px;
      display: flex;
      justify-content: center;
    }
  }

  // 搜索区域样式
  .search-area {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 16px;

    .search-input-wrapper {
      flex: 1;
      display: flex;
      align-items: center;
      gap: 12px;

      .search-input,
      .search-select {
        width: 220px;
      }

      .search-actions {
        display: flex;
        gap: 8px;
      }
    }
  }

  // 表格样式美化
  :deep(.el-table) {
    --el-table-header-bg-color: #f8fafc;
    --el-table-row-hover-bg-color: #f1f5f9;
    
    .el-table__header-wrapper {
      th {
        background: var(--el-table-header-bg-color);
        color: #475569;
        font-weight: 600;
        font-size: 14px;
        height: 48px;
      }
    }

    .el-table__row {
      td {
        padding: 12px 0;
        font-size: 14px;
        color: #1f2937;
      }

      &:hover {
        td {
          background: var(--el-table-row-hover-bg-color);
        }
      }
    }
  }

  // Card 统一样式
  :deep(.el-card) {
    border-radius: 8px;
    border: 1px solid #e5e7eb;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    
    .el-card__body {
      box-sizing: border-box;
    }
  }
}

.admin-manage {
  height: calc(100vh - 84px);
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 16px;
  background: #f5f7fa;

  .search-card {
    .search-area {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 16px;

      .search-input-wrapper {
        flex: 1;
        display: flex;
        align-items: center;
        gap: 12px;

        .search-input,
        .search-select {
          width: 220px;
          transition: all 0.3s ease;

          :deep(.el-input__wrapper),
          :deep(.el-select__wrapper) {
            border-radius: 40px;
            padding: 4px 8px;
            background: white;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
            transition: all 0.3s ease;
            border: 1px solid #e5e7eb;

            &:hover {
              border-color: #2563eb;
              box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }

            &.is-focus {
              border-color: #2563eb;
              box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
            }

            .el-input__inner {
              font-size: 15px;
              color: #1f2937;
              height: 36px;
              line-height: 36px;

              &::placeholder {
                color: #9ca3af;
                font-size: 14px;
              }
            }
          }

          .search-icon {
            font-size: 18px;
            color: #9ca3af;
            margin: 0 8px;
            transition: all 0.3s ease;
          }

          &:hover .search-icon {
            color: #2563eb;
          }
        }

        // 下拉选择框特殊样式
        .search-select {
          :deep(.el-input__wrapper) {
            height: 44px;
            padding: 0 16px;
          }

          :deep(.el-input__inner) {
            height: 44px;
            line-height: 44px;
          }

          :deep(.el-select__tags) {
            margin-left: 8px;
          }

          :deep(.el-select-dropdown__item) {
            padding: 8px 16px;
            height: 40px;
            line-height: 24px;
            
            &.selected {
              color: #2563eb;
              font-weight: 500;
              background: #f0f7ff;
            }
            
            &:hover {
              background: #f3f4f6;
            }
          }

          :deep(.el-input__suffix) {
            right: 12px;
          }
        }

        .search-input {
          :deep(.el-input__wrapper) {
            height: 44px;
            padding: 0 16px;
          }

          :deep(.el-input__inner) {
            height: 44px;
            line-height: 44px;
          }
        }

        .search-actions {
          display: flex;
          gap: 8px;

          .search-btn {
            border-radius: 40px;
            transition: all 0.3s ease;
            padding: 8px 20px;
            display: flex;
            align-items: center;
            gap: 6px;
            background: linear-gradient(45deg, #2563eb, #3b82f6);
            border: none;
            font-weight: 500;

            .el-icon {
              font-size: 16px;
              transition: transform 0.3s ease;
            }

            &:hover {
              background: linear-gradient(45deg, #1d4ed8, #2563eb);
              transform: translateY(-1px);
              box-shadow: 0 4px 12px rgba(37, 99, 235, 0.2);
            }
          }

          .reset-btn {
            border-radius: 40px;
            transition: all 0.3s ease;
            padding: 8px 20px;
            display: flex;
            align-items: center;
            gap: 6px;
            border: 1px solid #e5e7eb;
            color: #4b5563;

            .el-icon {
              font-size: 16px;
              transition: transform 0.3s ease;
            }

            &:hover {
              color: #2563eb;
              border-color: #2563eb;
              background: rgba(37, 99, 235, 0.05);

              .el-icon {
                animation: spin 1s linear;
              }
            }
          }
        }
      }

      .add-btn {
        border-radius: 40px;
        padding: 8px 20px;
        transition: all 0.3s ease;
        background: linear-gradient(45deg, #2563eb, #3b82f6);
        border: none;
        display: flex;
        align-items: center;
        gap: 6px;
        font-weight: 500;

        .el-icon {
          font-size: 16px;
          transition: transform 0.3s ease;
        }

        &:hover {
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(37, 99, 235, 0.2);
          background: linear-gradient(45deg, #1d4ed8, #2563eb);

          .el-icon {
            transform: rotate(90deg);
          }
        }
      }
    }
  }

  .table-card {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    :deep(.el-card__body) {
      flex: 1;
      padding: 0;
      display: flex;
      flex-direction: column;
      overflow: hidden;
    }

    .el-table {
      flex: 1;
      overflow: auto;

      :deep(.el-table__body-wrapper) {
        overflow-y: auto;
        &::-webkit-scrollbar {
          width: 6px;
          height: 6px;
        }
        
        &::-webkit-scrollbar-thumb {
          border-radius: 3px;
          background: #cbd5e1;
          
          &:hover {
            background: #94a3b8;
          }
        }
        
        &::-webkit-scrollbar-track {
          background: #f1f5f9;
        }
      }

      &::before {
        height: 100%;
      }

      border-radius: 8px;
      overflow: hidden;
      
      .el-table__header {
        th {
          border-bottom: 1px solid #e5e7eb;
        }
      }
      
      .table-row {
        border-bottom: 1px solid #f3f4f6;
        transition: all 0.3s ease;
        
        &:hover {
          background-color: #f8fafc;
          
          .action-buttons {
            opacity: 1;
            transform: translateX(0);
          }
        }
      }

      .user-avatar {
        background: #e5e7eb;
        color: #4b5563;
        font-weight: 600;
        font-size: 16px;
        border: 2px solid #fff;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;
        cursor: pointer;
        
        &:hover {
          transform: scale(1.1);
          box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }
      }
      
      .username {
        color: #1f2937;
        font-weight: 500;
        font-size: 14px;
      }

      .time-cell {
        color: #6b7280;
        font-size: 13px;
      }

      .status-tag {
        display: inline-flex;
        align-items: center;
        gap: 4px;
        padding: 4px 8px;
        border-radius: 6px;
        font-size: 13px;
        font-weight: 500;
        
        &.el-tag--success {
          background: #ecfdf5;
          border-color: #d1fae5;
          color: #059669;
        }
        
        &.el-tag--danger {
          background: #fef2f2;
          border-color: #fee2e2;
          color: #dc2626;
        }
        
        .status-icon {
          font-size: 14px;
        }
      }

      .action-buttons {
        display: flex;
        justify-content: center;
        align-items: center;
        
        .current-user-tag {
          font-size: 13px;
          color: #6b7280;
          font-style: italic;
        }
        
        .action-dropdown-button {
          display: inline-flex;
          align-items: center;
          gap: 4px;
          font-size: 14px;
          
          &:hover {
            .el-icon {
              transform: rotate(180deg);
            }
          }
          
          .el-icon {
            transition: transform 0.3s ease;
          }
        }
      }
    }
  }

  .pagination-card {
    :deep(.el-card__body) {
      display: flex;
      justify-content: center;
      padding: 12px 0;
    }
  }

  :deep(.el-card) {
    margin: 0;
    border-radius: 8px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03), 0 1px 6px -1px rgba(0, 0, 0, 0.02);
    border: 1px solid #ebeef5;
  }

  :deep(.el-card__body) {
    padding: 16px;
  }

  :deep(.el-table) {
    .el-button {
      padding: 4px 0;
      margin: 0 8px;
      font-size: 14px;
    }
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.drawer-form {
  padding: 24px;

  .avatar-uploader {
    display: flex;
    justify-content: center;
    margin-bottom: 12px;

    .avatar-upload-wrapper {
      text-align: center;

      .avatar-tip {
        color: #374151;
        font-size: 14px;
        margin-bottom: 12px;
        font-weight: 500;
      }

      .avatar-uploader-box {
        .avatar-wrapper {
          width: 120px;
          height: 120px;
          border-radius: 50%;
          border: 2px dashed #e5e7eb;
          display: flex;
          justify-content: center;
          align-items: center;
          cursor: pointer;
          transition: all 0.3s;
          overflow: hidden;
          background: #f9fafb;
          margin: 0 auto;

          &:hover {
            border-color: #2563eb;
            background: #f0f7ff;

            .avatar-icon {
              color: #2563eb;
              transform: scale(1.1);
            }
          }

          .avatar {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }

          .avatar-icon {
            font-size: 32px;
            color: #9ca3af;
            transition: all 0.3s;
          }
        }
      }

      .avatar-format-tip {
        margin-top: 8px;
        color: #6b7280;
        font-size: 13px;
      }
    }
  }

  :deep(.el-form-item) {
    margin-bottom: 28px;

    .el-form-item__label {
      font-weight: 500;
      color: #374151;
      font-size: 15px;
      padding-right: 16px;
    }

    .el-input__wrapper {
      border-radius: 8px;
      transition: all 0.3s;
      padding: 4px 12px;
      background: #f9fafb;
      border: 1px solid #e5e7eb;
      box-shadow: none;

      &:hover {
        border-color: #2563eb;
        background: white;
      }

      &.is-focus {
        background: white;
        border-color: #2563eb;
        box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.1);
      }

      .input-icon {
        font-size: 16px;
        color: #9ca3af;
        margin-right: 8px;
      }
    }

    .el-input__inner {
      height: 42px;
      color: #1f2937;
      font-size: 14px;

      &::placeholder {
        color: #9ca3af;
      }
    }

    .form-item-tip {
      margin-top: 6px;
      color: #6b7280;
      font-size: 13px;
      line-height: 1.4;
    }
  }

  .status-wrapper {
    display: flex;
    align-items: center;
    gap: 12px;

    .status-switch {
      :deep(.el-switch__core) {
        border-radius: 100px;
        width: 60px !important;
        height: 28px;

        .el-switch__inner {
          .is-text {
            color: white;
            font-size: 12px;
          }
        }
      }
    }

    .status-text {
      color: #6b7280;
      font-size: 13px;
    }
  }
}

.drawer-footer {
  padding: 20px 24px;
  text-align: right;
  background: white;
  border-top: 1px solid #f3f4f6;

  .el-button {
    min-width: 120px;
    border-radius: 8px;
    padding: 12px 24px;
    font-weight: 500;
    font-size: 14px;
    transition: all 0.3s;

    &:not(.el-button--primary) {
      border-color: #e5e7eb;

      &:hover {
        border-color: #2563eb;
        color: #2563eb;
        background: #f0f7ff;
      }
    }

    &.el-button--primary {
      background: linear-gradient(45deg, #2563eb, #3b82f6);
      border: none;

      &:hover {
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(37, 99, 235, 0.2);
      }
    }
  }
}

:deep(.el-drawer__header) {
  margin-bottom: 0;
  padding: 20px 24px;
  border-bottom: 1px solid #f3f4f6;

  .el-drawer__title {
    font-size: 18px;
    font-weight: 600;
    color: #111827;
  }

  .el-drawer__close {
    font-size: 18px;
    color: #6b7280;

    &:hover {
      color: #111827;
      transform: rotate(90deg);
    }
  }
}

:deep(.el-drawer__body) {
  padding: 0;
}

.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  
  .current-user-tag {
    font-size: 13px;
    color: #6b7280;
    font-style: italic;
  }
  
  .action-dropdown-button {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    font-size: 14px;
    
    &:hover {
      .el-icon {
        transform: rotate(180deg);
      }
    }
    
    .el-icon {
      transition: transform 0.3s ease;
    }
  }
}

:deep(.el-dropdown-menu) {
  padding: 4px;
  min-width: 120px;
  
  .el-dropdown-menu__item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 12px;
    font-size: 14px;
    border-radius: 4px;
    margin: 2px 0;
    
    .el-icon {
      font-size: 16px;
    }
    
    &:hover {
      background-color: #f3f4f6;
      
      &.el-dropdown-menu__item--divided {
        border-top-color: #e5e7eb;
      }
    }
    
    &.el-dropdown-menu__item--divided {
      border-top: 1px solid #f3f4f6;
      margin-top: 4px;
      padding-top: 8px;
    }
  }
}
</style> 