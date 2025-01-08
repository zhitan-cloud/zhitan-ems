import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 模型配置管理-能源类型管理-分页列表
export function listEnergyType(query) {
  return request({
    url: '/enerInfoManage/energy/page',
    method: 'get',
    params: query
  })
}
// 模型配置管理-能源类型管理-全部列表
export function listEnergyTypeList(query) {
  return request({
    url: '/enerInfoManage/energy/list',
    method: 'get',
    params: query
  })
}
// 模型配置管理-能源类型管理-新增
export function addEnergyType(data) {
  return request({
    url: '/enerInfoManage/energy',
    method: 'post',
    data: data
  })
}
// 模型配置管理-能源类型管理-查看
export function getEnergyType(id) {
  return request({
    url: '/enerInfoManage/energy/' + parseStrEmpty(id),
    method: 'get'
  })
}
// 模型配置管理-能源类型管理-编辑
export function updateEnergyType(data) {
  return request({
    url: '/enerInfoManage/energy',
    method: 'put',
    data: data
  })
}
// 模型配置管理-能源类型管理-删除
export function delEnergyType(id) {
  return request({
    url: '/enerInfoManage/energy/' + id,
    method: 'delete'
  })
}