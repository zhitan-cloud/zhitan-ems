import request from '@/utils/request'

// 查询模型信息列表
export function listModel(query) {
  return request({
    url: '/basicsetting/model/list',
    method: 'get',
    params: query
  })
}

// 查询模型信息详细
export function getModel(id) {
  return request({
    url: '/basicsetting/model/' + id,
    method: 'get'
  })
}

// 新增模型信息
export function addModel(data) {
  return request({
    url: '/basicsetting/model',
    method: 'post',
    data: data
  })
}

// 修改模型信息
export function updateModel(data) {
  return request({
    url: '/basicsetting/model',
    method: 'put',
    data: data
  })
}

// 删除模型信息
export function delModel(id) {
  return request({
    url: '/basicsetting/model/' + id,
    method: 'delete'
  })
}

// 导出模型信息
export function exportModel(query) {
  return request({
    url: '/basicsetting/model/export',
    method: 'get',
    params: query
  })
}