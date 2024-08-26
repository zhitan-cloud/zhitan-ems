import request from '@/utils/request'

// 查询列表
export function List(indexType,nodeId) {
  return request({
    url: '/basicsetting/modelnode/energyIndex/'+indexType+"/" + nodeId,
    method: 'get',
  })



}
