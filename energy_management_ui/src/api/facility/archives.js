import request from '@/utils/request'

// 查询设备档案列表
export function listArchives(query) {
  return request({
    url: '/facility/archives/list',
    method: 'get',
    params: query
  })
}

// 查询设备档案详细
export function getArchives(id) {
  return request({
    url: '/facility/archives/' + id,
    method: 'get'
  })
}

// 新增设备档案
export function addArchives(data) {
  return request({
    url: '/facility/archives',
    method: 'post',
    data: data
  })
}

// 修改设备档案
export function updateArchives(data) {
  return request({
    url: '/facility/archives',
    method: 'put',
    data: data
  })
}

// 删除设备档案
export function delArchives(id) {
  return request({
    url: '/facility/archives/' + id,
    method: 'delete'
  })
}

// 检定恢复设备档案
export function resetArchives(id) {
  return request({
    url: '/facility/archives/' + id,
    method: 'post'
  })
}

// 导出设备档案
export function exportArchives(query) {
  return request({
    url: '/facility/archives/export',
    method: 'get',
    params: query
  })
}

// 下载设备档案维护导入模板
export function importTemplate() {
  return request({
    url: '/facility/archives/importTemplate',
    method: 'get'
  })
}
