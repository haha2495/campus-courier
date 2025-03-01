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
      
      <!-- 表单部分 - 只有在选择了任务类型后显示 -->
      <view v-if="selectedTaskType" class="form-container" :class="{'form-show': selectedTaskType}">
        <!-- 任务详情卡片 -->
        <view class="form-card">
          <view class="card-header">
            <u-icon name="file-text" size="32rpx" color="#ff9500"></u-icon>
            <text class="card-title">任务详情</text>
          </view>
          
          <view class="card-body">
            <!-- 任务标题 -->
            <view class="form-item">
              <text class="form-label">任务标题</text>
              <view class="input-container">
                <input 
                  type="text" 
                  class="form-input" 
                  placeholder="为任务起个简洁明了的标题吧~" 
                  placeholder-class="placeholder"
                  v-model="taskForm.title"
                  maxlength="30"
                  @focus="titleFocused = true"
                  @blur="titleFocused = false"
                />
                <view class="input-icon" v-if="taskForm.title">
                  <u-icon name="checkmark-circle" color="#36c892" size="32rpx"></u-icon>
                </view>
              </view>
              <text class="input-counter" :class="{ 'input-warning': taskForm.title.length > 20 }">
                {{ taskForm.title.length }}/30
              </text>
            </view>
            
            <!-- 任务描述 -->
            <view class="form-item">
              <text class="form-label">任务描述</text>
              <view class="textarea-container">
                <textarea 
                  class="form-textarea" 
                  placeholder="详细描述您的任务要求，让接单者更好理解~" 
                  placeholder-class="placeholder"
                  v-model="taskForm.description"
                  maxlength="200"
                  @focus="descriptionFocused = true"
                  @blur="descriptionFocused = false"
                />
              </view>
              <text class="input-counter" :class="{ 'input-warning': taskForm.description.length > 150 }">
                {{ taskForm.description.length }}/200
              </text>
            </view>
            
            <!-- 取件地址 -->
            <view class="form-item">
              <text class="form-label">取件地址</text>
              <view class="input-container">
                <view class="input-prefix">
                  <u-icon name="map" size="32rpx" color="#ff9500"></u-icon>
                </view>
                <input 
                  type="text" 
                  class="form-input with-prefix" 
                  placeholder="请输入取件地址" 
                  placeholder-class="placeholder"
                  v-model="taskForm.pickupLocation"
                />
                <view class="input-suffix" @click="openMap('pickup')">
                  <u-icon name="map-fill" size="28rpx" color="#36c892"></u-icon>
                </view>
              </view>
            </view>
            
            <!-- 送达地址 -->
            <view class="form-item">
              <text class="form-label">送达地址</text>
              <view class="input-container">
                <view class="input-prefix">
                  <u-icon name="map-fill" size="32rpx" color="#ff9500"></u-icon>
                </view>
                <input 
                  type="text" 
                  class="form-input with-prefix" 
                  placeholder="请输入送达地址" 
                  placeholder-class="placeholder"
                  v-model="taskForm.deliveryLocation"
                />
                <view class="input-suffix" @click="openMap('delivery')">
                  <u-icon name="map-fill" size="28rpx" color="#36c892"></u-icon>
                </view>
              </view>
            </view>
            
            <!-- 预期完成时间 -->
            <view class="form-item">
              <text class="form-label">预期完成时间</text>
              <view class="time-picker">
                <picker 
                  mode="date" 
                  :value="taskForm.deliveryDate"
                  :start="currentDate"
                  @change="onDateChange"
                >
                  <view class="input-container date-picker">
                    <view class="input-prefix">
                      <u-icon name="calendar" size="32rpx" color="#ff9500"></u-icon>
                    </view>
                    <text class="picker-text" :class="{'picker-placeholder': !taskForm.deliveryDate}">
                      {{ taskForm.deliveryDate || '选择日期' }}
                    </text>
                    <view class="input-suffix">
                      <u-icon name="arrow-down" size="28rpx" color="#c0c4cc"></u-icon>
                    </view>
                  </view>
                </picker>
                
                <picker 
                  mode="time" 
                  :value="taskForm.deliveryTime" 
                  @change="onTimeChange"
                >
                  <view class="input-container time-picker">
                    <view class="input-prefix">
                      <u-icon name="clock" size="32rpx" color="#ff9500"></u-icon>
                    </view>
                    <text class="picker-text" :class="{'picker-placeholder': !taskForm.deliveryTime}">
                      {{ taskForm.deliveryTime || '选择时间' }}
                    </text>
                    <view class="input-suffix">
                      <u-icon name="arrow-down" size="28rpx" color="#c0c4cc"></u-icon>
                    </view>
                  </view>
                </picker>
              </view>
            </view>
            
            <!-- 物品估算信息 -->
            <view class="form-item">
              <text class="form-label">物品信息</text>
              <view class="item-info-row">
                <!-- 预估重量 -->
                <view class="item-info-col">
                  <view class="weight-input-wrapper">
                    <input 
                      type="digit" 
                      class="weight-input" 
                      placeholder="预估重量" 
                      placeholder-class="placeholder"
                      v-model="taskForm.estimatedWeight"
                    />
                    <text class="unit-label">kg</text>
                  </view>
                </view>
                
                <!-- 预估大小 -->
                <view class="item-info-col">
                  <picker 
                    :value="sizeIndex" 
                    :range="sizeOptions" 
                    @change="onSizeChange"
                  >
                    <view class="size-picker">
                      <text class="size-text" :class="{'size-placeholder': !taskForm.estimatedSize}">
                        {{ taskForm.estimatedSize || '物品大小' }}
                      </text>
                      <view class="picker-arrow">
                        <u-icon name="arrow-down" size="24rpx" color="#999"></u-icon>
                      </view>
                    </view>
                  </picker>
                </view>
              </view>
            </view>
            
            <!-- 任务报酬 -->
            <view class="form-item">
              <text class="form-label">任务报酬</text>
              <view class="money-input-container">
                <view class="money-input-wrapper">
                  <view class="currency-symbol-wrapper">
                    <text class="currency-symbol">¥</text>
                  </view>
                  <input 
                    type="digit" 
                    class="money-input" 
                    placeholder="请输入悬赏金额" 
                    placeholder-class="placeholder"
                    v-model="taskForm.reward"
                  />
                  <view class="money-suffix" v-if="taskForm.reward">元</view>
                </view>
              </view>
            </view>
            
            <!-- 是否加急 -->
            <view class="form-item">
              <view class="urgent-header">
                <text class="form-label">是否加急</text>
                <switch 
                  :checked="taskForm.urgent" 
                  @change="onUrgentChange" 
                  color="#ff9500"
                  style="transform:scale(0.8)"
                />
              </view>
              
              <!-- 加急费用，只在加急时显示 -->
              <view class="urgent-fee" v-if="taskForm.urgent">
                <view class="money-input-container">
                  <view class="money-input-wrapper">
                    <view class="currency-symbol-wrapper">
                      <text class="currency-symbol">¥</text>
                    </view>
                    <input 
                      type="digit" 
                      class="money-input" 
                      placeholder="加急费用" 
                      placeholder-class="placeholder"
                      v-model="taskForm.urgentReward"
                    />
                    <view class="money-suffix" v-if="taskForm.urgentReward">元</view>
                  </view>
                </view>
              </view>
            </view>
            
            <!-- 物品图片 -->
            <view class="form-item">
              <text class="form-label">物品图片</text>
              <view class="upload-area">
                <!-- 上传按钮 -->
                <view class="upload-button" @click="chooseImage" v-if="taskForm.images.length < 3">
                  <text class="upload-icon">+</text>
                  <text class="upload-text">上传图片</text>
                </view>
                
                <!-- 图片预览 -->
                <view 
                  class="image-preview" 
                  v-for="(img, index) in taskForm.images" 
                  :key="index"
                  @click="previewImage(index)"
                >
                  <image 
                    :src="img" 
                    class="preview-image" 
                    mode="aspectFill"
                    @load="imageLoaded(index)"
                    @error="imageError(index)"
                  >
                    <view class="image-loading" v-if="imageLoading[index]">
                      <u-icon name="photo" size="40rpx" color="#ddd"></u-icon>
                    </view>
                  </image>
                  <view class="delete-icon" @tap.stop="deleteImage(index)">
                    <u-icon name="close" size="24rpx" color="#fff"></u-icon>
                  </view>
                  <view class="image-error-overlay" v-if="imageErrors[index]">
                    <u-icon name="error-circle" size="40rpx" color="#f56c6c"></u-icon>
                    <text class="error-text">加载失败</text>
                  </view>
                </view>
              </view>
              <text class="upload-hint">最多上传3张图片</text>
            </view>
            
            <!-- 备注说明 -->
            <view class="form-item">
              <text class="form-label">备注说明</text>
              <view class="textarea-container">
                <textarea 
                  class="form-textarea" 
                  placeholder="补充说明（选填）" 
                  placeholder-class="placeholder"
                  v-model="taskForm.notes"
                  maxlength="100"
                />
              </view>
              <text class="input-counter">{{ taskForm.notes.length }}/100</text>
            </view>
          </view>
        </view>
        
        <!-- 联系人信息卡片 -->
        <view class="form-card">
          <view class="card-header">
            <u-icon name="account" size="32rpx" color="#ff9500"></u-icon>
            <text class="card-title">联系人信息</text>
          </view>
          
          <view class="card-body">
            <!-- 联系人姓名 -->
            <view class="form-item">
              <text class="form-label">联系人姓名</text>
              <view class="input-container">
                <view class="input-prefix">
                  <u-icon name="account" size="32rpx" color="#ff9500"></u-icon>
                </view>
                <input 
                  type="text" 
                  class="form-input with-prefix" 
                  placeholder="您的尊姓大名" 
                  placeholder-class="placeholder"
                  v-model="taskForm.contactName"
                  @focus="contactNameFocused = true"
                  @blur="contactNameFocused = false"
                />
              </view>
            </view>
            
            <!-- 联系电话 -->
            <view class="form-item">
              <text class="form-label">联系电话</text>
              <view class="input-container">
                <view class="input-prefix">
                  <u-icon name="phone" size="32rpx" color="#ff9500"></u-icon>
                </view>
                <input 
                  type="number" 
                  class="form-input with-prefix" 
                  placeholder="您的联系电话" 
                  placeholder-class="placeholder"
                  v-model="taskForm.contactPhone"
                  maxlength="11"
                  @focus="contactPhoneFocused = true; phoneError = false"
                  @blur="validatePhone"
                />
              </view>
              <text class="error-message" v-if="phoneError">请输入正确的11位手机号</text>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 提交按钮 -->
      <view 
        class="submit-button" 
        :class="{ 
          'button-disabled': !selectedTaskType || !isFormValid,
          'button-can-submit': selectedTaskType && isFormValid
        }"
        @click="submitTask"
      >
        <view class="button-content">
          <u-icon v-if="selectedTaskType && isFormValid" name="checkmark-circle" color="#fff" size="34rpx" class="button-icon"></u-icon>
          <text class="button-text">{{ 
            !selectedTaskType ? '请先选择任务类型' : 
            !isFormValid ? '请完善必填信息' : '发布任务' 
          }}</text>
        </view>
        <view class="button-ripple" v-if="selectedTaskType && isFormValid"></view>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch, nextTick } from 'vue'
