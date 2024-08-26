import request from '@/utils/request'

// 查询plannedOutput列表
export function listPlannedOutput(query) {
  return request({
    url: '/planned/plannedOutput/list',
    method: 'get',
    params: query
  })
}

// 查询plannedOutput详细
export function getPlannedOutput(id) {
  return request({
    url: '/planned/plannedOutput/' + id,
    method: 'get'
  })
}

// 新增plannedOutput
export function addPlannedOutput(data) {
  return request({
    url: '/planned/plannedOutput',
    method: 'post',
    data: data
  })
}

// 修改plannedOutput
export function updatePlannedOutput(data) {
  return request({
    url: '/planned/plannedOutput',
    method: 'put',
    params: data
  })
}

// 删除plannedOutput
export function delPlannedOutput(id) {
  return request({
    url: '/planned/plannedOutput/' + id,
    method: 'delete'
  })
}

// 导出plannedOutput
export function exportPlannedOutput(query) {
  return request({
    url: '/planned/plannedOutput/export',
    method: 'get',
    params: query
  })
}
