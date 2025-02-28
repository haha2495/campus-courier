/**
 * 纠纷类型枚举
 */
export const DisputeTypeEnum = {
  // 状态码
  DELIVERY_MISMATCH: 1,
  BAD_ATTITUDE: 2,
  GOODS_DAMAGED: 3,
  DELIVERY_DELAY: 4,
  WRONG_DELIVERY: 5,
  LOST_GOODS: 6,
  PRICE_DISPUTE: 7,
  SAFETY_ISSUE: 8,
  UNPROFESSIONAL: 9,
  OTHER: 10,

  // 获取描述
  getDesc(type) {
    const typeMap = {
      [this.DELIVERY_MISMATCH]: '送达与实际不符',
      [this.BAD_ATTITUDE]: '态度恶劣',
      [this.GOODS_DAMAGED]: '商品损坏',
      [this.DELIVERY_DELAY]: '送达延迟',
      [this.WRONG_DELIVERY]: '送错商品',
      [this.LOST_GOODS]: '商品丢失',
      [this.PRICE_DISPUTE]: '价格争议',
      [this.SAFETY_ISSUE]: '安全问题',
      [this.UNPROFESSIONAL]: '不专业行为',
      [this.OTHER]: '其他'
    }
    return typeMap[type] || '未知'
  },

  // 获取类型样式
  getType(type) {
    const typeMap = {
      [this.DELIVERY_MISMATCH]: 'warning',
      [this.BAD_ATTITUDE]: 'danger',
      [this.GOODS_DAMAGED]: 'danger',
      [this.DELIVERY_DELAY]: 'warning',
      [this.WRONG_DELIVERY]: 'warning',
      [this.LOST_GOODS]: 'danger',
      [this.PRICE_DISPUTE]: 'warning',
      [this.SAFETY_ISSUE]: 'danger',
      [this.UNPROFESSIONAL]: 'warning',
      [this.OTHER]: 'info'
    }
    return typeMap[type] || 'info'
  },

  // 获取图标
  getIcon(type) {
    const iconMap = {
      [this.DELIVERY_MISMATCH]: 'Warning',
      [this.BAD_ATTITUDE]: 'ChatDotRound',
      [this.GOODS_DAMAGED]: 'Box',
      [this.DELIVERY_DELAY]: 'Timer',
      [this.WRONG_DELIVERY]: 'SwitchButton',
      [this.LOST_GOODS]: 'Failed',
      [this.PRICE_DISPUTE]: 'Money',
      [this.SAFETY_ISSUE]: 'AlarmClock',
      [this.UNPROFESSIONAL]: 'User',
      [this.OTHER]: 'More'
    }
    return iconMap[type] || 'Info'
  },

  // 获取所有类型选项
  getOptions() {
    return [
      { value: null, label: '全部', icon: 'List' },
      { value: this.DELIVERY_MISMATCH, label: this.getDesc(this.DELIVERY_MISMATCH), icon: this.getIcon(this.DELIVERY_MISMATCH) },
      { value: this.BAD_ATTITUDE, label: this.getDesc(this.BAD_ATTITUDE), icon: this.getIcon(this.BAD_ATTITUDE) },
      { value: this.GOODS_DAMAGED, label: this.getDesc(this.GOODS_DAMAGED), icon: this.getIcon(this.GOODS_DAMAGED) },
      { value: this.DELIVERY_DELAY, label: this.getDesc(this.DELIVERY_DELAY), icon: this.getIcon(this.DELIVERY_DELAY) },
      { value: this.WRONG_DELIVERY, label: this.getDesc(this.WRONG_DELIVERY), icon: this.getIcon(this.WRONG_DELIVERY) },
      { value: this.LOST_GOODS, label: this.getDesc(this.LOST_GOODS), icon: this.getIcon(this.LOST_GOODS) },
      { value: this.PRICE_DISPUTE, label: this.getDesc(this.PRICE_DISPUTE), icon: this.getIcon(this.PRICE_DISPUTE) },
      { value: this.SAFETY_ISSUE, label: this.getDesc(this.SAFETY_ISSUE), icon: this.getIcon(this.SAFETY_ISSUE) },
      { value: this.UNPROFESSIONAL, label: this.getDesc(this.UNPROFESSIONAL), icon: this.getIcon(this.UNPROFESSIONAL) },
      { value: this.OTHER, label: this.getDesc(this.OTHER), icon: this.getIcon(this.OTHER) }
    ]
  }
} 