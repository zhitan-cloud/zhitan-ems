import request from '@/utils/request'

// 查询计算函数列表
export function listFunction(query) {
  return request({
    url: '/basicsetting/function/list',
    method: 'get',
    params: query
  })
}

// 查询计算函数详细
export function getFunction(info) {
  return request({
    url: '/basicsetting/function/' + info,
    method: 'get'
  })
}

// 新增计算函数
export function addFunction(data) {
  return request({
    url: '/basicsetting/function',
    method: 'post',
    data: data
  })
}

// 修改计算函数
export function updateFunction(data) {
  return request({
    url: '/basicsetting/function',
    method: 'put',
    data: data
  })
}

// 删除计算函数
export function delFunction(info) {
  return request({
    url: '/basicsetting/function/' + info,
    method: 'delete'
  })
}

// 导出计算函数
export function exportFunction(query) {
  return request({
    url: '/basicsetting/function/export',
    method: 'get',
    params: query
  })
}