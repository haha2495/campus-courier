import request from '@/utils/request'

/**
 * 纠纷相关接口
 */
export const disputeApi = {
  /**
   * 创建纠纷
   * @param {Object} data - 纠纷创建请求
   * @param {number} data.taskId - 任务ID
   * @param {number} data.type - 纠纷类型
   * @param {string} data.title - 纠纷标题
   * @param {string} data.content - 纠纷内容
   * @param {string[]} data.evidence - 证据图片列表
   * @returns {Promise} 返回创建的纠纷ID
   */
  create: (data) => {
    return request({
      url: '/api/dispute/create',
      method: 'POST',
      data
    })
  }
}

/**
 * 获取纠纷大厅列表
 * @returns {Promise} 纠纷列表
 */
export function getHallDisputes() {
  return request({
    url: '/api/dispute/hall/list',
    method: 'get'
  })
}

/**
 * 获取纠纷详情
 * @param {number} disputeId 纠纷ID
 * @returns {Promise} 纠纷详情
 */
export function getDisputeDetail(disputeId) {
  return request({
    url: `/api/dispute/${disputeId}`,
    method: 'get'
  })
} 