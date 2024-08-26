import request from '@/utils/request'

// 查询排班管理设置列表
export function listManagement(query) {
  return request({
    url: '/workforce/shiftManagement/list',
    method: 'get',
    params: query
  })
}

// 查询排班管理设置详细
export function getManagement(id) {
  return request({
    url: '/workforce/shiftManagement/' + id,
    method: 'get'
  })
}

// 新增排班管理设置
export function addManagement(data) {
  return request({
    url: '/workforce/shiftManagement',
    method: 'post',
    data: data
  })
}

// 修改排班管理设置
export function updateManagement(data) {
  return request({
    url: '/workforce/shiftManagement',
    method: 'put',
    data: data
  })
}

// 删除排班管理设置
export function delManagement(id) {
  return request({
    url: '/workforce/shiftManagement/' + id,
    method: 'delete'
  })
}

// 导出排班管理设置
export function exportManagement(query) {
  return request({
    url: '/workforce/shiftManagement/export',
    method: 'get',
    params: query
  })
}
export function treeList(query) {
  return request({
    url: '/workforce/shiftManagement/treeList',
    method: 'get',
    params: query
  })
}
