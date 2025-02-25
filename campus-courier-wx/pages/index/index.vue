<template>
  <view class="home-container">
    <!-- 状态栏占位 -->
    <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>
    
    <!-- 悬浮角色切换器 -->
    <view class="floating-role-switch">
      <view class="role-switch">
        <view 
          class="role-option" 
          :class="{ active: currentRole === 'publisher' }"
          @click="switchRole('publisher')"
        >
          <text class="role-text">发布模式</text>
        </view>
        <view class="switch-slider" :style="{ transform: currentRole === 'publisher' ? 'translateX(0)' : 'translateX(100%)' }"></view>
        <view 
          class="role-option" 
          :class="{ active: currentRole === 'runner' }"
          @click="switchRole('runner')"
        >
          <text class="role-text">接单模式</text>
        </view>
      </view>
    </view>
    
    <!-- 内容区域 -->
    <scroll-view 
      class="content-area" 
      scroll-y 
      refresher-enabled
      :refresher-triggered="isRefreshing"
      @refresherpulling="onPulling"
      @refresherrefresh="onRefresh"
      @refresherrestore="onRestore"
      @refresherabort="onAbort"
    >
      <!-- 发布者内容 -->
      <block v-if="currentRole === 'publisher'">
        <view class="welcome-card">
          <view class="welcome-content">
            <text class="welcome-title">Hi, 欢迎回来!</text>
            <text class="welcome-subtitle">有什么需要帮忙的吗？</text>
            <view class="publish-button" @click="navigateTo('publish')">
              <text class="button-text">发布新任务</text>
              <u-icon name="arrow-right" color="#fff" size="14"></u-icon>
            </view>
          </view>
          <view class="welcome-image">🛵</view>
        </view>
        
        <view class="stats-section">
          <view class="stats-card">
            <text class="stats-value">0</text>
            <text class="stats-label">进行中</text>
          </view>
          <view class="stats-card">
            <text class="stats-value">0</text>
            <text class="stats-label">已完成</text>
          </view>
          <view class="stats-card">
            <text class="stats-value">0</text>
            <text class="stats-label">待支付</text>
          </view>
        </view>
        
        <view class="task-section">
          <view class="section-header">
            <text class="section-title">最近任务</text>
            <view class="view-more" @click="navigateTo('tasks')">
              <text class="more-text">查看全部</text>
              <u-icon name="arrow-right" color="#666" size="12"></u-icon>
            </view>
          </view>
          
          <u-empty text="暂无任务记录" mode="list"></u-empty>
        </view>
      </block>
      
      <!-- 跑腿者内容 -->
      <block v-else>
        <view class="welcome-card runner">
          <view class="welcome-content">
            <text class="welcome-title">Hi, 接单英雄!</text>
            <text class="welcome-subtitle">准备开始新的旅程？</text>
            <view class="publish-button runner" @click="navigateTo('orders')">
              <text class="button-text">前往抢单</text>
              <u-icon name="arrow-right" color="#fff" size="14"></u-icon>
            </view>
          </view>
          <view class="welcome-image">📦</view>
        </view>
        
        <view class="stats-section">
          <view class="stats-card runner">
            <text class="stats-value">0</text>
            <text class="stats-label">进行中</text>
          </view>
          <view class="stats-card runner">
            <text class="stats-value">0</text>
            <text class="stats-label">已完成</text>
          </view>
          <view class="stats-card runner">
            <text class="stats-value">0元</text>
            <text class="stats-label">总收入</text>
          </view>
        </view>
        
        <view class="task-section">
          <view class="section-header">
            <text class="section-title">热门任务</text>
            <view class="view-more" @click="navigateTo('orders')">
              <text class="more-text">查看全部</text>
              <u-icon name="arrow-right" color="#666" size="12"></u-icon>
            </view>
          </view>
          
          <u-empty text="暂无热门任务" mode="list"></u-empty>
        </view>
      </block>
      
      <!-- 底部安全区域占位 -->
      <view style="height: 120rpx;"></view>
    </scroll-view>
    
    <!-- 底部导航栏 -->
    <view class="tabbar-wrapper" :style="{ paddingBottom: safeAreaInsetBottom + 'px' }">
      <!-- 发布者导航栏 -->
      <view class="tabbar" v-if="currentRole === 'publisher'">
        <!-- 左侧图标 -->
        <view 
          v-for="(item, index) in publisherTabs.filter((_, i) => i < 2)" 
          :key="index" 
          class="tab-item"
          :class="{ active: currentTab === item.name }"
          @click="switchTab(item.name)"
        >
          <image 
            :src="currentTab === item.name ? item.activeIcon : item.icon" 
            class="tab-icon"
            mode="aspectFit"
          ></image>
          <text 
            class="tab-text" 
            :class="{ 'active-text': currentTab === item.name }"
          >{{ item.text }}</text>
        </view>
        
        <!-- 中间发布按钮 -->
        <view class="center-button-wrapper">
          <view 
            class="center-button" 
            :class="{ active: currentTab === 'publish' }"
            @click="switchTab('publish')"
          >
            <image 
              src="https://cdn-icons-png.flaticon.com/128/4315/4315445.png" 
              class="center-icon"
              mode="aspectFit"
            ></image>
          </view>
          <text class="center-button-text">发布任务</text>
        </view>
        
        <!-- 右侧图标 -->
        <view 
          v-for="(item, index) in publisherTabs.filter((_, i) => i > 2)" 
          :key="index + 2" 
          class="tab-item"
          :class="{ active: currentTab === item.name }"
          @click="switchTab(item.name)"
        >
          <image 
            :src="currentTab === item.name ? item.activeIcon : item.icon" 
            class="tab-icon"
            mode="aspectFit"
          ></image>
          <text 
            class="tab-text" 
            :class="{ 'active-text': currentTab === item.name }"
          >{{ item.text }}</text>
        </view>
      </view>
      
      <!-- 跑腿者导航栏 -->
      <view class="tabbar" v-else>
        <!-- 左侧图标 -->
        <view 
          v-for="(item, index) in runnerTabs.filter((_, i) => i < 2)" 
          :key="index" 
          class="tab-item"
          :class="{ active: currentTab === item.name }"
          @click="switchTab(item.name)"
        >
          <image 
            :src="currentTab === item.name ? item.activeIcon : item.icon" 
            class="tab-icon"
            mode="aspectFit"
          ></image>
          <text 
            class="tab-text" 
            :class="{ 'active-text-runner': currentTab === item.name }"
          >{{ item.text }}</text>
        </view>
        
        <!-- 中间抢单按钮 -->
        <view class="center-button-wrapper">
          <view 
            class="center-button runner" 
            :class="{ active: currentTab === 'orders' }"
            @click="switchTab('orders')"
          >
            <image 
              src="https://cdn-icons-png.flaticon.com/128/2645/2645233.png" 
              class="center-icon"
              mode="aspectFit"
            ></image>
          </view>
          <text class="center-button-text">抢单大厅</text>
        </view>
        
        <!-- 右侧图标 -->
        <view 
          v-for="(item, index) in runnerTabs.filter((_, i) => i > 2)" 
          :key="index + 2" 
          class="tab-item"
          :class="{ active: currentTab === item.name }"
          @click="switchTab(item.name)"
        >
          <image 
            :src="currentTab === item.name ? item.activeIcon : item.icon" 
            class="tab-icon"
            mode="aspectFit"
          ></image>
          <text 
            class="tab-text" 
            :class="{ 'active-text-runner': currentTab === item.name }"
          >{{ item.text }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'

// 当前角色：publisher(发布者) 或 runner(跑腿者)
const currentRole = ref('publisher')

// 当前激活的Tab
const currentTab = ref('home')

// 系统信息相关变量
const statusBarHeight = ref(20)
const safeAreaInsetBottom = ref(0)

// 下拉刷新状态
const isRefreshing = ref(false)

// 发布者导航栏配置
const publisherTabs = [
  { 
    name: 'home', 
    text: '首页', 
    icon: 'https://cdn-icons-png.flaticon.com/128/1946/1946488.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/1946/1946436.png' 
  },
  { 
    name: 'tasks', 
    text: '我的任务', 
    icon: 'https://cdn-icons-png.flaticon.com/128/2098/2098561.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/2355/2355330.png' 
  },
  { 
    name: 'publish', 
    text: '发布任务', 
    icon: 'https://cdn-icons-png.flaticon.com/128/4315/4315445.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/4315/4315445.png' 
  },
  { 
    name: 'disputes', 
    text: '纠纷大厅', 
    icon: 'https://cdn-icons-png.flaticon.com/128/1585/1585464.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/1585/1585527.png' 
  },
  { 
    name: 'mine', 
    text: '我的', 
    icon: 'https://cdn-icons-png.flaticon.com/128/1077/1077114.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/1077/1077063.png' 
  }
]

// 跑腿者导航栏配置
const runnerTabs = [
  { 
    name: 'home', 
    text: '首页', 
    icon: 'https://cdn-icons-png.flaticon.com/128/1946/1946488.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/1946/1946436.png' 
  },
  { 
    name: 'current', 
    text: '当前任务', 
    icon: 'https://cdn-icons-png.flaticon.com/128/2838/2838779.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/2838/2838846.png' 
  },
  { 
    name: 'orders', 
    text: '抢单大厅', 
    icon: 'https://cdn-icons-png.flaticon.com/128/2645/2645233.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/2645/2645233.png' 
  },
  { 
    name: 'records', 
    text: '我的接单', 
    icon: 'https://cdn-icons-png.flaticon.com/128/2098/2098561.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/2355/2355330.png' 
  },
  { 
    name: 'mine', 
    text: '我的', 
    icon: 'https://cdn-icons-png.flaticon.com/128/1077/1077114.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/1077/1077063.png' 
  }
]

// 初始化函数
onMounted(() => {
  getSystemInfo()
  initRoleFromStorage()
  preloadTabIcons()
})

// 每次显示页面时执行
onShow(() => {
  // 可在此处刷新数据
})

// 获取系统信息
const getSystemInfo = () => {
  try {
    const info = uni.getSystemInfoSync()
    // 状态栏高度
    statusBarHeight.value = info.statusBarHeight || 20
    // 底部安全区域高度（适配iPhone X及以上机型）
    safeAreaInsetBottom.value = info.safeAreaInsets?.bottom || 0
    
    // 设置CSS变量
    document.documentElement.style.setProperty('--status-bar-height', `${statusBarHeight.value}px`)
    
    console.log('系统信息:', info)
    console.log('状态栏高度:', statusBarHeight.value)
    console.log('底部安全区域高度:', safeAreaInsetBottom.value)
  } catch (e) {
    console.error('获取系统信息失败', e)
  }
}

// 从本地存储初始化角色
const initRoleFromStorage = () => {
  const savedRole = uni.getStorageSync('userRole')
  if (savedRole && (savedRole === 'publisher' || savedRole === 'runner')) {
    currentRole.value = savedRole
  }
}

// 切换角色
const switchRole = (role) => {
  if (role !== 'publisher' && role !== 'runner') return
  
  currentRole.value = role
  currentTab.value = 'home'
  
  // 保存到本地存储
  uni.setStorageSync('userRole', role)
}

// 切换导航Tab
const switchTab = (tab) => {
  currentTab.value = tab
  
  // 处理导航逻辑
  handleTabNavigation(tab)
}

// 导航Tab处理逻辑
const handleTabNavigation = (tab) => {
  if (currentRole.value === 'publisher') {
    switch(tab) {
      case 'publish':
        showMessage('发布任务功能待实现')
        break
      case 'tasks':
        showMessage('我的任务功能待实现')
        break
      case 'disputes':
        showMessage('纠纷大厅功能待实现')
        break
      case 'mine':
        showMessage('个人中心功能待实现')
        break
    }
  } else {
    switch(tab) {
      case 'orders':
        showMessage('抢单大厅功能待实现')
        break
      case 'current':
        showMessage('当前任务功能待实现')
        break
      case 'records':
        showMessage('我的接单功能待实现')
        break
      case 'mine':
        showMessage('个人中心功能待实现')
        break
    }
  }
}

// 导航到特定页面
const navigateTo = (page) => {
  if (page === 'publish') {
    switchTab('publish')
    return
  }
  
  showMessage(`${page}页面待实现`)
}

// 显示消息提示
const showMessage = (message) => {
  uni.showToast({
    title: message,
    icon: 'none'
  })
}

// 下拉刷新处理
const onPulling = () => {
  console.log('下拉中...')
}

const onRefresh = () => {
  isRefreshing.value = true
  setTimeout(() => {
    isRefreshing.value = false
    showMessage('刷新成功')
  }, 1000)
}

const onRestore = () => {
  console.log('刷新还原')
}

const onAbort = () => {
  console.log('刷新中断')
}

// 预加载所有标签图标
const preloadTabIcons = () => {
  // 收集所有需要预加载的图标URL
  const iconUrls = new Set()
  
  // 添加发布者图标
  publisherTabs.forEach(tab => {
    iconUrls.add(tab.icon)
    iconUrls.add(tab.activeIcon)
  })
  
  // 添加跑腿者图标
  runnerTabs.forEach(tab => {
    iconUrls.add(tab.icon)
    iconUrls.add(tab.activeIcon)
  })
  
  // 创建本地缓存对象
  const iconCache = uni.getStorageSync('iconCache') || {}
  const currentTime = Date.now()
  // 清理过期缓存 (7天过期)
  const CACHE_EXPIRY = 7 * 24 * 60 * 60 * 1000 // 7天
  
  Object.keys(iconCache).forEach(key => {
    if (currentTime - iconCache[key].time > CACHE_EXPIRY) {
      delete iconCache[key]
    }
  })
  
  // 预加载并缓存每个图标
  iconUrls.forEach(url => {
    // 如果已有有效缓存，跳过下载
    if (iconCache[url] && iconCache[url].localPath) {
      console.log('使用缓存图标:', url)
      return
    }
    
    // 下载图片到本地
    uni.downloadFile({
      url: url,
      success: res => {
        if (res.statusCode === 200) {
          // 获取图片信息
          uni.getImageInfo({
            src: res.tempFilePath,
            success: info => {
              // 保存到本地缓存
              iconCache[url] = {
                localPath: res.tempFilePath,
                width: info.width,
                height: info.height,
                time: currentTime
              }
              
              // 更新存储
              uni.setStorageSync('iconCache', iconCache)
              console.log('图标缓存成功:', url)
            }
          })
        }
      },
      fail: err => {
        console.error('下载图标失败:', url, err)
      }
    })
  })
}
</script>

<style lang="scss" scoped>
// 全局容器
.home-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f6f7fb;
  position: relative;
  box-sizing: border-box;
}

