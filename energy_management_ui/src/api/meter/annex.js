import request from '@/utils/request'


// 查询计量器具档案附件列表
export function listAnnex(query) {
  return request({
    url: '/meter/annex/list',
    method: 'get',
    params: query
  })
}

// 查询计量器具档案附件详细
export function getAnnex(id) {
  return request({
    url: '/meter/annex/' + id,
    method: 'get'
  })
}

// 新增计量器具档案附件
export function addAnnex(data) {
  return request({
    url: '/meter/annex',
    method: 'post',
    data: data
  })
}

// 修改计量器具档案附件
export function updateAnnex(data) {
  return request({
    url: '/meter/annex',
    method: 'put',
    data: data
  })
}

// 删除计量器具档案附件
export function delAnnex(id) {
  return request({
    url: '/meter/annex/' + id,
    method: 'delete'
  })
}

// 导出计量器具档案附件
export function exportAnnex(query) {
  return request({
    url: '/meter/annex/export',
    method: 'get',
    params: query
  })
}

// 下载量器具档案维护导入模板
export function seeFile(data) {
  return request({
    url: '/meter/annex/assignDownload',
    method: 'post',
    params: data
  })
}
