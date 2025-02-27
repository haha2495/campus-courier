<template>
  <u-popup 
    :show="show" 
    mode="bottom" 
    :round="16"
    @close="handleClose"
    :safeAreaInsetBottom="true"
    :customStyle="{
      maxHeight: '92vh',
      overflow: 'hidden'
    }"
  >
    <view class="evaluate-popup">
      <!-- 头部 -->
      <view class="evaluate-header">
        <text class="title">评价跑腿者</text>
        <view class="close-btn" @tap="handleClose">
          <u-icon name="close" color="#999" size="32"></u-icon>
        </view>
      </view>

      <!-- 内容区域 -->
      <scroll-view 
        scroll-y 
        class="evaluate-content"
        :show-scrollbar="false"
        enhanced
        :bounces="true"
        :scroll-anchoring="true"
      >
        <!-- 评分 -->
        <view class="score-section">
          <view class="score-header">
            <text class="emoji">{{ getScoreEmoji(form.score) }}</text>
            <text class="score-text" :class="getScoreClass(form.score)">
              {{ getScoreText(form.score) }}
            </text>
          </view>
          <view class="score-stars">
            <u-rate
              v-model="form.score"
              :count="5"
              :allowHalf="true"
              activeColor="#ff9500"
              inactiveColor="#eee"
              size="40"
              gutter="10"
            ></u-rate>
            <text class="score-value">{{ form.score }}分</text>
          </view>
        </view>

        <!-- 标签选择 -->
        <view class="tags-section">
          <view class="section-header">
            <text class="section-title">评价标签</text>
            <text class="tag-tip">(最多选择{{ maxTags }}个)</text>
          </view>
          <view class="tags-grid">
            <view
              v-for="(tag, index) in tags"
              :key="index"
              class="tag-item"
              :class="{ active: form.tags.includes(tag) }"
              @tap="toggleTag(tag)"
            >
              <text class="tag-text">{{ tag }}</text>
              <text v-if="form.tags.includes(tag)" class="tag-icon">✓</text>
            </view>
          </view>
        </view>

        <!-- 评价内容 -->
        <view class="content-section">
          <view class="section-header">
            <text class="section-title">评价内容</text>
            <text class="content-tip">(选填)</text>
          </view>
          <view class="textarea-wrapper">
            <textarea
              v-model="form.content"
              class="evaluate-textarea"
              placeholder="请输入您的评价内容，帮助其他用户了解跑腿者的服务质量..."
              :maxlength="maxLength"
              :show-confirm-bar="false"
              :cursor-spacing="20"
            ></textarea>
            <text class="word-count" :class="{ 'word-limit': form.content.length >= maxLength }">
              {{ form.content.length }}/{{ maxLength }}
            </text>
          </view>
        </view>

        <!-- 匿名选项 -->
        <view class="anonymous-section">
          <view class="anonymous-left">
            <text class="anonymous-title">匿名评价</text>
            <text class="anonymous-desc">开启后评价将以匿名形式展示</text>
          </view>
          <u-switch 
            v-model="form.anonymous" 
            activeColor="#ff9500"
            size="28"
          ></u-switch>
        </view>
      </scroll-view>

      <!-- 底部按钮 -->
      <view class="evaluate-footer safe-area-bottom">
        <button
          class="submit-btn"
          :class="{ 
            'submit-btn-active': canSubmit && !submitting,
            'submit-btn-disabled': !canSubmit || submitting 
          }"
          :disabled="!canSubmit || submitting"
          :loading="submitting"
          @tap="handleSubmit"
        >
          <text class="btn-text">{{ submitting ? '提交中...' : '提交评价' }}</text>
          <text v-if="!submitting" class="btn-icon">→</text>
        </button>
      </view>
    </view>
  </u-popup>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { addTaskEvaluation } from '@/api/task'

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  taskId: {
    type: [Number, String],
    default: null
  }
})

const emit = defineEmits(['update:show', 'submit'])

// 常量定义
const maxTags = 5
const maxLength = 500

// 表单数据
const form = ref({
  taskId: props.taskId,
  score: 5,
  content: '',
  tags: [],
  anonymous: false
})

// 监听taskId变化
watch(() => props.taskId, (newVal) => {
  form.value.taskId = newVal
})

// 评价标签选项
const tags = [
  '准时送达',
  '态度友好',
  '服务周到',
  '包装完好',
  '沟通顺畅'
]

// 提交状态
const submitting = ref(false)

// 是否可以提交
const canSubmit = computed(() => {
  return form.value.score > 0 && !submitting.value
})

