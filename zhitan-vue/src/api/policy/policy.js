import request from "@/utils/request"

// 获取列表
export function policyPage(params) {
  return request({
    url: "/policiesRegulations/page",
    method: "get",
    params,
  })
}

// 新增
export function policyAdd(data) {
  return request({
    url: "/policiesRegulations/add",
    method: "post",
    data,
  })
}

// 查详情
export function policyInfo(params) {
  return request({
    url: "/policiesRegulations/detail?id=" + params,
    method: "get",
  })
}

// 编辑
export function policyEdit(data) {
  return request({
    url: "/policiesRegulations/edit",
    method: "post",
    data,
  })
}

//删除
export function policyDel(id) {
  return request({
    url: "/policiesRegulations/delete/" + id,
    method: "delete",
  })
}
