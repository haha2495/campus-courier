import request from '@/utils/request'
import {ElMessage} from 'element-plus'

/**
 * 获取院系专业年级信息
 * @returns {Promise} 返回院系信息列表
 */
export function getDepartments() {
    return request({
        url: '/student/departments',
        method: 'get'
    })
}

/**
 * 新增学生
 * @param {Object} data 学生信息
 * @returns {Promise}
 */
export function addStudent(data) {
    return request({
        url: '/student/add',
        method: 'post',
        data
    })
}

/**
 * 分页查询学生列表
 * @param {Object} data 查询参数
 * @returns {Promise<{total: number, list: Array}>} 返回分页结果
 */
export function pageStudent(data) {
    return request({
        url: '/student/page',
        method: 'post',
        data
    })
}

/**
 * 删除学生
 * @param {number} id 学生ID
 * @returns {Promise}
 */
export function deleteStudent(id) {
    return request({
        url: `/student/${id}`,
        method: 'delete'
    })
}

/**
 * 重置学生密码
 * @param {number} id 学生ID
 * @returns {Promise}
 */
export function resetStudentPassword(id) {
    return request({
        url: `/student/${id}/reset-password`,
        method: 'post'
    })
}

/**
 * 下载学生导入模板
 */
export function downloadTemplate() {
    window.open('/api/student/template', '_blank')
    ElMessage.success('模板下载成功')
}

/**
 * 根据ID查询学生信息
 * @param {number} id 学生ID
 * @returns {Promise<Object>} 返回学生信息
 */
export function getStudentById(id) {
    return request({
        url: `/student/${id}`,
        method: 'get'
    })
}

/**
 * 更新学生信息
 * @param {Object} data 学生信息
 * @returns {Promise}
 */
export function updateStudent(data) {
    return request({
        url: '/student/update',
        method: 'post',
        data
    })
}

/**
 * 获取学生记录列表
 * @param {number} studentId - 学生ID
 * @returns {Promise} 返回记录列表
 */
export function getStudentRecords(studentId) {
    return request({
        url: `/student/records/${studentId}`,
        method: 'get'
    })
}

/**
 * 学生登录
 * @param {Object} data 登录参数 {studentNo: string, password: string}
 * @returns {Promise} 返回token
 */
export function studentLogin(data) {
  return request({
    url: '/student/login',
    method: 'post',
    data
  })
}

/**
 * 学生退出登录
 * @returns {Promise}
 */
export function studentLogout() {
  return request({
    url: '/student/logout',
    method: 'post'
  })
}

/**
 * 获取当前学生信息
 * @returns {Promise} 返回学生信息
 */
export function getCurrentStudent() {
  return request({
    url: '/student/current',
    method: 'get'
  })
}

/**
 * 更新当前学生个人信息
 * @param {Object} data 更新信息（姓名、头像、邮箱、密码）
 * @returns {Promise}
 */
export function updateStudentProfile(data) {
  return request({
    url: '/student/profile',
    method: 'put',
    data
  })
}