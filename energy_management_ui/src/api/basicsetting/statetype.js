import request from '@/utils/request'

// 查询系统状态维护列表
export function listStatetype(query) {
  return request({
    url: '/basicsetting/statetype/list',
    method: 'get',
    params: query
  })
}

// 查询系统状态维护详细
export function getStatetype(stateId) {
  return request({
    url: '/basicsetting/statetype/' + stateId,
    method: 'get'
  })
}

// 新增系统状态维护
export function addStatetype(data) {
  return request({
    url: '/basicsetting/statetype',
    method: 'post',
    data: data
  })
}

// 修改系统状态维护
export function updateStatetype(data) {
  return request({
    url: '/basicsetting/statetype',
    method: 'put',
    data: data
  })
}

// 删除系统状态维护
export function delStatetype(stateId) {
  return request({
    url: '/basicsetting/statetype/' + stateId,
    method: 'delete'
  })
}

// 导出系统状态维护
export function exportStatetype(query) {
  return request({
    url: '/basicsetting/statetype/export',
    method: 'get',
    params: query
  })
}