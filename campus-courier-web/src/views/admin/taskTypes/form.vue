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
            <el-icon>
              <Back/>
            </el-icon>
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
                    @input="() => { if(formData.lastSelectedIconName === formData.typeName) formData.lastSelectedIconName = '' }"
                >
                  <template #prefix>
                    <div class="input-prefix-icon">📝</div>
                  </template>
                </el-input>
                <div class="input-hint">
                  好的名称能让同学们一目了然，例如"快递代取"、"证件照打印"
                  <span v-if="formData.lastSelectedIconName" class="auto-filled-hint">
                    (已根据所选图标自动填充，可以修改)
                  </span>
                </div>
              </div>
            </el-form-item>

            <!-- 简化后的类型图标选择 -->
            <el-form-item label="类型图标" prop="icon">
              <div class="icon-compact-selection">
                <!-- 已选图标预览 -->
                <div class="selected-icon-area" v-if="formData.icon">
                  <img :src="formData.icon" class="selected-icon-img" alt="Selected Icon">
                  <div class="icon-info">
                    <div class="icon-title">已选择图标</div>
                    <div class="icon-name">{{ getSelectedIconName() }}</div>
                  </div>
                </div>

                <!-- 选择图标按钮 -->
                <div class="icon-actions">
                  <el-button
                      @click="openIconDialog"
                      type="primary"
                      :icon="formData.icon ? 'Edit' : 'Plus'"
                      class="select-icon-btn"
                  >
                    {{ formData.icon ? '更换图标' : '选择图标' }}
                  </el-button>
                  <el-button
                      v-if="formData.icon"
                      @click="uploadCustomIcon"
                      plain
                      class="select-icon-btn"
                  >
                    上传自定义图标
                  </el-button>
                </div>

                <div class="icon-tip" v-if="!formData.icon">
                  <el-alert
                      title="请选择一个能代表此任务类型的图标"
                      type="info"
                      :closable="false"
                      show-icon
                  >
                    <template #default>
                      好的图标能帮助用户快速识别任务类型
                    </template>
                  </el-alert>
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
                    @input="() => formData.isDefaultDescription = false"
                />
                <div class="input-hint">
                  好的描述能提高接单效率，例如："提供专业的快递代取服务，包括校内所有快递点"
                  <span v-if="formData.isDefaultDescription" class="auto-filled-hint">
                    (已自动生成默认描述，可以修改)
                  </span>
                </div>
              </div>
            </el-form-item>

            <!-- 优先级 -->
            <el-form-item label="优先级" prop="priority">
              <div class="priority-container">
                <div class="priority-input">
                  <el-input-number
                      v-model="formData.priority"
                      :min="1"
                      :max="100"
                      placeholder="请输入优先级"
                      controls-position="right"
                  />
                </div>
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
                  {{
                    formData.status === 1 ? '启用后，该任务类型将对所有用户可见' : '禁用后，该任务类型将不会在用户端显示'
                  }}
                </div>
              </div>
            </el-form-item>

            <!-- 自动填充提示 -->
            <el-alert
                v-if="showAutoFillTip"
                title="已根据您选择的图标自动填充名称和描述"
                type="success"
                description="我们根据您选择的图标自动生成了任务类型名称和描述，您可以直接使用或进行修改。"
                :closable="true"
                show-icon
                class="auto-fill-alert"
            />

            <!-- 提交按钮 -->
            <el-form-item>
              <div class="form-actions">
                <el-button @click="goBack" class="cancel-button">
                  <i class="el-icon">✖</i>
                  取消
                </el-button>
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

    <!-- 图标选择弹窗 -->
    <el-dialog
        v-model="iconDialogVisible"
        title="选择任务类型图标"
        width="70%"
        class="icon-selection-dialog"
        :destroy-on-close="false"
        :close-on-click-modal="false"
    >
      <div class="icon-dialog-content">
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
                      @click="selectIconAndClose(icon)"
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
                  <el-avatar :size="120" :src="formData.icon"/>
                </div>
                <template v-else>
                  <el-icon class="avatar-uploader-icon">
                    <Plus/>
                  </el-icon>
                  <div class="upload-text">
                    <p>点击或拖拽图片到此处上传</p>
                    <p class="upload-subtext">自定义你的专属图标</p>
                  </div>
                </template>
              </el-upload>
              <div class="upload-tip">
                <div class="tip-header">
                  <el-icon>
                    <InfoFilled/>
                  </el-icon>
                  <span>上传提示</span>
                </div>
                <p>支持JPG、PNG格式，建议尺寸100x100px</p>
                <p>图标将显示在任务类型列表和任务详情中</p>
                <p>精美的图标能提升用户体验，建议使用浅色背景</p>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>

        <!-- 选择预览区域 -->
        <div class="dialog-preview-area">
          <div class="dialog-preview-title">实时预览</div>
          <div class="dialog-preview-card">
            <div class="preview-icon-container">
              <img :src="formData.icon || 'https://cdn-icons-png.flaticon.com/128/2897/2897230.png'"
                   class="preview-icon" alt="Icon Preview">
            </div>
            <div class="preview-text">
              <div class="preview-type-name">{{ formData.typeName || '任务类型名称' }}</div>
              <div class="preview-description">{{
                  truncateDescription(formData.description) || '任务类型描述信息'
                }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="iconDialogVisible = false" class="cancel-button">取消</el-button>
          <el-button type="primary" @click="confirmIconSelection" class="submit-button">
            确认选择
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 自定义图标上传弹窗 -->
    <el-dialog
        v-model="uploadDialogVisible"
        title="上传自定义图标"
        width="500px"
        :destroy-on-close="false"
        :close-on-click-modal="false"
        class="icon-selection-dialog"
    >
      <div class="upload-dialog-content">
        <el-upload
            class="custom-icon-uploader"
            action="#"
            :http-request="uploadCustomIconHandler"
            :show-file-list="false"
            :before-upload="beforeIconUpload"
            drag
        >
          <div v-if="tempUploadIcon" class="upload-preview">
            <el-avatar :size="100" :src="tempUploadIcon"/>
          </div>
          <template v-else>
            <el-icon class="upload-icon">
              <Plus/>
            </el-icon>
            <div class="upload-text">
              <p>点击或拖拽图片到此处上传</p>
              <p class="upload-subtext">支持JPG、PNG格式</p>
            </div>
          </template>
        </el-upload>

        <div class="upload-instructions">
          <p>
            <el-icon>
              <InfoFilled/>
            </el-icon>
            上传说明：
          </p>
          <ul>
            <li>推荐使用简洁明了的图标</li>
            <li>图标尺寸建议100x100像素</li>
            <li>图标将作为识别任务类型的主要视觉元素</li>
          </ul>
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelUpload" class="cancel-button">取消</el-button>
          <el-button type="primary" @click="confirmUpload" :disabled="!tempUploadIcon" class="submit-button">
            确认使用
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {Back, InfoFilled, Plus, Search} from '@element-plus/icons-vue'
import {IconCategoryEnum, PresetIconsEnum} from '@/utils/enums/iconEnums'
import {addTaskType, getTaskTypeDetail, updateTaskType} from '@/api/taskType' // 导入更新API

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)

