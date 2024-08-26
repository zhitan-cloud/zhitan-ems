import request from '@/utils/request'

// 查询产品列表
export function listProduct(query) {
  return request({
    url: '/enerInfoManage/product/list',
    method: 'get',
    params: query
  })
}

// 查询产品详细
export function getProduct(productid) {
  return request({
    url: '/enerInfoManage/product/' + productid,
    method: 'get'
  })
}

// 新增产品
export function addProduct(data) {
  return request({
    url: '/enerInfoManage/product',
    method: 'post',
    data: data
  })
}

// 修改产品
export function updateProduct(data) {
  return request({
    url: '/enerInfoManage/product',
    method: 'put',
    data: data
  })
}

// 删除产品
export function delProduct(productid) {
  return request({
    url: '/enerInfoManage/product/' + productid,
    method: 'delete'
  })
}

// 导出产品
export function exportProduct(query) {
  return request({
    url: '/enerInfoManage/product/export',
    method: 'get',
    params: query
  })
}
export function getFatherCP() {
  return request({
    url: '/enerInfoManage/product/getFatherCP',
    method: 'get',
  })
}