// 获取评分对应的表情
const getScoreEmoji = (score) => {
  if (score >= 4.5) return '🌟';
  if (score >= 4) return '😊';
  if (score >= 3) return '😐';
  return '😔';
}

// 获取评分对应的文本
const getScoreText = (score) => {
  if (score >= 4.5) return '非常满意';
  if (score >= 4) return '满意';
  if (score >= 3) return '一般';
  return '不满意';
}

// 获取评分对应的样式类
const getScoreClass = (score) => {
  if (score >= 4.5) return 'score-excellent';
  if (score >= 4) return 'score-good';
  if (score >= 3) return 'score-normal';
  return 'score-bad';
}

// 切换标签选择
const toggleTag = (tag) => {
  const index = form.value.tags.indexOf(tag)
  if (index > -1) {
    form.value.tags.splice(index, 1)
  } else if (form.value.tags.length < maxTags) {
    form.value.tags.push(tag)
  }
}

// 关闭弹窗
const handleClose = () => {
  if (submitting.value) return
  emit('update:show', false)
  // 重置表单
  resetForm()
}

// 重置表单
const resetForm = () => {
  form.value = {
    taskId: props.taskId,
    score: 5,
    content: '',
    tags: [],
    anonymous: false
  }
}

// 提交评价
const handleSubmit = async () => {
  if (!canSubmit.value) return
  
  // 表单验证
  if (!form.value.taskId) {
    uni.showToast({
      title: '任务ID不能为空',
      icon: 'none'
    })
    return
  }
  
  submitting.value = true
  uni.showLoading({
    title: '提交中...',
    mask: true
  })
  
  try {
    // 调用后端接口
    const res = await addTaskEvaluation({
      taskId: form.value.taskId,
      score: form.value.score,
      content: form.value.content.trim(),
      tags: form.value.tags,
      anonymous: form.value.anonymous
    })
    
    // 提交成功
    uni.showToast({
      title: '评价成功',
      icon: 'success'
    })
    
    // 触发成功事件
    emit('submit', { ...form.value })
    
    // 关闭弹窗并重置表单
    emit('update:show', false)
    resetForm()
    
  } catch (error) {
    // 提交失败
    uni.showToast({
      title: error.message || '评价失败',
      icon: 'none'
    })
  } finally {
    submitting.value = false
    uni.hideLoading()
  }
}
</script>

