import request from '@/utils/request'

/**
 * 上传文件
 * @param {File} file - 文件对象
 * @returns {Promise<string>} 返回文件访问路径
 */
export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/file/upload',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
} 