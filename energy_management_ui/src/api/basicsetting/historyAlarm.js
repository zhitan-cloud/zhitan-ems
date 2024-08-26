import request from '@/utils/request'

// 查询列表
export function listHistoryAlarm(query) {
  return request({
    url: 'energyAlarm/historicalAlarm/list',
    method: 'get',
    params: query
  })
}

// 导出设备档案
export function exportHistoricalAlarm(query) {
  return request({
    url: 'energyAlarm/historicalAlarm/export',
    method: 'get',
    params: query
  })
}


// 查询列表
export function listHistoryAlarmNote(query) {
  return request({
    url: 'energyAlarm/historicalAlarm/listNote',
    method: 'get',
    params: query
  })
}