import { getEnabledTaskTypes } from '@/api/taskType'
import { uploadApi } from '@/api/upload' // 导入上传API
import { publishTask } from '@/api/task' // 导入发布任务API

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

// 表单状态控制
const titleFocused = ref(false)
const descriptionFocused = ref(false)
const contactNameFocused = ref(false)
const contactPhoneFocused = ref(false)
const phoneError = ref(false)

// 验证手机号
const validatePhone = () => {
  if (taskForm.contactPhone && !/^1\d{10}$/.test(taskForm.contactPhone)) {
    phoneError.value = true
  } else {
    phoneError.value = false
  }
}

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
  urgent: false,
  urgentReward: '',
  estimatedWeight: '',
  estimatedSize: '',
  notes: '',
  contactName: '',
  contactPhone: '',
  images: []
})

// 物品大小选项
const sizeOptions = ['小件', '中件', '大件', '特大件']
const sizeIndex = ref(0)

// 表单验证
const isFormValid = computed(() => {
  // 必填字段验证
  if (!taskForm.title) return false
  if (!taskForm.description) return false
  if (!taskForm.pickupLocation) return false
  if (!taskForm.deliveryLocation) return false
  if (!taskForm.deliveryDate || !taskForm.deliveryTime) return false
  if (!taskForm.reward) return false
  if (taskForm.urgent && !taskForm.urgentReward) return false
  if (!taskForm.contactName) return false
  if (!taskForm.contactPhone) return false
  
  // 电话号码格式验证 (简单验证)
  if (!/^1\d{10}$/.test(taskForm.contactPhone)) return false
  
  return true
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
  // 保留原有功能，但不再使用
  console.log('选择位置类型:', type)
}

// 打开地图选择位置
const openMap = (type) => {
  uni.showToast({
    title: '选择地图位置功能开发中',
    icon: 'none'
  })
  // 这里可以集成地图选择功能
}

// 日期选择
const onDateChange = (e) => {
  taskForm.deliveryDate = e.detail.value
}

// 时间选择
const onTimeChange = (e) => {
  taskForm.deliveryTime = e.detail.value
}

// 物品大小选择
const onSizeChange = (e) => {
  sizeIndex.value = e.detail.value
  taskForm.estimatedSize = sizeOptions[e.detail.value]
}

// 加急开关切换
const onUrgentChange = (e) => {
  taskForm.urgent = e.detail.value
  // 如果取消加急，清空加急费
  if (!taskForm.urgent) {
    taskForm.urgentReward = ''
  }
}

// 预处理图片，确保能正确显示
const preprocessImage = (imageUrl) => {
  return new Promise((resolve, reject) => {
    console.log('预处理图片:', imageUrl)
    // 对于微信小程序，需要先下载图片到本地
    uni.getImageInfo({
      src: imageUrl,
      success: (res) => {
        console.log('图片信息获取成功:', res)
        // 返回本地路径
        resolve(res.path)
      },
      fail: (err) => {
        console.error('图片信息获取失败:', err)
        // 出错时仍然使用原始URL
        resolve(imageUrl)
      }
    })
  })
}

// 选择图片
const chooseImage = () => {
  uni.chooseImage({
    count: 3 - taskForm.images.length,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: (res) => {
      // 显示上传中的提示
      uni.showLoading({
        title: '上传中...',
        mask: true
      })

      // 创建上传任务数组
      const uploadTasks = res.tempFilePaths.map(filePath => {
        return uploadApi.upload(filePath)
          .then(fileUrl => {
            console.log('文件上传成功, URL:', fileUrl)
            // 预处理图片URL
            return preprocessImage(fileUrl)
          })
          .catch(error => {
            console.error('文件上传失败:', error)
            uni.showToast({
              title: '图片上传失败',
              icon: 'none'
            })
            return null
          })
      })

      // 并行上传所有图片
      Promise.all(uploadTasks)
        .then(results => {
          // 过滤掉上传失败的图片（值为null的项）
          const successUrls = results.filter(url => url !== null)
          
          // 更新图片加载状态数组
          const newLoadingStates = successUrls.map(() => true)
          imageLoading.value = [...imageLoading.value, ...newLoadingStates]
          
          // 重置错误状态数组
          const newErrorStates = successUrls.map(() => false)
          imageErrors.value = [...imageErrors.value, ...newErrorStates]
          
          // 将新上传的图片URL添加到已有的图片列表中
          taskForm.images = [...taskForm.images, ...successUrls]
          
          console.log('上传后的图片数组:', JSON.stringify(taskForm.images))
          
          uni.hideLoading()
          
          if (successUrls.length > 0) {
            uni.showToast({
              title: '上传成功',
              icon: 'success'
            })
          }
        })
        .catch(error => {
          console.error('批量上传失败:', error)
          uni.hideLoading()
          uni.showToast({
            title: '图片上传失败',
            icon: 'none'
          })
        })
    }
  })
}

// 图片加载状态
const imageLoading = ref([])
// 图片错误状态
const imageErrors = ref([])

// 图片加载完成
const imageLoaded = (index) => {
  console.log(`图片${index}加载完成`)
  imageLoading.value[index] = false
  imageErrors.value[index] = false
}

// 图片加载失败处理
const imageError = (index) => {
  console.error(`图片${index}加载失败:`, taskForm.images[index])
  imageLoading.value[index] = false
  imageErrors.value[index] = true
  
  // 图片重试机制：可能需要在微信小程序中进行特殊处理
  // 有时网络图片无法直接加载，尝试转换成本地临时路径
  uni.downloadFile({
    url: taskForm.images[index],
    success: (res) => {
      if (res.statusCode === 200) {
        console.log('图片下载成功，临时路径:', res.tempFilePath)
        // 更新图片路径为临时本地路径
        taskForm.images[index] = res.tempFilePath
        // 重置状态，触发重新加载
        imageLoading.value[index] = true
        imageErrors.value[index] = false
      }
    },
    fail: (err) => {
      console.error('图片下载失败:', err)
    }
  })
}

// 预览图片
const previewImage = (index) => {
  console.log('预览图片，索引:', index, '图片URL:', taskForm.images[index])
  uni.previewImage({
    urls: taskForm.images,
    current: taskForm.images[index]
  })
}

// 删除图片
const deleteImage = (index) => {
  taskForm.images.splice(index, 1)
  imageLoading.value.splice(index, 1)
  imageErrors.value.splice(index, 1)
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
    showMessage('请选择取件地点')
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
  
  if (taskForm.urgent && !taskForm.urgentReward) {
    showMessage('请输入加急费用')
    return
  }
  
  if (!taskForm.contactName) {
    showMessage('请输入联系人姓名')
    return
  }
  
  if (!taskForm.contactPhone) {
    showMessage('请输入联系电话')
    return
  }
  
  // 电话号码格式验证
  if (!/^1\d{10}$/.test(taskForm.contactPhone)) {
    showMessage('请输入正确的手机号码')
    return
  }
  
  // 获取选中的任务类型名称
  const selectedTypeObj = taskTypes.value.find(type => type.id.toString() === selectedTaskType.value)
  const typeName = selectedTypeObj ? selectedTypeObj.typeName : '未知类型'
  
  // 构建提交的数据
  const submitData = {
    typeId: parseInt(selectedTaskType.value),
    title: taskForm.title,
    description: taskForm.description,
    pickupAddress: taskForm.pickupLocation,
    deliveryAddress: taskForm.deliveryLocation,
    expectedFinishTime: `${taskForm.deliveryDate} ${taskForm.deliveryTime}:00`,
    reward: parseFloat(taskForm.reward),
    urgent: taskForm.urgent,
    urgentReward: taskForm.urgent ? parseFloat(taskForm.urgentReward) : 0,
    estimatedWeight: taskForm.estimatedWeight ? parseFloat(taskForm.estimatedWeight) : null,
    estimatedSize: taskForm.estimatedSize || null,
    contactName: taskForm.contactName,
    contactPhone: taskForm.contactPhone,
    notes: taskForm.notes || '',
    images: taskForm.images.length > 0 ? taskForm.images : []
  }
  
  // 显示加载状态
  uni.showLoading({
    title: '发布中...',
    mask: true
  })
  
  // 调用发布任务API
  publishTask(submitData)
    .then(res => {
      uni.hideLoading()
      
      if (res.code === 200) {
        // 发布成功
        uni.showToast({
          title: '发布成功',
          icon: 'success',
          duration: 2000
        })
        
        // 1.5秒后返回上一页
        setTimeout(() => {
          uni.navigateBack({
            delta: 1
          })
        }, 1500)
      } else {
        // 发布失败
        showMessage(res.message || '发布失败，请重试')
      }
    })
    .catch(err => {
      uni.hideLoading()
      console.error('发布任务失败:', err)
      showMessage('网络错误，请稍后重试')
    })
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
  padding-bottom: calc(160rpx + env(safe-area-inset-bottom, 40rpx)); /* 增加底部内边距 */
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
  position: relative;
  
  &:last-child {
    margin-bottom: 0;
  }
  
  .form-label {
    display: block;
    font-size: 28rpx;
    color: #333333;
    margin-bottom: 14rpx;
    font-weight: 600;
    position: relative;
    padding-left: 16rpx;
    
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 4rpx;
      height: 24rpx;
      width: 6rpx;
      background: linear-gradient(to bottom, #ff9500, #ffbb00);
      border-radius: 3rpx;
    }
  }
  
  .input-counter {
    position: absolute;
    right: 12rpx;
    bottom: 12rpx;
    font-size: 22rpx;
    color: #999;
    transition: color 0.3s;
    
    &.input-warning {
      color: #ff9500;
    }
  }
  
  .upload-hint {
    font-size: 22rpx;
    color: #999;
    margin-top: 8rpx;
    display: block;
  }
  
  .error-message {
    font-size: 24rpx;
    color: #ff4d4f;
    margin-top: 8rpx;
    animation: fadeIn 0.3s;
  }
}

.input-container, .textarea-container {
  position: relative;
  display: flex;
  align-items: center;
  background-color: #ffffff;
  border-radius: 20rpx; /* 增加圆角 */
  border: 2rpx solid #e8e8e8;
  transition: all 0.3s;
  overflow: hidden;
  box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.03);
  margin-bottom: 26rpx; /* 确保下方计数器有足够空间 */
  
  &:focus-within {
    border-color: #ff9500;
    background-color: #fffcf7;
    box-shadow: 0 8rpx 20rpx rgba(255, 149, 0, 0.15);
    transform: translateY(-2rpx); /* 轻微上浮效果 */
  }
  
  .input-prefix {
    padding: 0 16rpx 0 24rpx; /* 增加左侧内边距 */
    display: flex;
    align-items: center;
  }
  
  .input-icon {
    padding: 0 20rpx 0 10rpx;
  }
}

