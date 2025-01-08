import request from '@/utils/request'

// 列表
export function valueManageList(query) {
  return request({
    url: '/benchmarkManage/list',
    method: 'get',
    params: query
  })
}


// 详情
export function valueManageInfo(id) {
  return request({
    url: '/benchmarkManage/' + id,
    method: 'get'
  })
}


// 新建
export function valueManageAdd(data) {
  return request({
    url: '/benchmarkManage',
    method: 'post',
    data
  })
}

// 修改 
export function valueManageEdit(data) {
  return request({
    url: '/benchmarkManage',
    method: 'put',
    data
  })
}


// 删除 
export function valueManageDel(id) {
  return request({
    url: '/benchmarkManage/' + id,
    method: 'delete'
  })
}