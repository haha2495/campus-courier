import {baseUrl} from "../utils/request";

export const uploadApi = {
    // 上传文件
    upload: (filePath) => {
        return new Promise((resolve, reject) => {
            uni.uploadFile({
                url: `${baseUrl}/api/file/upload`,
                filePath: filePath,
                name: 'file',
                success: (res) => {
                    try {
                        const response = JSON.parse(res.data)
                        if (response.code === 200) {
                            // 确保返回完整的URL，解决可能的跨域问题
                            resolve(`${baseUrl}${response.data}`)
                        } else {
                            reject(new Error(response.message || '上传失败'))
                        }
                    } catch (e) {
                        reject(new Error('上传失败'))
                    }
                },
                fail: (err) => {
                    reject(new Error('上传失败: ' + err.errMsg))
                }
            })
        })
    }
} 