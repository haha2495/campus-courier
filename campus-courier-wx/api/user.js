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
    },
    getCurrentUser() {
        return http({
            url: '/api/student/current',
            method: 'get'
        })
    },
    // 更新头像
    updateAvatar(avatarUrl) {
        return http({
            url: `/api/student/avatar?avatarUrl=${avatarUrl}`,
            method: 'PUT'
        })
    },
    // 修改密码
    changePassword(data) {
        return http({
            url: '/api/student/password',
            method: 'PUT',
            data: {
                oldPassword: data.oldPassword,
                newPassword: data.newPassword,
                confirmPassword: data.confirmPassword
            }
        })
    }
} 