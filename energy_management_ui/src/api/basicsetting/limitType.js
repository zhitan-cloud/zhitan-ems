import request from '@/utils/request'

// 查询报警限值类型维护列表
export function listLimitType(query) {
  return request({
    url: '/basicsetting/limitType/list',
    method: 'get',
    params: query
  })
}

// 查询报警限值类型维护详细
export function getLimitType(id) {
  return request({
    url: '/basicsetting/limitType/' + id,
    method: 'get'
  })
}

// 新增报警限值类型维护
export function addLimitType(data) {
  return request({
    url: '/basicsetting/limitType',
    method: 'post',
    data: data
  })
}

// 修改报警限值类型维护
export function updateLimitType(data) {
  return request({
    url: '/basicsetting/limitType',
    method: 'put',
    data: data
  })
}

// 删除报警限值类型维护
export function delLimitType(id) {
  return request({
    url: '/basicsetting/limitType/' + id,
    method: 'delete'
  })
}

// 导出报警限值类型维护
export function exportLimitType(query) {
  return request({
    url: '/basicsetting/limitType/export',
    method: 'get',
    params: query
  })
}
