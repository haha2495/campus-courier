import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建 axios 实例
const request = axios.create({
  baseURL: '/api', // API 请求的默认前缀
  timeout: 15000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      // 将 token 添加到请求头
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 这里假设后端返回的数据结构为 { code: number, message: string, data: any }
    if (res.code === 200) {
      return res.data
    } else {
      // 处理特定的错误码
      if (res.code === 401) {
        // token 过期或未登录
        ElMessage.error('登录已过期，请重新登录')
        // 清除本地存储的登录信息
        localStorage.removeItem('token')
        localStorage.removeItem('userRole')
        localStorage.removeItem('userInfo')
        // 跳转到登录页
        router.push('/login')
        return Promise.reject(new Error('未登录或登录已过期'))
      }
      
      // 其他错误
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    console.error('响应错误:', error)
    // 处理网络错误或服务器错误
    const message = error.response?.data?.message || error.message || '网络错误'
    ElMessage.error(message)
    
    // 如果是 401 错误，清除登录信息并跳转到登录页
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userRole')
      localStorage.removeItem('userInfo')
      router.push('/login')
    }
    
    return Promise.reject(error)
  }
)

export default request 