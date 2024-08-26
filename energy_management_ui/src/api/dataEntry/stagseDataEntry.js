import request from '@/utils/request'
//获取模型下的指标
export function getSettingIndex(nodeId) {
  return request({
    url: '/basicsetting/modelnode/energyIndex/' + nodeId,
    method: 'get',
  })
}
// 查询阶段数据录入列表
export function listStagseDataEntry(query) {
  return request({
    url: '/dataEntry/stagseDataEntry/list',
    method: 'get',
    params: query
  })
}
export function listStagseDataEdit(query) {
  return request({
    url: '/dataEntry/stagseDataEntry/edit',
    method: 'get',
    params: query
  })
}
// 新增阶段数据
export function addStagseDataEntry(data) {
  return request({
    url: '/dataEntry/stagseDataEntry',
    method: 'post',
    data: data
  })
}
// 查询阶段数据录入列表
export function stagseDataEntryEdit(query) {
  return request({
    url: '/dataEntry/stagseDataEntry/listEdit',
    method: 'get',
    params: query
  })
}
