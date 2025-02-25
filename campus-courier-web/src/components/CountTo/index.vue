<template>
  <span>{{ displayValue }}</span>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useTransition } from '@vueuse/core'

const props = defineProps({
  startVal: {
    type: Number,
    default: 0
  },
  endVal: {
    type: Number,
    default: 0
  },
  duration: {
    type: Number,
    default: 2000
  },
  decimals: {
    type: Number,
    default: 0
  },
  separator: {
    type: String,
    default: ','
  },
  prefix: {
    type: String,
    default: ''
  },
  suffix: {
    type: String,
    default: ''
  }
})

const source = ref(props.startVal)
const displayValue = ref(formatNumber(props.startVal))

// 使用 VueUse 的 useTransition 来创建平滑过渡
const output = useTransition(source, {
  duration: props.duration,
  transition: [0.5, 0, 0.5, 1] // cubic-bezier 缓动函数
})

// 监听输出值的变化并格式化显示
watch(output, (val) => {
  displayValue.value = formatNumber(val)
})

// 监听结束值的变化并开始新的动画
watch(() => props.endVal, (val) => {
  source.value = val
})

// 格式化数字
function formatNumber(num) {
  // 处理小数点
  let value = Number(num).toFixed(props.decimals)
  
  // 分割整数和小数部分
  let [integer, decimal] = value.split('.')
  
  // 添加千位分隔符
  if (props.separator) {
    integer = integer.replace(/\B(?=(\d{3})+(?!\d))/g, props.separator)
  }
  
  // 组合最终结果
  let result = props.prefix + integer
  if (props.decimals > 0) {
    result += '.' + decimal
  }
  result += props.suffix
  
  return result
}

// 组件挂载时开始动画
onMounted(() => {
  source.value = props.endVal
})
</script> 