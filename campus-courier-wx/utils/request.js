// 引入配置
export const baseUrl = 'http://127.0.0.1:9001'

// 请求拦截器
const httpInterceptor = {
    invoke(options) {
        // 1. 非 http 开头需要拼接地址
        if (!options.url.startsWith('http')) {
            options.url = baseUrl + options.url
        }
        // 2. 请求超时
        options.timeout = 10000
        // 3. 添加小程序端请求头标识
        options.header = {
            ...options.header,
            'source-client': 'miniapp'
        }
        // 4. 添加 token 请求头标识
        const token = uni.getStorageSync('token')
        if (token) {
            options.header.Authorization = token
        }
    }
}

// 拦截 request 请求
uni.addInterceptor('request', httpInterceptor)
// 拦截 uploadFile 请求
uni.addInterceptor('uploadFile', httpInterceptor)

// 请求函数
export const http = (options) => {
    // 1. 返回 Promise 对象
    return new Promise((resolve, reject) => {
        uni.request({
            ...options,
            // 2. 请求成功
            success(res) {
                // 状态码 2xx，参考 axios 的设计
                if (res.statusCode >= 200 && res.statusCode < 300) {
                    // 2.1 提取核心数据 res.data
                    resolve(res.data)
                } else if (res.statusCode === 401) {
                    // 401错误  -> 清理用户信息，跳转到登录页
                    uni.removeStorageSync('token')
                    uni.redirectTo({ url: '/pages/login/index' })
                    reject(res)
                } else {
                    // 其他错误 -> 根据后端错误信息轻提示
                    uni.showToast({
                        icon: 'none',
                        title: res.data.message || '请求错误',
                    })
                    reject(res)
                }
            },
            // 3. 请求失败
            fail(err) {
                uni.showToast({
                    icon: 'none',
                    title: '网络错误，换个网络试试',
                })
                reject(err)
            },
        })
    })
}