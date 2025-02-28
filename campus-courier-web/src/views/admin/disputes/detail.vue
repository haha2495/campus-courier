<template>
  <div class="dispute-detail">
    <!-- 顶部返回区域 -->
    <el-card class="header-card" shadow="hover">
      <div class="header-wrapper">
        <div class="back-section" @click="handleBack">
          <el-icon class="back-icon"><ArrowLeft /></el-icon>
          <span>返回列表</span>
        </div>
        <div class="title">纠纷详情</div>
      </div>
    </el-card>

    <!-- 内容区域 -->
    <el-card class="content-card" shadow="hover">
      <div class="content-wrapper" v-loading="loading">
        <!-- 后续添加详情内容 -->
        <el-empty description="正在开发中..." />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const loading = ref(false)

// 获取路由参数
const taskId = route.query.taskId

// 返回列表页
const handleBack = () => {
  router.back()
}

// 获取详情数据
const fetchDetail = async () => {
  if (!taskId) {
    ElMessage.error('参数错误')
    router.replace('/admin/disputes')
    return
  }
  
  loading.value = true
  try {
    // TODO: 调用获取详情接口
    console.log('获取纠纷详情:', taskId)
  } catch (error) {
    console.error('获取纠纷详情失败:', error)
    ElMessage.error('系统异常，请稍后重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchDetail()
})
</script>

<style scoped lang="scss">
.dispute-detail {
  padding: 16px;
  min-height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
  gap: 16px;
}

/* 头部卡片样式 */
.header-card {
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

  :deep(.el-card__body) {
    padding: 16px 20px;
  }
}

.header-wrapper {
  display: flex;
  align-items: center;
  position: relative;
  height: 32px;

  .back-section {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    color: #666;
    transition: all 0.3s;
    position: absolute;
    left: 0;

    &:hover {
      color: #409EFF;
    }

    .back-icon {
      font-size: 16px;
    }
  }

  .title {
    flex: 1;
    text-align: center;
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }
}

/* 内容卡片样式 */
.content-card {
  flex: 1;
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  background: #fff;

  :deep(.el-card__body) {
    padding: 20px;
    min-height: 300px;
  }
}

.content-wrapper {
  min-height: inherit;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style> 