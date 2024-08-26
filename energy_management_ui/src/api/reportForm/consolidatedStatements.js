import request from '@/utils/request'

// 查询焦化工序综合报表列表
export function listConsolidatedStatements(query) {
  return request({
    url: '/reportForm/consolidatedStatements/list',
    method: 'get',
    params: query
  })
}

// 查询焦化工序综合报表详细
export function getConsolidatedStatements(id) {
  return request({
    url: '/reportForm/consolidatedStatements/' + id,
    method: 'get'
  })
}

// 新增焦化工序综合报表
export function addConsolidatedStatements(data) {
  return request({
    url: '/reportForm/consolidatedStatements',
    method: 'post',
    data: data
  })
}

// 修改焦化工序综合报表
export function updateConsolidatedStatements(data) {
  return request({
    url: '/reportForm/consolidatedStatements',
    method: 'put',
    data: data
  })
}

// 删除焦化工序综合报表
export function delConsolidatedStatements(id) {
  return request({
    url: '/reportForm/consolidatedStatements/' + id,
    method: 'delete'
  })
}

// 导出焦化工序综合报表
export function exportConsolidatedStatements(query) {
  return request({
    url: '/reportForm/consolidatedStatements/export',
    method: 'get',
    params: query
  })
}