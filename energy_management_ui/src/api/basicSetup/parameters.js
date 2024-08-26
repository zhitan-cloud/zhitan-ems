import request from '@/utils/request'

// 查询basicSetup列表
export function listParameters(query) {
  return request({
    url: '/basicSetup/parameters/list',
    method: 'get',
    params: query
  })
}

// 查询basicSetup详细
export function getParameters(id) {
  return request({
    url: '/basicSetup/parameters/' + id,
    method: 'get'
  })
}

// 新增basicSetup
export function addParameters(data) {
  return request({
    url: '/basicSetup/parameters',
    method: 'post',
    data: data
  })
}

// 修改basicSetup
export function updateParameters(data) {
  return request({
    url: '/basicSetup/parameters',
    method: 'put',
    data: data
  })
}

// 删除basicSetup
export function delParameters(id) {
  return request({
    url: '/basicSetup/parameters/' + id,
    method: 'delete'
  })
}
export function getAllCollectTag(data) {
  return request({
    url: '/basicsetting/energyindex/filter',
    method: 'get',
    params: data
  })
}

export function getListSee(titleId) {
  return request({
    url: '/basicSetup/parameters/listSee/'+titleId,
    method: 'get',
    //params: titleId
  })
}
