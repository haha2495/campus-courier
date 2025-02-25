Page({
  data: {
    task: null,
    steps: [
      { text: '待接单' },
      { text: '进行中' },
      { text: '已完成' }
    ],
    active: 1,
    showMessages: false,
    messages: [],
    messageContent: '',
    currentUserId: '',
    messagePageNum: 1,
    messagePageSize: 20,
    messageTotal: 0,
    messageLoading: false
  },

  onLoad() {
    this.loadCurrentTask()
    // 从本地存储获取当前用户ID
    const userInfo = wx.getStorageSync('userInfo')
    if (userInfo) {
      this.setData({ currentUserId: userInfo.id })
    }
  },

  // 加载当前任务
  async loadCurrentTask() {
    try {
      const res = await wx.http.get('/api/tasks/current')
      if (res.data && res.data.records.length > 0) {
        this.setData({ 
          task: res.data.records[0],
          active: res.data.records[0].status
        })
      }
    } catch (error) {
      console.error('加载当前任务失败:', error)
    }
  },

  // 查看留言
  onViewMessages() {
    this.setData({ 
      showMessages: true,
      messagePageNum: 1
    })
    this.loadMessages(true)
  },

  // 加载留言
  async loadMessages(refresh = false) {
    if (this.data.messageLoading) return
    try {
      this.setData({ messageLoading: true })
      const { task, messagePageNum, messagePageSize } = this.data
      const res = await wx.http.get(`/api/courier/tasks/${task.id}/messages`, {
        pageNum: refresh ? 1 : messagePageNum,
        pageSize: messagePageSize
      })
      const { records, total } = res.data
      this.setData({
        messages: refresh ? records : [...this.data.messages, ...records],
        messageTotal: total,
        messagePageNum: refresh ? 2 : messagePageNum + 1,
        messageLoading: false
      })
    } catch (error) {
      console.error('加载留言失败:', error)
      this.setData({ messageLoading: false })
    }
  },

  // 留言内容变化
  onMessageChange(e) {
    this.setData({ messageContent: e.detail })
  },

  // 发送留言
  async onSendMessage() {
    const { messageContent, task } = this.data
    if (!messageContent.trim()) {
      wx.showToast({
        title: '请输入留言内容',
        icon: 'none'
      })
      return
    }

    try {
      await wx.http.post('/api/courier/tasks/messages', {
        taskId: task.id,
        content: messageContent
      })
      this.setData({ messageContent: '' })
      this.loadMessages(true)
    } catch (error) {
      console.error('发送留言失败:', error)
      wx.showToast({
        title: '发送失败',
        icon: 'error'
      })
    }
  },

  // 加载更多留言
  onLoadMoreMessages() {
    if (this.data.messages.length >= this.data.messageTotal) return
    this.loadMessages()
  }
}) 