// 弹窗相关状态
const iconDialogVisible = ref(false)
const uploadDialogVisible = ref(false)
const tempUploadIcon = ref('')

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
  status: 1,
  // 新增：用于智能自动填充的辅助字段
  lastSelectedIconName: '',
  isDefaultDescription: false
})

// 表单验证规则
const rules = reactive({
  typeName: [
    {required: true, message: '请输入任务类型名称', trigger: 'blur'},
    {min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur'}
  ],
  icon: [
    {required: true, message: '请选择或上传任务类型图标', trigger: 'change'}
  ],
  description: [
    {required: true, message: '请输入任务类型描述', trigger: 'blur'},
    {max: 200, message: '长度不能超过 200 个字符', trigger: 'blur'}
  ],
  priority: [
    {required: true, message: '请输入优先级', trigger: 'blur'}
  ],
  status: [
    {required: true, message: '请选择状态', trigger: 'change'}
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

// 获取已选择图标的名称
const getSelectedIconName = () => {
  if (!formData.icon) return ''

  const selectedIcon = presetIcons.find(icon => icon.url === formData.icon)
  return selectedIcon ? selectedIcon.name : '自定义图标'
}

// 打开图标选择弹窗
const openIconDialog = () => {
  iconDialogVisible.value = true
}

// 打开自定义图标上传弹窗
const uploadCustomIcon = () => {
  tempUploadIcon.value = ''
  uploadDialogVisible.value = true
}

// 处理自定义图标上传
const uploadCustomIconHandler = (options) => {
  const file = options.file
  const reader = new FileReader()
  reader.readAsDataURL(file)
  reader.onload = () => {
    tempUploadIcon.value = reader.result
  }
}

// 确认使用上传的图标
const confirmUpload = () => {
  if (tempUploadIcon.value) {
    formData.icon = tempUploadIcon.value
    // 重置其他自动填充状态
    formData.lastSelectedIconName = ''
    uploadDialogVisible.value = false
  }
}

// 取消上传
const cancelUpload = () => {
  tempUploadIcon.value = ''
  uploadDialogVisible.value = false
}

// 截断描述文本（用于预览）
const truncateDescription = (text) => {
  if (!text) return ''
  return text.length > 60 ? text.substring(0, 60) + '...' : text
}

// 确认图标选择
const confirmIconSelection = () => {
  iconDialogVisible.value = false
}

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
    1: {text: '最低', icon: '⭐'},
    25: {text: '较低', icon: '⭐⭐'},
    50: {text: '中等', icon: '⭐⭐⭐'},
    75: {text: '较高', icon: '⭐⭐⭐⭐'},
    100: {text: '最高', icon: '⭐⭐⭐⭐⭐'}
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

// 选择预设图标并关闭弹窗
const selectIconAndClose = (icon) => {
  selectIcon(icon)
  iconDialogVisible.value = false
}

// 选择预设图标
const selectIcon = (icon) => {
  formData.icon = icon.url

  // 智能自动填充：当任务类型名称为空或与之前选择的图标名称相同时，自动更新名称
  if (!formData.typeName || formData.lastSelectedIconName === formData.typeName) {
    formData.typeName = icon.name
    formData.lastSelectedIconName = icon.name
  }

  // 智能自动填充：当描述为空或是默认描述时，自动生成描述
  if (!formData.description || formData.isDefaultDescription) {
    // 获取图标分类的中文名称
    const categoryObj = iconCategories.find(cat => cat.value === icon.category)
    const categoryName = categoryObj ? categoryObj.label : '通用'

    // 生成默认描述
    formData.description = `提供专业的${icon.name}服务，满足校园内${categoryName}相关需求，快速便捷。`
    formData.isDefaultDescription = true
  }

  // 添加选择动画效果的类名
  const iconElement = document.querySelector(`.icon-item[data-icon="${icon.name}"]`)
  if (iconElement) {
    iconElement.classList.add('icon-selected-animation')
    setTimeout(() => {
      iconElement.classList.remove('icon-selected-animation')
    }, 500)
  }

  // 显示自动填充提示
  showAutoFillTip.value = true
  setTimeout(() => {
    showAutoFillTip.value = false
  }, 3000)
}

// 新增：自动填充提示状态
const showAutoFillTip = ref(false)

// 返回列表页
const goBack = () => {
  ElMessageBox.confirm('确定要离开吗？未保存的内容将会丢失', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    router.push('/admin/task-types')
  }).catch(() => {
  })
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
    iconDialogVisible.value = false
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

// 表单提交
const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true;
      try {
        // 根据模式调用不同的API
        if (isEdit.value) {
          // 编辑模式，调用更新API
          await updateTaskType(formData);
          ElMessage.success('更新任务类型成功');
        } else {
          // 新增模式，调用添加API
          await addTaskType(formData);
          ElMessage.success('添加任务类型成功');
        }
        // 跳转到任务类型列表页
        router.push('/admin/task-types');
      } catch (error) {
        console.error('操作任务类型失败:', error);
        ElMessage.error(error.message || '操作失败');
      } finally {
        submitting.value = false;
      }
    } else {
      ElMessage.warning('请正确填写表单信息');
    }
  });
};

