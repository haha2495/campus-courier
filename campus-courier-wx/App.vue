<script>
import { userApi } from '@/api/user'

export default {
	onLaunch: function() {
		console.log('App Launch')
		// 检查登录状态
		this.checkLoginStatus()
	},
	onShow: function() {
		console.log('App Show')
	},
	onHide: function() {
		console.log('App Hide')
	},
	methods: {
		async checkLoginStatus() {
			const token = uni.getStorageSync('token')
      console.log(token)
			if (token) {
				try {
					// 获取用户信息
					const res = await userApi.getCurrentUser()
					if (res.code === 200) {
						// 保存用户信息到全局状态
						getApp().globalData = {
							...getApp().globalData,
							userInfo: res.data
						}
						
						// 获取当前页面路径
						const pages = getCurrentPages()
						const currentPage = pages[pages.length - 1]
						
						// 如果当前在登录页，则跳转到首页
						if (currentPage?.route === 'pages/login/index') {
							uni.reLaunch({
								url: '/pages/index/index'
							})
						}
					} else {
						// token无效，清除token
						uni.removeStorageSync('token')
						this.redirectToLogin()
					}
				} catch (err) {
					console.error('获取用户信息失败:', err)
					uni.removeStorageSync('token')
					this.redirectToLogin()
				}
			}
		},
		redirectToLogin() {
			const pages = getCurrentPages()
			const currentPage = pages[pages.length - 1]
			
			// 如果当前不在登录页，则跳转到登录页
			if (currentPage?.route !== 'pages/login/index') {
				uni.reLaunch({
					url: '/pages/login/index'
				})
			}
		}
	}
}
</script>

<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-plus/index.scss";
	@import "@/uni.scss";

	/* 此处为自定义的全局样式 */
	page {
		background-color: #f5f5f5;
	}
</style>
