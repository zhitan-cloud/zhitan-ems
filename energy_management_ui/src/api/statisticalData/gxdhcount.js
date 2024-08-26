import request from '@/utils/request'

// 查询工序单耗统计功能列表
export function listGxdhcount(query) {
  return request({
    url: '/statisticalData/gxdhcount/list',
    method: 'get',
    params: query
  })
}

// 查询工序单耗统计功能详细
export function getGxdhcount(id) {
  return request({
    url: '/statisticalData/gxdhcount/' + id,
    method: 'get'
  })
}

// 新增工序单耗统计功能
export function addGxdhcount(data) {
  return request({
    url: '/statisticalData/gxdhcount',
    method: 'post',
    data: data
  })
}

// 修改工序单耗统计功能
export function updateGxdhcount(data) {
  return request({
    url: '/statisticalData/gxdhcount',
    method: 'put',
    data: data
  })
}

// 删除工序单耗统计功能
export function delGxdhcount(id) {
  return request({
    url: '/statisticalData/gxdhcount/' + id,
    method: 'delete'
  })
}

// 导出工序单耗统计功能
export function exportGxdhcount(query) {
  return request({
    url: '/statisticalData/gxdhcount/export',
    method: 'get',
    params: query
  })
}

// 查询工序单耗统计 报表 指标展示内容
export function listGxdhcountIndex(query) {
  return request({
    url: '/statisticalData/gxdhcount/listReportIndex',
    method: 'get',
    params: query
  })
}
