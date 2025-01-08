import request from '@/utils/request'

// 能源实时监控-能源实时监控-列表
export function listEnergyRealTimeMonitor(query) {
  return request({
    url: '/rtdb/realtimeTrend/list',
    method: 'get',
    params: query
  })
}
export function getEnergyRealTimeMonitor(query) {
  return request({
    url: '/rtdb/realtimeTrend/chartByDay',
    method: 'get',
    params: query
  })
}

//获取拓扑图
export function getConfigure(nodeId) {
  return request({
    url: '/equipmentFile/configure/' + nodeId,
    method: 'get'
  })
}


// 保存拓扑图图片
export function updateEquipmentfile(data) {
  return request({
    url: '/equipmentFile',
    method: 'put',
    data: data
  })
}


export function getAllCollectTag(data) {
  return request({
    url: '/basicsetting/energyindex/filter',
    method: 'get',
    params: data
  })
}


//保存拓扑图
export function saveSetting(nodeId, data) {
  return request({
    url: '/equipmentFile/setting/' + nodeId,
    method: 'put',
    data: data
  })
}