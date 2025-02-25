<template>
  <div class="task-type-form">
    <el-card class="form-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">{{ isEdit ? '编辑任务类型' : '新增任务类型' }}</span>
          <el-button @click="goBack" text>
            <el-icon><Back /></el-icon>
            返回列表
          </el-button>
        </div>
      </template>
      
      <el-form 
        ref="formRef" 
        :model="formData" 
        :rules="rules" 
        label-width="100px" 
        label-position="right"
        status-icon
        class="task-type-form"
      >
        <!-- 类型名称 -->
        <el-form-item label="类型名称" prop="typeName">
          <el-input 
            v-model="formData.typeName" 
            placeholder="请输入任务类型名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        
        <!-- 类型图标 -->
        <el-form-item label="类型图标" prop="icon">
          <div class="icon-selection-container">
            <!-- 图标选择方式切换 -->
            <el-tabs v-model="iconSelectionMode" class="icon-tabs">
              <el-tab-pane label="选择图标" name="select">
                <div class="icon-categories">
                  <el-radio-group v-model="currentCategory" size="small">
                    <el-radio-button v-for="category in iconCategories" :key="category.value" :label="category.value">
                      {{ category.label }}
                    </el-radio-button>
                  </el-radio-group>
                </div>
                
                <div class="icon-grid">
                  <el-scrollbar height="250px">
                    <div class="icon-list">
                      <div 
                        v-for="icon in filteredIcons" 
                        :key="icon.name"
                        class="icon-item"
                        :class="{ 'is-selected': formData.icon === icon.url }"
                        @click="selectIcon(icon)"
                      >
                        <img :src="icon.url" class="icon-preview" :alt="icon.name">
                        <div class="icon-name">{{ icon.name }}</div>
                      </div>
                    </div>
                  </el-scrollbar>
                </div>
                
                <div class="icon-search">
                  <el-input
                    v-model="iconSearchText"
                    placeholder="搜索图标"
                    prefix-icon="Search"
                    clearable
                  />
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="上传图标" name="upload">
                <div class="upload-container">
                  <el-upload
                    class="avatar-uploader"
                    action="#"
                    :http-request="uploadIcon"
                    :show-file-list="false"
                    :before-upload="beforeIconUpload"
                  >
                    <div v-if="formData.icon && iconSelectionMode === 'upload'" class="avatar-preview">
                      <el-avatar :size="80" :src="formData.icon" />
                    </div>
                    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                  </el-upload>
                  <div class="upload-tip">
                    <p>支持JPG、PNG格式，建议尺寸100x100px</p>
                    <p>图标将显示在任务类型列表和任务详情中</p>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
            
            <!-- 当前选中的图标预览 -->
            <div class="selected-icon-preview" v-if="formData.icon">
              <div class="preview-title">已选图标</div>
              <img :src="formData.icon" class="selected-icon-image" alt="Selected Icon">
            </div>
          </div>
        </el-form-item>
        
        <!-- 类型描述 -->
        <el-form-item label="类型描述" prop="description">
          <el-input 
            v-model="formData.description" 
            type="textarea" 
            placeholder="请输入任务类型描述"
            maxlength="200"
            show-word-limit
            :rows="4"
          />
        </el-form-item>
        
        <!-- 优先级 -->
        <el-form-item label="优先级" prop="priority">
          <el-input-number 
            v-model="formData.priority" 
            :min="1" 
            :max="100" 
            placeholder="请输入优先级"
          />
          <div class="form-item-tip">数字越大优先级越高，默认为10</div>
        </el-form-item>
        
        <!-- 状态 -->
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <!-- 提交按钮 -->
        <el-form-item>
          <div class="form-actions">
            <el-button @click="goBack">取消</el-button>
            <el-button type="primary" @click="submitForm" :loading="submitting">
              {{ isEdit ? '保存修改' : '立即创建' }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Back, Plus, Search } from '@element-plus/icons-vue'
import { IconCategoryEnum, PresetIconsEnum } from '@/utils/enums/iconEnums'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)

// 判断是新增还是编辑模式
const isEdit = computed(() => {
  return route.query.id !== undefined
})

// 表单数据
const formData = reactive({
  id: null,
  typeName: '',
  icon: '',
  description: '',
  priority: 10,
  status: 1
})

