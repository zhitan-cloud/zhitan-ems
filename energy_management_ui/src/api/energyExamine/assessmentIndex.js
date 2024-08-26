import request from '@/utils/request'

// 查询用能考核标准列表
export function listAssessmentIndex(query) {
  return request({
    url: '/energyExamine/assessmentIndex/list',
    method: 'get',
    params: query
  })
}
// 查询用能考核标准列表
export function listAssessmentIndexModel(modeNodeId,query) {
  return request({
    url: '/energyExamine/assessmentIndex/list/'+modeNodeId,
    method: 'get',
    params: query
  })
}

// 查询用能考核标准详细
export function getAssessmentIndex(id) {
  return request({
    url: '/energyExamine/assessmentIndex/' + id,
    method: 'get'
  })
}

// 新增用能考核标准
export function addAssessmentIndex(data) {
  return request({
    url: '/energyExamine/assessmentIndex',
    method: 'post',
    data: data
  })
}

// 修改用能考核标准
export function updateAssessmentIndex(data) {
  return request({
    url: '/energyExamine/assessmentIndex',
    method: 'put',
    data: data
  })
}

// 删除用能考核标准
export function delAssessmentIndex(id) {
  return request({
    url: '/energyExamine/assessmentIndex/' + id,
    method: 'delete'
  })
}

// 导出用能考核标准
export function exportAssessmentIndex(query) {
  return request({
    url: '/energyExamine/assessmentIndex/export',
    method: 'get',
    params: query
  })
}
