import request from '@/utils/request'

// 查询单价设置列表
export function listPrice(query) {
  return request({
    url: '/price/price/list',
    method: 'get',
    params: query
  })
}

// 查询单价设置详细
export function getPrice(priceid) {
  return request({
    url: '/price/price/' + priceid,
    method: 'get'
  })
}

// 新增单价设置
export function addPrice(data) {
  return request({
    url: '/price/price',
    method: 'post',
    data: data
  })
}

// 修改单价设置
export function updatePrice(data) {
  return request({
    url: '/price/price',
    method: 'put',
    data: data
  })
}

// 删除单价设置
export function delPrice(priceid) {
  return request({
    url: '/price/price/' + priceid,
    method: 'delete'
  })
}

// 导出单价设置
export function exportPrice(query) {
  return request({
    url: '/price/price/export',
    method: 'get',
    params: query
  })
}