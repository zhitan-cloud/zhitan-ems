import request from '@/utils/request'

// 查询节能分析报告管理附件列表
export function listReportAnnex(query) {
  return request({
    url: '/energy/reportAnnex/list',
    method: 'get',
    params: query
  })
}

// 查询节能分析报告管理附件详细
export function getReportAnnex(id) {
  return request({
    url: '/energy/reportAnnex/' + id,
    method: 'get'
  })
}

// 新增节能分析报告管理附件
export function addReportAnnex(data) {
  return request({
    url: '/energy/reportAnnex',
    method: 'post',
    data: data
  })
}

// 修改节能分析报告管理附件
export function updateReportAnnex(data) {
  return request({
    url: '/energy/reportAnnex',
    method: 'put',
    data: data
  })
}

// 删除节能分析报告管理附件
export function delReportAnnex(id) {
  return request({
    url: '/energy/reportAnnex/' + id,
    method: 'delete'
  })
}

// 导出节能分析报告管理附件
export function exportReportAnnex(query) {
  return request({
    url: '/energy/reportAnnex/export',
    method: 'get',
    params: query
  })
}