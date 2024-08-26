import request from '@/utils/request'

// 查询实时页面数据
export function getRealTimeData(code, minute, pointCount) {
    return request({
        url: '/energyAlarm/realTimeAlarm/liveHistoryData/' + code + "/" + minute + "/" + pointCount,
        method: 'get'
    })
}

// 查询实时表格页面数据
export function getRealTimeTableData(code, dataTime) {
    return request({
        url: '/dataMonitoring/realTimeTrend/svgTrendView/energyIndex/history/' + code + '/' + dataTime,
        method: 'get'
    })
}

// 查询历史页面数据
export function getHistoryData(code, start, end, pointCount) {
    return request({
        url: '/energyAlarm/historicalAlarm/historyData/' + code + "/" + start + "/" + end + "/" + pointCount,
        method: 'get'
    })
}

// 导出历史表格数据
export function exportHistoryTable(code, start, end, pointCount, indexName, indexUnit) {
    return request({
        url: '/energyAlarm/historicalAlarm/historyDataExcel/' + code + "/" + start + "/" + end + "/" + pointCount + "/" + indexName + "/" + indexUnit,
        method: 'get'
    })
}
