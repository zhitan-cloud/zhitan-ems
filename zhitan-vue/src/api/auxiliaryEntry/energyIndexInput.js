import request from '@/utils/request'

// 辅助录入-能源指标录入-列表
export function listEnergyindexinput(query) {
  return request({
    url: '/energyIndicators/list',
    method: 'get',
    params: query
  })
}
// 辅助录入-能源指标录入-新增
export function addEnergyindexinput(data) {
  return request({
    url: '/energyIndicators',
    method: 'post',
    data: data
  })
}
// 辅助录入-能源指标录入-编辑
export function updateEnergyindexinput(data) {
  return request({
    url: '/energyIndicators',
    method: 'put',
    data: data
  })
}
// 辅助录入-能源指标录入-删除
export function delEnergyindexinput(id) {
  return request({
    url: '/energyIndicators/' + id,
    method: 'delete'
  })
}