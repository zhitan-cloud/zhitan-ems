import request from '@/utils/request'

 //查询日报表
export function getDataList(query) {
  return request({
    url: '/workingProcedure/dailyWorkingProcedure/list',
    method: 'get',
    params: query
  })
}
export function getlistChart(query) {
  return request({
    url: '/workingProcedure/dailyWorkingProcedure/listChart',
    method: 'get',
    params: query
  })
}
