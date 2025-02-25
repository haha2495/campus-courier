import App from './App'

// #ifdef VUE3
import { createSSRApp } from 'vue'
import { createPinia } from 'pinia'
import uviewPlus from 'uview-plus'

export function createApp() {
  const app = createSSRApp(App)
  const pinia = createPinia()
  
  app.use(pinia)
  app.use(uviewPlus)
  
  // uview-plus配置
  uni.$u.setConfig({
    // 修改$u.config的属性
    config: {
      // 修改默认单位为rpx
      unit: 'rpx'
    },
    // 修改$u.props的属性
    props: {
      // 修改button组件的默认样式
      button: {
        height: '80rpx'
      }
    }
  })
  
  return {
    app,
    pinia
  }
}
// #endif

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'

Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
// #endif