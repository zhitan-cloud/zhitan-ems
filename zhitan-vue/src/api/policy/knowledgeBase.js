import request from '@/utils/request'




// 获取列表
export function knowledgeBaseList(params) {
    return request({
        url: '/knowledgeBase/page',
        method: 'get',
        params
    })
}

// 新增
export function knowledgeBaseAdd(data) {
    return request({
        url: '/knowledgeBase/add',
        method: 'post',
        data
    })
}

// 查详情
export function knowledgeBaseInfo(params) {
    return request({
        url: "/knowledgeBase/detail?id="+params,
        method: 'get'
    })
}

// 编辑
export function knowledgeBaseEdit(data) {
    return request({
        url: '/knowledgeBase/edit',
        method: 'post',
        data
    })
}

//删除
export function knowledgeBaseDel(id) {
    return request({
        url: '/knowledgeBase/delete/' + id,
        method: 'delete',
    })
}