import request from '@/utils/request'

// 峰平谷能耗分析-峰平谷时段统计-列表
export function listTimeSharing(query) {
  return request({
    url: '/peakValley/segmentAnalysis/hour',
    method: 'get',
    params: query
  })
}