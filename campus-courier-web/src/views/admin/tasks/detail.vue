<template>
  <div class="task-detail">
    <el-card class="detail-card">
      <template #header>
        <div class="card-header">
          <span class="title">任务详情</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>
      <div class="detail-content">
        任务详情页面开发中...
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

// 返回上一页
const handleBack = () => {
  router.back()
}
</script>

<style scoped lang="scss">
.task-detail {
  padding: 20px;

  .detail-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .title {
        font-size: 16px;
        font-weight: 600;
      }
    }
  }
}
</style> 