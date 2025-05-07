// 适配 Nginx 反向代理
const baseUrl = process.env.VUE_APP_BASE_API === '/' ? '' : process.env.VUE_APP_BASE_API
const api = {
  state: {
    // websocket
    websocketApi: baseUrl + '/webSocket/{sid}',
    // 图片上传
    imagesUploadApi: baseUrl + '/api/localStorage/pictures',
    // 修改头像
    updateAvatarApi: baseUrl + '/api/users/updateAvatar',
    // Sql 监控
    sqlApi: baseUrl + '/druid/index.html',
    // 文件上传
    fileUploadApi: baseUrl + '/api/localStorage',
    // baseUrl，
    baseApi: baseUrl
  }
}

export default api
