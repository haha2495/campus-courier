<template>
  <view class="publish-container" :class="containerClass">
    <!-- 自定义导航栏 -->
    <view class="custom-nav-bar" :style="{ paddingTop: statusBarHeightValue + 'px' }">
      <view class="nav-left" @click="handleBack">
        <u-icon name="close" size="40rpx" color="#333"></u-icon>
      </view>
      <view class="nav-title">发布任务</view>
    </view>

    <!-- 页面内容 -->
    <scroll-view 
      scroll-y="true" 
      class="publish-content" 
      :style="{ top: contentTopValue + 'px' }" 
      :scroll-top="scrollTop"
      @scroll="onScroll"
      scroll-with-animation
      id="scrollView"
    >
      <!-- 任务类型选择 -->
      <view class="section-title">
        <text>选择任务类型</text>
        <text class="type-count" v-if="taskTypes.length > 0">({{ taskTypes.length }})</text>
      </view>
      
      <!-- 任务类型搜索框 -->
      <view class="search-box" v-if="taskTypes.length > 9">
        <u-icon name="search" size="40rpx" color="#999"></u-icon>
        <input 
          type="text" 
          placeholder="搜索任务类型" 
          v-model="searchText"
          confirm-type="search"
          class="search-input"
        />
        <u-icon name="close-circle-fill" size="40rpx" color="#999" v-if="searchText" @click="clearSearch"></u-icon>
      </view>

      <!-- 骨架屏 - 加载中 -->
      <view class="skeleton-container" v-if="isLoading">
        <view 
          v-for="i in 6" 
          :key="i" 
          class="skeleton-item"
        >
          <view class="skeleton-circle"></view>
          <view class="skeleton-text"></view>
        </view>
      </view>

      <!-- 任务类型列表 -->
      <view v-else>
        <swiper 
          class="types-swiper"
          :current="currentPage - 1"
          @change="onSwiperChange"
          :indicator-dots="false"
          :duration="300"
          :circular="true"
          :style="{ height: '450rpx' }"
        >
          <swiper-item v-for="page in Math.ceil(filteredTaskTypes.length / pageSize)" :key="page">
            <view class="task-types">
              <view 
                v-for="(type, index) in getPageTaskTypes(page)"
                :key="type.id"
                class="task-type-item"
                :class="{ 'active': selectedTaskType === type.id.toString() }"
                @click="selectTaskType(type.id.toString())"
              >
                <image 
                  v-if="type.icon && type.icon.includes('http')" 
                  :src="type.icon" 
                  class="type-icon-image" 
                  mode="aspectFit"
                ></image>
                <view class="type-icon" v-else>{{ type.icon || '🔍' }}</view>
                <text class="type-name">{{ type.typeName }}</text>
              </view>
            </view>
          </swiper-item>
        </swiper>
      </view>
      
      <!-- 分页指示器 -->
      <view class="pagination" v-if="totalPages > 1">
        <view 
          v-for="i in totalPages" 
          :key="i"
          class="page-dot"
          :class="{ 'active': currentPage === i }"
          @click="changePage(i)"
        ></view>
      </view>
      
      <!-- 没有任务类型的提示 -->
      <view class="empty-types" v-if="taskTypes.length === 0 && !isLoading">
        <view class="empty-icon">🔍</view>
        <text class="empty-text">暂无可用任务类型</text>
      </view>
      
      <!-- 选中的任务类型描述 -->
      <view class="type-description" v-if="selectedTaskTypeObj && selectedTaskTypeObj.description">
        <view class="description-header">
          <view class="header-icon">
            <image 
              v-if="selectedTaskTypeObj.icon && selectedTaskTypeObj.icon.includes('http')" 
              :src="selectedTaskTypeObj.icon" 
              class="header-icon-image" 
              mode="aspectFit"
            ></image>
            <text v-else>{{ selectedTaskTypeObj.icon || 'ℹ️' }}</text>
          </view>
          <view class="header-text">
            <text class="description-title">{{ selectedTaskTypeObj.typeName }} 说明</text>
          </view>
        </view>
        <view class="description-content">
          <text class="description-text">{{ selectedTaskTypeObj.description }}</text>
        </view>
      </view>
      
      <!-- 提交按钮 -->
      <view 
        class="submit-button" 
        :class="{ 'button-disabled': !selectedTaskType }"
        @click="submitTask"
      >
        <text class="button-text">{{ selectedTaskType ? '发布任务' : '请先选择任务类型' }}</text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch, nextTick } from 'vue'
