import request from '@/utils/request'

// 查询energy_benchmarking列表
export function listEnergyEenchmarking(query) {
  return request({
    url: '/energyEenchmarking/energyEenchmarking/list',
    method: 'get',
    params: query
  })
}

// 查询energy_benchmarking详细
export function getEnergyEenchmarking(id) {
  return request({
    url: '/energyEenchmarking/energyEenchmarking/' + id,
    method: 'get'
  })
}

// 新增energy_benchmarking
export function addEnergyEenchmarking(data) {
  return request({
    url: '/energyEenchmarking/energyEenchmarking',
    method: 'post',
    data: data
  })
}

// 修改energy_benchmarking
export function updateEnergyEenchmarking(data) {
  return request({
    url: '/energyEenchmarking/energyEenchmarking',
    method: 'put',
    data: data
  })
}

// 删除energy_benchmarking
export function delEnergyEenchmarking(id) {
  return request({
    url: '/energyEenchmarking/energyEenchmarking/' + id,
    method: 'delete'
  })
}

// 导出energy_benchmarking
export function exportEnergyEenchmarking(query) {
  return request({
    url: '/energyEenchmarking/energyEenchmarking/export',
    method: 'get',
    params: query
  })
}