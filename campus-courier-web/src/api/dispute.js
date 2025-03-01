import request from '@/utils/request'

/**
 * 创建纠纷
 * @param {Object} data 纠纷信息
 * @returns {Promise}
 */
export function createDispute(data) {
  return request({
    url: '/api/dispute/create',
    method: 'post',
    data
  })
}

/**
 * 分页查询纠纷任务
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function pageDisputes(params) {
  return request({
    url: '/api/dispute/page',
    method: 'get',
    params
  })
}

/**
 * 处理纠纷
 * @param {Object} data 处理信息
 * @param {number} data.disputeId 纠纷ID
 * @param {number} data.handleResult 处理结果：1-发布者胜，2-跑腿者胜，3-存在疑问
 * @param {string} data.handleRemark 处理说明
 * @param {number} data.displayHours 展示时长（小时）
 * @returns {Promise}
 */
export function handleDispute(data) {
  return request({
    url: '/dispute/handle',
    method: 'post',
    data
  })
}

/**
 * 获取纠纷详情
 * @param id 纠纷ID
 * @returns {Promise<any>} 纠纷详情
 */
export function getDisputeDetail(id) {
  return request({
    url: `/dispute/${id}`,
    method: 'get'
  })
} 