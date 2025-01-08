import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 模型配置管理-能源品种管理-分页列表
export function listEnergyVarieties(query) {
  return request({
    url: '/enerInfoManage/enerclass/page',
    method: 'get',
    params: query
  })
}
// 模型配置管理-能源品种管理-全部列表
export function listEnergyVarietiesList(query) {
  return request({
    url: '/enerInfoManage/enerclass/list',
    method: 'get',
    params: query
  })
}
// 模型配置管理-能源品种管理-新增
export function addEnergyVarieties(data) {
  return request({
    url: '/enerInfoManage/enerclass',
    method: 'post',
    data: data
  })
}
// 模型配置管理-能源品种管理-查看
export function getEnergyVarieties(id) {
  return request({
    url: '/enerInfoManage/enerclass/' + parseStrEmpty(id),
    method: 'get'
  })
}
// 模型配置管理-能源品种管理-编辑
export function updateEnergyVarieties(data) {
  return request({
    url: '/enerInfoManage/enerclass',
    method: 'put',
    data: data
  })
}
// 模型配置管理-能源品种管理-删除
export function delEnergyVarieties(id) {
  return request({
    url: '/enerInfoManage/enerclass/' + id,
    method: 'delete'
  })
}