import request from '@/utils/request'

 //查询日报表
export function getDataList(query) {
  return request({
    url: '/comprehensive/dailyComprehensive/list',
    method: 'get',
    params: query
  })
}
export function getlistChart(query) {
  return request({
    url: '/comprehensive/dailyComprehensive/listChart',
    method: 'get',
    params: query
  })
}
