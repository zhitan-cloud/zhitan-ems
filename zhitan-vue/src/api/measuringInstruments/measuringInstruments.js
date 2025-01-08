import request from '@/utils/request'


// 获取列表
export function getMaintainList(data) {
  return request({
    url: '/meter/implement/list',
    method: 'get',
    params: data
  })
}

//新增
export function addMaintain(data) {
  return request({
    url: '/meter/implement',
    method: 'post',
    data
  })
}


// 修改
export function updateMaintain(data) {
  return request({
    url: '/meter/implement',
    method: 'put',
    data: data
  })
}


// 删除参数配置
export function delMaintain(id) {
  return request({
    url: '/meter/implement/' + id,
    method: 'delete'
  })
}

// 指标列表  
export function getTargetList(id) {
  return request({
    url: '/basicsetting/energyindex/meterIndex/' + id,
    method: 'get',
  })
}

// 生成指标
export function addTarget(id) {
  return request({
    url: '/basicsetting/energyindex/meterIndex/' + id,
    method: 'post',
  })
}

// 删除指标
export function delTarget(id) {
  return request({
    url: '/basicsetting/energyindex/' + id,
    method: 'delete',
  })
}



// 获取附件列表
export function getAnnexList(data) {
  return request({
    url: '/meter/annex/list',
    method: 'get',
    params: data
  })
}

 

// // 获取监测能源类型列表
// export function energyTypeList() {
//   return request({
//     url: '/statisticsAnalysis/queryEnergyTypeList',
//     method: 'get',
//   })
// }

 




