import request from '@/utils/request'

//全厂综合能耗统计
export function getDataList(query) {
  return request({
    url: '/statisticalData/processEnergyConsumption/list',
    method: 'get',
    params: query
  })
}

//全厂综合能耗统计图
export function getlistChart(query) {
  return request({
    url: '/statisticalData/processEnergyConsumption/listChart',
    method: 'get',
    params: query
  })
}
