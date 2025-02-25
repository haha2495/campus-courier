<template>
  <el-drawer
    v-model="visible"
    :title="title"
    size="680px"
    :close-on-click-modal="false"
    :close-on-press-escape="true"
    destroy-on-close
  >
    <div class="record-drawer">
      <!-- 记录时间线 -->
      <el-timeline>
        <el-timeline-item
          v-for="record in records"
          :key="record.createTime"
          :type="getTimelineItemType(record.type)"
          :color="getTimelineItemColor(record.type)"
          :hollow="true"
          size="large"
        >
          <div class="timeline-wrapper">
            <div class="time-label">{{ record.createTime }}</div>
            <div class="timeline-content">
              <el-icon class="record-icon">
                <component :is="getRecordIcon(record.type)" />
              </el-icon>
              <span class="record-content">{{ record.content }}</span>
            </div>
          </div>
        </el-timeline-item>
      </el-timeline>

      <!-- 无数据提示 -->
      <el-empty
        v-if="records.length === 0"
        description="暂无记录"
      />
    </div>
  </el-drawer>
</template>

<script setup>
import {computed, ref, watch} from 'vue'
import {getStudentRecords} from '@/api/student'
import {ElMessage} from 'element-plus'
import {CircleCheck, CircleClose, Edit, Lock, Minus, Plus, User} from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
  studentId: {
    type: [String, Number],
    required: true
  },
  studentName: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['update:modelValue'])

// 抽屉可见性
const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

// 抽屉标题
const title = computed(() => `${props.studentName} 的操作记录`)

// 记录列表
const records = ref([])
const loading = ref(false)

// 加载记录数据
const loadRecords = async () => {
  if (!props.studentId) return
  
  try {
    loading.value = true
    records.value = await getStudentRecords(props.studentId)
  } catch (error) {
    ElMessage.error('获取记录失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 获取记录图标
const getRecordIcon = (type) => {
  const icons = {
    '创建账号': User,
    '重置密码': Lock,
    '加入社团': Plus,
    '退出社团': Minus,
    '身份变更': Edit,
    '禁用账号': CircleClose,
    '启用账号': CircleCheck,
    '社团职位变更': Edit
  }
  return icons[type] || User
}

// 获取时间线项类型
const getTimelineItemType = (type) => {
  const types = {
    '创建账号': 'primary',
    '重置密码': 'warning',
    '加入社团': 'success',
    '退出社团': 'info',
    '身份变更': 'warning',
    '禁用账号': 'danger',
    '启用账号': 'success',
    '社团职位变更': 'primary'
  }
  return types[type] || 'info'
}

// 获取时间线项颜色
const getTimelineItemColor = (type) => {
  const colors = {
    '创建账号': '#409EFF',
    '重置密码': '#E6A23C',
    '加入社团': '#67C23A',
    '退出社团': '#909399',
    '身份变更': '#E6A23C',
    '禁用账号': '#F56C6C',
    '启用账号': '#67C23A',
    '社团职位变更': '#409EFF'
  }
  return colors[type] || '#909399'
}

// 监听抽屉显示状态
watch(() => visible.value, (val) => {
  if (val) {
    loadRecords()
  } else {
    records.value = []
  }
})
</script>

<style lang="scss" scoped>
.record-drawer {
  padding: 0 20px;
  
  :deep(.el-timeline) {
    padding: 16px 0;
    
    .el-timeline-item {
      .el-timeline-item__node {
        background: transparent;
        border-width: 2px;
      }

      .timeline-wrapper {
        .time-label {
          color: #909399;
          font-size: 13px;
          margin-bottom: 8px;
          padding-left: 4px;
        }
        
        .timeline-content {
          display: flex;
          align-items: center;
          gap: 8px;
          padding: 12px 16px;
          background: #f8f9fa;
          border-radius: 8px;
          transition: all 0.3s ease;
          
          &:hover {
            background: #f2f3f5;
            transform: translateY(-2px);
          }
          
          .record-icon {
            font-size: 18px;
          }
          
          .record-content {
            color: #303133;
            font-size: 14px;
            line-height: 1.5;
          }
        }
      }
    }
  }
  
  :deep(.el-empty) {
    padding: 40px 0;
  }
}
</style> 