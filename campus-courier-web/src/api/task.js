import request from '@/utils/request'

/**
 * 获取任务详情
 * @param {string | LocationQueryValue[]} taskId - 任务ID
 * @returns {Promise<TaskDetailDTO>} 任务详情
 */
export function getTaskDetail(taskId) {
  return request({
    url: `/tasks/${taskId}`,
    method: 'get'
  })
}

/**
 * 管理员分页查询任务列表
 * @param {Object} params 查询参数
 * @returns {Promise} 返回Promise对象
 */
export function getTaskList(params) {
  return request({
    url: '/tasks/page',
    method: 'post',
    data: params
  })
}

// ... 其他任务相关接口 ... 