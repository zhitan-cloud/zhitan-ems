import request from '@/utils/request'

// 查询设备档案附件列表
export function listAnnex(query) {
  return request({
    url: '/facility/annex/list',
    method: 'get',
    params: query
  })
}

// 查询设备档案附件详细
export function getAnnex(createTime) {
  return request({
    url: '/facility/annex/' + createTime,
    method: 'get'
  })
}

// 新增设备档案附件
export function addAnnex(data) {
  return request({
    url: '/facility/annex',
    method: 'post',
    data: data
  })
}

// 修改设备档案附件
export function updateAnnex(data) {
  return request({
    url: '/facility/annex',
    method: 'put',
    data: data
  })
}

// 删除设备档案附件
export function delAnnex(createTime) {
  return request({
    url: '/facility/annex/' + createTime,
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