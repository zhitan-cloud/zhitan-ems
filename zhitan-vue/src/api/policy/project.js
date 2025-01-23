import request from "@/utils/request"

// 获取列表
export function projectPage(params) {
  return request({
    url: "/energySavingProject/page",
    method: "get",
    params,
  })
}

// 新增
export function projectAdd(data) {
  return request({
    url: "/energySavingProject/add",
    method: "post",
    data,
  })
}

// 查详情
export function projectInfo(params) {
  return request({
    url: "/energySavingProject/getById?id=" + params,
    method: "get",
  })
}

// 编辑
export function projectEdit(data) {
  return request({
    url: "/energySavingProject/edit",
    method: "post",
    data,
  })
}

//删除
export function projectDel(id) {
  return request({
    url: "/energySavingProject/del/" + id,
    method: "delete",
  })
}
