import axios from 'axios'
import Config from '@/settings'
import { setRequestHandler, setResponseHandler } from '@/utils/requestHandler'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.NODE_ENV === 'production' ? process.env.VUE_APP_BASE_API : '/', // api 的 base_url
  timeout: Config.timeout // 请求超时时间
})

setRequestHandler(service)
setResponseHandler(service)
export default service
