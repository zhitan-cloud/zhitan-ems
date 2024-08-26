import request from '@/utils/request'

export function selectSettingAuth(id, modelCode, authType) {
  return request({
    url: '/data/auth/' + authType + '/' + modelCode + '/' + id,
    method: 'GET',
  })
}

export function setDataAuth(id, modelCode, authType, authIds) {
  return request({
    url: '/data/auth/' + authType + '/' + modelCode + '/' + id,
    method: 'post',
    data: authIds
  })
}
