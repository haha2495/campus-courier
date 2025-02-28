import request from '@/utils/request'

/**
 * 余额相关接口
 */
export default {
  /**
   * 充值
   * @param {number} amount - 充值金额
   */
  recharge(amount) {
    return request({
      url: '/api/student/recharge',
      method: 'post',
      data: {
        amount
      }
    })
  }
} 