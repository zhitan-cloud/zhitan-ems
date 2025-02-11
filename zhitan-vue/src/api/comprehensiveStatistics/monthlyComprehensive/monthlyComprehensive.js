import request from '@/utils/request'

//查询日报表
export function getDataList(query) {
  return request({
    url: '/comprehensive/monthlyComprehensive/list',
    method: 'get',
    params: query
  })
}
export function getlistChart(query) {
  return request({
    url: '/comprehensive/monthlyComprehensive/listChart',
    method: 'get',
    params: query
  })
}
