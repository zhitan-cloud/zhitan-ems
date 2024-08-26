import request from '@/utils/request'

// 查询政策法规维护列表
export function listPolicyInsert(query) {
  return request({
    url: '/policy/policyInsert/list',
    method: 'get',
    params: query
  })
}

// 查询政策法规维护详细
export function getPolicyInsert(id) {
  return request({
    url: '/policy/policyInsert/' + id,
    method: 'get'
  })
}

// 新增政策法规维护
export function addPolicyInsert(data) {
  return request({
    url: '/policy/policyInsert',
    method: 'post',
    data: data
  })
}

// 修改政策法规维护
export function updatePolicyInsert(data) {
  return request({
    url: '/policy/policyInsert',
    method: 'put',
    data: data
  })
}

// 删除政策法规维护
export function delPolicyInsert(id) {
  return request({
    url: '/policy/policyInsert/' + id,
    method: 'delete'
  })
}

// 导出政策法规维护
export function exportPolicyInsert(query) {
  return request({
    url: '/policy/policyInsert/export',
    method: 'get',
    params: query
  })
}