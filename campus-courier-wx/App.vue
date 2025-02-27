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
	@import "@/styles/global.scss";

	/* 全局样式 */
	page {
		background-color: $uni-bg-color-grey;
		font-family: $base-font-family;
		font-size: $base-font-size;
		line-height: $base-line-height;
		color: $uni-text-color;
		box-sizing: border-box;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
	}

	/* 去除按钮默认边框 */
	button {
		&::after {
			border: none;
		}
	}

	/* 统一图片样式 */
	image {
		will-change: transform;
	}

	/* 统一过渡动画 */
	.fade-enter-active,
	.fade-leave-active {
		transition: opacity $animation-duration-base $animation-timing-function-ease;
	}

	.fade-enter,
	.fade-leave-to {
		opacity: 0;
	}

	/* 滑动动画 */
	.slide-left-enter-active,
	.slide-left-leave-active,
	.slide-right-enter-active,
	.slide-right-leave-active {
		transition: all $animation-duration-base $animation-timing-function-ease;
	}

	.slide-left-enter,
	.slide-right-leave-to {
		transform: translateX(-100%);
		opacity: 0;
	}

	.slide-left-leave-to,
	.slide-right-enter {
		transform: translateX(100%);
		opacity: 0;
	}

	/* 安全区适配 */
	.safe-area-inset-bottom {
		padding-bottom: constant(safe-area-inset-bottom);
		padding-bottom: env(safe-area-inset-bottom);
	}

	/* 隐藏滚动条 */
	::-webkit-scrollbar {
		display: none;
		width: 0;
		height: 0;
		color: transparent;
	}

	/* 点击反馈效果 */
	.hover-class {
		opacity: 0.7;
	}
</style>
