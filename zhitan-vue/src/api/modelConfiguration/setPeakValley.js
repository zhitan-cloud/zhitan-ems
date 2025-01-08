import request from '@/utils/request'

// 模型配置管理-尖峰平谷配置-列表
export function listPeakValleyConfiguration(query) {
  return request({
    url: '/electricitypricedate/list',
    method: 'get',
    params: query
  })
}
// 模型配置管理-尖峰平谷配置-新增
export function addPeakValleyConfiguration(data) {
  return request({
    url: '/electricitypricedate',
    method: 'post',
    data: data
  })
}
// 模型配置管理-尖峰平谷配置-查看
export function getPeakValleyConfiguration(id) {
  return request({
    url: '/electricitypricedate/' + id,
    method: 'get',
  })
}
// 模型配置管理-尖峰平谷配置-编辑
export function updatePeakValleyConfiguration(data) {
  return request({
    url: '/electricitypricedate',
    method: 'put',
    data: data
  })
}
// 模型配置管理-尖峰平谷配置-删除
export function delPeakValleyConfiguration(id) {
  return request({
    url: '/electricitypricedate/' + id,
    method: 'delete'
  })
}
// 模型配置管理-尖峰平谷配置-时段电价配置-列表
export function listPeakValleyConfigurationChild(query) {
  return request({
    url: '/electricityprice/list',
    method: 'get',
    params: query
  })
}
// 模型配置管理-尖峰平谷配置-时段电价配置-保存
export function updatePeakValleyConfigurationChild(data) {
  return request({
    url: '/electricityprice/save',
    method: 'put',
    data: data
  })
}