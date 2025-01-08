import request from '@/utils/request'

// 查询指标信息列表
export function listEnergyindex(query) {
  return request({
    url: '/basicsetting/energyindex/list',
    method: 'get',
    params: query
  })
}

// 查询指标信息详细
export function getEnergyindex(indexId) {
  return request({
    url: '/basicsetting/energyindex/' + indexId,
    method: 'get'
  })
}

// 新增指标信息
export function addEnergyindex(nodeId, data) {
  return request({
    url: '/basicsetting/energyindex/' + nodeId,
    method: 'post',
    data: data
  })
}

// 修改指标信息
export function updateEnergyindex(data) {
  return request({
    url: '/basicsetting/energyindex',
    method: 'put',
    data: data
  })
}

// 删除指标信息
export function delEnergyindex(nodeId, indexId) {
  return request({
    url: '/basicsetting/energyindex/' + nodeId + '/' + indexId,
    method: 'delete'
  })
}

// 导出指标信息
export function exportEnergyindex(query) {
  return request({
    url: '/basicsetting/energyindex/export',
    method: 'get',
    params: query
  })
}

export function listCollectIndex(query) {
  return request({
    url: '/basicsetting/energyindex/collectIndex',
    method: 'get',
    params: query
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/basicsetting/energyindex/importTemplate',
    method: 'get'
  })
}
