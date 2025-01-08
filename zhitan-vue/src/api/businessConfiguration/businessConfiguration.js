import request from '@/utils/request'




// 获取列表
export function alarmList(params) {
    return request({
        url: '/alarm/limit/list',
        method: 'get',
        params
    })
}

// 新增
export function alarmAdd(data) {
    return request({
        url: '/alarm/limit/add',
        method: 'post',
        data
    })
}

// 查详情
export function alarmInfo(params) {
    return request({
        url: `/alarm/limit/${params.id}`,
        method: 'get',
        params
    })
}

// 编辑
export function alarmEdit(data) {
    return request({
        url: '/alarm/limit',
        method: 'put',
        data
    })
}

//删除
export function alarmDel(id) {
    return request({
        url: '/alarm/limit/' + id,
        method: 'delete',
    })
}