// 如果是编辑模式，加载任务类型数据
onMounted(async () => {
  // 确保此页面可以滚动
  document.body.classList.add('admin-page');

  if (isEdit.value) {
    const id = route.query.id;
    try {
      // 显示加载中提示
      ElMessage.info({
        message: '正在加载任务类型数据...',
        duration: 1000
      });
      
      // 调用API获取任务类型详情
      const response = await getTaskTypeDetail(id);
      
      // 更新表单数据
      Object.assign(formData, {
        id: response.id,
        typeName: response.typeName,
        icon: response.icon,
        description: response.description,
        priority: response.priority,
        status: response.status
      });
      
      // 提示加载成功
      ElMessage.success('任务类型数据加载成功');
    } catch (error) {
      console.error('获取任务类型详情失败:', error);
      ElMessage.error('获取任务类型信息失败');
      router.push('/admin/task-types');
    }
  }
});
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
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(149, 157, 165, 0.2);
  overflow: hidden;
  transition: all 0.3s ease;
  background: linear-gradient(to bottom, white 0%, #f9fafd 100%);
  border: 1px solid rgba(230, 230, 250, 0.7);
}

.form-card:hover {
  box-shadow: 0 15px 35px rgba(149, 157, 165, 0.3);
  transform: translateY(-3px);
}

