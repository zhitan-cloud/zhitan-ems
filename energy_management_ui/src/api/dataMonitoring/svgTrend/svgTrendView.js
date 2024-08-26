import request from '@/utils/request'

//列表接口
export function getSvgTrendSettingIndex(query) {
  return request({
    url: '/dataMonitoring/svgTrendView/energyIndex/list',
    method: 'get',
    params: query
  })
}
