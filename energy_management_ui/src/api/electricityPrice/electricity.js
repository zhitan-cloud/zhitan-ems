import request from '@/utils/request'

// 查询electricityPrice列表
export function listElectricity(query) {
  return request({
    url: '/electricityPrice/electricity/list',
    method: 'get',
    params: query
  })
}

// 查询历史列表
export function listHistory(query) {
  return request({
    url: '/electricityPrice/electricity/listHistory',
    method: 'get',
    params: query
  })
}
// 查询生效日期列表
export function listDate(query) {
  return request({
    url: '/electricityPrice/electricity/listDate',
    method: 'get',
    params: query
  })
}
// 查询electricityPrice详细
export function getElectricity(id) {
  return request({
    url: '/electricityPrice/electricity/' + id,
    method: 'get'
  })
}

// 新增electricityPrice
export function addElectricity(data) {
  return request({
    url: '/electricityPrice/electricity',
    method: 'post',
    data: data
  })
}

// 修改electricityPrice
export function updateElectricity(data) {
  return request({
    url: '/electricityPrice/electricity',
    method: 'put',
    data: data
  })
}

// 删除electricityPrice
export function delElectricity(id) {
  return request({
    url: '/electricityPrice/electricity/' + id,
    method: 'delete'
  })
}

// 导出electricityPrice
export function exportElectricity(query) {
  return request({
    url: '/electricityPrice/electricity/export',
    method: 'get',
    params: query
  })
}
