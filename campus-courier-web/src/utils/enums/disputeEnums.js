/**
 * 纠纷状态枚举
 */
export const DisputeStatusEnum = {
  // 状态码
  PENDING: 0,
  PROCESSING: 1,
  FINISHED: 2,
  PUBLISHED: 3,

  // 状态描述
  getDesc(status) {
    const statusMap = {
      [this.PENDING]: '待处理',
      [this.PROCESSING]: '处理中',
      [this.FINISHED]: '已结束',
      [this.PUBLISHED]: '已发布纠纷大厅'
    }
    return statusMap[status] || '未知'
  },

  // 状态类型
  getType(status) {
    const typeMap = {
      [this.PENDING]: 'warning',
      [this.PROCESSING]: 'primary',
      [this.FINISHED]: 'success',
      [this.PUBLISHED]: 'info'
    }
    return typeMap[status] || 'info'
  },

  // 状态图标
  getIcon(status) {
    const iconMap = {
      [this.PENDING]: 'Warning',
      [this.PROCESSING]: 'Loading',
      [this.FINISHED]: 'CircleCheck',
      [this.PUBLISHED]: 'Share'
    }
    return iconMap[status] || 'Info'
  },

  // 获取所有状态选项
  getOptions() {
    return [
      { value: null, label: '全部', icon: 'List' },
      { value: this.PENDING, label: this.getDesc(this.PENDING), icon: this.getIcon(this.PENDING) },
      { value: this.PROCESSING, label: this.getDesc(this.PROCESSING), icon: this.getIcon(this.PROCESSING) },
      { value: this.FINISHED, label: this.getDesc(this.FINISHED), icon: this.getIcon(this.FINISHED) },
      { value: this.PUBLISHED, label: this.getDesc(this.PUBLISHED), icon: this.getIcon(this.PUBLISHED) }
    ]
  }
} 