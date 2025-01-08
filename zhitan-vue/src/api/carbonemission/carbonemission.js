import request from '@/utils/request'

// 碳排放管理-碳排放量核算-头部
export function listUpCarbonemission(query) {
  return request({
    url: '/carbonEmission/up',
    method: 'get',
    params: query
  })
}
// 碳排放管理-碳排放量核算-碳排放量同环比
export function listMiddleCarbonemission(query) {
  return request({
    url: '/carbonEmission/middle',
    method: 'get',
    params: query
  })
}