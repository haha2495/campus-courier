<template>
  <div class="disputes-container admin-page">
    <el-card class="disputes-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">
            <div class="title-icon">⚖️</div>
            纠纷管理
          </span>
        </div>
      </template>
      
      <div class="content-placeholder">
        <el-empty description="纠纷管理模块开发中...">
          <template #image>
            <div class="empty-icon">⚖️</div>
          </template>
        </el-empty>
      </div>
    </el-card>
  </div>
</template>

<script setup>
// 组件逻辑将在这里实现
</script>

<style scoped>
.disputes-container {
  padding: 20px;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.disputes-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
}

.title-icon {
  font-size: 24px;
  margin-right: 8px;
}

.content-placeholder {
  padding: 40px;
  text-align: center;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 20px;
}
</style> 