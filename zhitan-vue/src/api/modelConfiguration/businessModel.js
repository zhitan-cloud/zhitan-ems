import request from '@/utils/request'
import { da } from 'element-plus/es/locales.mjs'



// 查询模型信息列表
export function listModel(query) {
    return request({
        url: '/basicsetting/model/list',
        method: 'get',
        params: query
    })
}


// 新增模型信息
export function addModel(data) {
    return request({
        url: '/basicsetting/model',
        method: 'post',
        data: data
    })
}

// 修改模型信息
export function updateModel(data) {
    return request({
        url: '/basicsetting/model',
        method: 'put',
        data: data
    })
}

// 查询模型信息详细
export function getModel(id) {
    return request({
        url: '/basicsetting/model/' + id,
        method: 'get'
    })
}

// 删除模型信息
export function delModel(id) {
    return request({
        url: '/basicsetting/model/' + id,
        method: 'delete'
    })
}

//获取模型下的设备
export function getSettingDevice(nodeId) {
    return request({
        url: '/basicsetting/modelnode/device/' + nodeId,
        method: 'get'
    })
}
//保存模型下的设备
export function setDevice(nodeId, deviceIds) {
    return request({
        url: '/basicsetting/modelnode/device/' + nodeId,
        method: 'post',
        data: deviceIds
    })
}

//删除模型下的设备
export function delDevice(nodeId, deviceId) {
    return request({
        url: '/basicsetting/modelnode/device/' + nodeId,
        method: 'delete',
        data: [deviceId]
    })
}

//获取模型下的指标
export function getSettingIndex(nodeId) {
    return request({
        url: '/basicsetting/modelnode/energyIndex/' + nodeId,
        method: 'get'
    })
}

// 查询计量器具档案维护列表
export function listImplement(query) {
    return request({
        url: '/meter/implement/list',
        method: 'get',
        params: query
    })
}

//保存业务下的统计指标
export function setNodeToIndex(nodeId, indexIds, indexType) {
    return request({
        url: '/basicsetting/modelnode/energyIndex/' + nodeId + '/' + indexType,
        method: 'post',
        data: indexIds
    })
}

//删除指标
export function delIndex(nodeId, indexId) {
    return request({
        url: '/basicsetting/modelnode/energyIndex/' + nodeId,
        method: 'delete',
        data:  indexId 
    })
}


//采集指标列表
export function collectList(nodeId, query) {
    return request({
        url: '/basicsetting/modelnode/energyIndex/meterImplement/' + nodeId,
        method: 'get',
        params: query
    })
}

