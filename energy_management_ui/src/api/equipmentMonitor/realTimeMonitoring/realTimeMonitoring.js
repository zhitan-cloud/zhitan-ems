import request from '@/utils/request'

// 查询设备启停实时监测列表
export function listRealTimeMonitoring(query) {
  return request({
    url: '/realTimeMonitoring/realTimeMonitoring/list',
    method: 'get',
    params: query
  })
}

// 查询设备启停实时监测详细
export function getRealTimeMonitoring(id) {
  return request({
    url: '/realTimeMonitoring/realTimeMonitoring/' + id,
    method: 'get'
  })
}

// 新增设备启停实时监测
export function addRealTimeMonitoring(data) {
  return request({
    url: '/realTimeMonitoring/realTimeMonitoring',
    method: 'post',
    data: data
  })
}

// 修改设备启停实时监测
export function updateRealTimeMonitoring(data) {
  return request({
    url: '/realTimeMonitoring/realTimeMonitoring',
    method: 'put',
    data: data
  })
}

// 删除设备启停实时监测
export function delRealTimeMonitoring(id) {
  return request({
    url: '/realTimeMonitoring/realTimeMonitoring/' + id,
    method: 'delete'
  })
}

// 导出设备启停实时监测
export function exportRealTimeMonitoring(query) {
  return request({
    url: '/realTimeMonitoring/realTimeMonitoring/export',
    method: 'get',
    params: query
  })
}

//富工接口
export function getSettingIndex(query) {
  return request({
    url: '/realTimeMonitoring/realTimeMonitoring/energyIndex/list',
    method: 'get',
    params: query
  })
}
