import request from '@/utils/request'

//查询日报表
export function getDataList(query) {
  return request({
    url: '/workingProcedure/monthlyWorkingProcedure/list',
    method: 'get',
    params: query
  })
}
export function getlistChart(query) {
  return request({
    url: '/workingProcedure/monthlyWorkingProcedure/listChart',
    method: 'get',
    params: query
  })
}
