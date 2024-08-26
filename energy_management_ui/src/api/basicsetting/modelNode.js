import request from '@/utils/request'

// 查询模型节点列表
export function modelNodeTree(query, withAuth) {
  if (withAuth) {
    return request({
      url: '/basicsetting/modelnode/treelist/withAuth',
      method: 'get',
      params: query
    })
  } else {
    return request({
      url: '/basicsetting/modelnode/treelist',
      method: 'get',
      params: query
    })
  }
}

// 查询模型节点列表
export function listModelNode(query) {
  return request({
    url: '/basicsetting/modelnode/list',
    method: 'get',
    params: query
  })
}

// 查询模型节点详细
export function getModelNode(nodeId) {
  return request({
    url: '/basicsetting/modelnode/' + nodeId,
    method: 'get'
  })
}

// 新增模型节点
export function addModelNode(data) {
  return request({
    url: '/basicsetting/modelnode',
    method: 'post',
    data: data
  })
}

// 修改模型节点
export function updateModelNode(data) {
  return request({
    url: '/basicsetting/modelnode',
    method: 'put',
    data: data
  })
}

// 删除模型节点
export function delModelNode(nodeId) {
  return request({
    url: '/basicsetting/modelnode/' + nodeId,
    method: 'delete'
  })
}

// 导出模型节点
export function exportModelNode(query) {
  return request({
    url: '/basicsetting/modelnode/export',
    method: 'get',
    params: query
  })
}

export function updateModelNodeOrder(param) {
  return request({
    url: '/basicsetting/modelnode/order',
    method: 'put',
    data: param
  })
}

export function hasEnergyIndex(nodeId) {
  return request({
    url: '/basicsetting/modelnode/hasEnergyIndex',
    method: 'get',
    params: {nodeId: nodeId}
  })
}

export function getSettingDevice(nodeId) {
  return request({
    url: '/basicsetting/modelnode/device/' + nodeId,
    method: 'get'
  })
}

export function setDevice(nodeId, deviceIds) {
  return request({
    url: '/basicsetting/modelnode/device/' + nodeId,
    method: 'post',
    data: deviceIds
  })
}

export function delDevice(nodeId, deviceId) {
  return request({
    url: '/basicsetting/modelnode/device/' + nodeId,
    method: 'delete',
    data: [deviceId]
  })
}
//获取模型下的能源品种
export function getSettingEnergy(nodeId) {
  return request({
    url: '/basicsetting/modelnode/energy/' + nodeId,
    method: 'get'
  })
}

export function setEnergy(nodeId, energyIds) {
  return request({
    url: '/basicsetting/modelnode/energy/' + nodeId,
    method: 'post',
    data: energyIds
  })
}

export function delEnergy(nodeId, energyId) {
  return request({
    url: '/basicsetting/modelnode/energy/' + nodeId,
    method: 'delete',
    data: [energyId]
  })
}

export function getSettingProduct(nodeId) {
  return request({
    url: '/basicsetting/modelnode/product/' + nodeId,
    method: 'get'
  })
}

export function setProduct(nodeId, productIds) {
  return request({
    url: '/basicsetting/modelnode/product/' + nodeId,
    method: 'post',
    data: productIds
  })
}

export function delProduct(nodeId, productId) {
  return request({
    url: '/basicsetting/modelnode/product/' + nodeId,
    method: 'delete',
    data: [productId]
  })
}
//获取模型下的指标
export function getSettingIndex(nodeId) {
  return request({
    url: '/basicsetting/modelnode/energyIndex/' + nodeId,
    method: 'get'
  })
}

export function getSettingCollectIndex(nodeId) {
  return request({
    url: '/basicsetting/modelnode/energyIndex/COLLECT/' + nodeId,
    method: 'get'
  })
}

export function setNodeToIndex(nodeId, indexIds, indexType) {
  return request({
    url: '/basicsetting/modelnode/energyIndex/' + nodeId + "/" + indexType,
    method: 'post',
    data: indexIds
  })
}

export function delIndex(nodeId, indexId) {
  return request({
    url: '/basicsetting/modelnode/energyIndex/' + nodeId,
    method: 'delete',
    data: [indexId]
  })
}
