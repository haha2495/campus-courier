<template>
  <view class="dispute-supports">
    <template v-if="disputeSupports && disputeSupports.length > 0">
      <view class="support-content">
        支持信息
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

defineProps({
  dispute: {
    type: Object,
    default: () => null
  },
  disputeSupports: {
    type: Array,
    default: () => []
  }
})
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