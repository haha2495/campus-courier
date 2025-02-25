<!-- 管理员个人信息页面 -->
<template>
  <div class="admin-profile">
    <el-card class="profile-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <h2 class="title">个人信息</h2>
            <span class="subtitle">管理和更新您的个人信息</span>
          </div>
          <el-button type="primary" @click="handleEdit" v-if="!isEditing" class="edit-btn">
            <el-icon>
              <Edit/>
            </el-icon>
            编辑资料
          </el-button>
        </div>
      </template>

      <div class="profile-content" v-loading="loading">
        <div class="profile-layout">
          <!-- 左侧头像区域 -->
          <div class="profile-sidebar">
            <div class="avatar-section">
              <el-upload
                  v-if="isEditing"
                  class="avatar-uploader"
                  action="/api/file/upload"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload"
              >
                <div class="avatar-wrapper">
                  <img v-if="form.avatar" :src="form.avatar" class="avatar"/>
                  <div v-else class="avatar-placeholder">
                    <el-icon class="upload-icon">
                      <Plus/>
                    </el-icon>
                    <span>点击上传头像</span>
                    <span class="upload-tip">支持 JPG/PNG 格式</span>
                  </div>
                  <div class="avatar-mask" v-if="form.avatar">
                    <el-icon>
                      <Edit/>
                    </el-icon>
                    <span>更换头像</span>
                  </div>
                </div>
              </el-upload>
              <el-avatar
                  v-else
                  :size="120"
                  :src="form.avatar"
                  :class="{ 'no-avatar': !form.avatar }"
              >
                {{ form.nickname?.charAt(0)?.toUpperCase() }}
              </el-avatar>
              <h3 class="nickname">{{ form.nickname || '未设置昵称' }}</h3>
              <div class="user-role">
                <el-icon>
                  <UserFilled/>
                </el-icon>
                系统管理员
              </div>
            </div>

            <!-- 账号状态信息 -->
            <div class="status-section" v-if="!isEditing">
              <div class="status-item">
                <div class="status-icon" :class="{ 'is-active': form.status }">
                  <el-icon>
                    <CircleCheckFilled/>
                  </el-icon>
                </div>
                <div class="status-info">
                  <span class="label">账号状态</span>
                  <el-tag :type="form.status ? 'success' : 'danger'" effect="light" class="status-tag">
                    {{ form.status ? '正常' : '禁用' }}
                  </el-tag>
                </div>
              </div>
              <div class="status-item">
                <div class="status-icon">
                  <el-icon>
                    <Timer/>
                  </el-icon>
                </div>
                <div class="status-info">
                  <span class="label">注册时间</span>
                  <span class="value">{{ form.createTime }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 右侧表单区域 -->
          <div class="profile-main">
            <!-- 编辑表单 -->
            <el-form
                v-if="isEditing"
                ref="formRef"
                :model="form"
                :rules="rules"
                label-width="100px"
                class="profile-form"
            >
              <div class="form-section">
                <div class="section-title">
                  <el-icon>
                    <User/>
                  </el-icon>
                  <span>基本信息</span>
                </div>
                <el-form-item label="用户名" prop="username">
                  <el-input
                      v-model="form.username"
                      placeholder="请输入用户名"
                      :disabled="true"
                  >
                    <template #prefix>
                      <el-icon>
                        <User/>
                      </el-icon>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item label="昵称" prop="nickname">
                  <el-input
                      v-model="form.nickname"
                      placeholder="请输入昵称"
                      maxlength="20"
                      show-word-limit
                  >
                    <template #prefix>
                      <el-icon>
                        <UserFilled/>
                      </el-icon>
                    </template>
                  </el-input>
                </el-form-item>
              </div>

              <div class="form-section">
                <div class="section-title">
                  <el-icon>
                    <Lock/>
                  </el-icon>
                  <span>安全设置</span>
                </div>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input
                      v-model="form.newPassword"
                      type="password"
                      placeholder="如需修改密码请输入新密码"
                      show-password
                  >
                    <template #prefix>
                      <el-icon>
                        <Lock/>
                      </el-icon>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input
                      v-model="form.confirmPassword"
                      type="password"
                      placeholder="请再次输入新密码"
                      show-password
                  >
                    <template #prefix>
                      <el-icon>
                        <Lock/>
                      </el-icon>
                    </template>
                  </el-input>
                </el-form-item>
              </div>

              <div class="form-actions">
                <el-button type="primary" @click="handleSubmit" :loading="submitting" class="submit-btn">
                  <el-icon>
                    <Check/>
                  </el-icon>
                  保存修改
                </el-button>
                <el-button @click="cancelEdit" class="cancel-btn">
                  <el-icon>
                    <Close/>
                  </el-icon>
                  取消
                </el-button>
              </div>
            </el-form>

            <!-- 信息展示 -->
            <div v-else class="info-display">
              <div class="info-section">
                <div class="section-title">
                  <el-icon>
                    <User/>
                  </el-icon>
                  <span>账号信息</span>
                </div>
                <div class="info-grid">
                  <div class="info-item">
                    <div class="info-icon">
                      <el-icon>
                        <User/>
                      </el-icon>
                    </div>
                    <div class="info-content">
                      <span class="label">用户名</span>
                      <span class="value">{{ form.username }}</span>
                    </div>
                  </div>
                  <div class="info-item">
                    <div class="info-icon">
                      <el-icon>
                        <UserFilled/>
                      </el-icon>
                    </div>
                    <div class="info-content">
                      <span class="label">昵称</span>
                      <span class="value">{{ form.nickname || '未设置' }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {
  Edit,
  User,
  UserFilled,
  Lock,
  Plus,
  CircleCheckFilled,
  Timer,
  Check,
  Close
} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import axios from 'axios'
import {getCurrentAdmin, updateProfile} from '@/api/admin'
import emitter from '@/utils/eventBus'

const loading = ref(false)
const submitting = ref(false)
const isEditing = ref(false)
const formRef = ref(null)

// 表单数据
const form = ref({
  username: '',
  nickname: '',
  avatar: '',
  status: true,
  createTime: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const rules = {
  nickname: [
    {required: true, message: '请输入昵称', trigger: 'blur'},
    {max: 20, message: '昵称长度不能超过20个字符', trigger: 'blur'}
  ],
  newPassword: [
    {min: 6, message: '密码长度不能少于6个字符', trigger: 'blur'}
  ],
  confirmPassword: [
    {
      validator: (rule, value, callback) => {
        if (form.value.newPassword && !value) {
          callback(new Error('请确认密码'))
        } else if (value !== form.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 获取管理员信息
const loadAdminInfo = async () => {
  try {
    loading.value = true
    const data = await getCurrentAdmin()
    const {newPassword, confirmPassword, ...info} = form.value
    form.value = {
      ...info,
      ...data,
      newPassword: '',
      confirmPassword: ''
    }
  } catch (error) {
    console.error('Failed to load admin info:', error)
    ElMessage.error('获取管理员信息失败')
  } finally {
    loading.value = false
  }
}

// 处理头像上传
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

// 编辑处理
const handleEdit = () => {
  isEditing.value = true
}

const cancelEdit = () => {
  isEditing.value = false
  form.value.newPassword = ''
  form.value.confirmPassword = ''
  loadAdminInfo() // 重新加载数据
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitting.value = true

    const submitData = {
      nickname: form.value.nickname,
      avatar: form.value.avatar
    }

    if (form.value.newPassword) {
      submitData.password = form.value.newPassword
    }

    await updateProfile(submitData)
    ElMessage.success('更新成功')
    isEditing.value = false
    // 发送更新通知
    emitter.emit('admin-profile-updated')
    loadAdminInfo() // 重新加载数据
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadAdminInfo()
})
</script>

<style lang="scss" scoped>
.admin-profile {
  padding: 24px;
  background: #f5f7fa;
  min-height: calc(100vh - 64px);

  .profile-card {
    max-width: 1000px;
    margin: 0 auto;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
    background: linear-gradient(145deg, #ffffff 0%, #f8fafc 100%);
    overflow: hidden;

    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 20px 24px;
      border-bottom: 1px solid #f0f0f0;
      background: linear-gradient(to right, #f8fafc, #ffffff);

      .header-left {
        .title {
          font-size: 20px;
          font-weight: 600;
          color: #1a1a1a;
          margin: 0 0 4px;
        }

        .subtitle {
          font-size: 14px;
          color: #64748b;
        }
      }

      .edit-btn {
        background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
        border: none;
        padding: 10px 20px;
        border-radius: 8px;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(37, 99, 235, 0.2);
        }

        .el-icon {
          margin-right: 6px;
        }
      }
    }

    .profile-content {
      padding: 24px;
    }
  }

  .profile-layout {
    display: flex;
    gap: 24px;

    .profile-sidebar {
      width: 300px;
      flex-shrink: 0;
    }

    .profile-main {
      flex: 1;
      min-width: 0;
    }
  }

  .avatar-section {
    text-align: center;
    padding: 24px;
    background: #ffffff;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
    margin-bottom: 24px;

    .avatar-wrapper {
      position: relative;
      width: 120px;
      height: 120px;
      margin: 0 auto 16px;
      border-radius: 50%;
      overflow: hidden;

      &:hover .avatar-mask {
        opacity: 1;
      }

      .avatar-mask {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.5);
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #fff;
        opacity: 0;
        transition: all 0.3s ease;
        cursor: pointer;

        .el-icon {
          font-size: 24px;
          margin-bottom: 8px;
        }
      }
    }

    .avatar {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .el-avatar {
      margin-bottom: 16px;
      border: 4px solid #fff;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
      font-size: 36px;
      font-weight: 600;
      transition: all 0.3s ease;

      &:hover {
        transform: scale(1.05) rotate(5deg);
      }

      &.no-avatar {
        background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
      }
    }

    .nickname {
      font-size: 18px;
      font-weight: 600;
      color: #1a1a1a;
      margin: 0 0 8px;
    }

    .user-role {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      padding: 6px 12px;
      background: #f0f9ff;
      color: #0369a1;
      border-radius: 20px;
      font-size: 14px;

      .el-icon {
        font-size: 16px;
      }
    }
  }

  .status-section {
    padding: 20px;
    background: #ffffff;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

    .status-item {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 12px;
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        background: #f8fafc;
        transform: translateX(4px);
      }

      .status-icon {
        width: 40px;
        height: 40px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        background: #f1f5f9;
        color: #64748b;
        transition: all 0.3s ease;

        &.is-active {
          background: #ecfdf5;
          color: #059669;
        }

        .el-icon {
          font-size: 20px;
        }
      }

      .status-info {
        flex: 1;
        min-width: 0;

        .label {
          display: block;
          font-size: 13px;
          color: #64748b;
          margin-bottom: 4px;
        }

        .value {
          font-size: 14px;
          color: #1e293b;
          font-weight: 500;
        }

        .status-tag {
          font-size: 13px;
          padding: 2px 8px;
          border-radius: 4px;
        }
      }
    }
  }

  .profile-form {
    .form-section {
      background: #ffffff;
      border-radius: 12px;
      padding: 24px;
      margin-bottom: 24px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

      .section-title {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        font-weight: 600;
        color: #1e293b;
        margin-bottom: 20px;
        padding-bottom: 12px;
        border-bottom: 1px solid #e5e7eb;

        .el-icon {
          font-size: 18px;
          color: #2563eb;
        }
      }

      :deep(.el-form-item) {
        margin-bottom: 20px;

        &:last-child {
          margin-bottom: 0;
        }

        .el-form-item__label {
          font-weight: 500;
          color: #374151;
        }

        .el-input__wrapper {
          padding: 8px 16px;
          border-radius: 8px;
          background: #f8fafc;
          box-shadow: none;
          border: 2px solid #e5e7eb;
          transition: all 0.3s ease;

          &:hover {
            border-color: #2563eb;
            background: #fff;
          }

          &.is-focus {
            border-color: #2563eb;
            background: #fff;
            box-shadow: 0 4px 12px rgba(37, 99, 235, 0.1);
          }
        }

        .el-input__prefix-inner {
          margin-right: 8px;

          .el-icon {
            font-size: 16px;
            color: #94a3b8;
          }
        }
      }
    }

    .form-actions {
      display: flex;
      gap: 12px;
      justify-content: flex-end;
      margin-top: 24px;

      .el-button {
        padding: 10px 24px;
        border-radius: 8px;
        font-weight: 500;

        .el-icon {
          margin-right: 6px;
        }
      }

      .submit-btn {
        background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
        border: none;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(37, 99, 235, 0.2);
        }
      }

      .cancel-btn {
        &:hover {
          background: #f1f5f9;
          border-color: #e2e8f0;
        }
      }
    }
  }

  .info-display {
    .info-section {
      background: #ffffff;
      border-radius: 12px;
      padding: 24px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

      .section-title {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        font-weight: 600;
        color: #1e293b;
        margin-bottom: 20px;
        padding-bottom: 12px;
        border-bottom: 1px solid #e5e7eb;

        .el-icon {
          font-size: 18px;
          color: #2563eb;
        }
      }

      .info-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 16px;

        .info-item {
          display: flex;
          align-items: center;
          gap: 16px;
          padding: 16px;
          background: #f8fafc;
          border-radius: 12px;
          transition: all 0.3s ease;

          &:hover {
            background: #fff;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
          }

          .info-icon {
            width: 40px;
            height: 40px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: rgba(37, 99, 235, 0.1);
            color: #2563eb;

            .el-icon {
              font-size: 20px;
            }
          }

          .info-content {
            flex: 1;
            min-width: 0;

            .label {
              display: block;
              font-size: 13px;
              color: #64748b;
              margin-bottom: 4px;
            }

            .value {
              font-size: 14px;
              color: #1e293b;
              font-weight: 500;
            }
          }
        }
      }
    }
  }
}
</style> 