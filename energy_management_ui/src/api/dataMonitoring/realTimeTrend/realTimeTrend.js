import request from '@/utils/request'

//列表接口
export function getTrendSettingIndex(query) {
  return request({
    url: '/dataMonitoring/realTimeTrend/energyIndex/list',
    method: 'get',
    params: query
  })
}

//折线图数据接口
export function getTrendSettingIndexLineData(codes, minute, count) {
  return request({
    url: '/dataMonitoring/realTimeTrend/energyIndex/lineList/' + codes + "/" + minute + "/" + count,
    method: 'get',
  })
}

//趋势图列表接口
export function getSvgTrendSettingIndex(query) {
  return request({
    url: '/dataMonitoring/realTimeTrend/svgTrendView/energyIndex/list',
    method: 'get',
    params: query
  })
}

//daochu
export function exportRealTimeTrend(query) {
  return request({
    url: '/dataMonitoring/realTimeTrend/export',
    method: 'get',
    params: query
  })
}
