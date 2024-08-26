import request from '@/utils/request'

//查询日报表
export function getDataList(query) {
  console.log(query)
  return request({
    url: '/workingProcedure/yearWorkingProcedure/list',
    method: 'get',
    params: query
  })
}
export function getlistChart(query) {
  return request({
    url: '/workingProcedure/yearWorkingProcedure/listChart',
    method: 'get',
    params: query
  })
}
