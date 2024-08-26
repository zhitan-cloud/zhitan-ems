import request from '@/utils/request'

/**
 * 查询同环比列表
 * @param {*} query 
 * @param {*} energyType electric/water
 * @param {*} type 1同比 2环比
 * @returns 
 */
export function queryDataComparison(query, energyType, type) {
  if (energyType == 'electric') {
    if (type === 1) {
      // 电同比
      return request({
        url: '/statisticalAnalysis/getElectricDataComparisonYoY',
        method: 'post',
        data: query
      })
    } else {
      // 电环比
      return request({
        url: '/statisticalAnalysis/getElectricDataComparisonMoM',
        method: 'post',
        data: query
      })
    }
  } else {
    if (type === 1) {
      // 水同比
      return request({
        url: '/statisticalAnalysis/getWaterDataComparisonYoY',
        method: 'post',
        data: query
      })
    } else {
      // 水环比
      return request({
        url: '/statisticalAnalysis/getWaterDataComparisonMoM',
        method: 'post',
        data: query
      })
    }
  }
}

// 导出
export function exportDataComparison(query, energyType, type) {
  if (energyType == 'electric') {
    if (type === 1) {
      // 电同比
      return request({
        url: '/statisticalAnalysis/exportElectricYoY',
        method: 'post',
        data: query
      })
    } else {
      // 电环比
      return request({
        url: '/statisticalAnalysis/exportElectricMoM',
        method: 'post',
        data: query
      })
    }
  } else {
    if (type === 1) {
      // 水同比
      return request({
        url: '/statisticalAnalysis/exportWaterYoY',
        method: 'post',
        data: query
      })
    } else {
      // 水环比
      return request({
        url: '/statisticalAnalysis/exportWaterMoM',
        method: 'post',
        data: query
      })
    }
  }
}
export function exportListMonth(query) {
  return request({
    url: '/comprehensive/monthlyComprehensive/export',
    method: 'get',
    params: query
  })
}
