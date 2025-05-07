import request from '@/utils/requestConfig'

export function queryPage(data) {
  return request({
    url: 'kenaito-config/app/queryPage',
    method: 'post',
    data: data
  })
}

export function create(data) {
  return request({
    url: 'kenaito-config/app/create',
    method: 'post',
    data: data
  })
}

export function remove(data) {
  return request({
    url: 'kenaito-config/app/remove',
    method: 'post',
    data: data
  })
}

export function queryClientList(data) {
  return request({
    url: 'kenaito-config/app/queryClientList',
    method: 'post',
    data: data
  })
}

