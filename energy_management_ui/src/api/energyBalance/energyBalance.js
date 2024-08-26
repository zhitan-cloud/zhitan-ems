import request from '@/utils/request'

// 能源损失分析
export function lossAnalysisList(query) {
  return request({
    url: '/balance/energyBalance/lossAnalysisList',
    method: 'get',
    params: query
  })
}
export function energyLossTable(query) {
  return request({
    url: '/balance/energyBalance/lossTableList',
    method: 'get',
    params: query
  })
}
// 能源平衡分析
export function energyBalanceList(query) {
  return request({
    url: '/balance/energyBalance/list',
    method: 'get',
    params: query
  })
}
// 能源平衡分析
export function energyBalanceTable(query) {
  return request({
    url: '/balance/energyBalance/tableList',
    method: 'get',
    params: query
  })
}
