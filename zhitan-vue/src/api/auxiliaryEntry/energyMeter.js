import request from '@/utils/request'

// 辅助录入-能源仪表录入-列表
export function listEnergyMeter(query) {
  return request({
    url: '/meter/list',
    method: 'get',
    params: query
  })
}
// 辅助录入-能源仪表录入-新增
export function addEnergyMeter(data) {
  return request({
    url: '/meter',
    method: 'post',
    data: data
  })
}
// 辅助录入-能源仪表录入-编辑
export function updateEnergyMeter(data) {
  return request({
    url: '/meter',
    method: 'put',
    data: data
  })
}
// 辅助录入-能源仪表录入-删除
export function delEnergyMeter(id) {
  return request({
    url: '/meter/' + id,
    method: 'delete'
  })
}