import request from '@/utils/request'
// 首页-全厂能耗统计/全厂能耗占比-列表
export function listEnergyConsumptionSummation(query) {
  return request({
    url: '/homepage/energyConsumptionSummation',
    method: 'get',
    params: query
  })
}
// 首页-全厂能耗趋势-列表
export function listEnergyConsumptionTrend(query) {
  return request({
    url: '/homepage/energyConsumptionTrend',
    method: 'get',
    params: query
  })
}
// 首页-科室能耗排名TOP-列表
export function listEnergyConsumptionRanking(query) {
  return request({
    url: '/homepage/energyConsumptionRanking',
    method: 'get',
    params: query
  })
}
// 首页-尖峰平谷占比-列表
export function listPeakValley(query) {
  return request({
    url: '/homepage/peakValley',
    method: 'get',
    params: query
  })
}
