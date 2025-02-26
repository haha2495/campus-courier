import {http} from '../utils/request'

/**
 * 任务相关API
 */

/**
 * 发布任务
 * @param {Object} data 任务数据
 * @returns {Promise} 返回请求Promise对象
 */
export function publishTask(data) {
    return http({
        url: '/api/tasks/publish',
        method: 'POST',
        data
    })
}
