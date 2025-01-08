import request from '@/utils/request'

// 获取系统名称信息
export function systemName(query) {
    return request({
        url: '/system/nameconfig',
        method: 'get',
    })
}

// 保存系统名称设置
export function systemNameSave(data) {
    return request({
        url: '/system/nameconfig',
        method: 'post',
        data: data
    })
}