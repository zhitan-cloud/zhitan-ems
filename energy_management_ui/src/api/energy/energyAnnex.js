import request from '@/utils/request'

// 查询节能项目管理附件列表
export function listEnergyAnnex(query) {
  return request({
    url: '/energy/energyAnnex/list',
    method: 'get',
    params: query
  })
}

// 查询节能项目管理附件详细
export function getEnergyAnnex(id) {
  return request({
    url: '/energy/energyAnnex/' + id,
    method: 'get'
  })
}

// 新增节能项目管理附件
export function addEnergyAnnex(data) {
  return request({
    url: '/energy/energyAnnex',
    method: 'post',
    data: data
  })
}

// 修改节能项目管理附件
export function updateEnergyAnnex(data) {
  return request({
    url: '/energy/energyAnnex',
    method: 'put',
    data: data
  })
}

// 删除节能项目管理附件
export function delEnergyAnnex(id) {
  return request({
    url: '/energy/energyAnnex/' + id,
    method: 'delete'
  })
}

// 导出节能项目管理附件
export function exportEnergyAnnex(query) {
  return request({
    url: '/energy/energyAnnex/export',
    method: 'get',
    params: query
  })
}