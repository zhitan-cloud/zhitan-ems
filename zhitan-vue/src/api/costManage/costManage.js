import request from '@/utils/request'


// 成本管理-单价策略-列表
export function unitPriceStrategyList(query) {
    return request({
        url: '/cost/tactics/list',
        method: 'get',
        params: query
    })
}

// 成本管理-单价策略-列表（不分页）
export function unitPriceStrategyAllList() {
    return request({
        url: '/cost/tactics/allList',
        method: 'get',
    })
}

// 成本管理-单价策略-新增
export function unitPriceStrategyAdd(data) {
    return request({
        url: '/cost/tactics',
        method: 'post',
        data: data
    })
}
// 成本管理-单价策略-编辑
export function unitPriceStrategyEdit(data) {
    return request({
        url: '/cost/tactics',
        method: 'put',
        data: data
    })
}
// 成本管理-单价策略-删除
export function unitPriceStrategyDel(id) {
    return request({
        url: '/cost/tactics/' + id,
        method: 'delete'
    })
}

// 成本管理-单价策略-详情
export function unitPriceStrategyInfo(id) {
    return request({
        url: '/cost/tactics/' + id,
        method: 'get'
    })
}




// 成本管理-单价关联-列表
export function unitPriceCorrelationList(query) {
    return request({
        url: '/cost/relevancy/list',
        method: 'get',
        params: query
    })
}
// 成本管理-单价关联-新增
export function unitPriceCorrelationAdd(data) {
    return request({
        url: '/cost/relevancy',
        method: 'post',
        data: data
    })
}
// 成本管理-单价关联-编辑
export function unitPriceCorrelationEdit(data) {
    return request({
        url: '/cost/relevancy',
        method: 'put',
        data: data
    })
}
// 成本管理-单价关联-删除
export function unitPriceCorrelationDel(id) {
    return request({
        url: '/cost/relevancy/' + id,
        method: 'delete'
    })
}

// 成本管理-单价关联-详情
export function unitPriceCorrelationInfo(id) {
    return request({
        url: '/cost/relevancy/' + id,
        method: 'get'
    })
}



// 成本管理-电费偏差分析-电力数据、统计数据
export function deviationCardList(params) {
    return request({
        url: '/cost/statistics/statistics',
        method: 'get',
        params
    })
}
// 成本管理-电费偏差分析-耗电明细
export function deviationTableList(params) {
    return request({
        url: '/cost/statistics/getStatisticsInfoList',
        method: 'get',
        params
    })
}

// 成本管理-电费偏差分析-耗电明细详情
export function deviationInfo(params) {
    return request({
        url: '/cost/statistics/getStatisticsInfoEcharts',
        method: 'get',
        params
    })
}