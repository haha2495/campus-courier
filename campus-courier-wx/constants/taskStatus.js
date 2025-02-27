/**
 * 任务状态枚举
 * 对应后端 TaskStatusEnum
 */
export const TASK_STATUS = {
  // 状态码
  PENDING: 0,               // 待接单
  ACCEPTED: 1,              // 已接单
  IN_PROGRESS: 2,           // 进行中
  AWAITING_CONFIRMATION: 3, // 待确认
  COMPLETED: 4,             // 已完成
  CANCELED: 5,              // 已取消
  EXPIRED: 6,               // 已过期
  DISPUTED: 7,              // 存在纠纷
  
  // 获取状态文本
  getStatusText(status) {
    const statusMap = {
      0: '待接单',
      1: '已接单',
      2: '进行中',
      3: '待确认',
      4: '已完成',
      5: '已取消',
      6: '已过期',
      7: '存在纠纷'
    }
    return statusMap[status] || '未知状态'
  },
  
  // 获取状态样式类名
  getStatusClass(status) {
    const statusClassMap = {
      0: 'status-pending',
      1: 'status-accepted',
      2: 'status-ongoing',
      3: 'status-confirming',
      4: 'status-completed',
      5: 'status-cancelled',
      6: 'status-expired',
      7: 'status-dispute'
    }
    return statusClassMap[status] || ''
  },
  
  // 获取状态颜色
  getStatusColor(status) {
    const colorMap = {
      0: { text: '#1989fa', background: '#ecf5ff' },  // 待接单 - 蓝色
      1: { text: '#ff9500', background: '#fff7e6' },  // 已接单 - 橙色
      2: { text: '#07c160', background: '#e8f8f1' },  // 进行中 - 绿色
      3: { text: '#F59E0B', background: '#FEF3C7' },  // 待确认 - 黄色
      4: { text: '#8b5cf6', background: '#f3f0ff' },  // 已完成 - 紫色
      5: { text: '#999999', background: '#f5f5f5' },  // 已取消 - 灰色
      6: { text: '#909399', background: '#f4f4f5' },  // 已过期 - 灰色
      7: { text: '#ee0a24', background: '#feecef' }    // 存在纠纷 - 红色
    }
    return colorMap[status] || { text: '#999999', background: '#f5f5f5' }
  },
  
  // 获取状态选项列表（用于筛选）
  getStatusOptions() {
    return [
      { label: '全部', value: null },
      { label: '待接单', value: 0 },
      { label: '已接单', value: 1 },
      { label: '进行中', value: 2 },
      { label: '待确认', value: 3 },
      { label: '已完成', value: 4 },
      { label: '已取消', value: 5 }
    ]
  }
} 