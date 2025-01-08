import request from '@/utils/request'

// 获取列表
//富工接口
export function getSettingIndex(query) {
    return request({
        url: '/basicsetting/modelnode/energyIndex/' + query.indexType + "/" + query.nodeId,
        method: 'get',
        params:query,
    })
}

export function getStartStop(indexid) {
    return request({
        url: '/system/alarmitem/getStartStop/' + indexid,
        async: false,
        method: 'get'
    })
}

// 查询预id下设置限值的数量-采集点管理
export function getSetting(id) {
    return request({
        url: '/system/alarmitem/getSettingInfo/' + id,
        method: 'get',
    })
}



// 查询报警限值类型维护列表
export function listLimitType(query) {
    return request({
        url: '/basicsetting/limitType/list',
        method: 'get',
        params: query
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


// 修改预报警设置
export function updateSet(data, type) {
    return request({
        url: '/system/alarmitem/startstop/' + type,
        method: 'post',
        data: data
    })
}



// 查询预id下设置限值的数量-统计指标管理
export function getSettingCount(id) {
    return request({
        url: '/system/alarmitem/getSettingCount/' + id,
        method: 'get'
    })
}

