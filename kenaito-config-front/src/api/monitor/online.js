import request from '@/utils/requestSso'

export function del(keys) {
  return request({
    url: 'auth/online',
    method: 'delete',
    data: keys
  })
}
