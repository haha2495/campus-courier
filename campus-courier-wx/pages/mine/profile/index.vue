<template>
  <view class="profile-container">
    <!-- 状态栏占位 -->
    <view class="status-bar" :style="{ height: statusBarHeight + 'px' }"></view>
    
    <!-- 顶部导航栏 -->
    <view class="nav-bar">
      <view class="nav-left" @click="handleBack">
        <u-icon name="arrow-left" color="#333" size="40"></u-icon>
      </view>
      <text class="nav-title">个人信息</text>
    </view>

    <!-- 个人信息表单 -->
    <view class="profile-form">
      <!-- 头像 -->
      <view class="form-item avatar-item" @click="handleChangeAvatar">
        <text class="label">头像</text>
        <view class="avatar-wrapper">
          <view v-if="userInfo?.avatar" class="avatar">
            <image :src="userInfo.avatar" mode="aspectFill"></image>
          </view>
          <view v-else class="avatar text-avatar">
            <text>{{ getFirstChar(userInfo?.name) }}</text>
          </view>
          <view class="edit-badge">
            <u-icon name="camera-fill" color="#ffffff" size="24"></u-icon>
          </view>
        </view>
      </view>

      <!-- 基本信息 -->
      <view class="section-title">
        <u-icon name="account-fill" color="#ff9500" size="32"></u-icon>
        <text>基本信息</text>
      </view>
      <view class="form-item">
        <text class="label">姓名</text>
        <text class="value">{{ userInfo?.name || '--' }}</text>
      </view>
      <view class="form-item">
        <text class="label">学号</text>
        <text class="value">{{ userInfo?.studentNo || '--' }}</text>
      </view>
      <view class="form-item">
        <text class="label">性别</text>
        <text class="value">{{ userInfo?.gender === 1 ? '男' : '女' }}</text>
      </view>
      <view class="form-item">
        <text class="label">邮箱</text>
        <text class="value">{{ userInfo?.email || '--' }}</text>
      </view>

      <!-- 学籍信息 -->
      <view class="section-title">
        <u-icon name="bookmark-fill" color="#ff9500" size="32"></u-icon>
        <text>学籍信息</text>
      </view>
      <view class="form-item">
        <text class="label">院系</text>
        <text class="value">{{ userInfo?.department || '--' }}</text>
      </view>
      <view class="form-item">
        <text class="label">专业</text>
        <text class="value">{{ userInfo?.major || '--' }}</text>
      </view>
      <view class="form-item">
        <text class="label">年级</text>
        <text class="value">{{ userInfo?.grade || '--' }}</text>
      </view>
      <view class="form-item">
        <text class="label">班级</text>
        <text class="value">{{ userInfo?.className || '--' }}</text>
      </view>
      <view class="form-item">
        <text class="label">入学年份</text>
        <text class="value">{{ userInfo?.enrollmentYear || '--' }}</text>
      </view>

      <!-- 账号安全 -->
      <view class="section-title">
        <u-icon name="lock-fill" color="#ff9500" size="32"></u-icon>
        <text>账号安全</text>
      </view>
      <view class="form-item password-item" @click="handleChangePassword">
        <view class="password-info">
          <text class="label">修改密码</text>
          <text class="desc">定期修改密码更安全</text>
        </view>
        <u-icon name="arrow-right" color="#999" size="30"></u-icon>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userApi } from '@/api/user'
import { uploadApi } from '@/api/upload'

// 状态栏高度
const statusBarHeight = ref(20)

// 用户信息
const userInfo = ref(null)

// 获取系统信息
const getSystemInfo = () => {
  try {
    const info = uni.getSystemInfoSync()
    statusBarHeight.value = info.statusBarHeight || 20
  } catch (e) {
    console.error('获取系统信息失败', e)
  }
}

// 获取用户名第一个字
const getFirstChar = (name) => {
  if (!name) return ''
  return name.charAt(0)
}

