<script setup>
// 不需要 HelloWorld 组件
import { onMounted } from 'vue';
import { useRoute } from 'vue-router';

// 判断当前是否为管理页面并设置对应的body类
onMounted(() => {
  const route = useRoute();
  const isAdminPage = route.path.includes('/admin');
  
  if (isAdminPage) {
    document.body.classList.add('admin-page');
    document.body.classList.remove('app-page');
  } else {
    document.body.classList.add('app-page');
    document.body.classList.remove('admin-page');
  }
});
</script>

<template>
  <router-view v-slot="{ Component }">
    <transition name="zoom-fade" mode="out-in" appear>
      <component 
        :is="Component" 
        :class="['page-component', $route.path.includes('/admin') ? 'admin-component' : '']" 
      />
    </transition>
  </router-view>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body {
  width: 100%;
  height: 100%;
  /* 移除强制overflow:hidden，改为只在非管理页面使用 */
}

/* 允许管理页面滚动，如任务类型表单页面 */
body.admin-page {
  overflow-y: auto !important;
}

/* 默认仍保持原有效果 */
body.app-page {
  overflow: hidden;
}

#app {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow-x: hidden;
  perspective: 1200px; /* 添加透视效果，增强3D感 */
}

.page-component {
  width: 100%;
  height: 100%;
  position: absolute;
  left: 0;
  top: 0;
}

/* 允许管理类页面组件滚动 */
.page-component.admin-component {
  position: relative;
  height: auto;
  min-height: 100%;
  overflow-y: auto;
}

/* 新的缩放淡入淡出动画 - 更流畅，无卡顿 */
.zoom-fade-enter-active {
  transition: all 0.4s ease-out;
  transform-origin: center center;
}

.zoom-fade-leave-active {
  transition: all 0.3s ease-in;
  transform-origin: center center;
}

.zoom-fade-enter-from {
  opacity: 0;
  transform: scale(0.85);
}

.zoom-fade-leave-to {
  opacity: 0;
  transform: scale(1.15);
}

/* 弹性缩放动画 - 类似于iOS应用启动效果 */
.bounce-scale-enter-active {
  animation: bounceIn 0.5s;
}

.bounce-scale-leave-active {
  animation: bounceOut 0.4s;
}

@keyframes bounceIn {
  0% {
    opacity: 0;
    transform: scale(0.3);
  }
  40% {
    opacity: 0.7;
    transform: scale(1.03);
  }
  60% {
    opacity: 1;
    transform: scale(0.98);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes bounceOut {
  0% {
    opacity: 1;
    transform: scale(1);
  }
  40% {
    opacity: 0.8;
    transform: scale(1.03);
  }
  100% {
    opacity: 0;
    transform: scale(0.3);
  }
}

/* 3D翻页效果 - 模拟书页翻转 */
.flip-enter-active {
  animation: flipIn 0.6s ease-out forwards;
  transform-origin: center center;
}

.flip-leave-active {
  animation: flipOut 0.5s ease-in forwards;
  transform-origin: center center;
}

@keyframes flipIn {
  0% {
    opacity: 0;
    transform: rotateY(-90deg);
  }
  100% {
    opacity: 1;
    transform: rotateY(0deg);
  }
}

@keyframes flipOut {
  0% {
    opacity: 1;
    transform: rotateY(0deg);
  }
  100% {
    opacity: 0;
    transform: rotateY(90deg);
  }
}

/* 幻灯片效果 - 平滑滑动 */
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.35s ease;
}

.slide-fade-enter-from {
  opacity: 0;
  transform: translateY(30px);
}

.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-30px);
}
</style>

