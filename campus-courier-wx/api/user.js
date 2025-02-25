import {http} from '../utils/request'

// 用户相关接口
export const userApi = {
    // 用户登录
    login(data) {
        return http({
            url: '/api/student/login',
            method: 'POST',
            data: data
        })
    }
} 