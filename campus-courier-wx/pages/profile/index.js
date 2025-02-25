Page({
  data: {
    courier: {},
    showUploader: false,
    fileList: [],
    tempAvatar: ''
  },

  onLoad() {
    this.loadCourierInfo()
  },

  // 加载跑腿者信息
  async loadCourierInfo() {
    try {
      const res = await wx.http.get('/api/couriers/info')
      this.setData({ courier: res.data })
    } catch (error) {
      console.error('加载跑腿者信息失败:', error)
    }
  },

  // 修改头像
  onChangeAvatar() {
    this.setData({ showUploader: true })
  },

  // 关闭上传弹窗
  onCloseUploader() {
    this.setData({ 
      showUploader: false,
      fileList: [],
      tempAvatar: ''
    })
  },

  // 选择文件后的回调
  afterRead(event) {
    const { file } = event.detail
    this.setData({
      fileList: [{ url: file.url }],
      tempAvatar: file.url
    })
  },

  // 删除文件
  onDeleteFile() {
    this.setData({
      fileList: [],
      tempAvatar: ''
    })
  },

  // 确认修改头像
  async onConfirmAvatar() {
    if (!this.data.tempAvatar) return
    try {
      // 这里应该先调用后端接口上传图片，获取图片URL
      // 这里简化处理，直接使用临时路径
      const courier = { ...this.data.courier }
      courier.avatar = this.data.tempAvatar
      this.setData({ 
        courier,
        showUploader: false,
        fileList: [],
        tempAvatar: ''
      })
    } catch (error) {
      console.error('上传头像失败:', error)
      wx.showToast({
        title: '上传失败',
        icon: 'error'
      })
    }
  },

  // 昵称变化
  onNicknameChange(event) {
    const courier = { ...this.data.courier }
    courier.nickname = event.detail
    this.setData({ courier })
  },

  // 联系方式变化
  onContactChange(event) {
    const courier = { ...this.data.courier }
    courier.contact = event.detail
    this.setData({ courier })
  },

  // 个人简介变化
  onIntroductionChange(event) {
    const courier = { ...this.data.courier }
    courier.introduction = event.detail
    this.setData({ courier })
  },

  // 保存修改
  async onSave() {
    const { courier } = this.data
    if (!courier.nickname || !courier.contact) {
      wx.showToast({
        title: '昵称和联系方式不能为空',
        icon: 'none'
      })
      return
    }

    try {
      await wx.http.put('/api/couriers/profile', {
        avatar: courier.avatar,
        nickname: courier.nickname,
        contact: courier.contact,
        introduction: courier.introduction
      })
      wx.showToast({
        title: '保存成功',
        icon: 'success'
      })
    } catch (error) {
      console.error('保存失败:', error)
      wx.showToast({
        title: '保存失败',
        icon: 'error'
      })
    }
  }
}) 