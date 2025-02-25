<template>
  <el-drawer
    v-model="visible"
    :title="isEdit ? '编辑学生' : '新增学生'"
    size="40%"
    :show-close="true"
    :close-on-click-modal="false"
    destroy-on-close
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
      class="drawer-form"
    >
      <!-- 基本信息 -->
      <div class="form-section">
        <div class="section-title">基本信息</div>
        <el-form-item label="学号" prop="studentNo">
          <el-input
            v-model="form.studentNo"
            placeholder="请输入学号"
            :disabled="isEdit"
            clearable
          >
            <template #prefix>
              <el-icon><Document /></el-icon>
            </template>
          </el-input>
          <div class="form-item-tip">{{ isEdit ? '学号不可修改' : '请输入8-12位数字学号' }}</div>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名"/>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item label="照片" prop="avatar">
          <el-upload
            class="avatar-uploader"
            action="/api/file/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar"/>
            <div v-else class="avatar-placeholder">
              <el-icon class="avatar-uploader-icon"><Plus/></el-icon>
              <span>上传学生照片</span>
            </div>
          </el-upload>
        </el-form-item>
      </div>

      <!-- 学籍信息 -->
      <div class="form-section">
        <div class="section-title">学籍信息</div>
        <el-form-item label="院系" prop="department">
          <el-select
            v-model="form.department"
            placeholder="请选择院系"
            @change="handleDepartmentChange"
          >
            <el-option
              v-for="dept in departmentOptions"
              :key="dept.name"
              :label="dept.name"
              :value="dept.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-select
            v-model="form.major"
            placeholder="请选择专业"
            :disabled="!form.department"
            @change="handleMajorChange"
          >
            <el-option
              v-for="major in majorOptions"
              :key="major.name"
              :label="major.name"
              :value="major.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-select
            v-model="form.grade"
            placeholder="请选择年级"
            :disabled="!form.major"
            @change="handleGradeChange"
          >
            <el-option
              v-for="grade in gradeOptions"
              :key="grade.name"
              :label="grade.name"
              :value="grade.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="班级" prop="className">
          <el-select
            v-model="form.className"
            placeholder="请选择班级"
            :disabled="!form.grade"
          >
            <el-option
              v-for="cls in classOptions"
              :key="cls"
              :label="cls"
              :value="cls"
            />
          </el-select>
        </el-form-item>
      </div>

      <!-- 其他信息 -->
      <div class="form-section">
        <div class="section-title">其他信息</div>
        <el-form-item label="身份类型" prop="identityType">
          <el-select v-model="form.identityType" placeholder="请选择身份类型" disabled>
            <el-option label="学生" :value="4"/>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="form.status"
            :active-value="true"
            :inactive-value="false"
            inline-prompt
            active-text="正常"
            inactive-text="禁用"
          />
        </el-form-item>
      </div>
    </el-form>

    <!-- 表单底部按钮 -->
    <template #footer>
      <div class="drawer-footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          {{ isEdit ? '保存' : '新增' }}
        </el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { Plus, Document } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getDepartments } from '@/api/student'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  isEdit: {
    type: Boolean,
    default: false
  },
  editData: {
    type: Object,
    default: () => ({})
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

// 表单数据
const formRef = ref(null)
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
    const data = await getDepartments()
    if (data) {
      departmentOptions.value = data
      console.log('院系数据:', data)
    }
  } catch (error) {
    ElMessage.error('获取院系信息失败：' + error.message)
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

// 提交表单
const submitting = ref(false)
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    emit('success', form.value)
  } catch (error) {
    console.error('表单验证失败:', error)
  } finally {
    submitting.value = false
  }
}

// 计算属性
const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

// 监听编辑数据变化
watch(() => props.editData, (newVal) => {
  if (newVal && Object.keys(newVal).length > 0) {
    form.value = { ...newVal }
  }
}, { deep: true })

// 初始化加载数据
fetchDepartments()
</script>

<style scoped lang="scss">
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

.form-item-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}
</style> 