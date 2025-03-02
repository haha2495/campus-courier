<template>
  <view class="home-container">
    <!-- 状态栏占位 -->
    <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>
    
    <!-- 悬浮角色切换器 -->
    <view class="floating-role-switch">
      <view class="role-switch">
        <view 
          class="role-option" 
          :class="{ active: currentRole === TabBarType.PUBLISHER }"
          @click="switchRole(TabBarType.PUBLISHER)"
        >
          <text class="role-text">发布模式</text>
        </view>
        <view class="switch-slider" :style="{ transform: currentRole === TabBarType.PUBLISHER ? 'translateX(0)' : 'translateX(100%)' }"></view>
        <view 
          class="role-option" 
          :class="{ active: currentRole === TabBarType.RUNNER }"
          @click="switchRole(TabBarType.RUNNER)"
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
      <block v-if="currentRole === TabBarType.PUBLISHER">
        <view class="welcome-card">
          <view class="welcome-content">
            <view class="welcome-header">
              <text class="welcome-title">Hi, 欢迎回来!</text>
              <text class="welcome-emoji">👋</text>
            </view>
            <text class="welcome-subtitle">有什么需要帮忙的吗？</text>
            <view class="publish-button" @click="navigateTo('publish')">
              <text class="button-text">发布新任务</text>
              <image src="https://cdn-icons-png.flaticon.com/128/271/271228.png" class="arrow-icon"></image>
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
              <image src="https://cdn-icons-png.flaticon.com/128/271/271228.png" class="arrow-icon"></image>
            </view>
          </view>
          
          <view v-if="showEmpty" class="custom-empty">
            <image class="empty-image" src="https://cdn-icons-png.flaticon.com/128/7486/7486754.png" mode="aspectFit"></image>
            <text class="empty-text">暂无数据</text>
            <text class="empty-tips">下拉刷新试试看</text>
          </view>
        </view>
      </block>
      
      <!-- 跑腿者内容 -->
      <block v-else>
        <view class="welcome-card runner">
          <view class="welcome-content">
            <view class="welcome-header">
              <text class="welcome-title">Hi, 接单英雄!</text>
              <text class="welcome-emoji">💪</text>
            </view>
            <text class="welcome-subtitle">准备开始新的旅程？</text>
            <view class="publish-button runner" @click="navigateTo('orders')">
              <text class="button-text">前往抢单</text>
              <image src="https://cdn-icons-png.flaticon.com/128/271/271228.png" class="arrow-icon"></image>
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
              <image src="https://cdn-icons-png.flaticon.com/128/271/271228.png" class="arrow-icon"></image>
            </view>
          </view>
          
          <view v-if="showEmpty" class="custom-empty">
            <image class="empty-image" src="https://cdn-icons-png.flaticon.com/128/7486/7486754.png" mode="aspectFit"></image>
            <text class="empty-text">暂无数据</text>
            <text class="empty-tips">下拉刷新试试看</text>
          </view>
        </view>
      </block>
      
      <!-- 底部安全区域占位 -->
      <view style="height: 120rpx;"></view>
    </scroll-view>
    
    <!-- 底部导航栏 -->
    <view class="tabbar-wrapper" :style="{ paddingBottom: safeAreaInsetBottom + 'px' }">
      <!-- 发布者导航栏 -->
      <view class="tabbar" v-if="currentRole === TabBarType.PUBLISHER">
        <!-- 左侧图标 -->
        <view 
          v-for="(item, index) in currentTabs.filter((_, i) => i < 2)" 
          :key="index" 
          class="tab-item"
          :class="{ active: currentTab === item.name }"
          @click="switchTab(item.name)"
        >
          <image 
            :src="getIconSrc(currentTab === item.name ? item.activeIcon : item.icon)" 
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
            :class="{ active: currentTab === centerButton.name }"
            @click="switchTab(centerButton.name)"
          >
            <image 
              :src="getIconSrc(centerButton.icon)" 
              class="center-icon"
              mode="aspectFit"
            ></image>
          </view>
          <text class="center-button-text">{{ centerButton.text }}</text>
        </view>
        
        <!-- 右侧图标 -->
        <view 
          v-for="(item, index) in currentTabs.filter((_, i) => i > 2)" 
          :key="index + 2" 
          class="tab-item"
          :class="{ active: currentTab === item.name }"
          @click="switchTab(item.name)"
        >
          <image 
            :src="getIconSrc(currentTab === item.name ? item.activeIcon : item.icon)" 
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
          v-for="(item, index) in currentTabs.filter((_, i) => i < 2)" 
          :key="index" 
          class="tab-item"
          :class="{ active: currentTab === item.name }"
          @click="switchTab(item.name)"
        >
          <image 
            :src="getIconSrc(currentTab === item.name ? item.activeIcon : item.icon)" 
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
            :class="{ active: currentTab === centerButton.name }"
            @click="switchTab(centerButton.name)"
          >
            <image 
              :src="getIconSrc(centerButton.icon)" 
              class="center-icon"
              mode="aspectFit"
            ></image>
          </view>
          <text class="center-button-text">{{ centerButton.text }}</text>
        </view>
        
        <!-- 右侧图标 -->
        <view 
          v-for="(item, index) in currentTabs.filter((_, i) => i > 2)" 
          :key="index + 2" 
          class="tab-item"
          :class="{ active: currentTab === item.name }"
          @click="switchTab(item.name)"
        >
          <image 
            :src="getIconSrc(currentTab === item.name ? item.activeIcon : item.icon)" 
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
import { ref, onMounted, computed } from 'vue'
import { TabBarType, TabPageName, publisherTabConfig, runnerTabConfig, getCenterButton, getVersionedIconUrl, ICON_VERSION } from '@/utils/tabConfig'

