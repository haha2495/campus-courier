<template>
  <div class="side-menu-layout">
    <!-- 侧边栏 -->
    <aside class="side-menu" :class="{ collapsed: isCollapsed }">
      <div class="logo">
        <el-icon>
          <School/>
        </el-icon>
        <span v-show="!isCollapsed">高校跑腿管理系统</span>
      </div>

      <el-menu
          :default-active="activeMenu"
          class="side-menu-list"
          :collapse="isCollapsed"
          router
          :background-color="'transparent'"
          :text-color="'#4b5563'"
          :active-text-color="'#2563eb'"
      >
        <!-- 管理员菜单 -->
        <template v-if="userInfo.role === 'admin'">
          <el-menu-item index="/admin/home">
            <el-icon>
              <HomeFilled/>
            </el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/admin/admins">
            <el-icon>
              <UserFilled/>
            </el-icon>
            <span>管理员管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/students">
            <el-icon>
              <User/>
            </el-icon>
            <span>学生管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/task-types">
            <el-icon>
              <List/>
            </el-icon>
            <span>任务类型管理</span>
          </el-menu-item>
        </template>
      </el-menu>

      <div class="collapse-trigger" @click="toggleCollapse">
        <el-icon>
          <component :is="isCollapsed ? 'Expand' : 'Fold'"/>
        </el-icon>
      </div>
    </aside>

    <!-- 主要内容区 -->
    <div class="main-container">
      <!-- 顶部导航栏 -->
      <header class="top-header">
        <div class="breadcrumb">
          <el-breadcrumb>
            <el-breadcrumb-item :to="{ path: `/${userInfo.role}/home` }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ getBreadcrumbTitle() }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="user-info">
          <el-dropdown>
            <span class="user-dropdown">
              <el-avatar
                  :size="32"
                  :src="adminInfo.avatar"
                  :class="{ 'no-avatar': !adminInfo.avatar }"
              >
                {{ adminInfo.nickname?.charAt(0)?.toUpperCase() || adminInfo.username?.charAt(0)?.toUpperCase() }}
              </el-avatar>
              <span class="username">{{ adminInfo.nickname || adminInfo.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleProfile">个人信息</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 内容区域 -->
      <main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" :key="route.path"/>
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {HomeFilled, School, User, UserFilled, List} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {adminLogout, getCurrentAdmin} from '@/api/admin'
import {getCurrentStudent, studentLogout} from '@/api/student'
import emitter from '@/utils/eventBus'

const router = useRouter()
const route = useRoute()
const activeMenu = ref(route.path)
const isCollapsed = ref(false)
const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
const adminInfo = ref({})

// 获取面包屑标题
const getBreadcrumbTitle = () => {
  const path = route.path
  const menuTitles = {
    '/admin/home': '首页',
    '/admin/students': '学生管理',
    '/admin/admins': '管理员管理',
    '/admin/task-type': '任务类型管理',
    '/admin/task': '任务管理',
    '/admin/dispute': '纠纷管理',
    '/admin/dispute-type': '纠纷类型管理',
  }
  return menuTitles[path] || '当前页面'
}

// 切换菜单折叠状态
const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value
}

// 处理个人信息
const handleProfile = () => {
  const role = userInfo.value.role
  let path = ''

  switch (role) {
    case 'admin':
      path = '/admin/profile'
      break
    case 'president':
    case 'vice_president':
      path = '/club/profile'
      break
    default:
      path = '/student/profile'
  }

  router.push(path)
}

// 处理退出登录
const handleLogout = async () => {
  try {
    // 如果是管理员，调用退出登录接口
    if (userInfo.value.role === 'admin') {
      await adminLogout()
    } else {
      await studentLogout();
    }

    // 清除本地存储的信息
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')

    // 跳转到登录页
    router.push('/login')
    ElMessage.success('已退出登录')
  } catch (error) {
    ElMessage.error('退出登录失败：' + (error.message || '未知错误'))
  }
}

// 获取管理员信息
const loadAdminInfo = async () => {
  try {
    // 获取用户信息
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (userInfo.role === 'admin') {
      const res = await getCurrentAdmin()
      adminInfo.value = res
    } else if (userInfo.role === 'president') {
      const res = await getCurrentStudent()
      adminInfo.value = {
        nickname: res.name,
        avatar: res.avatar
      }
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 监听路由变化
watch(() => route.path, (newPath) => {
  activeMenu.value = newPath
})

// 监听个人信息更新事件
emitter.on('admin-profile-updated', () => {
  loadAdminInfo()
})

// 监听学生个人信息更新事件
emitter.on('student-profile-updated', () => {
  loadAdminInfo()
})

// 组件卸载时移除事件监听
onUnmounted(() => {
  emitter.off('admin-profile-updated')
  emitter.off('student-profile-updated')
})

onMounted(() => {
  activeMenu.value = route.path
  loadAdminInfo() // 初始加载管理员信息
})
</script>

<style scoped lang="scss">
.side-menu-layout {
  min-height: 100vh;
  display: flex;
  position: relative;
  overflow: hidden;
}

.side-menu {
  width: 240px;
  height: 100vh;
  background: white;
  position: fixed;
  left: 0;
  top: 0;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  z-index: 1000;
  will-change: width;
  border-right: 1px solid #f0f0f0;

  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    padding: 0 20px;
    background: white;
    border-bottom: 1px solid #f0f0f0;
    overflow: hidden;
    position: relative;

    .el-icon {
      font-size: 24px;
      position: relative;
      z-index: 2;
      color: #1a1a1a;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }

    span {
      font-size: 16px;
      font-weight: 600;
      margin-left: 12px;
      white-space: nowrap;
      opacity: 1;
      position: relative;
      z-index: 1;
      transform: translateX(0);
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      color: #1a1a1a;
    }
  }

  &.collapsed {
    width: 64px;

    .logo {
      padding: 0;
      justify-content: center;

      .el-icon {
        transform: scale(1.1);
      }

      span {
        opacity: 0;
        transform: translateX(-20px);
      }
    }

    .side-menu-list {
      padding: 8px 0;

      :deep(.el-menu-item) {
        padding: 0;
        margin: 4px auto;
        width: 50px;
        justify-content: center;

        .el-icon {
          margin: 0;
          font-size: 20px;
          transform: translateX(0);
        }

        span {
          opacity: 0;
          transform: translateX(-20px);
        }
      }
    }
  }

  .side-menu-list {
    flex: 1;
    border: none;
    padding: 8px;
    transition: padding 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    background: transparent;

    :deep(.el-menu) {
      background: transparent;
      border: none;
    }

    :deep(.el-menu-item) {
      height: 40px;
      line-height: 40px;
      border-radius: 6px;
      margin: 4px 0;
      padding: 0 16px;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      overflow: hidden;
      color: #4b5563;
      background: transparent;
      font-size: 14px;

      .el-icon {
        font-size: 16px;
        min-width: 16px;
        margin-right: 12px;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        color: #4b5563;
      }

      span {
        opacity: 1;
        transform: translateX(0);
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        color: #4b5563;
      }

      &:hover {
        background: #f3f4f6;
        color: #1a1a1a;

        .el-icon {
          color: #1a1a1a;
          transform: translateX(4px);
        }

        span {
          color: #1a1a1a;
        }
      }

      &.is-active {
        background: #f0f7ff;
        color: #2563eb;
        font-weight: 500;

        .el-icon, span {
          color: #2563eb;
        }

        &:hover {
          background: #e6f0ff;
        }
      }
    }
  }

  .collapse-trigger {
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #4b5563;
    cursor: pointer;
    transition: all 0.3s;
    border-top: 1px solid #f0f0f0;
    background: white;

    &:hover {
      background: #f3f4f6;
      color: #1a1a1a;

      .el-icon {
        transform: scale(1.1);
      }
    }

    .el-icon {
      transition: transform 0.3s;
    }
  }
}

.main-container {
  flex: 1;
  margin-left: 240px;
  min-height: 100vh;
  background: #f8fafc;
  transition: margin-left 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .top-header {
    height: 60px;
    background: white;
    border-bottom: 1px solid #f0f0f0;
    padding: 0 24px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-shrink: 0;
  }

  .main-content {
    flex: 1;
    overflow: hidden;
    position: relative;
    height: calc(100vh - 60px);
  }
}

.side-menu.collapsed + .main-container {
  margin-left: 64px;
}

.top-header {
  height: 64px;
  background: white;
  padding: 0 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03), 0 1px 6px -1px rgba(0, 0, 0, 0.02);
  position: relative;
  z-index: 100;

  .breadcrumb {
    display: flex;
    align-items: center;
    gap: 8px;

    :deep(.el-breadcrumb__item) {
      display: flex;
      align-items: center;

      .el-breadcrumb__inner {
        color: #4b5563;
        font-weight: 500;
        padding: 4px 8px;
        border-radius: 4px;
        transition: all 0.3s;

        &.is-link {
          cursor: pointer;

          &:hover {
            color: #4f46e5;
            background: rgba(79, 70, 229, 0.1);
          }
        }
      }

      .el-breadcrumb__separator {
        color: #9ca3af;
        margin: 0 4px;
      }

      &:last-child {
        .el-breadcrumb__inner {
          color: #111827;
          font-weight: 600;
          background: #f3f4f6;
        }
      }
    }
  }

  .user-info {
    .user-dropdown {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
      padding: 4px 8px;
      border-radius: 6px;
      transition: all 0.3s;

      &:hover {
        background: #f3f4f6;
      }

      .username {
        font-size: 14px;
        color: #4b5563;
      }
    }
  }
}

.main-content {
  flex: 1;
  padding: 12px;
  width: 100%;
  position: relative;
  display: flex;
  flex-direction: column;

  :deep(> *) {
    width: 100%;
    min-width: 0;
    flex: 1;
  }
}

/* 路由切换动画 */
.fade-transform-enter-active {
  animation: fade-in 0.3s ease-out;
}

.fade-transform-leave-active {
  animation: fade-out 0.3s ease-in;
}

@keyframes fade-in {
  0% {
    opacity: 0;
    transform: translateX(20px);
  }
  100% {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fade-out {
  0% {
    opacity: 1;
    transform: translateX(0);
  }
  100% {
    opacity: 0;
    transform: translateX(-20px);
  }
}
</style> 