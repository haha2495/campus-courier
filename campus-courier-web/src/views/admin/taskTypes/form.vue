<template>
  <div class="task-type-form admin-form">
    <el-card class="form-card" :body-style="{ padding: '0px' }">
      <template #header>
        <div class="card-header">
          <span class="header-title">
            <div class="title-icon">{{ isEdit ? '✏️' : '🆕' }}</div>
            {{ isEdit ? '编辑任务类型' : '新增任务类型' }}
          </span>
          <el-button @click="goBack" text class="back-button">
            <el-icon><Back /></el-icon>
            返回列表
          </el-button>
        </div>
      </template>
      
      <div class="form-content">
        <div class="form-wrapper">
          <!-- 表单引导语 -->
          <div class="form-intro">
            <div class="intro-icon">🎯</div>
            <p>创建一个清晰明了的任务类型，帮助同学们快速找到需要的服务</p>
          </div>
          
          <el-form 
            ref="formRef" 
            :model="formData" 
            :rules="rules" 
            label-width="100px" 
            label-position="right"
            status-icon
            class="task-type-form-inner"
          >
            <!-- 类型名称 -->
            <el-form-item label="类型名称" prop="typeName">
              <div class="fancy-input-wrapper">
                <el-input 
                  v-model="formData.typeName" 
                  placeholder="为你的任务类型取个简洁明了的名字吧~"
                  maxlength="50"
                  show-word-limit
                  class="fancy-input"
                >
                  <template #prefix>
                    <div class="input-prefix-icon">📝</div>
                  </template>
                </el-input>
                <div class="input-hint">好的名称能让同学们一目了然，例如"快递代取"、"证件照打印"</div>
              </div>
            </el-form-item>
            
            <!-- 类型图标 -->
            <el-form-item label="类型图标" prop="icon">
              <div class="icon-selection-container">
                <!-- 图标选择方式切换 -->
                <el-tabs v-model="iconSelectionMode" class="icon-tabs" type="border-card">
                  <el-tab-pane label="选择精美图标" name="select">
                    <!-- 图标选择小提示 -->
                    <div class="selection-tip">
                      <el-alert
                        title="选择一个能代表你任务类型的精美图标"
                        type="info"
                        :closable="false"
                        show-icon
                      >
                        <template #default>
                          我们准备了140+精美图标，涵盖校园生活的方方面面
                        </template>
                      </el-alert>
                    </div>
                    
                    <div class="icon-categories">
                      <div class="category-title">图标分类</div>
                      <el-radio-group v-model="currentCategory" size="large">
                        <el-radio-button 
                          v-for="category in iconCategories" 
                          :key="category.value" 
                          :label="category.value"
                          class="category-button"
                        >
                          {{ getCategoryEmoji(category.value) }} {{ category.label }}
                        </el-radio-button>
                      </el-radio-group>
                    </div>
                    
                    <div class="icon-grid">
                      <el-scrollbar height="300px">
                        <div class="icon-list">
                          <div 
                            v-for="icon in filteredIcons" 
                            :key="icon.name"
                            class="icon-item"
                            :class="{ 'is-selected': formData.icon === icon.url }"
                            @click="selectIcon(icon)"
                            :data-icon="icon.name"
                          >
                            <div class="icon-wrapper">
                              <img :src="icon.url" class="icon-preview" :alt="icon.name">
                              <div class="hover-effect"></div>
                            </div>
                            <div class="icon-name">{{ icon.name }}</div>
                          </div>
                        </div>
                      </el-scrollbar>
                    </div>
                    
                    <div class="icon-search">
                      <el-input
                        v-model="iconSearchText"
                        placeholder="搜索图标，例如：快递、打印、代课..."
                        :prefix-icon="Search"
                        clearable
                        class="search-input"
                      />
                    </div>
                  </el-tab-pane>
                  
                  <el-tab-pane label="上传自定义图标" name="upload">
                    <div class="upload-container">
                      <el-upload
                        class="avatar-uploader"
                        action="#"
                        :http-request="uploadIcon"
                        :show-file-list="false"
                        :before-upload="beforeIconUpload"
                        drag
                      >
                        <div v-if="formData.icon && iconSelectionMode === 'upload'" class="avatar-preview">
                          <el-avatar :size="120" :src="formData.icon" />
                        </div>
                        <template v-else>
                          <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
                          <div class="upload-text">
                            <p>点击或拖拽图片到此处上传</p>
                            <p class="upload-subtext">自定义你的专属图标</p>
                          </div>
                        </template>
                      </el-upload>
                      <div class="upload-tip">
                        <div class="tip-header">
                          <el-icon><InfoFilled /></el-icon>
                          <span>上传提示</span>
                        </div>
                        <p>支持JPG、PNG格式，建议尺寸100x100px</p>
                        <p>图标将显示在任务类型列表和任务详情中</p>
                        <p>精美的图标能提升用户体验，建议使用浅色背景</p>
                      </div>
                    </div>
                  </el-tab-pane>
                </el-tabs>
                
                <!-- 当前选中的图标预览 -->
                <div class="selected-icon-preview" v-if="formData.icon">
                  <div class="preview-header">
                    <div class="preview-title">已选图标</div>
                    <el-tag type="success" effect="dark" size="small">预览效果</el-tag>
                  </div>
                  <div class="preview-card">
                    <img :src="formData.icon" class="selected-icon-image" alt="Selected Icon">
                    <div class="preview-name">{{ formData.typeName || '任务类型名称' }}</div>
                  </div>
                </div>
              </div>
            </el-form-item>
            
            <!-- 类型描述 -->
            <el-form-item label="类型描述" prop="description">
              <div class="fancy-input-wrapper">
                <el-input 
                  v-model="formData.description" 
                  type="textarea" 
                  placeholder="描述一下这个任务类型是做什么的，帮助同学们更好地理解~"
                  maxlength="200"
                  show-word-limit
                  :rows="4"
                  class="fancy-textarea"
                />
                <div class="input-hint">
                  好的描述能提高接单效率，例如："提供专业的快递代取服务，包括校内所有快递点"
                </div>
              </div>
            </el-form-item>
            
            <!-- 优先级 -->
            <el-form-item label="优先级" prop="priority">
              <div class="priority-container">
                <el-input-number 
                  v-model="formData.priority" 
                  :min="1" 
                  :max="100" 
                  placeholder="请输入优先级"
                  class="priority-input"
                  controls-position="right"
                />
                <div class="priority-slider">
                  <el-slider 
                    v-model="formData.priority" 
                    :min="1" 
                    :max="100"
                    :format-tooltip="formatPriorityTooltip"
                    class="custom-slider"
                  />
                </div>
              </div>
              <div class="form-item-tip">
                <el-tag size="small" type="info" effect="plain">优先级说明</el-tag>
                数字越大优先级越高，将在任务类型列表中排序靠前，默认为10
              </div>
            </el-form-item>
            
            <!-- 状态 -->
            <el-form-item label="状态" prop="status">
              <div class="status-switch-container">
                <el-switch
                  v-model="formData.status"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="启用"
                  inactive-text="禁用"
                  class="status-switch"
                />
                <div class="status-description">
                  {{ formData.status === 1 ? '启用后，该任务类型将对所有用户可见' : '禁用后，该任务类型将不会在用户端显示' }}
                </div>
              </div>
            </el-form-item>
            
            <!-- 提交按钮 -->
            <el-form-item>
              <div class="form-actions">
                <el-button @click="goBack" class="cancel-button">取消</el-button>
                <el-button type="primary" @click="submitForm" :loading="submitting" class="submit-button">
                  <span class="submit-icon">{{ isEdit ? '✅' : '🚀' }}</span>
                  {{ isEdit ? '保存修改' : '立即创建' }}
                </el-button>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Back, Plus, Search, InfoFilled } from '@element-plus/icons-vue'
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

