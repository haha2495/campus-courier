<template>
  <view class="dispute-supports">
    <template v-if="disputeSupports && disputeSupports.length > 0">
      <view class="support-content">
        <!-- 支持数据对比区域 -->
        <view class="support-comparison">
          <!-- 发布者支持信息 -->
          <view class="support-side publisher">
            <view class="side-header">
              <u-icon name="account" size="32" color="#3B82F6"></u-icon>
              <text class="side-title">发布者</text>
            </view>
            <text class="support-count">{{ publisherCount }}</text>
            <text class="support-percent">{{ publisherPercent }}%</text>
            <view class="progress-bar">
              <view 
                class="progress-inner publisher" 
                :style="{ width: publisherPercent + '%' }"
              ></view>
            </view>
          </view>

          <!-- 中间VS -->
          <view class="vs-section">
            <view class="vs-circle">
              <text>VS</text>
            </view>
          </view>

          <!-- 跑腿者支持信息 -->
          <view class="support-side runner">
            <view class="side-header">
              <u-icon name="car" size="32" color="#10B981"></u-icon>
              <text class="side-title">跑腿者</text>
            </view>
            <text class="support-count">{{ runnerCount }}</text>
            <text class="support-percent">{{ runnerPercent }}%</text>
            <view class="progress-bar">
              <view 
                class="progress-inner runner" 
                :style="{ width: runnerPercent + '%' }"
              ></view>
            </view>
          </view>
        </view>

        <!-- 支持列表 -->
        <view class="support-list">
          <view 
            v-for="(support, index) in disputeSupports" 
            :key="index"
            class="support-item"
          >
            <view class="support-user">
              <text class="support-time">{{ formatTime(support.createTime) }}</text>
            </view>
            <view class="support-info">
              <text class="support-text">有用户支持了</text>
              <text :class="['support-target', support.supportType === 1 ? 'publisher' : 'runner']">
                {{ support.supportType === 1 ? '发布者' : '跑腿者' }}
              </text>
            </view>
            <text v-if="support.reason" class="support-remark">{{ support.reason }}</text>
          </view>
        </view>
      </view>
    </template>
    <template v-else>
      <view class="empty-state">
        <view class="court-empty">
          <!-- 天平图标 -->
          <view class="scale-icon">
            <u-icon name="file-text" size="80" color="#3B82F6"></u-icon>
          </view>
          
          <!-- 主标题 -->
          <view class="title-box">
            <text class="title-text">等待陪审团</text>
            <view class="dot-loading">
              <text class="dot"></text>
              <text class="dot"></text>
              <text class="dot"></text>
            </view>
          </view>
          
          <!-- 描述文本 -->
          <view class="desc-box">
            <text class="desc-text">其他用户正在仔细审阅案件</text>
            <text class="sub-desc">请耐心等待判决结果</text>
          </view>
          
          <!-- 装饰元素 -->
          <view class="decoration">
            <view class="line left"></view>
            <view class="gavel">
              <u-icon name="more-dot-fill" size="24" color="#E5E7EB"></u-icon>
            </view>
            <view class="line right"></view>
          </view>
        </view>
      </view>
    </template>
  </view>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  dispute: {
    type: Object,
    default: () => null
  },
  disputeSupports: {
    type: Array,
    default: () => []
  }
})

// 计算发布者支持数量 (supportType === 1)
const publisherCount = computed(() => {
  return props.disputeSupports.filter(s => s.supportType === 1).length
})

// 计算跑腿者支持数量 (supportType === 2)
const runnerCount = computed(() => {
  return props.disputeSupports.filter(s => s.supportType === 2).length
})

// 计算发布者支持百分比
const publisherPercent = computed(() => {
  const total = publisherCount.value + runnerCount.value
  return total ? Math.round((publisherCount.value / total) * 100) : 0
})

// 计算跑腿者支持百分比
const runnerPercent = computed(() => {
  const total = publisherCount.value + runnerCount.value
  return total ? Math.round((runnerCount.value / total) * 100) : 0
})

