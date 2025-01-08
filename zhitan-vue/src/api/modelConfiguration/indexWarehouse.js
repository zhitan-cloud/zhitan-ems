import request from '@/utils/request'

// 获取树列表
export function treeList(params) {
    return request({
        url: '/basicsetting/modelnode/treelist',
        method: 'get',
        params
    })
}

// 点击树节点
export function updateModelNodeOrder(param) {
    return request({
        url: '/basicsetting/modelnode/order',
        method: 'put',
        data: param
    })
}


//重点设备
export function getPointFacility() {
    return request({
        url: '/statisticalData/comprehensiveStatistics/getPointFacility',
        method: 'get'
    })
}

// 查询指标信息列表
export function listEnergyindex(query) {
    return request({
        url: '/basicsetting/energyindex/list',
        method: 'get',
        params: query
    })
}

// 新增指标信息
export function addEnergyindex(nodeId, data) {
    return request({
        url: '/basicsetting/energyindex/' + nodeId,
        method: 'post',
        data: data
    })
}

// 修改指标信息
export function updateEnergyindex(data) {
    return request({
        url: '/basicsetting/energyindex',
        method: 'put',
        data: data
    })
}

// 查询指标信息详细
export function getEnergyindex(indexId) {
    return request({
        url: '/basicsetting/energyindex/' + indexId,
        method: 'get'
    })
}

// 删除指标信息
export function delEnergyindex(nodeId, indexId) {
    return request({
        url: '/basicsetting/energyindex/' + nodeId + '/' + indexId,
        method: 'delete'
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

export function hasEnergyIndex(nodeId) {
    return request({
        url: '/basicsetting/modelnode/hasEnergyIndex',
        method: 'get',
        params: { nodeId: nodeId }
    })
}

export function parseFormula(data) {
    return request({
        url: '/basicsetting/indexStorage/parseFormula',
        method: 'post',
        data: data
    })
}

// 查询计算函数列表
export function listFunction(query) {
    return request({
        url: '/basicsetting/function/list',
        method: 'get',
        params: query
    })
}

// 删除计算函数
export function delFunction(info) {
    return request({
        url: '/basicsetting/function/' + info,
        method: 'delete'
    })
}


export function getNodeIndex(nodeId) {
    return request({
        url: '/basicsetting/energyindex/includeChildrenNode/' + nodeId,
        method: 'GET'
    })
}


export function includeChildrenNode(query) {
    return request({
        url: '/basicsetting/energyindex/getIndexByCode',
        method: 'GET',
        params: query
    })
}






export function getIndexStorage(indexId) {
    return request({
        url: '/basicsetting/indexStorage/' + indexId,
        method: 'GET'
    })
}

export function saveIndexStorage(indexId, data) {
    return request({
        url: '/basicsetting/indexStorage/' + indexId,
        method: 'post',
        data: data
    });
}
