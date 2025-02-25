import request from '@/utils/request'

/**
 * 管理员登录
 * @param {Object} data 登录参数
 * @returns {Promise} 返回token
 */
export function adminLogin(data) {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

/**
 * 管理员退出登录
 * @returns {Promise}
 */
export function adminLogout() {
  return request({
    url: '/admin/logout',
    method: 'post'
  })
}

/**
 * 新增管理员
 * @param {Object} data 管理员信息
 * @returns {Promise}
 */
export function addAdmin(data) {
  return request({
    url: '/admin/add',
    method: 'post',
    data
  })
}

/**
 * 分页查询管理员列表
 * @param {Object} data 查询参数
 * @returns {Promise<{total: number, list: Array}>} 返回分页结果
 */
export function pageAdmin(data) {
  return request({
    url: '/admin/page',
    method: 'post',
    data
  })
}

/**
 * 获取当前登录管理员信息
 * @returns {Promise<Object>} 返回管理员信息
 */
export function getCurrentAdmin() {
  return request({
    url: '/admin/current',
    method: 'get'
  })
}

/**
 * 根据ID查询管理员信息
 * @param {number} id 管理员ID
 * @returns {Promise<Object>} 返回管理员信息
 */
export function getAdminById(id) {
  return request({
    url: `/admin/${id}`,
    method: 'get'
  })
}

/**
 * 修改管理员信息
 * @param {Object} data 管理员信息
 * @returns {Promise}
 */
export function updateAdmin(data) {
  return request({
    url: '/admin/update',
    method: 'post',
    data
  })
}

/**
 * 删除管理员
 * @param {number} id 管理员ID
 * @returns {Promise}
 */
export function deleteAdmin(id) {
  return request({
    url: `/admin/${id}`,
    method: 'delete'
  })
}

/**
 * 重置管理员密码
 * @param {number} id 管理员ID
 * @returns {Promise}
 */
export function resetAdminPassword(id) {
  return request({
    url: `/admin/${id}/reset-password`,
    method: 'post'
  })
}

/**
 * 更新当前管理员个人信息
 * @param {Object} data 更新信息（昵称、头像、密码）
 * @returns {Promise}
 */
export function updateProfile(data) {
  return request({
    url: '/admin/profile',
    method: 'put',
    data
  })
} 