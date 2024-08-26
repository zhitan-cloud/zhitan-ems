import request from '@/utils/request'

// 查询能源品种设置列表
export function listEnerclass(query) {
  return request({
    url: '/enerInfoManage/enerclass/list',
    method: 'get',
    params: query
  })
}

// 查询能源品种设置详细
export function getEnerclass(enerclassid) {
  return request({
    url: '/enerInfoManage/enerclass/' + enerclassid,
    method: 'get'
  })
}

// 新增能源品种设置
export function addEnerclass(data) {
  return request({
    url: '/enerInfoManage/enerclass',
    method: 'post',
    data: data
  })
}

// 修改能源品种设置
export function updateEnerclass(data) {
  return request({
    url: '/enerInfoManage/enerclass',
    method: 'put',
    data: data
  })
}

// 删除能源品种设置
export function delEnerclass(enerclassid) {
  return request({
    url: '/enerInfoManage/enerclass/' + enerclassid,
    method: 'delete'
  })
}

// 导出能源品种设置
export function exportEnerclass(query) {
  return request({
    url: '/enerInfoManage/enerclass/export',
    method: 'get',
    params: query
  })
}