.textarea-container {
  align-items: flex-start;
}

// 改进的表单输入项样式
.form-input, .form-textarea {
  flex: 1;
  background-color: transparent;
  padding: 24rpx 20rpx; /* 增加内边距 */
  font-size: 30rpx;
  color: #333333;
  border: none;
  
  &.with-prefix {
    padding-left: 10rpx;
  }
  
  &::placeholder {
    color: #c0c4cc;
    font-size: 28rpx;
    transition: all 0.3s;
  }
  
  &:focus::placeholder {
    opacity: 0.7; /* 聚焦时减淡占位符 */
    transform: translateX(4rpx); /* 轻微位移效果 */
  }
}

.form-textarea {
  width: 100%;
  min-height: 180rpx;
  line-height: 1.6;
  padding: 20rpx;
  font-size: 30rpx;
  color: #333333;
  border: none;
}

.location-text, .picker-text {
  flex: 1;
  font-size: 30rpx;
  color: #333333;
  padding: 22rpx 0;
  
  &.location-placeholder, &.picker-placeholder {
    color: #c0c4cc;
  }
}

.input-suffix {
  padding: 0 24rpx 0 10rpx; /* 增加右侧内边距 */
}

// 时间选择器样式优化
.time-picker {
  display: flex;
  gap: 20rpx;
  
  picker {
    flex: 1;
  }
  
  .date-picker, .time-picker {
    height: 86rpx;
    transition: all 0.3s;
    
    &:active {
      transform: scale(0.98); /* 轻微缩放效果 */
    }
  }
}

