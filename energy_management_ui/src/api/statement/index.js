import request from '@/utils/request'
// 查询MC用电量、综合能耗  
export function queryComprehensiveList(query) {
  return request({
    url: '/report/comprehensive/list',
    method: 'get',
    params: query
  })
}
// 查询MC用电量、综合能耗  
export function queryComprehensiveUnitChart(query) {
  return request({
    url: '/report/comprehensive/listEnergyUnitChart',
    method: 'get',
    params: query
  })
}

// 查询能耗占比信息   GET请求,参数与上面一致
export function getEnergyConsumptionRatio(query) {
  return request({
    url: '/report/comprehensive/getEnergyConsumptionRatio',
    method: 'get',
    params: query
  })
}

// 查询能耗设备占比信息  GET请求,参数与上面一致
export function getEnergyUnitConsumptionRatio(query) {
  return request({
    url: '/report/comprehensive/getEnergyUnitConsumptionRatio',
    method: 'get',
    params: query
  })
}
