import request from '@/utils/request'

 //查询日报表
export function reportStatisticsList(query) {
  return request({
    url: '/report/dailyReport/list',
    method: 'get',
    params: query
  })
}
// 导出综合报表
export function exportList(query) {
  return request({
    url: '/report/dailyReport/export',
    method: 'get',
    params: query
  })
}
// 综合年报表导出
export function exportListYear(query) {
  return request({
    url: '/comprehensive/yearComprehensive/export',
    method: 'get',
    params: query
  })
}

// 综合年报表导出
export function exportListMonth(query) {
  return request({
    url: '/comprehensive/monthlyComprehensive/export',
    method: 'get',
    params: query
  })
}
