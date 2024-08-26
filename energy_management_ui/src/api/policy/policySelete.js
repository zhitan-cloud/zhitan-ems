import request from '@/utils/request'

// 查询政策法规查询列表
export function listPolicyselete(query) {
  return request({
    url: '/policy/policyselete/list',
    method: 'get',
    params: query
  })
}

// 查询政策法规查询详细
export function getPolicyselete(id) {
  return request({
    url: '/policy/policyselete/' + id,
    method: 'get'
  })
}

// 新增政策法规查询
export function addPolicyselete(data) {
  return request({
    url: '/policy/policyselete',
    method: 'post',
    data: data
  })
}

// 修改政策法规查询
export function updatePolicyselete(data) {
  return request({
    url: '/policy/policyselete',
    method: 'put',
    data: data
  })
}

// 删除政策法规查询
export function delPolicyselete(id) {
  return request({
    url: '/policy/policyselete/' + id,
    method: 'delete'
  })
}

// 导出政策法规查询
export function exportPolicyselete(query) {
  return request({
    url: '/policy/policyselete/export',
    method: 'get',
    params: query
  })
}