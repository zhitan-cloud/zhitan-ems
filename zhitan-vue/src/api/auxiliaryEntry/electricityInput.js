import request from '@/utils/request'


// 辅助录入-用电录入-列表
export function electricityInputList(query) {
    return request({
      url: '/cost/input/list',
      method: 'get',
      params: query
    })
  }
  // 辅助录入-用电录入-新增
  export function electricityInputAdd(data) {
    return request({
      url: '/cost/input',
      method: 'post',
      data: data
    })
  }
  // 辅助录入-用电录入-编辑
  export function electricityInputEdit(data) {
    return request({
      url: '/cost/input',
      method: 'put',
      data: data
    })
  }
  // 辅助录入-用电录入-删除
  export function electricityInputDel(id) {
    return request({
      url: '/cost/input/' + id,
      method: 'delete'
    })
  }

   // 辅助录入-用电录入-详情
   export function electricityInputInfo(id) {
    return request({
      url: '/cost/input/' + id,
      method: 'get'
    })
  }
