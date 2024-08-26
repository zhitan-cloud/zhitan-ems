import request from '@/utils/request'

// 查询计量器具统计查询列表
export function listImplementCount(query) {
  return request({
    url: '/meter/implementCount/list',
    method: 'get',
    params: query
  })
}

// 查询计量器具统计查询详细
export function getImplementCount(code) {
  return request({
    url: '/meter/implementCount/' + code,
    method: 'get'
  })
}

// 新增计量器具统计查询
export function addImplementCount(data) {
  return request({
    url: '/meter/implementCount',
    method: 'post',
    data: data
  })
}

// 修改计量器具统计查询
export function updateImplementCount(data) {
  return request({
    url: '/meter/implementCount',
    method: 'put',
    data: data
  })
}

// 删除计量器具统计查询
export function delImplementCount(code) {
  return request({
    url: '/meter/implementCount/' + code,
    method: 'delete'
  })
}

// 导出计量器具统计查询
export function exportImplementCount(query) {
  return request({
    url: '/meter/implementCount/export',
    method: 'get',
    params: query
  })
}
