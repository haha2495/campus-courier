/**
 * 任务状态枚举
 * 对应后端 TaskStatusEnum
 */
export const TaskStatusEnum = {
  // 状态码
  PENDING: 0,               // 待接单
  ACCEPTED: 1,              // 已接单
  IN_PROGRESS: 2,           // 进行中
  AWAITING_CONFIRMATION: 3, // 待确认
  COMPLETED: 4,             // 已完成
  CANCELED: 5,              // 已取消
  EXPIRED: 6,               // 已过期
  DISPUTED: 7,              // 存在纠纷
  EVALUATED: 8,             // 已评价

  // 获取状态描述
  getDesc(status) {
    const statusMap = {
      0: '待接单',
      1: '已接单',
      2: '进行中',
      3: '待确认',
      4: '已完成',
      5: '已取消',
      6: '已过期',
      7: '存在纠纷',
      8: '已评价'
    }
    return statusMap[status] || '未知状态'
  },

  // 获取状态类型(用于el-tag的type属性)
  getType(status) {
    const typeMap = {
      0: 'info',      // 待接单 - 灰色
      1: 'warning',   // 已接单 - 橙色
      2: 'primary',   // 进行中 - 蓝色
      3: 'warning',   // 待确认 - 橙色
      4: 'success',   // 已完成 - 绿色
      5: 'info',      // 已取消 - 灰色
      6: 'info',      // 已过期 - 灰色
      7: 'danger',    // 存在纠纷 - 红色
      8: 'success'    // 已评价 - 绿色
    }
    return typeMap[status] || 'info'
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
      7: { text: '#ee0a24', background: '#feecef' },  // 存在纠纷 - 红色
      8: { text: '#10B981', background: '#ECFDF5' }   // 已评价 - 绿色
    }
    return colorMap[status] || { text: '#999999', background: '#f5f5f5' }
  },

  // 获取状态选项(用于筛选)
  getOptions() {
    return [
      { label: '全部', value: null },
      { label: '待接单', value: 0 },
      { label: '已接单', value: 1 },
      { label: '进行中', value: 2 },
      { label: '待确认', value: 3 },
      { label: '已完成', value: 4 },
      { label: '已取消', value: 5 },
      { label: '已过期', value: 6 },
      { label: '存在纠纷', value: 7 },
      { label: '已评价', value: 8 }
    ]
  }
}