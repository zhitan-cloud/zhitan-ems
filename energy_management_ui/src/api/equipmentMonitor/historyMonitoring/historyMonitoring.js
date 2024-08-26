import request from '@/utils/request'

// 查询设备启停历史监测列表
export function listHistoryMonitoring(query) {
  return request({
    url: '/historyMonitoring/historyMonitoring/list',
    method: 'get',
    params: query
  })
}

// 查询设备启停历史监测详细
export function getHistoryMonitoring(id) {
  return request({
    url: '/historyMonitoring/historyMonitoring/' + id,
    method: 'get'
  })
}

// 新增设备启停历史监测
export function addHistoryMonitoring(data) {
  return request({
    url: '/historyMonitoring/historyMonitoring',
    method: 'post',
    data: data
  })
}

// 修改设备启停历史监测
export function updateHistoryMonitoring(data) {
  return request({
    url: '/historyMonitoring/historyMonitoring',
    method: 'put',
    data: data
  })
}

// 删除设备启停历史监测
export function delHistoryMonitoring(id) {
  return request({
    url: '/historyMonitoring/historyMonitoring/' + id,
    method: 'delete'
  })
}

// 导出设备启停历史监测
export function exportHistoryMonitoring(query) {
  return request({
    url: '/historyMonitoring/historyMonitoring/export',
    method: 'get',
    params: query
  })
}

// 查询设备启停历史监测列表(新加)
export function listHistoryMonitoringNew(query) {
  return request({
    url: '/historyMonitoring/historyMonitoring/history/list',
    method: 'get',
    params: query
  })
}

// 导出设备启停历史监测(新加)
export function exportHistoryMonitoringTableList(query) {
  return request({
    url: '/historyMonitoring/historyMonitoring/export/tableList',
    method: 'get',
    params: query
  })
}
