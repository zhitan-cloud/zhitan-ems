import request from '@/utils/request'

// 获取列表
export function listTemplate(params) {
  return request({
    url: '/system/template/list',
    method: 'get',
    params
  })
}

// 新增
export function addTemplate(data) {
  return request({
    url: '/system/template',
    method: 'post',
    data
  })
}

// 查详情
export function getTempById(params) {
  return request({
    url: `/system/template/${params.id}`,
    method: 'get',
    params
  })
}

// 编辑
export function putTemplate(data) {
  return request({
    url: '/system/template',
    method: 'put',
    data
  })
}

//删除
export function delTempById(id) {
  return request({
    url: '/system/template/' + id,
    method: 'delete',
  })
}