import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 计量器具档案-配电室管理-分页列表
export function listDistributionRoom(query) {
  return request({
    url: '/powerDistribution/pageList',
    method: 'get',
    params: query
  })
}
// 计量器具档案-配电室管理-全部列表
export function listDistributionRoomList(query) {
  return request({
    url: '/powerDistribution/list',
    method: 'get',
    params: query
  })
}
// 计量器具档案-配电室管理-新增
export function addDistributionRoom(data) {
  return request({
    url: '/powerDistribution/add',
    method: 'post',
    data: data
  })
}
// 计量器具档案-配电室管理-查看
export function getDistributionRoom(query) {
  return request({
    url: '/powerDistribution/getDetail',
    method: 'get',
    params: query
  })
}
// 计量器具档案-配电室管理-编辑
export function updateDistributionRoom(data) {
  return request({
    url: '/powerDistribution/edit',
    method: 'post',
    data: data
  })
}
// 计量器具档案-配电室管理-删除
export function delDistributionRoom(query) {
  return request({
    url: '/powerDistribution/remove',
    method: 'delete',
    params: query
  })
}