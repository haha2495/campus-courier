<template>
  <view class="evaluation-container">
    
    <!-- 评价统计 -->
    <view class="stats-card">
      <view class="stats-item main-score">
        <text class="big-score">{{ statistics.averageScore || '0.0' }}</text>
        <text class="stats-label">综合评分</text>
      </view>
      <view class="stats-right">
        <view class="stats-item">
          <text class="stats-value highlight">{{ formatRate(statistics.positiveRate) }}</text>
          <text class="stats-label">好评率</text>
        </view>
        <view class="stats-divider"></view>
        <view class="stats-item">
          <text class="stats-value highlight">{{ statistics.totalCount || 0 }}</text>
          <text class="stats-label">评价总数</text>
        </view>
      </view>
    </view>

    <!-- 评价列表 -->
    <view class="evaluation-list">
      <template v-if="evaluations.length > 0">
        <view 
          class="evaluation-item" 
          v-for="item in evaluations" 
          :key="item.id"
        >
          <view class="evaluation-header">
            <view class="score">
              <view class="star-list">
                <text 
                  v-for="index in 5" 
                  :key="index"
                  class="star-item"
                  :class="{ 
                    'star-active': index <= Math.floor(item.score),
                    'star-half': index === Math.ceil(item.score) && item.score % 1 !== 0
                  }"
                >★</text>
              </view>
              <text class="score-text">{{ item.score }}分</text>
            </view>
            <text class="time">{{ formatTime(item.createTime) }}</text>
          </view>
          <view class="evaluation-content" v-if="item.content">
            {{ item.content }}
          </view>
          <view class="evaluation-tags" v-if="item.tags && item.tags.length > 0">
            <u-tag
              v-for="(tag, index) in item.tags"
              :key="index"
              :text="tag"
              plain
              size="mini"
              type="warning"
            />
          </view>
        </view>
      </template>
      <template v-else>
        <u-empty mode="data" text="暂无评价记录"></u-empty>
      </template>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getReceivedEvaluations } from '@/api/evaluation'
import dayjs from 'dayjs'

// 评价数据
const statistics = ref({})
const evaluations = ref([])

// 获取评价列表
const getEvaluationList = async () => {
  try {
    const res = await getReceivedEvaluations()
    if (res.code === 200) {
      statistics.value = res.data.statistics
      evaluations.value = res.data.evaluations
      // 调试输出
      console.log('评价列表:', evaluations.value)
      evaluations.value.forEach(item => {
        console.log('评分值:', item.score, '类型:', typeof item.score)
      })
    } else {
      uni.showToast({
        title: res.message || '获取评价列表失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('获取评价列表失败:', error)
    uni.showToast({
      title: '获取评价列表失败',
      icon: 'none'
    })
  }
}

// 格式化好评率
const formatRate = (rate) => {
  if (!rate && rate !== 0) return '0%'
  return `${(rate * 100).toFixed(0)}%`
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

// 返回上一页
const navigateBack = () => {
  uni.navigateBack()
}

onMounted(() => {
  getEvaluationList()
})
</script>

<style lang="scss" scoped>
.evaluation-container {
  min-height: 100vh;
  background-color: #f8f9fd;
  padding: 20rpx 0;
}

.stats-card {
  margin: 30rpx;
  background: linear-gradient(135deg, #ffffff 0%, #f9f9f9 100%);
  border-radius: 24rpx;
  padding: 40rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.08);
  position: relative;
  overflow: hidden;

  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 6rpx;
    background: linear-gradient(90deg, #ff9500, #ffb700);
  }

  .main-score {
    flex: 1.2;
    padding-right: 40rpx;
    display: flex;
    flex-direction: column;
    align-items: flex-start;

    .big-score {
      font-size: 72rpx;
      font-weight: 600;
      background: linear-gradient(90deg, #ff9500, #ffb700);
      -webkit-background-clip: text;
      color: transparent;
      line-height: 1;
      margin-bottom: 16rpx;
    }
  }

  .stats-right {
    flex: 1;
    display: flex;
    justify-content: space-around;
    position: relative;
    
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 2rpx;
      height: 80%;
      background: #f0f0f0;
    }
  }

  .stats-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .stats-value {
      font-size: 36rpx;
      font-weight: 600;
      color: #333333;
      margin-bottom: 8rpx;
      
      &.highlight {
        color: #ff9500;
      }
    }
    
    .stats-label {
      font-size: 24rpx;
      color: #999999;
    }
  }
}

.evaluation-list {
  margin: 30rpx;

  .evaluation-item {
    background-color: #ffffff;
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
    transition: transform 0.2s ease;

    &:active {
      transform: scale(0.98);
    }

    .evaluation-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;

      .score {
        display: flex;
        align-items: center;
        gap: 16rpx;

        .score-text {
          font-size: 28rpx;
          color: #ff9500;
          font-weight: 500;
        }
      }

      .time {
        font-size: 24rpx;
        color: #999999;
      }
    }

    .evaluation-content {
      font-size: 28rpx;
      color: #333333;
      line-height: 1.6;
      margin-bottom: 20rpx;
    }

    .evaluation-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 12rpx;
    }
  }
}

.star-list {
  display: flex;
  align-items: center;
  gap: 6rpx;

  .star-item {
    font-size: 36rpx;
    color: #e8e8e8;
    position: relative;
    transition: all 0.3s ease;
    transform-origin: center;
    
    &.star-active {
      color: #ff9500;
      text-shadow: 0 2rpx 8rpx rgba(255, 149, 0, 0.3);
      transform: scale(1.05);
    }
    
    &.star-half {
      background: linear-gradient(90deg, #ff9500 50%, #e8e8e8 50%);
      -webkit-background-clip: text;
      color: transparent;
      transform: scale(1.05);
    }
  }
}

/* 修改标签样式 */
::v-deep .u-tag--warning {
  background-color: rgba(255, 149, 0, 0.1) !important;
  border-color: rgba(255, 149, 0, 0.2) !important;
}

::v-deep .u-tag--warning--text {
  color: #ff9500 !important;
}
</style> 