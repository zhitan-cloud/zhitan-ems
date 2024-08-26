import request from '@/utils/request'


//  根据计量器具ID获取指标信息z
export function listIndex(id) {
  return request({
    url: '/basicsetting/energyindex/meterIndex/'+id,
    method: 'get'
  })
}


// 根据计量器具ID生成或重新生成指标
export function addIndex(id) {
  return request({
    url: '/basicsetting/energyindex/meterIndex/'+id,
    method: 'post'
  })
}

// // 修改计量器具档案附件
// export function updateAnnex(data) {
//   return request({
//     url: '/meter/annex',
//     method: 'put',
//     data: data
//   })
// }
//
// 删除计量器具档案附件
export function delIndex(id) {
  return request({
    url: '/basicsetting/energyindex/' + id,
    method: 'delete'
  })
}
