import request from '@/utils/request'

// 辅助录入-能源指标录入-列表
export function listEnergyIndicators(query) {
  return request({
    url: '/energyindex/list',
    method: 'get',
    params: query
  })
}
// 辅助录入-能源指标录入-新增
export function addEnergyIndicators(data) {
  return request({
    url: '/energyindex',
    method: 'post',
    data: data
  })
}
// 辅助录入-能源指标录入-编辑
export function updateEnergyIndicators(data) {
  return request({
    url: '/energyindex',
    method: 'put',
    data: data
  })
}
// 辅助录入-能源指标录入-删除
export function delEnergyIndicators(id) {
  return request({
    url: '/energyindex/' + id,
    method: 'delete'
  })
}