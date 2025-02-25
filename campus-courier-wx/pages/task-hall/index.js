Page({
  data: {
    keyword: '',
    typeId: '',
    typeOptions: [],
    tasks: [],
    pageNum: 1,
    pageSize: 10,
    total: 0,
    loading: false,
    showDetail: false,
    currentTask: null
  },

  onLoad() {
    this.loadTaskTypes()
    this.loadTasks()
  },

  // 加载任务类型
  async loadTaskTypes() {
    try {
      const res = await wx.http.get('/api/task-types')
      const options = res.data.records.map(item => ({
        text: item.name,
        value: item.id
      }))
      options.unshift({ text: '全部类型', value: '' })
      this.setData({ typeOptions: options })
    } catch (error) {
      console.error('加载任务类型失败:', error)
    }
  },

  // 加载任务列表
  async loadTasks(refresh = false) {
    if (this.data.loading) return
    try {
      this.setData({ loading: true })
      const { keyword, typeId, pageNum, pageSize } = this.data
      const res = await wx.http.get('/api/tasks/hall', {
        keyword,
        typeId,
        pageNum: refresh ? 1 : pageNum,
        pageSize
      })
      const { records, total } = res.data
      this.setData({
        tasks: refresh ? records : [...this.data.tasks, ...records],
        total,
        pageNum: refresh ? 2 : pageNum + 1,
        loading: false
      })
    } catch (error) {
      console.error('加载任务列表失败:', error)
      this.setData({ loading: false })
    }
  },

  // 搜索框输入
  onSearchChange(e) {
    this.setData({ keyword: e.detail })
  },

  // 搜索
  onSearch() {
    this.loadTasks(true)
  },

  // 类型选择
  onTypeChange(e) {
    this.setData({ typeId: e.detail })
    this.loadTasks(true)
  },

  // 加载更多
  onLoadMore() {
    if (this.data.tasks.length >= this.data.total) return
    this.loadTasks()
  },

  // 查看详情
  async onViewDetail(e) {
    const taskId = e.currentTarget.dataset.id
    try {
      const res = await wx.http.get(`/api/courier/tasks/${taskId}`)
      this.setData({
        currentTask: res.data,
        showDetail: true
      })
    } catch (error) {
      console.error('获取任务详情失败:', error)
    }
  },

  // 关闭详情
  onCloseDetail() {
    this.setData({
      showDetail: false,
      currentTask: null
    })
  },

  // 接单
  async onTakeTask() {
    try {
      await wx.http.post(`/api/courier/tasks/${this.data.currentTask.id}/take`)
      wx.showToast({
        title: '接单成功',
        icon: 'success'
      })
      this.setData({ showDetail: false })
      this.loadTasks(true)
    } catch (error) {
      console.error('接单失败:', error)
      wx.showToast({
        title: '接单失败',
        icon: 'error'
      })
    }
  }
}) 