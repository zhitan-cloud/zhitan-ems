import request from '@/utils/request'

export function govReportList(query) {
  return request({
    url: '/govReports/govReports/list',
    method: 'get',
    params: query
  })
}