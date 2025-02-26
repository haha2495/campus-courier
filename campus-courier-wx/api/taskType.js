import {http} from '../utils/request'

/**
 * 任务类型相关API
 */

/**
 * 获取所有已启用的任务类型
 * @returns {Promise} 返回请求Promise对象
 */
export function getEnabledTaskTypes() {
    return http({
        url: '/api/task/types/enabled',
        method: 'GET'
    })
}
