import request from '@/utils/request'

// 查询electricity列表
export function listPrice(query) {
  return request({
    url: '/electricityPrice/price/list',
    method: 'get',
    params: query
  })
}

// 查询electricity详细
export function getPrice(id) {
  return request({
    url: '/electricityPrice/price/' + id,
    method: 'get'
  })
}

// 新增electricity
export function addPrice(data) {
  return request({
    url: '/electricityPrice/price',
    method: 'post',
    data: data
  })
}

// 修改electricity
export function updatePrice(data) {
  return request({
    url: '/electricityPrice/price',
    method: 'put',
    data: data
  })
}

// 删除electricity
export function delPrice(id) {
  return request({
    url: '/electricityPrice/price/' + id,
    method: 'delete'
  })
}

// 导出electricity
export function exportPrice(query) {
  return request({
    url: '/electricityPrice/price/export',
    method: 'get',
    params: query
  })
}
// 查询字典项详细
// 根据字典类型查询字典数据信息
export function EdictType(query) {
  return request({
    url: '/electricityPrice/price/dictType',
    method: 'get',
    params: query
  })
}
export function dictTypeList(query) {
  return request({
    url: '/electricityPrice/price/dictTypeList',
    method: 'get',
    params: query
  })
}