// 表单验证规则
const rules = reactive({
  typeName: [
    { required: true, message: '请输入任务类型名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  icon: [
    { required: true, message: '请选择或上传任务类型图标', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入任务类型描述', trigger: 'blur' },
    { max: 200, message: '长度不能超过 200 个字符', trigger: 'blur' }
  ],
  priority: [
    { required: true, message: '请输入优先级', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
})

// 图标选择相关状态
const iconSelectionMode = ref('select') // 'select' 或 'upload'
const currentCategory = ref('delivery')
const iconSearchText = ref('')

// 图标分类 - 使用枚举
const iconCategories = IconCategoryEnum

// 预设图标库 - 使用枚举
const presetIcons = reactive(PresetIconsEnum)

// 根据分类和搜索文本过滤图标
const filteredIcons = computed(() => {
  let result = presetIcons.filter(icon => icon.category === currentCategory.value)
  
  if (iconSearchText.value) {
    const searchTerm = iconSearchText.value.toLowerCase()
    result = result.filter(icon => icon.name.toLowerCase().includes(searchTerm))
  }
  
  return result
})

// 选择预设图标
const selectIcon = (icon) => {
  formData.icon = icon.url
}

// 返回列表页
const goBack = () => {
  router.push('/admin/task-types')
}

// 图标上传前的验证
const beforeIconUpload = (file) => {
  const isJPG = file.type === 'image/jpeg'
  const isPNG = file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG && !isPNG) {
    ElMessage.error('图标只能是 JPG 或 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图标大小不能超过 2MB!')
    return false
  }
  return true
}

// 上传图标
const uploadIcon = (options) => {
  // 这里应该调用实际的上传API，现在只是模拟
  const file = options.file
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => {
    formData.icon = reader.result
    // 实际项目中，这里应该调用上传API，获取返回的URL
    // 然后将URL赋值给formData.icon
  }
}

// 切换图标选择模式时重置一些状态
watch(iconSelectionMode, (newMode) => {
  if (newMode === 'select' && !formData.icon) {
    // 如果切换到选择模式且当前没有选择图标，可以设置一个默认图标
    if (filteredIcons.value.length > 0) {
      // 可选择不自动选择默认图标
      // selectIcon(filteredIcons.value[0])
    }
  }
})

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        // 这里应该调用实际的API保存数据
        // 模拟API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        ElMessage.success(isEdit.value ? '修改成功' : '创建成功')
        router.push('/admin/task-types')
      } catch (error) {
        ElMessage.error(error.message || '操作失败，请重试')
      } finally {
        submitting.value = false
      }
    } else {
      ElMessage.warning('请正确填写表单内容')
      return false
    }
  })
}

// 如果是编辑模式，加载任务类型数据
onMounted(async () => {
  if (isEdit.value) {
    const id = route.query.id
    try {
      // 这里应该调用实际的API获取数据
      // 模拟API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 模拟数据，实际项目中应该从API获取
      Object.assign(formData, {
        id: id,
        typeName: '示例任务类型',
        icon: 'https://cdn-icons-png.flaticon.com/128/2271/2271048.png', // 使用预设图标之一
        description: '这是一个示例任务类型的描述信息',
        priority: 10,
        status: 1
      })
    } catch (error) {
      ElMessage.error('获取任务类型信息失败')
      router.push('/admin/task-types')
    }
  }
})
</script>

<style scoped>
.task-type-form {
  padding: 16px;
}

.form-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

/* 图标选择器样式 */
.icon-selection-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.icon-tabs {
  width: 100%;
}

.icon-categories {
  margin-bottom: 15px;
}

.icon-grid {
  margin-bottom: 15px;
}

.icon-list {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
  padding: 8px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 8px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid transparent;
}

.icon-item:hover {
  background-color: #f5f7fa;
  border-color: #e4e7ed;
}

.icon-item.is-selected {
  background-color: #ecf5ff;
  border-color: #409eff;
}

.icon-preview {
  width: 40px;
  height: 40px;
  margin-bottom: 6px;
}

.icon-name {
  font-size: 12px;
  color: #606266;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

.icon-search {
  margin-top: 15px;
}

.upload-container {
  display: flex;
  align-items: flex-start;
  gap: 20px;
}

.avatar-uploader {
  width: 100px;
  height: 100px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}

.avatar-preview {
  width: 100px;
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.upload-tip {
  color: #909399;
  font-size: 12px;
  line-height: 1.5;
}

.selected-icon-preview {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 15px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 6px;
}

.preview-title {
  font-size: 14px;
  font-weight: 500;
  color: #606266;
  margin-bottom: 10px;
}

.selected-icon-image {
  width: 60px;
  height: 60px;
}

.form-item-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
}
</style> 