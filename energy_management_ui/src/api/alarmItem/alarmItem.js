import request from '@/utils/request'

// 查询预报警设置列表
export function listSet(query) {
  return request({
    url: '/system/alarmitem/list',
    method: 'get',
    params: query
  })
}

// 查询预报警设置详细
export function getSet(id) {
  return request({
    url: '/system/alarmitem/' + id,
    method: 'get'
  })
}

// 新增预报警设置
export function addSet(data) {
  return request({
    url: '/system/alarmitem',
    method: 'post',
    data: data
  })
}

// 修改预报警设置
export function updateSet(data,type) {
  return request({
    url: '/system/alarmitem/startstop/'+type,
    method: 'post',
    data: data
  })
}

// 删除预报警设置
export function delSet(id) {
  return request({
    url: '/system/alarmitem/' + id,
    method: 'delete'
  })
}

// 导出预报警设置
export function exportSet(query) {
  return request({
    url: '/system/alarmitem/export',
    method: 'get',
    params: query
  })
}


//
export function getStartStop(indexid) {
  return request({
    url: '/system/alarmitem/getStartStop/'+indexid,
    async: false,
    method: 'get'
  })
}

// 查询预id下设置限值的数量
export function getSettingCount(id) {
  return request({
    url: '/system/alarmitem/getSettingCount/'+id,
    method: 'get',
  })
}

//弹出窗口
// 查询预报警设置详细
export function getSetting(id) {
  return request({
    url: '/system/alarmitem/getSettingInfo/'+id,
    method: 'get',
  })
}
// 修改预报警设置
export function updateSetting(data) {
  return request({
    url: '/system/alarmitem',
    method: 'put',
    data: data
  })
}
//富工接口
export function getSettingIndex(para) {
  // console.log("nodeId=="+para.nodeId+"index=="+para.indexType);
  return request({
    url: '/basicsetting/modelnode/energyIndex/'+para.indexType+"/" + para.nodeId,
    method: 'get'
  })
}


// 修改表单数据
export function updateDialogForm(data) {
  return request({
    url: '/system/alarmitem/editLimitVal',
    method: 'put',
    data: data
  })
}