/* 内容区域 */
.form-content {
  overflow: visible;
}

.form-wrapper {
  padding: 25px;
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
  padding: 18px 25px;
  position: relative;
  overflow: hidden;
}

.card-header::before {
  content: "";
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.2) 0%, rgba(255, 255, 255, 0) 60%);
  opacity: 0;
  transform: scale(0.5);
  animation: pulse-header 15s infinite alternate;
}

@keyframes pulse-header {
  0% {
    opacity: 0;
    transform: scale(0.5) rotate(0deg);
  }
  50% {
    opacity: 0.7;
    transform: scale(1) rotate(180deg);
  }
  100% {
    opacity: 0;
    transform: scale(0.5) rotate(360deg);
  }
}

.header-title {
  font-size: 22px;
  font-weight: 600;
  display: flex;
  align-items: center;
}

.title-icon {
  margin-right: 12px;
  font-size: 24px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-8px);
  }
  100% {
    transform: translateY(0px);
  }
}

.back-button {
  color: white;
  backdrop-filter: blur(5px);
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  transition: all 0.3s;
  padding: 8px 16px;
}

.back-button:hover {
  color: #f0f0f0;
  background-color: rgba(255, 255, 255, 0.25);
  transform: translateX(-5px);
}

/* 表单介绍部分 */
.form-intro {
  background-color: #f0f7ff;
  border-left: 4px solid #409eff;
  padding: 18px;
  margin-bottom: 30px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.1);
  transition: all 0.3s;
}

.form-intro:hover {
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.15);
  transform: translateY(-2px);
}

