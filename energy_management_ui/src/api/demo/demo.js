import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listDemo(query) {
  return request({
    url: '/system/demo/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getDemo(id) {
  return request({
    url: '/system/demo/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addDemo(data) {
  return request({
    url: '/system/demo',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateDemo(data) {
  return request({
    url: '/system/demo',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delDemo(id) {
  return request({
    url: '/system/demo/' + id,
    method: 'delete'
  })
}

// 导出【请填写功能名称】
export function exportDemo(query) {
  return request({
    url: '/system/demo/export',
    method: 'get',
    params: query
  })
}