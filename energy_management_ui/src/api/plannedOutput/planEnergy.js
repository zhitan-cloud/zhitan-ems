import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listEnergy(query) {
  return request({
    url: '/PlanEnergy/list',
    method: 'get',
    params: query
  })
}
// 新增【请填写功能名称】
export function addEnergy(data) {
  return request({
    url: '/PlanEnergy',
    method: 'post',
    data: data
  })
}
