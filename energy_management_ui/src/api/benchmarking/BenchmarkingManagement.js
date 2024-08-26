import request from '@/utils/request'

// 查询标杆管理列表
export function listBenchmarkingManagement(query) {
  return request({
    url: '/benchmarking/BenchmarkingManagement/list',
    method: 'get',
    params: query
  })
}

// 查询标杆管理详细
export function getBenchmarkingManagement(indexId) {
  return request({
    url: '/benchmarking/BenchmarkingManagement/' + indexId,
    method: 'get'
  })
}

// 新增标杆管理
export function addBenchmarkingManagement(data) {
  return request({
    url: '/benchmarking/BenchmarkingManagement',
    method: 'post',
    data: data
  })
}

// 修改标杆管理
export function updateBenchmarkingManagement(data) {
  return request({
    url: '/benchmarking/BenchmarkingManagement',
    method: 'put',
    data: data
  })
}

// 删除标杆管理
export function delBenchmarkingManagement(indexId) {
  return request({
    url: '/benchmarking/BenchmarkingManagement/' + indexId,
    method: 'delete'
  })
}

// 导出标杆管理
export function exportBenchmarkingManagement(query) {
  return request({
    url: '/benchmarking/BenchmarkingManagement/export',
    method: 'get',
    params: query
  })
}