export const DisputeTypeEnum = {
  DELIVERY_MISMATCH: { code: 1, desc: '送达与实际不符', icon: 'error-circle' },
  BAD_ATTITUDE: { code: 2, desc: '态度恶劣', icon: 'thumb-down' },
  GOODS_DAMAGED: { code: 3, desc: '商品损坏', icon: 'warning' },
  DELIVERY_DELAY: { code: 4, desc: '送达延迟', icon: 'clock' },
  WRONG_DELIVERY: { code: 5, desc: '送错商品', icon: 'shopping-cart' },
  LOST_GOODS: { code: 6, desc: '商品丢失', icon: 'question-circle' },
  PRICE_DISPUTE: { code: 7, desc: '价格争议', icon: 'rmb-circle' },
  SAFETY_ISSUE: { code: 8, desc: '安全问题', icon: 'info-circle' },
  UNPROFESSIONAL: { code: 9, desc: '不专业行为', icon: 'close-circle' },
  OTHER: { code: 10, desc: '其他', icon: 'more-circle' }
} 