import { getEnabledTaskTypes } from '@/api/taskType'

// 状态栏高度
const statusBarHeight = ref(20)
const statusBarHeightValue = computed(() => statusBarHeight.value)
const contentTopValue = computed(() => statusBarHeight.value + 44)
const currentDate = new Date().toISOString().split('T')[0]

// 创建动画控制变量
const isPageReady = ref(false)
const isClosing = ref(false)
const containerClass = computed(() => {
  return {
    'page-show': isPageReady.value,
    'page-hide': isClosing.value
  }
})

// 任务类型选择
const selectedTaskType = ref('')
const isLoading = ref(true)
const searchText = ref('')
const currentPage = ref(1)
const pageSize = 9 // 每页显示的任务类型数

// 任务类型列表
const taskTypes = ref([])

// 计算过滤后的任务类型
const filteredTaskTypes = computed(() => {
  let filtered = taskTypes.value
  
  // 如果有搜索文本，则过滤
  if (searchText.value) {
    filtered = filtered.filter(type => 
      type.typeName.toLowerCase().includes(searchText.value.toLowerCase())
    )
  }
  
  console.log('过滤后的任务类型数量:', filtered.length)
  return filtered
})

// 计算总页数
const totalPages = computed(() => {
  if (searchText.value) {
    // 搜索时的页数
    const filtered = taskTypes.value.filter(type => 
      type.typeName.toLowerCase().includes(searchText.value.toLowerCase())
    )
    return Math.ceil(filtered.length / pageSize)
  }
  return Math.ceil(taskTypes.value.length / pageSize)
})

// 搜索文本变化时，重置页码
watch(searchText, () => {
  currentPage.value = 1
})

// 表单数据
const taskForm = reactive({
  title: '',
  description: '',
  pickupLocation: '',
  deliveryLocation: '',
  deliveryDate: '',
  deliveryTime: '',
  reward: '',
  images: []
})

// 滚动相关
const scrollTop = ref(0)
const lastScrollTop = ref(0)

// 触摸相关变量
const touchStartX = ref(0)
const touchEndX = ref(0)
const swipeThreshold = 50 // 触发滑动的最小距离
const isSwipingLeft = ref(false)
const isSwipingRight = ref(false)

// 触摸开始事件
const touchStart = (e) => {
  touchStartX.value = e.touches[0].clientX
}

// 触摸结束事件
const touchEnd = (e) => {
  touchEndX.value = e.changedTouches[0].clientX
  handleSwipe()
  
  // 重置滑动状态
  setTimeout(() => {
    isSwipingLeft.value = false
    isSwipingRight.value = false
  }, 300)
}

// 处理滑动
const handleSwipe = () => {
  // 如果只有一页，不处理滑动
  if (totalPages.value <= 1) return
  
  const swipeDistance = touchEndX.value - touchStartX.value
  
  // 向左滑动（下一页）
  if (swipeDistance < -swipeThreshold) {
    isSwipingRight.value = true
    nextPage()
  }
  
  // 向右滑动（上一页）
  if (swipeDistance > swipeThreshold) {
    isSwipingLeft.value = true
    prevPage()
  }
}

// 下一页（给分页器按钮使用）
const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  } else {
    // 从最后一页回到第一页
    currentPage.value = 1
  }
}

// 上一页（给分页器按钮使用）
const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  } else {
    // 从第一页回到最后一页
    currentPage.value = totalPages.value
  }
}

// 滚动事件处理
const onScroll = (e) => {
  lastScrollTop.value = e.detail.scrollTop
}

// 页面加载获取设备信息和任务类型
onMounted(() => {
  getSystemInfo()
  getTaskTypes()
  
  // 添加短暂延迟，确保页面已经加载
  setTimeout(() => {
    isPageReady.value = true
  }, 50)
})

// 获取系统信息
const getSystemInfo = () => {
  try {
    const systemInfo = uni.getSystemInfoSync()
    console.log('System info:', systemInfo)
    statusBarHeight.value = systemInfo.statusBarHeight || 20
  } catch (e) {
    console.error('获取系统信息失败:', e)
  }
}