.intro-icon {
  font-size: 28px;
  margin-right: 15px;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.form-intro p {
  margin: 0;
  color: #444;
  font-size: 16px;
  line-height: 1.5;
}

/* 输入框样式增强 */
.fancy-input-wrapper {
  position: relative;
  width: 100%;
  margin-bottom: 8px;
}

.fancy-input, .fancy-textarea {
  transition: all 0.3s ease;
  border: 2px solid #e6e8eb;
  border-radius: 12px;
  overflow: hidden;
}

.fancy-input:hover, .fancy-textarea:hover {
  border-color: #c0d7ff;
  box-shadow: 0 0 15px rgba(64, 158, 255, 0.15);
  transform: translateY(-2px);
}

.fancy-input:focus-within, .fancy-textarea:focus-within {
  border-color: #409eff;
  box-shadow: 0 0 0 4px rgba(64, 158, 255, 0.2);
  transform: translateY(-2px);
}

.fancy-input :deep(input) {
  height: 50px;
  font-size: 16px;
  padding-left: 48px;
  transition: all 0.3s;
  border-radius: 12px;
}

.fancy-textarea :deep(textarea) {
  padding: 15px;
  font-size: 16px;
  line-height: 1.6;
  border-radius: 12px;
  transition: all 0.3s;
  resize: vertical;
}

.fancy-textarea :deep(textarea):focus {
  background-color: #fafcff;
}

.input-prefix-icon {
  font-size: 22px;
  margin-right: 10px;
  opacity: 0.85;
  transition: all 0.3s;
}

.fancy-input:focus-within .input-prefix-icon,
.fancy-input:hover .input-prefix-icon {
  transform: scale(1.1);
  opacity: 1;
}

.input-hint {
  font-size: 13px;
  color: #909399;
  margin-top: 8px;
  padding-left: 3px;
  transition: all 0.3s;
}

.fancy-input:focus-within + .input-hint,
.fancy-textarea:focus-within + .input-hint {
  color: #409eff;
}

/* 创意表单项装饰 */
.el-form-item {
  position: relative;
  margin-bottom: 30px;
  transition: all 0.3s;
}

.el-form-item:hover {
  transform: translateX(5px);
}

.el-form-item::before {
  content: "";
  position: absolute;
  left: -15px;
  top: 15px;
  height: 0;
  width: 3px;
  background: linear-gradient(to bottom, #6b8cce, #8662c7);
  border-radius: 3px;
  transition: height 0.3s;
  opacity: 0.6;
}

.el-form-item:hover::before {
  height: calc(100% - 30px);
}

.el-form-item :deep(.el-form-item__label) {
  font-weight: 600;
  color: #444;
  font-size: 16px;
}

/* 新增：简化的图标选择区域 */
.icon-compact-selection {
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 12px;
  margin-bottom: 15px;
  box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
  border: 2px solid #f0f0f0;
}

.icon-compact-selection:hover {
  border-color: #e0e7ff;
  background-color: #f5f8ff;
}

.selected-icon-area {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 20px;
  border-radius: 12px;
  border: 2px solid #edf2fc;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  margin-bottom: 15px;
  transition: all 0.3s;
}

.selected-icon-area:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  border-color: #d0def5;
}

.selected-icon-img {
  width: 70px;
  height: 70px;
  margin-right: 20px;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  padding: 8px;
  background-color: #f9faff;
  border: 2px solid #f0f5ff;
  transition: all 0.3s;
}

.selected-icon-area:hover .selected-icon-img {
  transform: scale(1.05) rotate(5deg);
}

.icon-info {
  display: flex;
  flex-direction: column;
}

.icon-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 6px;
}

.icon-name {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  position: relative;
  display: inline-block;
}

.icon-name::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #6b8cce, #8662c7);
  transition: width 0.3s;
}

.selected-icon-area:hover .icon-name::after {
  width: 100%;
}

.icon-actions {
  display: flex;
  gap: 15px;
}

.select-icon-btn {
  min-width: 150px;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.select-icon-btn:hover {
  transform: translateY(-3px) scale(1.02);
  box-shadow: 0 6px 18px rgba(64, 158, 255, 0.3);
}

.select-icon-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0));
  transition: all 0.6s;
}

.select-icon-btn:hover::before {
  left: 100%;
}

.icon-tip {
  margin-top: 10px;
}