// 根据分类获取对应的表情符号
const getCategoryEmoji = (category) => {
  const emojiMap = {
    'delivery': '📦',
    'print': '🖨️',
    'shopping': '🛒',
    'food': '🍔',
    'study': '📚',
    'life': '🏠',
    'sports': '⚽',
    'entertainment': '🎮',
    'campus': '🏫',
    'other': '🔍'
  }
  return emojiMap[category] || '📋'
}

// 格式化优先级提示文本
const formatPriorityTooltip = (val) => {
  const levelMap = {
    1: { text: '最低', icon: '⭐' },
    25: { text: '较低', icon: '⭐⭐' },
    50: { text: '中等', icon: '⭐⭐⭐' },
    75: { text: '较高', icon: '⭐⭐⭐⭐' },
    100: { text: '最高', icon: '⭐⭐⭐⭐⭐' }
  }
  
  let level = '普通'
  let icon = '⭐⭐'
  
  if (val <= 5) {
    level = levelMap[1].text
    icon = levelMap[1].icon
  } else if (val <= 30) {
    level = levelMap[25].text
    icon = levelMap[25].icon
  } else if (val <= 70) {
    level = levelMap[50].text
    icon = levelMap[50].icon
  } else if (val <= 90) {
    level = levelMap[75].text
    icon = levelMap[75].icon
  } else {
    level = levelMap[100].text
    icon = levelMap[100].icon
  }
  
  return `${icon} ${val} (${level})`
}

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
  // 添加选择动画效果的类名
  const iconElement = document.querySelector(`.icon-item[data-icon="${icon.name}"]`)
  if (iconElement) {
    iconElement.classList.add('icon-selected-animation')
    setTimeout(() => {
      iconElement.classList.remove('icon-selected-animation')
    }, 500)
  }
}

