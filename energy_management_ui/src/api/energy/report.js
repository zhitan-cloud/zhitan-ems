import request from '@/utils/request'

// 查询节能分析报告管理列表
export function listReport(query) {
  return request({
    url: '/energy/report/list',
    method: 'get',
    params: query
  })
}

// 查询节能分析报告管理详细
export function getReport(id) {
  return request({
    url: '/energy/report/' + id,
    method: 'get'
  })
}

// 新增节能分析报告管理
export function addReport(data) {
  return request({
    url: '/energy/report',
    method: 'post',
    data: data
  })
}

// 修改节能分析报告管理
export function updateReport(data) {
  return request({
    url: '/energy/report',
    method: 'put',
    data: data
  })
}

// 删除节能分析报告管理
export function delReport(id) {
  return request({
    url: '/energy/report/' + id,
    method: 'delete'
  })
}

// 导出节能分析报告管理
export function exportReport(query) {
  return request({
    url: '/energy/report/export',
    method: 'get',
    params: query
  })
}