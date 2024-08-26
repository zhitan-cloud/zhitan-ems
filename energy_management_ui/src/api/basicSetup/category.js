import request from '@/utils/request'

// 查询category列表
export function listCategory(query) {
  return request({
    url: '/basicSetup/category/list',
    method: 'get',
    params: query
  })
}

// 查询category详细
export function getCategory(id) {
  return request({
    url: '/basicSetup/category/' + id,
    method: 'get'
  })
}

// 新增category
export function addCategory(data) {
  return request({
    url: '/basicSetup/category',
    method: 'post',
    data: data
  })
}

// 修改category
export function updateCategory(data) {
  return request({
    url: '/basicSetup/category',
    method: 'put',
    data: data
  })
}

// 删除category
export function delCategory(id) {
  return request({
    url: '/basicSetup/category/' + id,
    method: 'delete'
  })
}

// 导出category
export function exportCategory(query) {
  return request({
    url: '/basicSetup/category/export',
    method: 'get',
    params: query
  })
}