// 返回列表页
const goBack = () => {
  ElMessageBox.confirm('确定要离开吗？未保存的内容将会丢失', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    router.push('/admin/task-types')
  }).catch(() => {})
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
        
        ElMessage({
          message: isEdit.value ? '修改成功！任务类型已更新' : '创建成功！新的任务类型已添加',
          type: 'success',
          duration: 3000,
          showClose: true,
          customClass: 'fancy-message'
        })
        
        router.push('/admin/task-types')
      } catch (error) {
        ElMessage.error(error.message || '操作失败，请重试')
      } finally {
        submitting.value = false
      }
    } else {
      ElMessage({
        message: '请正确填写表单内容',
        type: 'warning',
        showClose: true
      })
      return false
    }
  })
}

// 如果是编辑模式，加载任务类型数据
onMounted(async () => {
  // 确保此页面可以滚动
  document.body.classList.add('admin-page');
  
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
/* 基础样式 */
.task-type-form {
  padding: 16px;
  background-color: #f9fafc;
  min-height: 100vh;
  width: 100%;
  position: relative;
}

.task-type-form.admin-form {
  overflow-y: auto;
}

.form-card {
  max-width: 850px;
  margin: 20px auto 60px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(149, 157, 165, 0.2);
  overflow: hidden;
  transition: all 0.3s ease;
}

.form-card:hover {
  box-shadow: 0 12px 30px rgba(149, 157, 165, 0.3);
  transform: translateY(-2px);
}

/* 内容区域 */
.form-content {
  overflow: visible;
}

.form-wrapper {
  padding: 20px;
  background: white;
}

.task-type-form-inner {
  margin-bottom: 30px;
}

/* 头部样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #6b8cce, #8662c7);
  color: white;
  padding: 15px 20px;
}

.header-title {
  font-size: 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
}

.title-icon {
  margin-right: 10px;
  font-size: 22px;
}

.back-button {
  color: white;
}

.back-button:hover {
  color: #f0f0f0;
  background-color: rgba(255, 255, 255, 0.1);
}

/* 表单介绍部分 */
.form-intro {
  background-color: #f0f7ff;
  border-left: 4px solid #409eff;
  padding: 15px;
  margin-bottom: 25px;
  border-radius: 4px;
  display: flex;
  align-items: center;
}

.intro-icon {
  font-size: 24px;
  margin-right: 12px;
}

.form-intro p {
  margin: 0;
  color: #555;
  font-size: 15px;
}

/* 输入框样式 */
.fancy-input-wrapper {
  position: relative;
  width: 100%;
}

.fancy-input, .fancy-textarea {
  transition: all 0.3s ease;
}

.fancy-input:hover, .fancy-textarea:hover {
  box-shadow: 0 0 8px rgba(64, 158, 255, 0.2);
}

.fancy-input :deep(input) {
  height: 45px;
  font-size: 15px;
  padding-left: 45px;
}

.input-prefix-icon {
  font-size: 18px;
  margin-right: 10px;
}

.input-hint {
  font-size: 12px;
  color: #909399;
  margin-top: 6px;
  padding-left: 2px;
}

/* 图标选择器样式 */
.icon-selection-container {
  display: flex;
  flex-direction: column;
  gap: 25px;
  margin-bottom: 10px;
}

.icon-tabs {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

.selection-tip {
  margin-bottom: 15px;
}

.icon-categories {
  margin-bottom: 20px;
}

.category-title {
  font-weight: 500;
  margin-bottom: 10px;
  color: #606266;
}

.category-button {
  margin-right: 8px;
  margin-bottom: 8px;
  border-radius: 20px;
  transition: all 0.2s ease;
}

.icon-grid {
  margin-bottom: 20px;
  border-radius: 8px;
  background-color: #f9fafc;
  padding: 10px;
}

.icon-list {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
  padding: 10px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
  position: relative;
}

.icon-wrapper {
  position: relative;
  width: 50px;
  height: 50px;
  margin-bottom: 10px;
  border-radius: 8px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.hover-effect {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0);
  transition: all 0.2s ease;
}

.icon-item:hover .hover-effect {
  background-color: rgba(0,0,0,0.05);
}

.icon-preview {
  width: 40px;
  height: 40px;
  transition: transform 0.3s ease;
}

.icon-item:hover .icon-preview {
  transform: scale(1.1);
}

.icon-item:hover {
  background-color: #f0f7ff;
  border-color: #c6e2ff;
  transform: translateY(-2px);
}

.icon-item.is-selected {
  background-color: #ecf5ff;
  border-color: #409eff;
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

.search-input {
  width: 100%;
}

.search-input :deep(input) {
  border-radius: 20px;
  padding-left: 15px;
  height: 40px;
}

/* 图标上传区域 */
.upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 25px;
  padding: 20px;
}

.avatar-uploader {
  width: 220px;
  height: 220px;
  border: 2px dashed #d9d9d9;
  border-radius: 12px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #fafafa;
}

.avatar-uploader:hover {
  border-color: #409eff;
  background-color: #f5f7fa;
}

.avatar-uploader-icon {
  font-size: 40px;
  color: #c0c4cc;
  margin-bottom: 10px;
}

.upload-text {
  text-align: center;
  color: #606266;
}

.upload-subtext {
  font-size: 12px;
  color: #909399;
}

.avatar-preview {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.upload-tip {
  color: #909399;
  font-size: 13px;
  line-height: 1.6;
  background-color: #f8f9fb;
  padding: 15px;
  border-radius: 8px;
  width: 100%;
}

.tip-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #606266;
  font-weight: 500;
}

.tip-header .el-icon {
  margin-right: 6px;
  color: #909399;
}

/* 选中图标预览 */
.selected-icon-preview {
  display: flex;
  flex-direction: column;
  margin-top: 15px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.preview-title {
  font-size: 15px;
  font-weight: 500;
  color: #606266;
}

.preview-card {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}

.selected-icon-image {
  width: 50px;
  height: 50px;
  margin-right: 15px;
}

.preview-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

/* 优先级样式 */
.priority-container {
  display: flex;
  align-items: center;
  gap: 20px;
}

.priority-input {
  width: 120px;
}

.priority-slider {
  flex: 1;
}

.custom-slider {
  margin-top: 10px;
}

/* 状态开关样式 */
.status-switch-container {
  display: flex;
  align-items: center;
  gap: 20px;
}

.status-description {
  color: #606266;
  font-size: 14px;
}

/* 表单提交区域 */
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 30px;
}

.cancel-button {
  min-width: 100px;
}

.submit-button {
  min-width: 120px;
  border-radius: 8px;
  height: 42px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
}

.submit-icon {
  margin-right: 6px;
}

/* 表单项提示 */
.form-item-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 动画效果 */
.icon-selected-animation {
  animation: pulse 0.5s ease;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .icon-list {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .priority-container {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .priority-slider {
    width: 100%;
  }
  
  .form-card {
    margin: 10px auto 30px;
  }
}
</style>