// 获取任务类型
const getTaskTypes = async () => {
  try {
    isLoading.value = true
    console.log('开始获取任务类型...')
    const response = await getEnabledTaskTypes()
    console.log('获取任务类型响应:', response)
    
    if (response && response.code === 200 && response.data) {
      console.log('获取到的原始任务类型数据:', JSON.stringify(response.data).substring(0, 200) + '...')
      
      // 数据处理：确保图标数据格式一致
      taskTypes.value = response.data.map(type => {
        // 如果图标是URL但没有http前缀，添加前缀
        if (type.icon && typeof type.icon === 'string' && !type.icon.startsWith('http') && !type.icon.startsWith('data:')) {
          type.icon = type.icon.trim()
          // 如果图标是emoji或其他非URL格式，保持原样
          if (type.icon.length < 5 || !type.icon.includes('/')) {
            // 保持原样，可能是emoji或其他字符
          } else {
            // 可能是相对路径URL，添加http前缀
            type.icon = type.icon.startsWith('/') ? `https:${type.icon}` : `https://${type.icon}`
          }
        }
        return type
      })
      
      console.log('处理后的任务类型数量:', taskTypes.value.length)
      
      // 如果有任务类型，默认选择第一个
      if (taskTypes.value.length > 0) {
        selectedTaskType.value = taskTypes.value[0].id.toString()
        console.log('已默认选择第一个任务类型:', selectedTaskType.value)
      }
    } else {
      console.error('获取任务类型失败:', response)
      showMessage('获取任务类型失败')
    }
  } catch (error) {
    console.error('获取任务类型出错:', error)
    showMessage('网络错误，请稍后重试')
  } finally {
    isLoading.value = false
    console.log('任务类型加载完成，isLoading设为false')
  }
}

// 切换页码
const changePage = (page) => {
  currentPage.value = page
}

// 清除搜索
const clearSearch = () => {
  searchText.value = ''
}

// 返回上一页
const handleBack = () => {
  // 开始关闭动画
  isClosing.value = true
  
  // 动画结束后再执行返回操作
  setTimeout(() => {
    uni.navigateBack({
      delta: 1,
      animationType: 'slide-out-bottom',
      animationDuration: 300
    })
  }, 300)
}

// 选择任务类型
const selectTaskType = (typeId) => {
  // 如果选择的是当前已选择的任务类型，不做任何处理
  if (selectedTaskType.value === typeId) return
  
  // 更新选中的任务类型
  selectedTaskType.value = typeId
  
  // 由于描述不在初始视图中，需要延迟滚动
  setTimeout(() => {
    if (selectedTaskTypeObj.value && selectedTaskTypeObj.value.description) {
      // 计算合适的滚动位置（根据任务类型列表高度估算）
      const typeSectionHeight = taskTypes.value.length > 9 ? 460 : 320
      scrollTop.value = typeSectionHeight
    }
  }, 100)
}

// 选择位置
const showLocationPicker = (type) => {
  uni.showToast({
    title: '位置选择功能开发中',
    icon: 'none'
  })
}

// 日期选择
const onDateChange = (e) => {
  taskForm.deliveryDate = e.detail.value
}

// 时间选择
const onTimeChange = (e) => {
  taskForm.deliveryTime = e.detail.value
}

// 选择图片
const chooseImage = () => {
  uni.chooseImage({
    count: 3 - taskForm.images.length,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: (res) => {
      taskForm.images = [...taskForm.images, ...res.tempFilePaths]
    }
  })
}

// 删除图片
const deleteImage = (index) => {
  taskForm.images.splice(index, 1)
}

// 显示消息
const showMessage = (message) => {
  uni.showToast({
    title: message,
    icon: 'none'
  })
}