// 当前角色：publisher(发布者) 或 runner(跑腿者)
const currentRole = ref(TabBarType.PUBLISHER)

// 当前激活的Tab
const currentTab = ref(TabPageName.HOME)

// 系统信息相关变量
const statusBarHeight = ref(20)
const safeAreaInsetBottom = ref(0)

// 下拉刷新状态
const isRefreshing = ref(false)

// 获取中间按钮配置
const centerButton = computed(() => getCenterButton(currentRole.value))

// 获取当前导航栏配置
const currentTabs = computed(() => {
  return currentRole.value === TabBarType.PUBLISHER ? publisherTabConfig : runnerTabConfig
})

// 图标缓存对象
const iconCache = ref({})

// 添加空状态控制
const showEmpty = ref(true)
const isLoading = ref(false)

// 页面生命周期钩子
onMounted(() => {
  // 初始化操作
  getSystemInfo()
  initRoleFromStorage()
  refreshIconCache()
  loadIconCache()
  preloadTabIcons()
  refreshData()
})

// 页面显示时执行
defineExpose({
  onShow() {
    console.log('页面显示')
    // 页面显示时刷新数据
    loadIconCache()
    refreshData()
  }
})

// 刷新数据方法
const refreshData = async () => {
  isLoading.value = true
  showEmpty.value = false
  
  try {
    // 这里添加你的数据加载逻辑
    // 模拟数据加载
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 如果没有数据则显示空状态
    showEmpty.value = true
  } catch (error) {
    console.error('加载数据失败:', error)
    showEmpty.value = true
  } finally {
    isLoading.value = false
  }
}

// 从缓存获取图标URL
const getIconSrc = (url) => {
  // 给URL添加版本号，用于缓存控制
  const versionedUrl = getVersionedIconUrl(url)
  
  // 检查缓存中是否有该图标
  if (iconCache.value[versionedUrl] && iconCache.value[versionedUrl].localPath) {
    try {
      // 检查本地文件是否存在
      const fs = uni.getFileSystemManager()
      fs.accessSync(iconCache.value[versionedUrl].localPath)
      return iconCache.value[versionedUrl].localPath
    } catch (e) {
      // 本地文件不存在，删除缓存记录并返回原始URL
      console.warn('缓存的图片不存在:', versionedUrl)
      delete iconCache.value[versionedUrl]
      uni.setStorageSync('iconCache', iconCache.value)
      return url
    }
  }
  return url
}