// 日期时间选择器增强
.input-container.date-picker, .input-container.time-picker {
  &:active {
    background-color: #fffcf7;
    border-color: #ffcb8c;
  }
}

// 搜索框样式增强
.search-box {
  display: flex;
  align-items: center;
  background-color: #f2f2f2;
  border-radius: 30rpx; /* 增加圆角 */
  padding: 10rpx 24rpx; /* 增加内边距 */
  margin-bottom: 20rpx;
  box-shadow: inset 0 2rpx 6rpx rgba(0, 0, 0, 0.05);
  border: 1rpx solid #e6e6e6;
  transition: all 0.3s;
  
  &:focus-within {
    background-color: #ffffff;
    border-color: #ff9500;
    box-shadow: 0 4rpx 12rpx rgba(255, 149, 0, 0.1);
  }
  
  .search-input {
    flex: 1;
    height: 60rpx;
    font-size: 26rpx;
    margin: 0 15rpx;
  }
}

// 上传按钮增强
.upload-button {
  width: 160rpx;
  height: 160rpx;
  background-color: #f8f8f8;
  border-radius: 20rpx; /* 增加圆角 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2rpx dashed #ddd;
  transition: all 0.3s;
  
  &:active {
    background-color: #f0f0f0;
    transform: scale(0.97); /* 轻微缩放效果 */
  }
}

