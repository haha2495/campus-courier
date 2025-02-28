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
    },
    // 退出登录
    logout() {
        return http({
            url: '/api/student/logout',
            method: 'POST'
        })
    },
    // 余额充值
    recharge(amount) {
        return http({
            url: '/api/student/recharge',
            method: 'POST',
            data: {
                amount
            }
        })
    },
    // 余额提现
    withdraw(amount) {
        return http({
            url: '/api/student/withdraw',
            method: 'POST',
            data: {
                amount
            }
        })
    },
    // 获取余额变动记录
    getBalanceRecords() {
        return http({
            url: '/api/student/balance/records',
            method: 'GET'
        })
    }
} 