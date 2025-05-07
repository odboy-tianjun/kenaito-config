import request from '@/utils/requestConfig'

export function queryList(data) {
  return request({
    url: 'kenaito-config/env/queryList',
    method: 'post',
    data: data
  })
}

export function create(data) {
  return request({
    url: 'kenaito-config/env/create',
    method: 'post',
    data: data
  })
}

export function remove(data) {
  return request({
    url: 'kenaito-config/env/remove',
    method: 'post',
    data: data
  })
}
