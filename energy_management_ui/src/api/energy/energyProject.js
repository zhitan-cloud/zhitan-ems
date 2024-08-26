import request from '@/utils/request'

// 查询节能项目管理列表
export function listEnergyProject(query) {
  return request({
    url: '/energy/energyProject/list',
    method: 'get',
    params: query
  })
}

// 查询节能项目管理详细
export function getEnergyProject(id) {
  return request({
    url: '/energy/energyProject/' + id,
    method: 'get'
  })
}

// 新增节能项目管理
export function addEnergyProject(data) {
  return request({
    url: '/energy/energyProject',
    method: 'post',
    data: data
  })
}

// 修改节能项目管理
export function updateEnergyProject(data) {
  return request({
    url: '/energy/energyProject',
    method: 'put',
    data: data
  })
}

// 删除节能项目管理
export function delEnergyProject(id) {
  return request({
    url: '/energy/energyProject/' + id,
    method: 'delete'
  })
}

// 导出节能项目管理
export function exportEnergyProject(query) {
  return request({
    url: '/energy/energyProject/export',
    method: 'get',
    params: query
  })
}
