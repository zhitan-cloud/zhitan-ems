import request from '@/utils/request'

// 查询设备档案附件列表
export function listArchives(query) {
  return request({
    url: '/gateway/gatewaysetting/list',
    method: 'get',
    params: query
  })
}

// 查询设备档案附件列表
export function getlist(query) {
    return request({
      url: '/gateway/gatewaysetting/monitor',
      method: 'get',
    
    })
  }
// 查询设备档案附件详细
export function getArchives(createTime) {
  return request({
    url: '/gateway/gatewaysetting/' + createTime,
    method: 'get'
  })
}

// 新增设备档案附件
export function addArchives(data) {
  return request({
    url: '/gateway/gatewaysetting/',
    method: 'post',
    data: data
  })
}

// 修改设备档案附件
export function updateArchives(data) {
  return request({
    url: '/gateway/gatewaysetting',
    method: 'put',
    data: data
  })
}

// 删除设备档案附件
export function delArchives(createTime) {
  return request({
    url: '/gateway/gatewaysetting/' + createTime,
    method: 'delete'
  })
}

// 导出设备档案附件
export function exportAnnex(query) {
  return request({
    url: '/facility/annex/export',
    method: 'get',
    params: query
  })
}