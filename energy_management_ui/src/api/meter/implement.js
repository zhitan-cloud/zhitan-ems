import request from '@/utils/request'

// 查询计量器具档案维护列表
export function listImplement(query) {
  return request({
    url: '/meter/implement/list',
    method: 'get',
    params: query
  })
}

// 查询计量器具档案维护详细
export function getImplement(id) {
  return request({
    url: '/meter/implement/' + id,
    method: 'get'
  })
}

// 新增计量器具档案维护
export function addImplement(data) {
  return request({
    url: '/meter/implement',
    method: 'post',
    data: data
  })
}

// 修改计量器具档案维护
export function updateImplement(data) {
  return request({
    url: '/meter/implement',
    method: 'put',
    data: data
  })
}

// 删除计量器具档案维护
export function delImplement(id) {
  return request({
    url: '/meter/implement/' + id,
    method: 'delete'
  })
}

// 导出计量器具档案维护
export function exportImplement(query) {
  return request({
    url: '/meter/implement/export',
    method: 'get',
    params: query
  })
}
// 下载量器具档案维护导入模板
export function importTemplate() {
  return request({
    url: '/meter/implement/importTemplate',
    method: 'get'
  })
}
