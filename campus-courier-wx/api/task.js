import {http} from '../utils/request'

/**
 * 任务相关接口
 */

/**
 * 获取我发布的任务列表
 * @param {Object} params 查询参数
 * @param {number} params.pageNum 页码
 * @param {number} params.pageSize 每页条数
 * @param {number} [params.status] 任务状态，不传则查询全部
 * @returns {Promise} 返回Promise对象
 */
export const getMyPublishedTasks = (params) => {
    return http({
        url: '/api/tasks/my-published',
        method: 'GET',
        data: params
    })
}

/**
 * 发布任务
 * @param {Object} data 任务数据
 * @returns {Promise} 返回Promise对象
 */
export const publishTask = (data) => {
    return http({
        url: '/api/tasks/publish',
        method: 'POST',
        data
    })
}

/**
 * 取消任务
 * @param {number} taskId 任务ID
 * @returns {Promise} 返回Promise对象
 */
export const cancelTask = (taskId) => {
    return http({
        url: `/api/tasks/${taskId}/cancel`,
        method: 'POST'
    })
}

/**
 * 获取任务详情
 * @param {number} taskId 任务ID
 * @returns {Promise} 返回Promise对象
 */
export const getTaskDetail = (taskId) => {
    return http({
        url: `/api/tasks/${taskId}`,
        method: 'GET'
    })
}

/**
 * 添加任务留言
 * @param {Object} data 留言数据
 * @param {number} data.taskId 任务ID
 * @param {string} data.content 留言内容
 * @param {Array<string>} data.imageUrls 图片URL列表
 */
export function addTaskMessage(data) {
    return http({
        url: '/api/tasks/message',
        method: 'post',
        data
    })
}

/**
 * 获取抢单大厅任务列表
 */
export function getTaskHallList() {
    return http({
        url: '/api/tasks/hall',
        method: 'GET'
    })
}

// 抢单（接受任务）
export function acceptTask(taskId) {
    return http({
        url: '/api/tasks/' + taskId + '/accept',
        method: 'POST'
    })
}

/**
 * 获取我接单的任务列表
 */
export function getMyAcceptedTasks() {
    return http({
        url: '/api/tasks/my-accepted',
        method: 'GET'
    })
}

/**
 * 开始任务
 * @param {number|string} taskId 任务ID
 */
export function startTask(taskId) {
    return http({
        url: '/api/tasks/' + taskId + '/start',
        method: 'POST'
    })
}
