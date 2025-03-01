import {http} from '../utils/request'

/**
 * 评价相关接口
 */

/**
 * 获取收到的评价列表
 *
 * @returns {Promise} 评价列表数据
 */
export function getReceivedEvaluations() {
    return http({
        url: '/api/task-evaluations/received',
        method: 'GET'
    })
}
