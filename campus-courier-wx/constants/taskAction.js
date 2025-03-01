/**
 * 任务操作枚举
 */
export const TASK_ACTION = {
  PUBLISH: 1,        // 发布任务
  ACCEPT: 2,         // 接受任务
  CANCEL: 3,         // 取消任务
  START: 4,          // 开始执行
  DELIVER: 5,       // 完成任务
  CONFIRM: 6,        // 确认完成
  SUBMIT_DISPUTE: 7, // 提交纠纷
  RESOLVE_DISPUTE: 8, // 解决纠纷
  EVALUATE: 9        // 评价
}

/**
 * 获取操作类型对应的文本
 * @param {number} action 操作类型
 * @returns {string} 操作文本
 */
export const getActionText = (action) => {
  const actionMap = {
    [TASK_ACTION.PUBLISH]: '发布任务',
    [TASK_ACTION.ACCEPT]: '接受任务',
    [TASK_ACTION.CANCEL]: '取消任务',
    [TASK_ACTION.START]: '开始执行',
    [TASK_ACTION.DELIVER]: '完成任务',
    [TASK_ACTION.CONFIRM]: '确认完成',
    [TASK_ACTION.SUBMIT_DISPUTE]: '提交纠纷',
    [TASK_ACTION.RESOLVE_DISPUTE]: '解决纠纷',
    [TASK_ACTION.EVALUATE]: '评价任务'
  }
  return actionMap[action] || '未知操作'
}

/**
 * 获取操作类型对应的图标
 * @param {number} action 操作类型
 * @returns {string} 图标名称
 */
export const getActionIcon = (action) => {
  const iconMap = {
    [TASK_ACTION.PUBLISH]: 'plus-circle-fill',         // 发布
    [TASK_ACTION.ACCEPT]: 'checkmark-circle-fill',     // 接单
    [TASK_ACTION.CANCEL]: 'close-circle-fill',         // 取消
    [TASK_ACTION.START]: 'play-circle-fill',           // 开始
    [TASK_ACTION.DELIVER]: 'car-fill',                 // 送达 - 改用送货车图标
    [TASK_ACTION.CONFIRM]: 'star-fill',                // 确认
    [TASK_ACTION.SUBMIT_DISPUTE]: 'error-circle-fill', // 纠纷
    [TASK_ACTION.RESOLVE_DISPUTE]: 'checkmark-circle-fill', // 解决
    [TASK_ACTION.EVALUATE]: 'thumb-up-fill'            // 评价
  }
  return iconMap[action] || 'info-circle-fill'
}

/**
 * 获取操作类型对应的颜色
 * @param {number} action 操作类型
 * @returns {string} 颜色值
 */
export const getActionColor = (action) => {
  const colorMap = {
    [TASK_ACTION.PUBLISH]: '#3B82F6',    // 发布 - 蓝色
    [TASK_ACTION.ACCEPT]: '#10B981',     // 接单 - 绿色
    [TASK_ACTION.CANCEL]: '#EF4444',     // 取消 - 红色
    [TASK_ACTION.START]: '#F59E0B',      // 开始 - 橙色
    [TASK_ACTION.DELIVER]: '#06B6D4',    // 送达 - 青色
    [TASK_ACTION.CONFIRM]: '#6366F1',    // 确认 - 靛蓝色
    [TASK_ACTION.SUBMIT_DISPUTE]: '#F97316', // 纠纷 - 深橙色
    [TASK_ACTION.RESOLVE_DISPUTE]: '#10B981', // 解决 - 绿色
    [TASK_ACTION.EVALUATE]: '#8B5CF6'    // 评价 - 紫色
  }
  return colorMap[action] || '#6B7280'
}

/**
 * 获取操作类型对应的样式类
 * @param {number} action 操作类型
 * @returns {string} 样式类名
 */
export const getActionClass = (action) => {
  const classMap = {
    [TASK_ACTION.PUBLISH]: 'node-publish',
    [TASK_ACTION.ACCEPT]: 'node-accept',
    [TASK_ACTION.CANCEL]: 'node-cancel',
    [TASK_ACTION.START]: 'node-start',
    [TASK_ACTION.DELIVER]: 'node-deliver',  // 修改类名
    [TASK_ACTION.CONFIRM]: 'node-confirm',
    [TASK_ACTION.SUBMIT_DISPUTE]: 'node-dispute',
    [TASK_ACTION.RESOLVE_DISPUTE]: 'node-resolve',
    [TASK_ACTION.EVALUATE]: 'node-evaluate'
  }
  return classMap[action] || 'node-default'
} 