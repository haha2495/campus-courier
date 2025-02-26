import request from '../utils/request'

/**
 * 任务类型相关API
 */

/**
 * 获取所有已启用的任务类型
 * @returns {Promise} 返回请求Promise对象
 */
export function getEnabledTaskTypes() {
    return request({
        url: '/api/task/types/enabled',
        method: 'GET'
    })
}

/**
 * 根据ID获取任务类型详情
 * @param {Number} id 任务类型ID
 * @returns {Promise} 返回请求Promise对象
 */
export function getTaskTypeDetail(id) {
    return request({
        url: `/api/task/types/detail/${id}`,
        method: 'GET'
    })
} 