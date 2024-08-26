import request from '@/utils/request'

 //查询日报表
export function getDataList(query) {
  return request({
    url: '/keyEquipment/dailyKeyEquipment/list',
    method: 'get',
    params: query
  })
}
export function getlistChart(query) {
  return request({
    url: '/keyEquipment/dailyKeyEquipment/listChart',
    method: 'get',
    params: query
  })
}
export function getFacilityArchives(query) {
  return request({
    url: '/keyEquipment/dailyKeyEquipment/getFacilityArchives',
    method: 'get',
    params: query
  })
}

export function getPointFacility(query) {
  return request({
    url: '/keyEquipment/dailyKeyEquipment/getPointFacility',
    method: 'get',
    params: query
  })
}
