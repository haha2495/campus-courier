<template>
  <div class="admin-home">
    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="data-overview">
      <el-col :span="6" v-for="(item, index) in statisticsData" :key="index">
        <el-card class="statistics-card" :class="item.type">
          <div class="card-content">
            <div class="icon-wrapper">
              <el-icon><component :is="item.icon" /></el-icon>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ item.value }}</div>
              <div class="label">{{ item.label }}</div>
            </div>
            <div class="trend" :class="item.trend">
              {{ item.percentage }}
              <el-icon><component :is="item.trend === 'up' ? 'ArrowUp' : 'ArrowDown'" /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 主要内容区 -->
    <el-row :gutter="20" class="main-content">
      <!-- 任务类型分布 -->
      <el-col :span="8">
        <el-card class="chart-card type-card">
          <template #header>
            <div class="card-header">
              <h3>任务类型分布</h3>
            </div>
          </template>
          <div id="taskTypeChart" class="type-chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import * as echarts from 'echarts'
import { 
  Bicycle, 
  User, 
  Money, 
  DataLine,
  ArrowUp,
  ArrowDown
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 统计数据
const statisticsData = ref([
  {
    label: '总任务数',
    value: '0',
    icon: 'Bicycle',
    type: 'primary',
    trend: 'up',
    percentage: '0%'
  },
  {
    label: '活跃用户',
    value: '0',
    icon: 'User',
    type: 'success',
    trend: 'up',
    percentage: '0%'
  },
  {
    label: '总收入',
    value: '￥0',
    icon: 'Money',
    type: 'warning',
    trend: 'up',
    percentage: '0%'
  },
  {
    label: '完成率',
    value: '0%',
    icon: 'DataLine',
    type: 'info',
    trend: 'up',
    percentage: '0%'
  }
])

// 任务类型数据
const taskTypes = ref([])

// 获取仪表盘数据
const fetchDashboardStats = async () => {
  try {
    const { data } = await axios.get('/api/dashboard/stats')
    if (data.code === 200) {
      const stats = data.data
      
      // 更新统计卡片数据
      statisticsData.value = [
        {
          label: '总任务数',
          value: stats.totalTaskCount.toLocaleString(),
          icon: 'Bicycle',
          type: 'primary',
          trend: stats.totalTaskGrowth >= 0 ? 'up' : 'down',
          percentage: `${Math.abs(stats.totalTaskGrowth).toFixed(1)}%`
        },
        {
          label: '活跃用户',
          value: stats.activeUserCount.toLocaleString(),
          icon: 'User',
          type: 'success',
          trend: stats.activeUserGrowth >= 0 ? 'up' : 'down',
          percentage: `${Math.abs(stats.activeUserGrowth).toFixed(1)}%`
        },
        {
          label: '总收入',
          value: `￥${stats.totalIncome.toLocaleString()}`,
          icon: 'Money',
          type: 'warning',
          trend: stats.totalIncomeGrowth >= 0 ? 'up' : 'down',
          percentage: `${Math.abs(stats.totalIncomeGrowth).toFixed(1)}%`
        },
        {
          label: '完成率',
          value: `${stats.taskCompletionRate.toFixed(1)}%`,
          icon: 'DataLine',
          type: 'info',
          trend: stats.completionRateChange >= 0 ? 'up' : 'down',
          percentage: `${Math.abs(stats.completionRateChange).toFixed(1)}%`
        }
      ]
    } else {
      ElMessage.error(data.message || '获取统计数据失败')
    }
  } catch (error) {
    console.error('获取统计数据出错:', error)
    ElMessage.error('获取统计数据失败')
  }
}

// 获取任务类型统计数据
const fetchTaskTypeStats = async () => {
  try {
    const { data } = await axios.get('/api/dashboard/task-type-stats')
    if (data.code === 200) {
      // 设置固定的颜色数组
      const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
      
      // 为每个类型分配颜色
      taskTypes.value = data.data.map((item, index) => ({
        name: item.typeName,
        value: item.taskCount,
        percentage: item.percentage,
        color: colors[index % colors.length] // 循环使用颜色
      }))
    } else {
      ElMessage.error(data.message || '获取任务类型统计失败')
    }
  } catch (error) {
    console.error('获取任务类型统计出错:', error)
    ElMessage.error('获取任务类型统计失败')
  }
}

// 图表实例引用
const taskTypeChart = ref(null)

// 初始化图表
onMounted(() => {
  // 获取统计数据
  fetchDashboardStats()
  // 获取任务类型统计
  fetchTaskTypeStats()

  // 任务类型图表
  taskTypeChart.value = echarts.init(document.getElementById('taskTypeChart'))
  
  // 监听数据变化，更新图表
  watch(taskTypes, (newValue) => {
    if (newValue.length > 0) {
      taskTypeChart.value.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} 个<br/>占比: {d}%'
        },
        series: [
          {
            type: 'pie',
            radius: ['45%', '70%'],
            center: ['50%', '50%'],
            data: newValue.map(item => ({
              name: item.name,
              value: item.value,
              itemStyle: {
                color: item.color
              }
            })),
            label: {
              show: true,
              position: 'outside',
              formatter: '{b}\n{d}%',
              fontSize: 12,
              color: '#606266'
            },
            labelLine: {
              length: 10,
              length2: 10,
              smooth: true
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 14,
                fontWeight: 'bold'
              },
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      })
    }
  }, { immediate: true })

  // 监听窗口大小变化
  window.addEventListener('resize', () => {
    taskTypeChart.value?.resize()
  })
})
</script>

<style scoped lang="scss">
.admin-home {
  .data-overview {
    margin-bottom: 20px;
  }

  .statistics-card {
    .card-content {
      display: flex;
      align-items: center;
      padding: 20px;
      position: relative;
      overflow: hidden;

      .icon-wrapper {
        width: 48px;
        height: 48px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
        font-size: 24px;
      }

      .data-wrapper {
        flex: 1;

        .number {
          font-size: 24px;
          font-weight: 600;
          line-height: 1;
          margin-bottom: 8px;
        }

        .label {
          font-size: 14px;
          color: #909399;
        }
      }

      .trend {
        position: absolute;
        right: 20px;
        top: 20px;
        font-size: 12px;
        display: flex;
        align-items: center;
        gap: 4px;

        &.up {
          color: #67C23A;
        }

        &.down {
          color: #F56C6C;
        }
      }
    }

    &.primary .icon-wrapper {
      background: rgba(64, 158, 255, 0.1);
      color: #409EFF;
    }

    &.success .icon-wrapper {
      background: rgba(103, 194, 58, 0.1);
      color: #67C23A;
    }

    &.warning .icon-wrapper {
      background: rgba(230, 162, 60, 0.1);
      color: #E6A23C;
    }

    &.info .icon-wrapper {
      background: rgba(144, 147, 153, 0.1);
      color: #909399;
    }
  }

  .main-content {
    .chart-card {
      margin-bottom: 20px;

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        h3 {
          font-size: 16px;
          font-weight: 600;
          margin: 0;
        }
      }
    }

    .type-card {
      height: 480px;

      .type-chart {
        height: 400px;
        width: 100%;
      }
    }
  }
}
</style>
