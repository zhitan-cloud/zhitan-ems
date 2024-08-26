import request from '@/utils/request'

// 查询能源折标系数列表
export function listEnercoefficient(query) {
  return request({
    url: '/enerInfoManage/enercoefficient/list',
    method: 'get',
    params: query
  })
}

// 查询能源折标系数详细
export function getEnercoefficient(ecid) {
  return request({
    url: '/enerInfoManage/enercoefficient/' + ecid,
    method: 'get'
  })
}

// 新增能源折标系数
export function addEnercoefficient(data) {
  return request({
    url: '/enerInfoManage/enercoefficient',
    method: 'post',
    data: data
  })
}

// 修改能源折标系数
export function updateEnercoefficient(data) {
  return request({
    url: '/enerInfoManage/enercoefficient',
    method: 'put',
    data: data
  })
}

// 删除能源折标系数
export function delEnercoefficient(ecid) {
  return request({
    url: '/enerInfoManage/enercoefficient/' + ecid,
    method: 'delete'
  })
}

// 导出能源折标系数
export function exportEnercoefficient(query) {
  return request({
    url: '/enerInfoManage/enercoefficient/export',
    method: 'get',
    params: query
  })
}