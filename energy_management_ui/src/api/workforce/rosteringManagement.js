import request from '@/utils/request'

// 查询排班方案列表
export function getschemeNameList(query) {
  return request({
    url: '/workforce/rosteringManagement/schemeNamelist',
    method: 'get',
    params: query
  })
}


// 查询排班表查询列表
export function listRosteringManagement(query) {
  return request({
    url: '/workforce/rosteringManagement/list',
    method: 'get',
    params: query
  })
}

// 查询排班表查询详细
export function getRosteringManagement(id) {
  return request({
    url: '/workforce/rosteringManagement/' + id,
    method: 'get'
  })
}

// 新增排班表查询
export function addRosteringManagement(data) {
  console.log(data);
  return request({
    url: '/workforce/rosteringManagement',
    method: 'post',
    data: data
  })
}

// 修改排班表查询
export function updateRosteringManagement(data) {
  return request({
    url: '/workforce/rosteringManagement',
    method: 'put',
    data: data
  })
}

// 删除排班表查询
export function delRosteringManagement(id) {
  return request({
    url: '/workforce/rosteringManagement/' + id,
    method: 'delete'
  })
}

// 导出排班表查询
export function exportRosteringManagement(query) {
  return request({
    url: '/workforce/rosteringManagement/export',
    method: 'get',
    params: query
  })
}
