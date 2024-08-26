import request from '@/utils/request'

// 查询plannedOutput列表
export function listEnergyMonitoring(query) {
  return request({
    url: '/energyMonitoring/list',
    method: 'get',
    params: query
  })
}