// 状态栏占位区
.status-bar {
  width: 100%;
  background-color: transparent;
}

// 悬浮角色切换器
.floating-role-switch {
  position: absolute;
  top: calc(var(--status-bar-height, 20px) + 10rpx);
  left: 50%;
  transform: translateX(-50%);
  z-index: 99;
  padding: 0;
  border-radius: 40rpx;
  box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.1);
}

// 角色切换样式
.role-switch {
  width: 400rpx;
  height: 70rpx;
  background-color: #ffffff;
  border-radius: 40rpx;
  display: flex;
  position: relative;
  overflow: hidden;
}

.role-option {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  z-index: 2;
  
  .role-text {
    font-size: 26rpx;
    font-weight: 500;
    transition: color 0.3s;
    color: #666;
  }
  
  &.active .role-text {
    color: #ffffff;
    font-weight: bold;
  }
}

.switch-slider {
  position: absolute;
  width: 50%;
  height: 100%;
  background: linear-gradient(to right, #ff9500, #ff7d00);
  border-radius: 40rpx;
  transition: transform 0.3s ease;
  z-index: 1;
  
  .runner & {
    background: linear-gradient(to right, #3498db, #2980b9);
  }
}

// 内容区域
.content-area {
  flex: 1;
  overflow: hidden;
  padding: 20rpx 30rpx;
  margin-top: 100rpx; // 给悬浮切换器留出空间
  box-sizing: border-box;
  padding-bottom: 120rpx; // 增加底部间距，确保内容不被悬浮按钮遮挡
  
  // 确保内部所有内容有统一的左右安全距离
  :deep(view) {
    box-sizing: border-box;
  }
}

// 欢迎卡片
.welcome-card {
  display: flex;
  padding: 30rpx;
  background: linear-gradient(135deg, #ff9500, #ff7d00);
  border-radius: 20rpx;
  margin: 20rpx 0 24rpx;
  box-shadow: 0 10rpx 20rpx rgba(255, 149, 0, 0.1);
  width: 100%;
  box-sizing: border-box;
  
  &.runner {
    background: linear-gradient(135deg, #3498db, #2980b9);
    box-shadow: 0 10rpx 20rpx rgba(52, 152, 219, 0.1);
  }
}

.welcome-content {
  flex: 1;
  
  .welcome-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #fff;
    margin-bottom: 12rpx;
  }
  
  .welcome-subtitle {
    font-size: 26rpx;
    color: rgba(255, 255, 255, 0.9);
    margin-bottom: 30rpx;
  }
  
  .publish-button {
    display: inline-flex;
    align-items: center;
    background: rgba(255, 255, 255, 0.25);
    border-radius: 40rpx;
    padding: 12rpx 25rpx;
    backdrop-filter: blur(10rpx);
    
    .button-text {
      color: #ffffff;
      font-size: 26rpx;
      font-weight: 500;
      margin-right: 8rpx;
    }
    
    &.runner {
      background: rgba(255, 255, 255, 0.2);
    }
  }
}

.welcome-image {
  font-size: 70rpx;
  margin-left: 15rpx;
  align-self: center;
}

// 统计卡片
.stats-section {
  display: flex;
  justify-content: space-between;
  margin-bottom: 24rpx;
  width: 100%;
  box-sizing: border-box;
}

.stats-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx 15rpx;
  margin: 0 8rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 6rpx;
    background: linear-gradient(to right, #ff9500, #ff7d00);
    border-radius: 6rpx 6rpx 0 0;
  }
  
  &.runner::before {
    background: linear-gradient(to right, #3498db, #2980b9);
  }
  
  &:first-child {
    margin-left: 0;
  }
  
  &:last-child {
    margin-right: 0;
  }
  
  .stats-value {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 8rpx;
  }
  
  .stats-label {
    font-size: 22rpx;
    color: #999;
  }
}

// 任务区域
.task-section {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
  width: 100%;
  box-sizing: border-box;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  
  .section-title {
    font-size: 30rpx;
    font-weight: bold;
    color: #333;
  }
  
  .view-more {
    display: flex;
    align-items: center;
    
    .more-text {
      font-size: 22rpx;
      color: #666;
      margin-right: 6rpx;
    }
  }
}

// 底部导航栏
.tabbar-wrapper {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 100;
  background-color: #ffffff;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.tabbar {
  display: flex;
  height: 100rpx;
  position: relative;
  padding: 0 15rpx;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10rpx 0;
  position: relative;
  
  .tab-icon {
    width: 44rpx;
    height: 44rpx;
    margin-bottom: 4rpx;
    will-change: transform;
    transition: transform 0.2s;
  }
  
  .tab-text {
    font-size: 20rpx;
    color: #999;
    margin-top: 4rpx;
  }
  
  .active-text {
    color: #ff9500;
  }
  
  .active-text-runner {
    color: #3498db;
  }
}

.tab-item:active .tab-icon {
  transform: scale(0.9);
}

// 中间按钮样式
.center-button-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 110rpx;
  position: relative;
  justify-content: flex-end;
  height: 120rpx;
  margin-top: -45rpx;
  
  .center-button {
    width: 90rpx;
    height: 90rpx;
    background: linear-gradient(135deg, #ff9500, #ff7d00);
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    box-shadow: 0 6rpx 20rpx rgba(255, 149, 0, 0.35);
    margin-bottom: 5rpx;
    transition: all 0.3s;
    position: relative;
    overflow: hidden;
    
    .center-icon {
      width: 50rpx;
      height: 50rpx;
    }
    
    &::before {
      content: '';
      position: absolute;
      top: -10rpx;
      left: -10rpx;
      right: -10rpx;
      bottom: -10rpx;
      background: linear-gradient(135deg, rgba(255, 255, 255, 0.4), rgba(255, 255, 255, 0));
      z-index: 0;
      transform: rotate(45deg);
      transition: all 0.3s;
    }
    
    &:active::before {
      transform: rotate(180deg);
    }
    
    &.runner {
      background: linear-gradient(135deg, #3498db, #2980b9);
      box-shadow: 0 6rpx 20rpx rgba(52, 152, 219, 0.35);
    }
    
    &.active {
      transform: scale(1.05);
    }

    &::after {
      content: '';
      position: absolute;
      width: 104rpx;
      height: 104rpx;
      border: 2rpx solid rgba(255, 255, 255, 0.5);
      border-radius: 50%;
      animation: pulse 2s infinite;
    }

    &.runner::after {
      border-color: rgba(255, 255, 255, 0.5);
    }
  }
  
  .center-button-text {
    font-size: 20rpx;
    color: #999;
    margin-top: 2rpx;
  }
}

// 发布按钮脉冲动画
@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.8;
  }
  70% {
    transform: scale(1.1);
    opacity: 0.2;
  }
  100% {
    transform: scale(1.15);
    opacity: 0;
  }
}

/* 小屏幕设备适配 */
@media screen and (max-height: 650px) {
  .floating-role-switch {
    top: calc(var(--status-bar-height, 20px) + 5rpx);
  }
  
  .role-switch {
    width: 360rpx;
    height: 60rpx;
  }
  
  .role-option .role-text {
    font-size: 24rpx;
  }
  
  .content-area {
    margin-top: 80rpx;
  }
  
  .welcome-card {
    padding: 24rpx;
  }
  
  .welcome-content .welcome-title {
    font-size: 32rpx;
  }
  
  .welcome-content .welcome-subtitle {
    font-size: 24rpx;
    margin-bottom: 20rpx;
  }
  
  .stats-card {
    padding: 18rpx 10rpx;
  }
  
  .stats-value {
    font-size: 32rpx !important;
  }
  
  .stats-label {
    font-size: 20rpx !important;
  }
}
</style>
