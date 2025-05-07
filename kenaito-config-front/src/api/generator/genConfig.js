import request from '@/utils/requestSso'

export function get(tableName) {
  return request({
    url: 'api/genConfig/' + tableName,
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'api/genConfig',
    data,
    method: 'put'
  })
}
