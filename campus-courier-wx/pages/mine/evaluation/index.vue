<template>
  <view class="evaluation-container">
    
    <!-- 评价统计 -->
    <view class="stats-card">
      <view class="stats-item">
        <text class="stats-value">{{ statistics.averageScore || '0.0' }}</text>
        <text class="stats-label">综合评分</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-value">{{ formatRate(statistics.positiveRate) }}</text>
        <text class="stats-label">好评率</text>
      </view>
      <view class="stats-divider"></view>
      <view class="stats-item">
        <text class="stats-value">{{ statistics.totalCount || 0 }}</text>
        <text class="stats-label">评价总数</text>
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
              <u-rate 
                :value="parseFloat(item.score)"
                :count="5"
                allowHalf
                readonly 
                activeColor="#ff9500"
                size="32"
                inactiveColor="#eee"
              />
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
  background-color: #f7f8fa;
}

.stats-card {
  margin: 30rpx;
  background-color: #ffffff;
  border-radius: 20rpx;
  padding: 40rpx 30rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);

  .stats-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .stats-value {
      font-size: 40rpx;
      font-weight: 600;
      color: #333333;
      margin-bottom: 8rpx;
    }
    
    .stats-label {
      font-size: 24rpx;
      color: #999999;
    }
  }

  .stats-divider {
    width: 2rpx;
    height: 60rpx;
    background-color: #f0f0f0;
  }
}

.evaluation-list {
  margin: 30rpx;

  .evaluation-item {
    background-color: #ffffff;
    border-radius: 20rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;

    .evaluation-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16rpx;

      .score {
        display: flex;
        align-items: center;
        gap: 12rpx;

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
      line-height: 1.5;
      margin-bottom: 16rpx;
    }

    .evaluation-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 12rpx;
    }
  }
}
</style> 