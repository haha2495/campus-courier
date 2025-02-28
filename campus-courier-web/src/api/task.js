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

// ... 其他任务相关接口 ... 