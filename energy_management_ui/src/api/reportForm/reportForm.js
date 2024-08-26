import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

//
export function reportFormList(query) {
  return request({
    url: '/reportForm/list',
    method: 'get',
    params: query
  })
}

// 查询日报表
export function reportStatisticsList(query) {
  return request({
    url: '/report/reportStatistics/list',
    method: 'get',
    params: query
  })
}
// 导出综合报表
export function exportList(query) {
  return request({
    url: '/report/reportStatistics/export',
    method: 'get',
    params: query
  })
}
