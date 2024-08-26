import request from '@/utils/request'

// 查询预报警设置列表
export function listSet(query) {
  return request({
    url: '/system/report/list',
    method: 'get',
    params: query
  })
}

//富工接口
export function getSettingIndex(para) {
  return request({
    url: '/system/report/listNodeEnergyIndex',
    method: 'get',
    params: para
  })
}

// 修改表单数据
export function updateDialogForm(data) {
  return request({
    url: '/system/report/save',
    method: 'put',
    data: data
  })
}

// 查询预报警设置列表
export function updateEnableStatus(query) {
  return request({
    url: '/system/report/updateEnableStatus',
    method: 'get',
    params: query
  })
}
