import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 辅助录入-产品产量录入-列表
export function listProductoutput(query) {
  return request({
    url: '/productoutput/list',
    method: 'get',
    params: query
  })
}
// 辅助录入-产品产量录入-新增
export function addProductoutput(data) {
  return request({
    url: '/productoutput',
    method: 'post',
    data: data
  })
}
// 辅助录入-产品产量录入-编辑
export function updateProductoutput(data) {
  return request({
    url: '/productoutput',
    method: 'put',
    data: data
  })
}
// 辅助录入-产品产量录入-删除
export function delProductoutput(id) {
  return request({
    url: '/productoutput/' + id,
    method: 'delete'
  })
}