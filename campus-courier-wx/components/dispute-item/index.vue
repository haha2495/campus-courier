<template>
  <task-card :task="dispute" @click="handleClick">
    <template #actions>
      <button class="judge-btn" @click.stop="handleJudge">前去审判</button>
    </template>
  </task-card>
</template>

<script setup>
import TaskCard from '@/components/task-card/index.vue'

const props = defineProps({
  dispute: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['click', 'judge'])

// 点击卡片
const handleClick = () => {
  emit('click', props.dispute)
}

// 点击前去审判按钮
const handleJudge = () => {
  emit('judge', props.dispute)
}
</script>

<style lang="scss" scoped>
.judge-btn {
  background: linear-gradient(135deg, #ff4d4f 0%, #ff7875 100%);
  color: white;
  font-size: 24rpx;
  padding: 8rpx 24rpx;
  border-radius: 24rpx;
  border: none;
  font-weight: 500;
  box-shadow: 0 2rpx 8rpx rgba(255, 77, 79, 0.2);
  transition: all 0.3s ease;
  line-height: 1.5;
  height: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  &:active {
    transform: scale(0.95);
    box-shadow: 0 2rpx 4rpx rgba(255, 77, 79, 0.15);
  }
}
</style> 