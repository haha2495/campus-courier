/**
 * 任务操作枚举
 */
export const TASK_ACTION = {
  PUBLISH: 'PUBLISH',   // 发布任务
  ACCEPT: 'ACCEPT',     // 接受任务
  START: 'START',       // 开始任务
  FINISH: 'FINISH',     // 完成任务
  CANCEL: 'CANCEL'      // 取消任务
}

/**
 * 获取操作类型对应的文本
 * @param {string} action 操作类型
 * @returns {string} 操作文本
 */
export const getActionText = (action) => {
  const actionMap = {
    [TASK_ACTION.PUBLISH]: '发布任务',
    [TASK_ACTION.ACCEPT]: '接受任务',
    [TASK_ACTION.START]: '开始执行',
    [TASK_ACTION.FINISH]: '完成任务',
    [TASK_ACTION.CANCEL]: '取消任务'
  }
  return actionMap[action] || action
}

/**
 * 获取操作类型对应的样式类
 * @param {string} action 操作类型
 * @returns {string} 样式类名
 */
export const getActionClass = (action) => {
  const actionMap = {
    [TASK_ACTION.PUBLISH]: 'node-publish',
    [TASK_ACTION.ACCEPT]: 'node-accept',
    [TASK_ACTION.START]: 'node-start',
    [TASK_ACTION.FINISH]: 'node-finish',
    [TASK_ACTION.CANCEL]: 'node-cancel'
  }
  return actionMap[action] || ''
} 