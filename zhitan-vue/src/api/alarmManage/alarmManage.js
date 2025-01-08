import request from '@/utils/request'
export function historicalAlarm(query) {
    return request({
        url: '/energyAlarm/historicalAlarm/list',
        method: 'get',
        params: query
    })
}

export function getByNodeId(query) {
    return request({
        url: '/alarmAnalysis/getByNodeId',
        method: 'get',
        params: query
    })
}

export function getCountInfo(query) {
    return request({
        url: '/alarmAnalysis/getCountInfo',
        method: 'get',
        params: query
    })
}

