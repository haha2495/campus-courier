Page({
  data: {
    active: 0,
    orders: [],
    pageNum: 1,
    pageSize: 10,
    total: 0,
    loading: false
  },

  onLoad() {
    this.loadOrders()
  },

  // 切换标签
  onTabChange(event) {
    this.setData({
      active: event.detail.index,
      pageNum: 1,
      orders: []
    })
    this.loadOrders(true)
  },

  // 加载订单列表
  async loadOrders(refresh = false) {
    if (this.data.loading) return
    try {
      this.setData({ loading: true })
      const { active, pageNum, pageSize } = this.data
      const api = active === 0 ? '/api/tasks/current' : '/api/tasks/history'
      const res = await wx.http.get(api, {
        pageNum: refresh ? 1 : pageNum,
        pageSize
      })
      const { records, total } = res.data
      this.setData({
        orders: refresh ? records : [...this.data.orders, ...records],
        total,
        pageNum: refresh ? 2 : pageNum + 1,
        loading: false
      })
    } catch (error) {
      console.error('加载订单列表失败:', error)
      this.setData({ loading: false })
    }
  },

  // 加载更多
  onLoadMore() {
    if (this.data.orders.length >= this.data.total) return
    this.loadOrders()
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.loadOrders(true).then(() => {
      wx.stopPullDownRefresh()
    })
  }
}) 