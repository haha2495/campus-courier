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
 * 处理纠纷
 * @param {Object} data - 纠纷处理数据
 * @param {string} data.disputeId - 纠纷ID
 * @param {string} data.taskId - 任务ID
 * @param {string} data.winner - 胜诉方ID
 * @param {string} data.remark - 处理备注
 * @returns {Promise} 返回处理结果
 */
export function handleDispute(data) {
  return request({
    url: '/api/dispute/handle',
    method: 'post',
    data
  })
}

/**
 * 获取纠纷大厅列表
 * @returns {Promise} 返回纠纷列表
 */
export function getHallDisputes() {
  return request({
    url: '/api/dispute/hall/list',
    method: 'get'
  })
}

/**
 * 获取纠纷详情
 * @param {string} disputeId - 纠纷ID
 * @returns {Promise} 返回纠纷详情
 */
export function getDisputeDetail(disputeId) {
  return request({
    url: `/api/dispute/${disputeId}`,
    method: 'get'
  })
}

/**
 * 支持纠纷
 * @param {Object} data - 纠纷支持数据
 * @param {string} data.disputeId - 纠纷ID
 * @param {string} data.taskId - 任务ID
 * @param {string} data.supportedUserId - 支持的用户ID
 * @param {string} data.remark - 支持理由
 * @returns {Promise} 返回支持结果
 */
export function supportDispute(data) {
  return request({
    url: '/api/dispute/support',
    method: 'post',
    data
  })
} 