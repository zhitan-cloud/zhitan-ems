import request from '@/utils/request'

//全厂综合能耗统计
export function getDataList(query) {
  return request({
    url: '/statisticalData/comprehensiveStatistics/list',
    method: 'get',
    params: query
  })
}

//全厂综合能耗统计图
export function getlistChart(query) {
  return request({
    url: '/statisticalData/comprehensiveStatistics/listChart',
    method: 'get',
    params: query
  })
}
export function exportList(query) {
  return request({
    url: '/statisticalData/comprehensiveStatistics/export',
    method: 'get',
    params: query
  })
}
export function getEnergyList(query) {
  return request({
    url: '/statisticalData/comprehensiveStatistics/getList',
    method: 'get',
    params: query
  })
}
//重点设备能耗排名
export function energyList(query) {
  return request({
    url: '/statisticalData/comprehensiveStatistics/energyList',
    method: 'get',
    params: query
  })
}
//设备
export function getFacilityArchives() {
  return request({
    url: '/statisticalData/comprehensiveStatistics/getFacilityArchives',
    method: 'get'
  })
}
//重点设备
export function getPointFacility() {
  return request({
    url: '/statisticalData/comprehensiveStatistics/getPointFacility',
    method: 'get'
  })
}
export function getDeviceList(query) {
  return request({
    url: '/statisticalData/comprehensiveStatistics/getDeviceList',
    method: 'get',
    params: query
  })
}
//获取模型下的能源品种
export function energyDevice(query) {
  return request({
    url: '/statisticalData/comprehensiveStatistics/energyDevice',
    method: 'get',
    params: query
  })
}