// 提交任务
const submitTask = () => {
  if (!selectedTaskType.value) {
    showMessage('请先选择任务类型')
    return
  }
  
  // 表单验证
  if (!taskForm.title) {
    showMessage('请输入任务标题')
    return
  }
  
  if (!taskForm.description) {
    showMessage('请输入任务描述')
    return
  }
  
  if (!taskForm.pickupLocation) {
    showMessage('请选择取货地点')
    return
  }
  
  if (!taskForm.deliveryLocation) {
    showMessage('请选择送达地点')
    return
  }
  
  if (!taskForm.deliveryDate || !taskForm.deliveryTime) {
    showMessage('请选择期望送达时间')
    return
  }
  
  if (!taskForm.reward) {
    showMessage('请输入悬赏金额')
    return
  }
  
  // 获取选中的任务类型名称
  const selectedTypeObj = taskTypes.value.find(type => type.id.toString() === selectedTaskType.value)
  const typeName = selectedTypeObj ? selectedTypeObj.typeName : '未知类型'
  
  // 构建提交的数据
  const submitData = {
    typeId: parseInt(selectedTaskType.value),
    typeName: typeName,
    title: taskForm.title,
    description: taskForm.description,
    pickupLocation: taskForm.pickupLocation,
    deliveryLocation: taskForm.deliveryLocation,
    expectedDeliveryTime: `${taskForm.deliveryDate} ${taskForm.deliveryTime}`,
    reward: parseFloat(taskForm.reward),
    images: taskForm.images
  }
  
  // 提交表单
  showMessage('准备提交任务...')
  console.log('任务表单数据:', submitData)
  
  // TODO: 实际提交到服务器
  // 示例代码，等待后端任务发布接口完成后再实现
  /*
  uni.showLoading({
    title: '发布中...'
  })
  
  // 调用发布任务API
  publishTask(submitData).then(res => {
    if (res.data && res.data.code === 200) {
      uni.hideLoading()
      uni.showToast({
        title: '发布成功',
        icon: 'success',
        duration: 2000
      })
      
      // 延迟返回首页
      setTimeout(() => {
        handleBack()
      }, 1500)
    } else {
      uni.hideLoading()
      showMessage(res.data.message || '发布失败')
    }
  }).catch(err => {
    uni.hideLoading()
    showMessage('网络错误，请稍后重试')
    console.error('发布任务失败:', err)
  })
  */
  
  // 临时：模拟提交成功
  uni.showLoading({
    title: '发布中...'
  })
  
  setTimeout(() => {
    uni.hideLoading()
    uni.showToast({
      title: '发布成功',
      icon: 'success',
      duration: 2000
    })
    
    // 延迟返回首页
    setTimeout(() => {
      handleBack()
    }, 1500)
  }, 1500)
}

// 计算选中的任务类型对象
const selectedTaskTypeObj = computed(() => {
  if (!selectedTaskType.value) return null
  return taskTypes.value.find(type => type.id.toString() === selectedTaskType.value) || null
})

// Swiper切换事件处理
const onSwiperChange = (e) => {
  currentPage.value = e.detail.current + 1
}

// 获取当前页的任务类型
const getPageTaskTypes = (page) => {
  const start = (page - 1) * pageSize
  const end = start + pageSize
  const result = filteredTaskTypes.value.slice(start, end)
  console.log(`获取第${page}页任务类型: 从${start}到${end}, 共${result.length}个`)
  return result
}
</script>

<style lang="scss" scoped>
.publish-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 100;
  border-radius: 30rpx 30rpx 0 0;
  overflow: hidden;
  box-shadow: 0 -10rpx 30rpx rgba(0, 0, 0, 0.1);
  /* 初始状态：位于屏幕底部外 */
  transform: translateY(100%);
  transition: transform 0.35s cubic-bezier(0.16, 1, 0.3, 1);
  will-change: transform;
  
  /* 显示动画 */
  &.page-show {
    transform: translateY(0);
  }
  
  /* 隐藏动画 */
  &.page-hide {
    transform: translateY(100%);
  }
}

// 自定义导航栏
.custom-nav-bar {
  position: fixed;
  left: 0;
  right: 0;
  height: 44px;
  background-color: #ffffff;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  border-radius: 30rpx 30rpx 0 0;
  
  .nav-left {
    position: absolute;
    left: 30rpx;
    height: 100%;
    display: flex;
    align-items: center;
  }
  
  .nav-title {
    font-size: 34rpx;
    font-weight: bold;
    color: #333;
  }
}

// 页面主体内容
.publish-content {
  height: calc(100vh - var(--window-top) - 10px);
  padding: 30rpx;
  box-sizing: border-box;
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  -webkit-overflow-scrolling: touch; /* 增强iOS滚动体验 */
}

// 任务类型选择
.section-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
  display: flex;
  align-items: center;
  
  .type-count {
    font-size: 24rpx;
    color: #999;
    margin-left: 10rpx;
    font-weight: normal;
  }
}

.task-types {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -10rpx 40rpx;
  position: relative;
  width: 100%;
  height: 100%;
}

