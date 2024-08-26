import request from '@/utils/request'

// 查询对标列表
export function listPhaseBenchmarking(query) {
  return request({
    url: '/benchmarking/phaseBenchmarking/list',
    method: 'get',
    params: query
  })
}

// 查询对标详细
export function listRealTimeListrealTime(query) {
  return request({
    url: '/benchmarking/phaseBenchmarking/realTimeListrealTime',
    method: 'get',
    params: query
  })
}