// 格式化时间
const formatTime = (timeString) => {
  if (!timeString) return ''
  const date = new Date(timeString)
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${month}-${day} ${hours}:${minutes}`
}
</script>

<style lang="scss" scoped>
.dispute-supports {
  padding: 20rpx;
  min-height: 400rpx;
  display: flex;
  flex-direction: column;
  
  .support-content {
    background: #FFFFFF;
    border-radius: 16rpx;
    padding: 30rpx;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);

    .support-comparison {
      display: flex;
      align-items: stretch;
      justify-content: space-between;
      padding: 30rpx;
      background: #F8FAFC;
      border-radius: 16rpx;
      margin-bottom: 30rpx;

      .support-side {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20rpx;

        .side-header {
          display: flex;
          align-items: center;
          gap: 8rpx;
          margin-bottom: 16rpx;

          .side-title {
            font-size: 28rpx;
            font-weight: 500;
          }
        }

        .support-count {
          font-size: 48rpx;
          font-weight: 600;
          font-family: 'DIN Condensed', sans-serif;
          margin-bottom: 8rpx;
        }

        .support-percent {
          font-size: 24rpx;
          color: #6B7280;
          margin-bottom: 16rpx;
        }

        .progress-bar {
          width: 100%;
          height: 8rpx;
          background: #E5E7EB;
          border-radius: 4rpx;
          overflow: hidden;

          .progress-inner {
            height: 100%;
            transition: width 0.3s ease;

            &.publisher {
              background: linear-gradient(to right, #3B82F6, #60A5FA);
            }

            &.runner {
              background: linear-gradient(to right, #10B981, #34D399);
            }
          }
        }

        &.publisher {
          .support-count {
            color: #3B82F6;
          }
        }

        &.runner {
          .support-count {
            color: #10B981;
          }
        }
      }

      .vs-section {
        display: flex;
        align-items: center;
        padding: 0 20rpx;

        .vs-circle {
          width: 60rpx;
          height: 60rpx;
          border-radius: 50%;
          background: #FFFFFF;
          display: flex;
          align-items: center;
          justify-content: center;
          box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);

          text {
            font-size: 24rpx;
            font-weight: 600;
            color: #6B7280;
          }
        }
      }
    }

    .support-list {
      margin-top: 40rpx;
      
      .support-item {
        padding: 30rpx;
        margin-bottom: 20rpx;
        border-radius: 12rpx;
        background: #F8FAFC;
        border: 2rpx solid #E5E7EB;

        &:last-child {
          margin-bottom: 0;
        }

        .support-user {
          display: flex;
          justify-content: flex-end;
          margin-bottom: 16rpx;

          .support-time {
            font-size: 24rpx;
            color: #9CA3AF;
          }
        }

        .support-info {
          display: flex;
          align-items: center;
          gap: 8rpx;
          margin-bottom: 16rpx;

          .support-text {
            font-size: 28rpx;
            color: #4B5563;
          }

          .support-target {
            font-size: 28rpx;
            font-weight: 600;

            &.publisher {
              color: #3B82F6;
            }

            &.runner {
              color: #10B981;
            }
          }
        }

        .support-remark {
          font-size: 28rpx;
          color: #4B5563;
          background: #FFFFFF;
          padding: 20rpx;
          border-radius: 8rpx;
          display: block;
          line-height: 1.5;
          border: 2rpx solid #E5E7EB;
        }
      }
    }
  }

  .empty-state {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 40rpx 0;

    .court-empty {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      padding: 40rpx;
      background: #F8FAFC;
      border-radius: 24rpx;
      
      .scale-icon {
        margin-bottom: 30rpx;
        background: rgba(59, 130, 246, 0.1);
        width: 120rpx;
        height: 120rpx;
        border-radius: 60rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        animation: float 3s ease-in-out infinite;
      }

      .title-box {
        display: flex;
        align-items: center;
        gap: 12rpx;
        margin-bottom: 24rpx;

        .title-text {
          font-size: 32rpx;
          color: #1F2937;
          font-weight: 600;
        }

        .dot-loading {
          display: flex;
          align-items: center;
          gap: 8rpx;
          
          .dot {
            width: 6rpx;
            height: 6rpx;
            background-color: #3B82F6;
            border-radius: 50%;
            
            @for $i from 1 through 3 {
              &:nth-child(#{$i}) {
                animation: dotBounce 1.4s #{$i * 0.2}s infinite ease-in-out;
              }
            }
          }
        }
      }

      .desc-box {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 8rpx;
        margin-bottom: 40rpx;

        .desc-text {
          font-size: 28rpx;
          color: #4B5563;
        }

        .sub-desc {
          font-size: 24rpx;
          color: #6B7280;
        }
      }

      .decoration {
        display: flex;
        align-items: center;
        width: 100%;
        margin-top: 20rpx;
        padding: 0 60rpx;

        .line {
          flex: 1;
          height: 1rpx;
          background: linear-gradient(to var(--direction, right), #E5E7EB 0%, transparent 100%);

          &.left {
            --direction: right;
          }

          &.right {
            --direction: left;
          }
        }

        .gavel {
          margin: 0 20rpx;
        }
      }
    }
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10rpx);
  }
}

@keyframes dotBounce {
  0%, 80%, 100% {
    transform: scale(0);
    opacity: 0.3;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}
</style> 