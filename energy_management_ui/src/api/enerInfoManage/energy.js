import request from '@/utils/request'

// 查询energy列表
export function listEnergy(query) {
  return request({
    url: '/enerInfoManage/energy/list',
    method: 'get',
    params: query
  })
}
export function classselect() {
  return request({
    url: '/enerInfoManage/energy/getenerclassname',
    method: 'get',
  })
}

// 查询energy详细
export function getEnergy(enerid) {
  return request({
    url: '/enerInfoManage/energy/' + enerid,
    method: 'get'
  })
}
// 保存单价设置
export function updateEnergyPrice(data){
  return request({
    url: '/enerInfoManage/energy/updateEnergyPrice',
    method: 'put',
    data: data
  })
}
export function updateEnergyCoefficient(data) {
  return request({
    url: '/enerInfoManage/energy/updateEnergyCoefficient',
    method: 'put',
    data: data
  })
}

// 新增energy
export function addEnergy(data) {
  return request({
    url: '/enerInfoManage/energy',
    method: 'post',
    data: data
  })
}

// 修改energy
export function updateEnergy(data) {
  return request({
    url: '/enerInfoManage/energy',
    method: 'put',
    data: data
  })
}

// 删除energy
export function delEnergy(enerid) {
  return request({
    url: '/enerInfoManage/energy/' + enerid,
    method: 'delete'
  })
}

// 导出energy
export function exportEnergy(query) {
  return request({
    url: '/enerInfoManage/energy/export',
    method: 'get',
    params: query
  })
}
