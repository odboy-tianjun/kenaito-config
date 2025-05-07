import axios from 'axios'
import Config from '@/settings'
import { setRequestHandler, setResponseHandler } from '@/utils/requestHandler'

function createService(subSystemCode) {
  // 创建axios实例
  const innerService = axios.create({
    baseURL: process.env.NODE_ENV === 'production' ? process.env['VUE_' + subSystemCode + '_BASE_API'] : '/', // api 的 base_url
    timeout: Config.timeout // 请求超时时间
  })
  setRequestHandler(innerService)
  setResponseHandler(innerService)
  return innerService
}

export default createService
