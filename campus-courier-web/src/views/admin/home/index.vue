<template>
  <div class="admin-home">
    <!-- 主要内容区 -->
    <el-row :gutter="20" class="main-content">
      <!-- 左侧区域 -->
      <el-col :span="16">
      </el-col>

      <!-- 右侧区域 -->
      <el-col :span="8">
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
</script>

<style scoped lang="scss">
.admin-home {
  .main-content {
    .el-card {
      margin-bottom: 20px;
      border-radius: 12px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);

      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}
</style>
