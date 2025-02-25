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

