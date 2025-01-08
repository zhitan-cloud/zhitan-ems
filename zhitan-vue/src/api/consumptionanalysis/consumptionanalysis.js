import request from '@/utils/request'

// 产品单耗分析-产品单耗分析-列表
export function listConsumptionanalysis(query) {
  return request({
    url: '/consumptionanalysis/getProdEnergy',
    method: 'get',
    params: query
  })
}