import request from '@/utils/request'



// 网关台账统计
export function gatewayStatistics(params) {
    return request({
        url: '/productoutput/ptNum',
        method: 'get',
        params
    })
}



// 网关台账列表
export function gatewayList(params) {
    return request({
        url: '/gatewaySetting/list',
        method: 'get',
        params

    })
}

//网关台账新增
export function gatewayAdd(data) {
    return request({
        url: '/gatewaySetting',
        method: 'post',
        data
    })
}

// 网关台账编辑
export function gatewayEdit(data) {
    return request({
        url: '/gatewaySetting',
        method: 'put',
        data
    })
}



// 网关台账删除
export function gatewayDel(id) {
    return request({
        url: '/gatewaySetting/' + id,
        method: 'delete',
    })
}
 


// 网关台账详情
export function gatewayInfo(id) {
    return request({
        url: '/gatewaySetting/' + id,
        method: 'get',
    })
}


// 网关台账下拉列表
export function gatewayBaseList() {
    return request({
        url: '/gatewaySetting/baseList',
        method: 'get',
    })
}




