import request from '@/utils/request'

// 查询轮值方案列表
export function listSchemeItem(query) {
  return request({
    url: '/rosteringSchemeitem/schemeItem/list',
    method: 'get',
    params: query
  })
}

// 查询轮值方案详细
export function getSchemeItem(description) {
  return request({
    url: '/rosteringSchemeitem/schemeItem/' + description,
    method: 'get'
  })
}

// 新增轮值方案
export function addSchemeItem(data) {
  return request({
    url: '/rosteringSchemeitem/schemeItem',
    method: 'post',
    data: data
  })
}

// 修改轮值方案
export function updateSchemeItem(data) {
  return request({
    url: '/rosteringSchemeitem/schemeItem',
    method: 'put',
    data: data
  })
}

// 删除轮值方案
export function delSchemeItem(description) {
  return request({
    url: '/rosteringSchemeitem/schemeItem/' + description,
    method: 'delete'
  })
}

// 导出轮值方案
export function exportSchemeItem(query) {
  return request({
    url: '/rosteringSchemeitem/schemeItem/export',
    method: 'get',
    params: query
  })
}
export function listDutyManagement(query) {
  return request({
    url: '/workforce/dutyManagement/list',
    method: 'get',
    params: query
  })
}