/* 优先级样式增强 */
.priority-container {
  display: flex;
  align-items: center;
  gap: 25px;
  background-color: #f9fafc;
  padding: 20px;
  border-radius: 12px;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.priority-container:hover {
  background-color: #f0f7ff;
  border-color: #e0e7ff;
}

.priority-input {
  width: 140px;
}

.priority-input :deep(.el-input-number) {
  border-radius: 10px;
  overflow: hidden;
  transition: all 0.3s;
}

.priority-input :deep(.el-input-number:hover) {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.priority-slider {
  flex: 1;
  padding: 0 10px;
}

.custom-slider :deep(.el-slider__button) {
  width: 24px;
  height: 24px;
  border: 2px solid #409eff;
  background-color: white;
  transition: all 0.3s;
}

.custom-slider :deep(.el-slider__button:hover) {
  transform: scale(1.2);
  box-shadow: 0 0 0 5px rgba(64, 158, 255, 0.2);
}

.custom-slider :deep(.el-slider__bar) {
  background: linear-gradient(90deg, #6b8cce, #8662c7);
  height: 8px;
  border-radius: 4px;
}

.custom-slider :deep(.el-slider__runway) {
  height: 8px;
  background-color: #e4e7ed;
  border-radius: 4px;
}

/* 状态开关样式增强 */
.status-switch-container {
  display: flex;
  align-items: center;
  gap: 25px;
  background-color: #f9fafc;
  padding: 20px;
  border-radius: 12px;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
  border: 2px solid transparent;
}

.status-switch-container:hover {
  background-color: #f0f7ff;
  border-color: #e0e7ff;
}

.status-switch :deep(.el-switch__core) {
  height: 24px;
  border-radius: 12px;
  width: 50px !important;
}

.status-switch :deep(.el-switch__core .el-switch__action) {
  height: 20px;
  width: 20px;
  margin: 2px;
}

.status-description {
  color: #606266;
  font-size: 15px;
  font-weight: 500;
  transition: all 0.3s;
}

.status-switch-container:hover .status-description {
  transform: translateX(5px);
}

/* 表单提交区域 */
.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 40px;
  padding: 10px;
}

.cancel-button, .submit-button {
  min-width: 150px;
  height: 50px;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}

.cancel-button {
  background-color: #f0f2f5;
  color: #5a5a5a;
  border: none;
}

.cancel-button:hover {
  background-color: #e6e9ec;
  color: #333;
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.12);
}

.submit-button {
  background: linear-gradient(135deg, #6b8cce, #8662c7);
  border: none;
  color: white;
}

.submit-button:hover {
  transform: translateY(-3px) scale(1.03);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.submit-button::before, .cancel-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0));
  transition: all 0.6s;
}

.submit-button:hover::before, .cancel-button:hover::before {
  left: 100%;
}

.submit-icon {
  margin-right: 8px;
  font-size: 18px;
}

/* 表单项提示 */
.form-item-tip {
  font-size: 13px;
  color: #909399;
  margin-top: 12px;
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: #f9f9f9;
  padding: 10px 12px;
  border-radius: 8px;
  transition: all 0.3s;
}

.form-item-tip:hover {
  background-color: #f0f7ff;
  color: #606266;
}

/* 自动填充提示样式 */
.auto-filled-hint {
  color: #67c23a;
  font-weight: 500;
  margin-left: 5px;
  display: inline-flex;
  align-items: center;
}

