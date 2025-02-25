/**
 * 导航栏配置文件
 * 包含发布者和跑腿者的导航栏配置
 */

// 图标版本，更新图标时修改此版本号可强制刷新缓存
export const ICON_VERSION = '1.0.0';

// 验证图标URL是否有效
export const validateIconUrl = (url) => {
  if (!url) return false;
  return url.startsWith('http') || url.startsWith('https');
}

// 获取带版本的图标URL，用于缓存键
export const getVersionedIconUrl = (url) => {
  if (!validateIconUrl(url)) return url;
  
  // 如果URL已经有参数，添加版本参数
  if (url.includes('?')) {
    return `${url}&v=${ICON_VERSION}`;
  }
  
  // 如果URL没有参数，添加版本参数
  return `${url}?v=${ICON_VERSION}`;
}

// 导航栏类型枚举
export const TabBarType = {
  PUBLISHER: 'publisher',
  RUNNER: 'runner'
}

// 导航栏页面名称枚举
export const TabPageName = {
  HOME: 'home',
  TASKS: 'tasks',
  PUBLISH: 'publish',
  DISPUTES: 'disputes',
  MINE: 'mine',
  CURRENT: 'current',
  ORDERS: 'orders',
  RECORDS: 'records'
}

// 发布者导航栏配置
export const publisherTabConfig = [
  { 
    name: TabPageName.HOME, 
    text: '首页', 
    icon: 'https://cdn-icons-png.flaticon.com/128/1946/1946488.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/1946/1946436.png' 
  },
  { 
    name: TabPageName.TASKS, 
    text: '我的任务', 
    icon: 'https://cdn-icons-png.flaticon.com/128/2098/2098561.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/2355/2355330.png' 
  },
  { 
    name: TabPageName.PUBLISH, 
    text: '发布任务', 
    icon: 'https://cdn-icons-png.flaticon.com/128/4315/4315445.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/4315/4315445.png' 
  },
  { 
    name: TabPageName.DISPUTES, 
    text: '纠纷大厅', 
    icon: 'https://cdn-icons-png.flaticon.com/128/1585/1585464.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/1585/1585527.png' 
  },
  { 
    name: TabPageName.MINE, 
    text: '我的', 
    icon: 'https://cdn-icons-png.flaticon.com/128/1077/1077114.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/1077/1077063.png' 
  }
]

// 跑腿者导航栏配置
export const runnerTabConfig = [
  { 
    name: TabPageName.HOME, 
    text: '首页', 
    icon: 'https://cdn-icons-png.flaticon.com/128/1946/1946488.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/1946/1946436.png' 
  },
  { 
    name: TabPageName.CURRENT, 
    text: '当前任务', 
    icon: 'https://cdn-icons-png.flaticon.com/128/2838/2838779.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/2838/2838846.png' 
  },
  { 
    name: TabPageName.ORDERS, 
    text: '抢单大厅', 
    icon: 'https://cdn-icons-png.flaticon.com/128/2645/2645233.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/2645/2645233.png' 
  },
  { 
    name: TabPageName.RECORDS, 
    text: '我的接单', 
    icon: 'https://cdn-icons-png.flaticon.com/128/2098/2098561.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/2355/2355330.png' 
  },
  { 
    name: TabPageName.MINE, 
    text: '我的', 
    icon: 'https://cdn-icons-png.flaticon.com/128/1077/1077114.png', 
    activeIcon: 'https://cdn-icons-png.flaticon.com/128/1077/1077063.png' 
  }
]

// 获取中间按钮信息
export const getCenterButton = (type) => {
  if (type === TabBarType.PUBLISHER) {
    return {
      name: TabPageName.PUBLISH,
      text: '发布任务',
      icon: 'https://cdn-icons-png.flaticon.com/128/4315/4315445.png'
    }
  } else {
    return {
      name: TabPageName.ORDERS,
      text: '抢单大厅',
      icon: 'https://cdn-icons-png.flaticon.com/128/2645/2645233.png'
    }
  }
}

// 根据角色获取配置
export const getTabConfig = (type) => {
  return type === TabBarType.PUBLISHER ? publisherTabConfig : runnerTabConfig
}

export default {
  TabBarType,
  TabPageName,
  publisherTabConfig,
  runnerTabConfig,
  getCenterButton,
  getTabConfig
} 