.task-type-item {
  width: calc(33.33% - 20rpx);
  margin: 0 10rpx 20rpx;
  height: 140rpx;
  background-color: #ffffff;
  border-radius: 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.2), rgba(255, 255, 255, 0));
    z-index: 1;
  }
  
  .type-icon {
    font-size: 50rpx;
    margin-bottom: 10rpx;
    position: relative;
    z-index: 2;
    transition: transform 0.3s ease;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 60rpx;
    height: 60rpx;
  }
  
  .type-icon-image {
    width: 60rpx;
    height: 60rpx;
    margin-bottom: 10rpx;
    position: relative;
    z-index: 2;
    transition: transform 0.3s ease;
  }
  
  .type-name {
    font-size: 24rpx;
    color: #666;
    position: relative;
    z-index: 2;
    transition: color 0.3s ease;
    text-align: center;
    max-width: 90%;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  &:active {
    transform: scale(0.95);
  }
  
  &.active {
    background: linear-gradient(135deg, #ff9500, #ff7d00);
    box-shadow: 0 6rpx 15rpx rgba(255, 149, 0, 0.2);
    
    .type-icon, .type-icon-image {
      transform: scale(1.1);
    }
    
    .type-name {
      color: #ffffff;
      font-weight: 500;
    }
  }
}

// 表单样式
.form-item {
  margin-bottom: 30rpx;
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 20rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.03);
}

.form-label {
  display: block;
  font-size: 26rpx;
  color: #333;
  margin-bottom: 15rpx;
  font-weight: 500;
}

.form-input, .form-textarea {
  width: 100%;
  background-color: #f8f8f8;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
  border: 2rpx solid transparent;
  transition: all 0.3s;
  
  &:focus {
    border-color: #ff9500;
    background-color: #fff;
    box-shadow: 0 0 0 2rpx rgba(255, 149, 0, 0.1);
  }
}

.form-textarea {
  min-height: 180rpx;
  line-height: 1.5;
}

.placeholder {
  color: #bbb;
}

// 位置选择器
.location-picker {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #f8f8f8;
  border-radius: 12rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #333;
  
  .location-placeholder {
    color: #bbb;
  }
  
  .location-icon {
    font-size: 32rpx;
  }
}

// 时间选择器
.time-picker {
  display: flex;
  gap: 20rpx;
  
  .picker-item {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    background-color: #f8f8f8;
    border-radius: 12rpx;
    padding: 20rpx;
    font-size: 28rpx;
    
    .picker-text {
      color: #bbb;
    }
    
    .picker-arrow {
      font-size: 24rpx;
      color: #999;
    }
  }
}

// 金额输入
.reward-input-wrapper {
  display: flex;
  align-items: center;
  background-color: #f8f8f8;
  border-radius: 12rpx;
  padding: 0 20rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s;
  
  &:focus-within {
    border-color: #ff9500;
    background-color: #fff;
    box-shadow: 0 0 0 2rpx rgba(255, 149, 0, 0.1);
  }
  
  .currency-symbol {
    font-size: 32rpx;
    color: #ff9500;
    font-weight: bold;
    margin-right: 8rpx;
  }
  
  .reward-input {
    flex: 1;
    height: 80rpx;
    font-size: 28rpx;
    color: #333;
    background-color: transparent;
  }
}

// 上传区域
.upload-area {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  
  .upload-button {
    width: 160rpx;
    height: 160rpx;
    background-color: #f8f8f8;
    border-radius: 12rpx;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 2rpx dashed #ddd;
    
    .upload-icon {
      font-size: 48rpx;
      color: #bbb;
      line-height: 1;
      margin-bottom: 10rpx;
    }
    
    .upload-text {
      font-size: 24rpx;
      color: #bbb;
    }
  }
  
  .image-preview {
    width: 160rpx;
    height: 160rpx;
    position: relative;
    border-radius: 12rpx;
    overflow: hidden;
    
    .preview-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .delete-icon {
      position: absolute;
      top: 0;
      right: 0;
      width: 40rpx;
      height: 40rpx;
      background-color: rgba(0, 0, 0, 0.5);
      color: #fff;
      font-size: 28rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 0 0 0 12rpx;
    }
  }
}

// 选择类型提示
.select-type-hint {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60rpx 0;
  
  .hint-icon {
    font-size: 60rpx;
    margin-bottom: 20rpx;
    animation: bounce 2s infinite;
  }
  
  .hint-text {
    font-size: 28rpx;
    color: #999;
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-20rpx);
  }
  60% {
    transform: translateY(-10rpx);
  }
}

