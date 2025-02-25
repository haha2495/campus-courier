import request from '@/utils/request'

/**
 * 新增任务类型
 * @param {Object} data - 任务类型数据
 * @returns {Promise}
 */
export function addTaskType(data) {
    return request({
        url: '/task/types/add',
        method: 'post',
        data
    })
}

/**
 * 分页查询任务类型
 * @param {Object} data - 查询参数，包含pageNum、pageSize和可选的status
 * @returns {Promise}
 */
export function pageTaskTypes(data) {
    return request({
        url: '/task/types/page',
        method: 'post',
        data
    })
}

/**
 * 启用任务类型
 * @param {Number} id - 任务类型ID
 * @returns {Promise}
 */
export function enableTaskType(id) {
    return request({
        url: `/task/types/enable/${id}`,
        method: 'post'
    })
}

/**
 * 禁用任务类型
 * @param {Number} id - 任务类型ID
 * @returns {Promise}
 */
export function disableTaskType(id) {
    return request({
        url: `/task/types/disable/${id}`,
        method: 'post'
    })
}

/**
 * 获取任务类型详情
 * @param {string | LocationQueryValue[]} id - 任务类型ID
 * @returns {Promise}
 */
export function getTaskTypeDetail(id) {
    return request({
        url: `/task/types/detail/${id}`,
        method: 'get'
    })
}

