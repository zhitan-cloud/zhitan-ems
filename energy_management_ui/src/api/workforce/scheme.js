import request from '@/utils/request'

// 查询轮值方案列表
export function listScheme(query) {
  return request({
    url: '/rosteringSrcheme/scheme/list',
    method: 'get',
    params: query
  })
}

// 查询轮值方案详细
export function getScheme(id) {
  return request({
    url: '/rosteringSrcheme/scheme/' + id,
    method: 'get'
  })
}

// 新增轮值方案
export function addScheme(data) {
  return request({
    url: '/rosteringSrcheme/scheme',
    method: 'post',
    data: data
  })
}

// 修改轮值方案
export function updateScheme(data) {
  return request({
    url: '/rosteringSrcheme/scheme',
    method: 'put',
    data: data
  })
}

// 删除轮值方案
export function delScheme(id) {
  return request({
    url: '/rosteringSrcheme/scheme/' + id,
    method: 'delete'
  })
}

// 导出轮值方案
export function exportScheme(query) {
  return request({
    url: '/rosteringSrcheme/scheme/export',
    method: 'get',
    params: query
  })
}