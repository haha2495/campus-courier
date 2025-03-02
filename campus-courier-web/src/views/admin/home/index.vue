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
      <!-- 任务趋势图表 -->
      <el-col :span="16">
        <el-card class="chart-card trend-card">
          <template #header>
            <div class="card-header">
              <h3>任务趋势分析</h3>
              <div class="header-right">
                <el-radio-group v-model="timeRange" size="small">
                  <el-radio-button label="week">本周</el-radio-button>
                  <el-radio-button label="month">本月</el-radio-button>
                </el-radio-group>
              </div>
            </div>
          </template>
          <div id="taskTrendChart" class="trend-chart"></div>
        </el-card>
      </el-col>

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
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { 
  Bicycle, 
  User, 
  Money, 
  DataLine,
  ArrowUp,
  ArrowDown
} from '@element-plus/icons-vue'

// 统计数据
const statisticsData = [
  {
    label: '总任务数',
    value: '2,851',
    icon: 'Bicycle',
    type: 'primary',
    trend: 'up',
    percentage: '12%'
  },
  {
    label: '活跃用户',
    value: '1,264',
    icon: 'User',
    type: 'success',
    trend: 'up',
    percentage: '8%'
  },
  {
    label: '总收入',
    value: '￥15,841',
    icon: 'Money',
    type: 'warning',
    trend: 'up',
    percentage: '15%'
  },
  {
    label: '完成率',
    value: '94.5%',
    icon: 'DataLine',
    type: 'info',
    trend: 'down',
    percentage: '2%'
  }
]

// 时间范围
const timeRange = ref('week')

// 任务类型数据
const taskTypes = [
  { name: '快递代取', value: 45, color: '#409EFF' },
  { name: '食堂代购', value: 25, color: '#67C23A' },
  { name: '打印服务', value: 15, color: '#E6A23C' },
  { name: '校园跑腿', value: 10, color: '#F56C6C' },
  { name: '其他服务', value: 5, color: '#909399' }
]

// 初始化图表
onMounted(() => {
  // 任务趋势图表
  const taskTrendChart = echarts.init(document.getElementById('taskTrendChart'))
  taskTrendChart.setOption({
    tooltip: {
      trigger: 'axis'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '已完成',
        type: 'line',
        smooth: true,
        data: [120, 132, 101, 134, 90, 230, 210],
        areaStyle: {
          opacity: 0.1
        },
        lineStyle: {
          width: 3
        },
        itemStyle: {
          color: '#409EFF'
        }
      },
      {
        name: '进行中',
        type: 'line',
        smooth: true,
        data: [220, 182, 191, 234, 290, 330, 310],
        areaStyle: {
          opacity: 0.1
        },
        lineStyle: {
          width: 3
        },
        itemStyle: {
          color: '#67C23A'
        }
      }
    ]
  })

  // 任务类型图表
  const taskTypeChart = echarts.init(document.getElementById('taskTypeChart'))
  taskTypeChart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}%<br/>占比: {d}%'
    },
    series: [
      {
        type: 'pie',
        radius: ['45%', '70%'],
        center: ['50%', '50%'],
        data: taskTypes.map(item => ({
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

  // 监听窗口大小变化
  window.addEventListener('resize', () => {
    taskTrendChart.resize()
    taskTypeChart.resize()
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

    .trend-card {
      height: 480px;
      
      .trend-chart {
        height: 400px;
        width: 100%;
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