<style lang="scss" scoped>
.evaluate-popup {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: linear-gradient(to bottom, #ffffff, #f8fafc);
  border-radius: 32rpx 32rpx 0 0;
  overflow: hidden;
  width: 100%;
  
  .evaluate-header {
    flex-shrink: 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 32rpx 40rpx;
    background: rgba(255, 255, 255, 0.98);
    backdrop-filter: blur(20px);
    border-bottom: 2rpx solid rgba(0, 0, 0, 0.04);
    position: relative;
    z-index: 10;

    &::after {
      content: '';
      position: absolute;
      left: 0;
      right: 0;
      bottom: -20rpx;
      height: 20rpx;
      background: linear-gradient(to bottom, rgba(0,0,0,0.02), transparent);
      pointer-events: none;
    }

    .title {
      font-size: 36rpx;
      font-weight: 600;
      color: #333;
      letter-spacing: 1px;
    }

    .close-btn {
      width: 72rpx;
      height: 72rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      background: #f5f5f5;
      transition: all 0.3s;

      &:active {
        transform: scale(0.9);
        background: #e8e8e8;
      }
    }
  }

  .evaluate-content {
    flex: 1;
    overflow-y: auto;
    padding: 32rpx 40rpx;
    padding-bottom: calc(120rpx + env(safe-area-inset-bottom));
    box-sizing: border-box;
    width: 100%;

    .section-header {
      display: flex;
      align-items: center;
      gap: 12rpx;
      margin-bottom: 24rpx;

      .section-title {
        font-size: 32rpx;
        font-weight: 600;
        color: #333;
      }

      .tag-tip, .content-tip {
        font-size: 24rpx;
        color: #999;
        font-weight: normal;
      }
    }

    .score-section {
      background: #fff;
      border-radius: 24rpx;
      padding: 32rpx;
      margin-bottom: 32rpx;
      box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);
      
      .score-header {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 16rpx;
        margin-bottom: 24rpx;

        .emoji {
          font-size: 48rpx;
          animation: bounce 1s ease infinite;
        }

        .score-text {
          font-size: 36rpx;
          font-weight: 600;
          
          &.score-excellent {
            color: #ff9500;
            background: linear-gradient(120deg, #ff9500, #ff6b00);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
          }
          
          &.score-good { color: #07c160; }
          &.score-normal { color: #2b85e4; }
          &.score-bad { color: #ff4d4f; }
        }
      }

      .score-stars {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 16rpx;

        .score-value {
          font-size: 48rpx;
          font-weight: 700;
          color: #ff9500;
          font-family: 'DIN Condensed', -apple-system, BlinkMacSystemFont, sans-serif;
        }
      }
    }

    .tags-section {
      margin-bottom: 32rpx;

      .tags-grid {
        width: 100%;
        box-sizing: border-box;
        display: flex;
        flex-wrap: wrap;
        gap: 16rpx;
        justify-content: flex-start;

        .tag-item {
          padding: 16rpx 24rpx;
          border-radius: 32rpx;
          font-size: 28rpx;
          color: #666;
          background: #f5f5f5;
          transition: all 0.3s;
          display: flex;
          align-items: center;
          gap: 8rpx;
          border: 2rpx solid transparent;
          box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);

          .tag-text {
            transition: all 0.3s;
          }

          .tag-icon {
            opacity: 0;
            transform: scale(0);
            transition: all 0.3s;
          }

          &.active {
            color: #ff9500;
            background: #fff7e6;
            border-color: #ff9500;
            font-weight: 500;
            box-shadow: 0 2rpx 12rpx rgba(255, 149, 0, 0.2);

            .tag-icon {
              opacity: 1;
              transform: scale(1);
            }
          }

          &:active {
            transform: scale(0.95);
          }
        }
      }
    }

    .content-section {
      margin-bottom: 32rpx;

      .textarea-wrapper {
        width: 100%;
        box-sizing: border-box;
        margin: 0;
        position: relative;
        background: #fff;
        border-radius: 24rpx;
        padding: 24rpx;
        box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);

        .evaluate-textarea {
          width: 100%;
          height: 240rpx;
          font-size: 28rpx;
          line-height: 1.6;
          color: #333;
          padding: 0;
        }

        .word-count {
          position: absolute;
          right: 24rpx;
          bottom: 24rpx;
          font-size: 24rpx;
          color: #999;
          padding: 4rpx 12rpx;
          background: rgba(0, 0, 0, 0.03);
          border-radius: 20rpx;

          &.word-limit {
            color: #ff4d4f;
            background: rgba(255, 77, 79, 0.1);
          }
        }
      }
    }

    .anonymous-section {
      background: #fff;
      border-radius: 24rpx;
      padding: 24rpx;
      margin-bottom: 32rpx;
      display: flex;
      justify-content: space-between;
      align-items: center;
      box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.04);

      .anonymous-left {
        .anonymous-title {
          font-size: 28rpx;
          font-weight: 500;
          color: #333;
          margin-bottom: 8rpx;
        }

        .anonymous-desc {
          font-size: 24rpx;
          color: #999;
        }
      }
    }
  }

  .evaluate-footer {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    padding: 24rpx 40rpx;
    padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
    background: rgba(255, 255, 255, 0.98);
    backdrop-filter: blur(20px);
    border-top: 2rpx solid rgba(0, 0, 0, 0.04);
    z-index: 10;
    width: 100%;
    box-sizing: border-box;

    .submit-btn {
      width: 100%;
      height: 88rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 12rpx;
      background: linear-gradient(120deg, #ff9500, #ff6b00);
      color: #fff;
      font-size: 32rpx;
      font-weight: 600;
      border-radius: 44rpx;
      transition: all 0.3s;
      overflow: hidden;
      position: relative;

      &::after {
        content: '';
        position: absolute;
        top: -50%;
        left: -50%;
        width: 200%;
        height: 200%;
        background: linear-gradient(
          to right,
          rgba(255, 255, 255, 0) 0%,
          rgba(255, 255, 255, 0.2) 50%,
          rgba(255, 255, 255, 0) 100%
        );
        transform: rotate(45deg);
        animation: shine 2s infinite;
      }

      &:active {
        transform: scale(0.98);
      }

      &.submit-btn-disabled {
        background: #ccc;
        opacity: 0.6;
      }

      .btn-text {
        z-index: 1;
      }

      .btn-icon {
        font-size: 28rpx;
        transition: transform 0.3s;
        z-index: 1;
      }

      &:active .btn-icon {
        transform: translateX(10rpx);
      }
    }
  }
}

@keyframes shine {
  0% {
    transform: translateX(-100%) rotate(45deg);
  }
  100% {
    transform: translateX(100%) rotate(45deg);
  }
}

@keyframes bounce {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

/* 隐藏滚动条 */
::-webkit-scrollbar {
  width: 0;
  height: 0;
  background: transparent;
}

/* 安全区适配 */
.safe-area-bottom {
  padding-bottom: calc(24rpx + env(safe-area-inset-bottom));
}
</style> 