// 获取用户信息
const getUserInfo = async () => {
  try {
    const res = await userApi.getCurrentUser()
    if (res.code === 200) {
      userInfo.value = res.data
    } else {
      uni.showToast({
        title: '获取用户信息失败',
        icon: 'none'
      })
    }
  } catch (error) {
    uni.showToast({
      title: '获取用户信息失败',
      icon: 'none'
    })
  }
}

// 返回上一页
const handleBack = () => {
  uni.navigateBack()
}

// 修改头像
const handleChangeAvatar = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: async (res) => {
      const tempFilePath = res.tempFilePaths[0]
      try {
        uni.showLoading({ title: '上传中...' })
        
        // 使用uploadApi上传文件
        const fileUrl = await uploadApi.upload(tempFilePath)
        console.log(fileUrl)
        
        // 更新头像URL
        const res = await userApi.updateAvatar(fileUrl)
        if (res.code === 200) {
          await getUserInfo() // 刷新用户信息
          uni.showToast({
            title: '更新成功',
            icon: 'success'
          })
        } else {
          throw new Error(res.message)
        }
        
        uni.hideLoading()
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: error.message || '更新失败',
          icon: 'none'
        })
      }
    }
  })
}

// 修改密码
const handleChangePassword = () => {
  uni.navigateTo({
    url: '/pages/mine/change-password/index'
  })
}

onMounted(() => {
  getSystemInfo()
  getUserInfo()
})
</script>

<style lang="scss" scoped>
.profile-container {
  min-height: 100vh;
  background-color: #f7f8fa;
  padding-bottom: 40rpx;
}

.status-bar {
  width: 100%;
  background: #ffffff;
}

.nav-bar {
  background: #ffffff;
  height: 88rpx;
  display: flex;
  align-items: center;
  padding: 0 30rpx;
  position: relative;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
  
  .nav-left {
    padding: 20rpx;
    margin-left: -20rpx;
  }
  
  .nav-title {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    font-size: 34rpx;
    font-weight: 500;
    color: #333;
  }
}

.profile-form {
  margin-top: 20rpx;
  
  .section-title {
    display: flex;
    align-items: center;
    gap: 12rpx;
    padding: 30rpx;
    font-size: 32rpx;
    font-weight: 600;
    color: #333;
    background: #ffffff;
    margin-top: 20rpx;
    border-bottom: 1rpx solid #f5f5f5;
  }
  
  .form-item {
    background: #ffffff;
    padding: 30rpx;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1rpx solid #f5f5f5;
    
    &.avatar-item {
      padding: 40rpx 30rpx;
      
      .avatar-wrapper {
        position: relative;
        
        .avatar {
          width: 120rpx;
          height: 120rpx;
          border-radius: 60rpx;
          border: 4rpx solid #ff9500;
          overflow: hidden;
          box-shadow: 0 4rpx 12rpx rgba(255, 149, 0, 0.2);
          
          image {
            width: 100%;
            height: 100%;
          }
          
          &.text-avatar {
            background: linear-gradient(135deg, #ff9500, #ff7d00);
            display: flex;
            align-items: center;
            justify-content: center;
            
            text {
              font-size: 48rpx;
              font-weight: 600;
              color: #ffffff;
            }
          }
        }
        
        .edit-badge {
          position: absolute;
          right: -6rpx;
          bottom: -6rpx;
          width: 48rpx;
          height: 48rpx;
          border-radius: 24rpx;
          background: #ff9500;
          display: flex;
          align-items: center;
          justify-content: center;
          box-shadow: 0 4rpx 12rpx rgba(255, 149, 0, 0.3);
        }
      }
    }
    
    &.password-item {
      .password-info {
        display: flex;
        flex-direction: column;
        gap: 6rpx;
        
        .desc {
          font-size: 24rpx;
          color: #999;
        }
      }
    }
    
    .label {
      font-size: 30rpx;
      color: #333;
    }
    
    .value {
      font-size: 30rpx;
      color: #666;
      max-width: 60%;
      text-align: right;
    }
  }
}
</style> 