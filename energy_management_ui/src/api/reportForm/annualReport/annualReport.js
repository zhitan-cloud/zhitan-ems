import request from '@/utils/request'

 //查询日报表
export function reportStatisticsList(query) {
  return request({
    url: '/report/annualReport/list',
    method: 'get',
    params: query
  })
}
// 导出综合报表
export function exportList(query) {
  return request({
    url: '/report/annualReport/export',
    method: 'get',
    params: query
  })
}
