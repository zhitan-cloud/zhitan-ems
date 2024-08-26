import request from '@/utils/request'

// 查询排班管理列表
export function listManagement(query) {
  return request({
    url: '/workforce/dutyManagement/list',
    method: 'get',
    params: query
  })
}

// 查询排班管理详细
export function getManagement(id) {
  return request({
    url: '/workforce/dutyManagement/' + id,
    method: 'get'
  })
}

// 新增排班管理
export function addManagement(data) {
  return request({
    url: '/workforce/dutyManagement',
    method: 'post',
    data: data
  })
}

// 修改排班管理
export function updateManagement(data) {
  return request({
    url: '/workforce/dutyManagement',
    method: 'put',
    data: data
  })
}

// 删除排班管理
export function delManagement(id) {
  return request({
    url: '/workforce/dutyManagement/' + id,
    method: 'delete'
  })
}

// 导出排班管理
export function exportManagement(query) {
  return request({
    url: '/workforce/dutyManagement/export',
    method: 'get',
    params: query
  })
}
