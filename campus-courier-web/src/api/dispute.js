import request from '@/utils/request'

/**
 * 分页查询纠纷列表
 * @param params 查询参数
 * @returns {Promise<any>} 分页结果
 */
export function pageDisputes(params) {
  return request({
    url: '/dispute/page',
    method: 'get',
    params
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