// 提交按钮
.submit-button {
  height: 90rpx;
  background: linear-gradient(to right, #ff9500, #ff7d00);
  border-radius: 45rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10rpx 20rpx rgba(255, 149, 0, 0.2);
  margin: 40rpx 0 calc(40rpx + env(safe-area-inset-bottom, 40rpx));
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, rgba(255, 255, 255, 0), rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0));
    animation: shine 2s infinite;
  }
  
  .button-text {
    font-size: 32rpx;
    color: #ffffff;
    font-weight: bold;
  }
  
  &:active {
    transform: scale(0.98);
    box-shadow: 0 5rpx 10rpx rgba(255, 149, 0, 0.2);
  }
  
  &.button-disabled {
    background: linear-gradient(to right, #cccccc, #bbbbbb);
    box-shadow: 0 5rpx 10rpx rgba(0, 0, 0, 0.1);
    
    &::before {
      display: none;
    }
    
    &:active {
      transform: none;
    }
  }
}

@keyframes shine {
  0% {
    left: -100%;
  }
  20% {
    left: 100%;
  }
  100% {
    left: 100%;
  }
}

// 新增：搜索框样式
.search-box {
  display: flex;
  align-items: center;
  background-color: #f2f2f2;
  border-radius: 16rpx;
  padding: 10rpx 20rpx;
  margin-bottom: 20rpx;
  
  .search-input {
    flex: 1;
    height: 60rpx;
    font-size: 26rpx;
    margin: 0 15rpx;
  }
}

// 骨架屏
.skeleton-container {
  display: flex;
  flex-wrap: wrap;
  margin: 0 -10rpx 40rpx;
}

.skeleton-item {
  width: calc(33.33% - 20rpx);
  margin: 0 10rpx 20rpx;
  height: 140rpx;
  background-color: #f5f5f5;
  border-radius: 16rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  animation: skeleton-pulse 1.5s infinite ease-in-out;
  
  .skeleton-circle {
    width: 60rpx;
    height: 60rpx;
    border-radius: 50%;
    background-color: #e0e0e0;
    margin-bottom: 15rpx;
  }
  
  .skeleton-text {
    width: 80rpx;
    height: 20rpx;
    background-color: #e0e0e0;
    border-radius: 10rpx;
  }
}

@keyframes skeleton-pulse {
  0% {
    opacity: 0.6;
  }
  50% {
    opacity: 0.8;
  }
  100% {
    opacity: 0.6;
  }
}

// 分页指示器
.pagination {
  display: flex;
  justify-content: center;
  gap: 12rpx;
  margin-top: -20rpx;
  margin-bottom: 30rpx;
  
  .page-dot {
    width: 16rpx;
    height: 16rpx;
    border-radius: 50%;
    background-color: #ddd;
    transition: all 0.3s;
    
    &.active {
      width: 30rpx;
      border-radius: 8rpx;
      background-color: #ff9500;
    }
  }
}

// 空状态
.empty-types {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40rpx 0;
  
  .empty-icon {
    font-size: 80rpx;
    margin-bottom: 20rpx;
  }
  
  .empty-text {
    font-size: 28rpx;
    color: #999;
  }
}

// 任务类型描述
.type-description {
  margin-bottom: 30rpx;
  background: linear-gradient(to right, #fff8e6, #fffcf2);
  border-radius: 16rpx;
  padding: 0;
  box-shadow: 0 4rpx 12rpx rgba(255, 149, 0, 0.1);
  overflow: hidden;
  animation: fadeIn 0.3s ease-in-out;
  border: 1rpx solid rgba(255, 149, 0, 0.2);
  
  .description-header {
    display: flex;
    align-items: center;
    padding: 16rpx 20rpx;
    background: linear-gradient(to right, #ff9500, #ffbb00);
    
    .header-icon {
      width: 40rpx;
      height: 40rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15rpx;
      
      .header-icon-image {
        width: 40rpx;
        height: 40rpx;
      }
    }
    
    .header-text {
      flex: 1;
      
      .description-title {
        font-size: 26rpx;
        color: #ffffff;
        font-weight: 600;
      }
    }
  }
  
  .description-content {
    padding: 20rpx;
    
    .description-text {
      font-size: 26rpx;
      color: #666;
      line-height: 1.6;
      word-break: break-all;
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 新增swiper样式
.types-swiper {
  height: 450rpx !important; // 设置明确的高度，确保能容纳一行任务类型
  width: 100%;
  margin-bottom: 40rpx;
}
</style> 