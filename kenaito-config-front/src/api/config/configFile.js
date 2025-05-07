import request from '@/utils/requestConfig'

export function queryList(data) {
  return request({
    url: 'kenaito-config/file/queryList',
    method: 'post',
    data: data
  })
}

export function getContentById(data) {
  return request({
    url: 'kenaito-config/file/getContentById',
    method: 'post',
    data: data
  })
}

export function modifyFileContent(data) {
  return request({
    url: 'kenaito-config/file/modifyFileContent',
    method: 'post',
    data: data
  })
}

export function remove(data) {
  return request({
    url: 'kenaito-config/file/remove',
    method: 'post',
    data: data
  })
}
