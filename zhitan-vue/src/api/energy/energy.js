import request from '@/utils/request'
// 当日能耗
export function listDay(query) {
  return request({
    url: '/energydatahomepage/energyConsumptionSummation/day',
    method: 'get',
    params: query
  })
}
// 同环比分析
export function listEnergyConsumptionSummation(query) {
  return request({
    url: '/energydatahomepage/energyConsumptionSummation',
    method: 'get',
    params: query
  })
}
// 耗电占比
export function listPeakValley(query) {
  return request({
    url: '/energydatahomepage/peakValley',
    method: 'get',
    params: query
  })
}
// 本月综合能耗/本月能耗费用/本月碳排放/本月用能完成指标
export function listFactoryEnergyConsumption(query) {
  return request({
    url: '/energydatahomepage/factoryEnergyConsumption',
    method: 'get',
    params: query
  })
}
// 用电趋势分析
export function listSegmentAnalysisYear(query) {
  return request({
    url: '/energydatahomepage/segmentAnalysis/year',
    method: 'get',
    params: query
  })
}
export function listSegmentAnalysisMonth(query) {
  return request({
    url: '/energydatahomepage/segmentAnalysis/month',
    method: 'get',
    params: query
  })
}
export function listSegmentAnalysisDay(query) {
  return request({
    url: '/energydatahomepage/segmentAnalysis/day',
    method: 'get',
    params: query
  })
}
// 能源购入与消耗对比
export function listPurchaseConsumption(query) {
  return request({
    url: '/energydatahomepage/purchaseConsumption',
    method: 'get',
    params: query
  })
}
// 能耗排名
export function listEnergyConsumptionRanking(query) {
  return request({
    url: '/energydatahomepage/energyConsumptionRanking',
    method: 'get',
    params: query
  })
}
// 能源成本占比
export function listCostProp(query) {
  return request({
    url: '/energydatahomepage/costProp',
    method: 'get',
    params: query
  })
}
