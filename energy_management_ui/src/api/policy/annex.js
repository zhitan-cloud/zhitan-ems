import request from '@/utils/request'

// 查询政策法规附件列表
export function listAnnex(query) {
  return request({
    url: '/policy/annex/list',
    method: 'get',
    params: query
  })
}

// 查询政策法规附件详细
export function getAnnex(filePath) {
  return request({
    url: '/policy/annex/' + filePath,
    method: 'get'
  })
}

// 新增政策法规附件
export function addAnnex(data) {
  return request({
    url: '/policy/annex',
    method: 'post',
    data: data
  })
}

// 修改政策法规附件
export function updateAnnex(data) {
  return request({
    url: '/policy/annex',
    method: 'put',
    data: data
  })
}

// 删除政策法规附件
export function delAnnex(filePath) {
  return request({
    url: '/policy/annex/' + filePath,
    method: 'delete'
  })
}

// 导出政策法规附件
export function exportAnnex(query) {
  return request({
    url: '/policy/annex/export',
    method: 'get',
    params: query
  })
}