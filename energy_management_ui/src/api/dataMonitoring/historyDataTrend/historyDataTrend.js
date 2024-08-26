import request from '@/utils/request'

//列表接口
export function getCodeList(query) {
  return request({
    url: '/dataMonitoring/historyDataTrend/getHistoricalDataByIndexId',
    method: 'get',
    params: query
  })
}

//获取点位列表接口
export function getIndexList(id) {
  return request({
    url: '/basicsetting/model/getEnergyIndexByModelId',
    method: 'get',
    params: {
      modelId: id
    }
  })
}

export function exportHistoryDataTrend(query) {
  return request({
    url: '/dataMonitoring/historyDataTrend/export',
    method: 'get',
    params: query
  })
}

// // 实时监测-导出Excel  get
// dataMonitoring/realTimeTrend/export?nodeId=ca900cb2-70d9-4bd7-8ff1-f93e5808f2d1

// // 历史数据-导出Excel  get
// dataMonitoring/historyDataTrend/export?nodeId=b7a014a7-a994-457d-ad10-31654f003efa&indexId=2a026b78-3a1d-4f28-8f62-23bd34b1b118&pickerType=datetime&dataTime=2023-03-16+14:50:43&timeType=HOUR