.auto-filled-hint::before {
  content: '✓';
  display: inline-block;
  margin-right: 4px;
  animation: pop 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

@keyframes pop {
  0% {
    transform: scale(0);
  }
  80% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

.auto-fill-alert {
  margin: 0 0 25px 0;
  border-radius: 12px;
  animation: slideInUp 0.5s cubic-bezier(0.23, 1, 0.32, 1);
  box-shadow: 0 8px 24px rgba(103, 194, 58, 0.15);
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 图标选择弹窗样式 */
.icon-dialog-content {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

.icon-selection-dialog :deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.icon-selection-dialog :deep(.el-dialog__header) {
  background: linear-gradient(135deg, #6b8cce, #8662c7);
  padding: 20px;
}

.icon-selection-dialog :deep(.el-dialog__title) {
  font-weight: 600;
  color: white;
  font-size: 20px;
}

.icon-selection-dialog :deep(.el-dialog__headerbtn .el-dialog__close) {
  color: white;
  font-size: 20px;
}

.icon-selection-dialog :deep(.el-dialog__body) {
  padding: 20px 25px;
}

.dialog-preview-area {
  margin-top: 25px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 12px;
  border: 2px solid #e4e7ed;
  box-shadow: inset 0 2px 10px rgba(0, 0, 0, 0.03);
}

.dialog-preview-title {
  font-size: 18px;
  font-weight: 600;
  color: #606266;
  margin-bottom: 20px;
  position: relative;
  display: inline-block;
}

.dialog-preview-title::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 40px;
  height: 3px;
  background: linear-gradient(90deg, #6b8cce, #8662c7);
  border-radius: 3px;
}

.dialog-preview-card {
  display: flex;
  align-items: center;
  background-color: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
  border: 2px solid #f0f0f0;
}

.dialog-preview-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
  border-color: #e0e7ff;
}

.preview-icon-container {
  margin-right: 25px;
}

.preview-icon {
  width: 70px;
  height: 70px;
  border-radius: 12px;
  padding: 10px;
  background-color: #f9faff;
  border: 2px solid #f0f5ff;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.dialog-preview-card:hover .preview-icon {
  transform: scale(1.05) rotate(5deg);
}

.preview-text {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.preview-type-name {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.preview-description {
  font-size: 15px;
  color: #606266;
  max-width: 600px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 1.5;
}

/* 图标选择器样式 */
.icon-tabs {
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.selection-tip {
  margin-bottom: 20px;
}

.icon-categories {
  margin-bottom: 25px;
}

.category-title {
  font-weight: 600;
  margin-bottom: 15px;
  color: #606266;
  font-size: 16px;
}

.category-button {
  margin-right: 10px;
  margin-bottom: 10px;
  border-radius: 20px;
  transition: all 0.3s;
  overflow: hidden;
}

.category-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.icon-grid {
  margin-bottom: 25px;
  border-radius: 12px;
  background-color: #f9fafc;
  padding: 15px;
  box-shadow: inset 0 2px 10px rgba(0, 0, 0, 0.03);
}

.icon-list {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
  padding: 10px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px 10px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  border: 2px solid transparent;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.03);
  background-color: white;
  position: relative;
  overflow: hidden;
}

.icon-wrapper {
  position: relative;
  width: 60px;
  height: 60px;
  margin-bottom: 12px;
  border-radius: 10px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f9faff;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.05);
  border: 2px solid #f0f5ff;
  transition: all 0.3s;
}

.hover-effect {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  transition: all 0.2s;
}

.icon-item:hover .hover-effect {
  background-color: rgba(0, 0, 0, 0.05);
}

.icon-preview {
  width: 40px;
  height: 40px;
  transition: transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.icon-item:hover .icon-preview {
  transform: scale(1.2) rotate(5deg);
}

.icon-item:hover {
  background-color: #f0f7ff;
  border-color: #c6e2ff;
  transform: translateY(-5px) scale(1.05);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  z-index: 1;
}

.icon-item.is-selected {
  background-color: #ecf5ff;
  border-color: #409eff;
}

.icon-item.is-selected::after {
  content: '✓';
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: #409eff;
  color: white;
  width: 20px;
  height: 20px;
  font-size: 12px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: pop 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.icon-name {
  font-size: 14px;
  color: #606266;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
  font-weight: 500;
}

.icon-search {
  margin-top: 20px;
}

.search-input {
  width: 100%;
}

.search-input :deep(.el-input__wrapper) {
  border-radius: 25px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  padding: 0 15px;
  transition: all 0.3s;
}

.search-input :deep(.el-input__wrapper:hover) {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.search-input :deep(input) {
  height: 50px;
  font-size: 16px;
}

/* 动画效果 */
.icon-selected-animation {
  animation: pulse 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
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

  .icon-selection-dialog {
    width: 95% !important;
  }

  .dialog-preview-card {
    flex-direction: column;
    text-align: center;
  }

  .preview-icon-container {
    margin-right: 0;
    margin-bottom: 15px;
  }
}
</style>