import request from '@/utils/request'


//  根据指标类型和模型节点id获取模型下的指标列表
export function listIndex(indexType,nodeId) {
  return request({
    url: '/basicsetting/modelnode/energyIndex/'+indexType+'/'+nodeId,
    method: 'get'
  })
}


// 根据计量器具ID生成或重新生成指标
export function addIndex(data,modeNodeId) {
  return request({
    url: '/energyExamine/assessmentIndex/addIndex/'+modeNodeId,
    method: 'post',
    data:data
  })
}

//
// 删除计量器具档案附件
export function delIndex(id) {
  return request({
    url: '/basicsetting/energyindex/' + id,
    method: 'delete'
  })
}