// 图片预览增强
.image-preview {
  width: 160rpx;
  height: 160rpx;
  position: relative;
  border-radius: 20rpx; /* 增加圆角 */
  overflow: hidden;
  border: 2rpx solid #f0f0f0;
  transition: all 0.3s;
  
  &:active {
    transform: scale(0.95); /* 轻微缩放效果 */
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  }
  
  .delete-icon {
    position: absolute;
    top: 8rpx;
    right: 8rpx;
    width: 40rpx;
    height: 40rpx;
    background-color: rgba(0, 0, 0, 0.6);
    color: #fff;
    font-size: 24rpx;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    transition: all 0.2s;
    
    &:active {
      transform: scale(0.9);
      background-color: rgba(255, 77, 79, 0.8);
    }
  }

  .image-error-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(245, 245, 245, 0.9);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    z-index: 2;
    
    .error-text {
      font-size: 20rpx;
      color: #f56c6c;
      margin-top: 10rpx;
    }
  }
}

// 提交按钮增强
.submit-button {
  height: 98rpx; /* 增加高度 */
  background: linear-gradient(to right, #cccccc, #bbbbbb);
  border-radius: 49rpx; /* 保持半圆形状 */
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 10rpx 20rpx rgba(0, 0, 0, 0.1);
  margin: 60rpx 20rpx calc(80rpx + env(safe-area-inset-bottom, 40rpx)); /* 增加按钮左右边距 */
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;
  
  .button-content {
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    z-index: 2;
  }
  
  .button-icon {
    margin-right: 10rpx;
    animation: bounceIn 0.5s;
  }
  
  .button-text {
    font-size: 32rpx;
    color: #ffffff;
    font-weight: bold;
  }
  
  &:active {
    transform: scale(0.98);
  }
  
  &.button-can-submit {
    background: linear-gradient(to right, #ff9500, #ff7d00);
    box-shadow: 0 10rpx 30rpx rgba(255, 149, 0, 0.25); /* 更明显的阴影 */
  }
}

@keyframes bounceIn {
  0% {
    opacity: 0;
    transform: scale(0.3);
  }
  50% {
    opacity: 1;
    transform: scale(1.1);
  }
  70% {
    transform: scale(0.9);
  }
  100% {
    transform: scale(1);
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

// 表单容器样式
.form-container {
  opacity: 0;
  transform: translateY(20rpx);
  transition: all 0.4s ease;
  
  &.form-show {
    opacity: 1;
    transform: translateY(0);
  }
}

// 表单卡片样式
.form-card {
  background-color: #ffffff;
  border-radius: 16rpx;
  margin-bottom: 36rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.06);
  
  .card-header {
    display: flex;
    align-items: center;
    padding: 24rpx;
    background: linear-gradient(to right, #fff8e6, #fffcf5);
    border-bottom: 1px solid #f0f0f0;
    
    .card-title {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
      margin-left: 12rpx;
    }
  }
  
  .card-body {
    padding: 30rpx 24rpx;
  }
}

// 物品信息行样式
.item-info-row {
  display: flex;
  gap: 20rpx;
  
  .item-info-col {
    flex: 1;
  }
}

// 预估重量输入框
.weight-input-wrapper {
  display: flex;
  align-items: center;
  background-color: #ffffff;
  border-radius: 20rpx; /* 增加圆角 */
  padding: 0 24rpx; /* 增加内边距 */
  border: 2rpx solid #e8e8e8;
  transition: all 0.3s;
  box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.03);
  
  &:focus-within {
    border-color: #ff9500;
    background-color: #fffcf7;
    box-shadow: 0 8rpx 20rpx rgba(255, 149, 0, 0.15);
    transform: translateY(-2rpx); /* 轻微上浮效果 */
  }
  
  .weight-input {
    flex: 1;
    height: 80rpx;
    font-size: 30rpx;
    color: #333333;
    background-color: transparent;
  }
  
  .unit-label {
    font-size: 26rpx;
    color: #999;
    margin-left: 8rpx;
  }
}

// 预估大小选择器
.size-picker {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #ffffff;
  border-radius: 20rpx; /* 增加圆角 */
  padding: 24rpx 24rpx; /* 增加内边距 */
  font-size: 30rpx;
  border: 2rpx solid #e8e8e8;
  box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.03);
  transition: all 0.3s;
  
  &:active {
    background-color: #fffcf7;
    border-color: #ffcb8c;
    transform: scale(0.99); /* 轻微缩放效果 */
  }
  
  .size-text {
    color: #333333;
    
    &.size-placeholder {
      color: #c0c4cc;
    }
  }
}

// 加急设置样式
.urgent-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16rpx;
}

.urgent-fee {
  margin-top: 16rpx;
  animation: fadeIn 0.3s;
}

// 改进的图片上传区域样式
.upload-area {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
  margin-bottom: 8rpx;
  
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
    transition: all 0.3s;
    
    &:active {
      background-color: #f0f0f0;
    }
    
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
    border: 2rpx solid #f0f0f0;
    
    .preview-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .image-loading {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-color: rgba(245, 245, 245, 0.8);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 2;
      animation: pulse 1.5s infinite;
    }
    
    .delete-icon {
      position: absolute;
      top: 6rpx;
      right: 6rpx;
      width: 36rpx;
      height: 36rpx;
      background-color: rgba(0, 0, 0, 0.6);
      color: #fff;
      font-size: 24rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 50%;
      z-index: 3;
      
      &:active {
        transform: scale(0.9);
        background-color: rgba(255, 77, 79, 0.8);
      }
    }
    
    &:active {
      transform: scale(0.95);
    }
  }
}