// 获取系统信息
const getSystemInfo = () => {
  try {
    const info = uni.getSystemInfoSync()
    // 状态栏高度
    statusBarHeight.value = info.statusBarHeight || 20
    // 底部安全区域高度（适配iPhone X及以上机型）
    safeAreaInsetBottom.value = info.safeAreaInsets?.bottom || 0
    
    // 设置CSS变量 - 修复document对象的使用
    // 小程序环境不支持直接操作document，改为使用样式变量
    const statusBarHeightValue = `${statusBarHeight.value}px`
    
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
  if (savedRole && (savedRole === TabBarType.PUBLISHER || savedRole === TabBarType.RUNNER)) {
    currentRole.value = savedRole
  }
}

// 切换角色
const switchRole = (role) => {
  if (role !== TabBarType.PUBLISHER && role !== TabBarType.RUNNER) return
  
  currentRole.value = role
  currentTab.value = TabPageName.HOME
  
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
  if (currentRole.value === TabBarType.PUBLISHER) {
    switch(tab) {
      case TabPageName.PUBLISH:
        // 跳转到发布任务页面
        uni.navigateTo({
          url: '/pages/publish/index',
          animationType: 'slide-in-bottom',
          animationDuration: 300
        })
        break
      case TabPageName.TASKS:
        // 跳转到我的任务页面
        uni.navigateTo({
          url: '/pages/my-tasks/index'
        })
        break
      case TabPageName.DISPUTES:
        // 跳转到纠纷大厅页面
        uni.navigateTo({
          url: '/pages/dispute/index'
        })
        break
      case TabPageName.MINE:
        // 跳转到个人中心页面
        uni.navigateTo({
          url: '/pages/mine/index'
        })
        break
    }
  } else {
    switch(tab) {
      case TabPageName.ORDERS:
        // 跳转到抢单大厅
        uni.navigateTo({
          url: '/pages/task-hall/index'
        })
        break
      case TabPageName.CURRENT:
        // 跳转到当前任务页面
        uni.navigateTo({
          url: '/pages/current-tasks/index'
        })
        break
      case TabPageName.RECORDS:
        // 跑腿者的我的任务页面
        uni.navigateTo({
          url: '/pages/runner-tasks/index'
        })
        break
      case TabPageName.MINE:
        // 跳转到个人中心页面
        uni.navigateTo({
          url: '/pages/mine/index'
        })
        break
    }
  }
}

// 导航到特定页面
const navigateTo = (page) => {
  if (page === 'publish') {
    // 直接跳转到发布任务页面
    uni.navigateTo({
      url: '/pages/publish/index',
      animationType: 'slide-in-bottom',
      animationDuration: 300
    })
    return
  }
  
  if (page === 'tasks') {
    // 跳转到我的任务页面 - 使用navigateTo而不是switchTab
    uni.navigateTo({
      url: '/pages/my-tasks/index'
    })
    return
  }

  if (page === 'orders') {
    // 跳转到抢单大厅
    uni.navigateTo({
      url: '/pages/task-hall/index'
    })
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
  publisherTabConfig.forEach(tab => {
    iconUrls.add(tab.icon)
    iconUrls.add(tab.activeIcon)
  })
  
  // 添加跑腿者图标
  runnerTabConfig.forEach(tab => {
    iconUrls.add(tab.icon)
    iconUrls.add(tab.activeIcon)
  })
  
  // 添加中间按钮图标
  iconUrls.add(getCenterButton(TabBarType.PUBLISHER).icon)
  iconUrls.add(getCenterButton(TabBarType.RUNNER).icon)
  
  // 创建本地缓存对象
  const currentTime = Date.now()
  // 清理过期缓存 (7天过期)
  const CACHE_EXPIRY = 7 * 24 * 60 * 60 * 1000 // 7天
  
  Object.keys(iconCache.value).forEach(key => {
    if (currentTime - iconCache.value[key].time > CACHE_EXPIRY) {
      try {
        // 尝试删除本地文件
        const fs = uni.getFileSystemManager()
        fs.unlinkSync(iconCache.value[key].localPath)
      } catch (e) {
        // 忽略错误
      }
      delete iconCache.value[key]
    }
  })
  
  // 更新缓存
  uni.setStorageSync('iconCache', iconCache.value)
  
  // 预加载并缓存每个图标
  iconUrls.forEach(url => {
    // 给URL添加版本号
    const versionedUrl = getVersionedIconUrl(url)
    
    // 如果已有有效缓存，跳过下载
    if (iconCache.value[versionedUrl] && iconCache.value[versionedUrl].localPath) {
      try {
        // 验证本地文件存在
        const fs = uni.getFileSystemManager()
        fs.accessSync(iconCache.value[versionedUrl].localPath)
        console.log('使用缓存图标:', url)
        return
      } catch (e) {
        // 文件不存在，继续下载
        console.warn('缓存的图片文件不存在，重新下载:', url)
      }
    }
    
    // 下载图片到本地
    uni.downloadFile({
      url: url,
      success: res => {
        if (res.statusCode === 200) {
          // 将临时文件保存到应用缓存目录
          const tempFilePath = res.tempFilePath
          const fs = uni.getFileSystemManager()
          
          // 创建目录
          try {
            fs.mkdirSync(`${uni.env.USER_DATA_PATH}/icons`, true)
          } catch (e) {
            // 目录可能已存在，忽略错误
          }
          
          // 生成唯一文件名
          const fileName = `${uni.env.USER_DATA_PATH}/icons/${url.split('/').pop().split('?')[0]}_${Date.now()}.png`
          
          try {
            // 保存文件
            fs.saveFileSync(tempFilePath, fileName)
            
            // 获取图片信息
            uni.getImageInfo({
              src: fileName,
              success: info => {
                // 保存到本地缓存
                iconCache.value[versionedUrl] = {
                  localPath: fileName,
                  width: info.width,
                  height: info.height,
                  time: currentTime,
                  originalUrl: url
                }
                
                // 更新存储
                uni.setStorageSync('iconCache', iconCache.value)
                console.log('图标缓存成功:', url)
              },
              fail: () => {
                console.error('获取图片信息失败:', url)
              }
            })
          } catch (e) {
            console.error('保存文件失败:', e)
          }
        }
      },
      fail: err => {
        console.error('下载图标失败:', url, err)
      }
    })
  })
}

// 添加刷新图标缓存的方法
const refreshIconCache = () => {
  // 检查缓存版本
  const cacheVersion = uni.getStorageSync('ICON_CACHE_VERSION')
  if (cacheVersion !== ICON_VERSION) {
    console.log(`Icon version changed from ${cacheVersion} to ${ICON_VERSION}, refreshing cache`)
    // 版本不同，清除现有缓存
    uni.removeStorageSync('iconCache')
    // 更新缓存版本
    uni.setStorageSync('ICON_CACHE_VERSION', ICON_VERSION)
    
    // 可以在这里添加额外的缓存清理逻辑
    // 例如删除之前缓存的文件等
  }
}

// 加载图标缓存
const loadIconCache = () => {
  const cache = uni.getStorageSync('iconCache') || {}
  iconCache.value = cache
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
  top: v-bind('statusBarHeight + 10 + "px"');
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
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.welcome-header {
  display: flex;
  align-items: center;
  margin-bottom: 8rpx;
}

.welcome-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #fff;
}

.welcome-emoji {
  font-size: 36rpx;
  margin-left: 10rpx;
}

.welcome-subtitle {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 30rpx;
  font-weight: 400;
}

.publish-button {
  display: inline-flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.25);
  border-radius: 40rpx;
  padding: 16rpx 32rpx;
  backdrop-filter: blur(10rpx);
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  width: 85%;
  margin-top: 10rpx;
  
  &:active {
    transform: scale(0.98);
    background: rgba(255, 255, 255, 0.3);
  }
  
  .button-text {
    color: #ffffff;
    font-size: 28rpx;
    font-weight: 500;
    flex: 1;
  }
  
  .arrow-icon {
    width: 32rpx;
    height: 32rpx;
    opacity: 0.9;
  }
  
  &.runner {
    background: rgba(255, 255, 255, 0.2);
    border: 1px solid rgba(255, 255, 255, 0.3);
    
    &:active {
      background: rgba(255, 255, 255, 0.25);
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
    transition: all 0.3s;
  }
  
  .tab-text {
    font-size: 20rpx;
    color: #999;
    margin-top: 4rpx;
    transition: all 0.3s;
  }
  
  &.active {
    .tab-icon {
      transform: scale(1.15);
      filter: drop-shadow(0 2rpx 6rpx rgba(0, 0, 0, 0.15));
    }
    
    &::after {
      content: '';
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%);
      width: 16rpx;
      height: 6rpx;
      border-radius: 3rpx;
      background: linear-gradient(to right, #ff9500, #ff7d00);
      transition: width 0.3s;
    }
    
    &:hover::after {
      width: 24rpx;
    }
  }
  
  &.active:nth-child(5),
  &.active:nth-child(6) {
    &::after {
      background: linear-gradient(to right, #3498db, #2980b9);
    }
  }
  
  .active-text {
    color: #ff9500;
    font-weight: 600;
  }
  
  .active-text-runner {
    color: #3498db;
    font-weight: 600;
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
    top: v-bind('statusBarHeight + 5 + "px"');
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

// 添加箭头图标样式
.arrow-icon {
  width: 24rpx;
  height: 24rpx;
  margin-left: 4rpx;
}

// 自定义空状态样式
.custom-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 30px 0;
  
  .empty-image {
    width: 150rpx;
    height: 150rpx;
    margin-bottom: 20rpx;
  }
  
  .empty-text {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 10rpx;
  }
  
  .empty-tips {
    font-size: 24rpx;
    color: #999;
  }
}

// 添加浮动按钮样式
.float-button {
  position: fixed;
  right: 30rpx;
  bottom: 150rpx;
  background-color: #ff9500;
  color: white;
  padding: 20rpx 30rpx;
  border-radius: 40rpx;
  box-shadow: 0 6rpx 16rpx rgba(255, 149, 0, 0.3);
  z-index: 100;
  
  .button-text {
    font-size: 28rpx;
    font-weight: 500;
  }
}
</style>
