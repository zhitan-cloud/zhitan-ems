import request from '@/utils/request'
// 查询工序单耗统计 报表 指标展示内容
export function selectEnergyPicList(query) {
  return request({
    url: '/balance/energyBalance/listEnergyPicIndex',
    method: 'get',
    params: query
  })
}