@keyframes pulse {
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

// 新增：金额输入容器和输入框样式增强
.money-input-container {
  position: relative;
  margin-bottom: 16rpx;
}

.money-input-wrapper {
  display: flex;
  align-items: center;
  background-color: #ffffff;
  border-radius: 20rpx;
  border: 2rpx solid #e8e8e8;
  box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.03);
  transition: all 0.3s ease;
  overflow: hidden;
  padding-right: 20rpx;
  
  &:focus-within {
    border-color: #ff9500;
    background-color: #fffcf7;
    box-shadow: 0 8rpx 20rpx rgba(255, 149, 0, 0.15);
    transform: translateY(-2rpx);
  }
  
  .currency-symbol-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 70rpx;
    height: 80rpx;
    background: linear-gradient(135deg, #ff9500, #ff7d00);
    padding: 0 16rpx;
    
    .currency-symbol {
      font-size: 34rpx;
      color: #ffffff;
      font-weight: bold;
    }
  }
  
  .money-input {
    flex: 1;
    height: 80rpx;
    font-size: 32rpx;
    color: #333333;
    background-color: transparent;
    padding: 0 20rpx;
    font-weight: 500;
    
    &::placeholder {
      color: #c0c4cc;
      font-size: 28rpx;
      font-weight: normal;
      transition: all 0.3s;
    }
    
    &:focus::placeholder {
      opacity: 0.7;
      transform: translateX(4rpx);
    }
  }
  
  .money-suffix {
    font-size: 28rpx;
    color: #999;
    margin-left: 8rpx;
    transition: all 0.3s;
  }
}
</style> 