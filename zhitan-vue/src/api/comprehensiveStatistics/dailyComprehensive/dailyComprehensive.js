import request from "@/utils/request"

//查询日报表
export function getDataList(query) {
  return request({
    url: "/comprehensive/dailyComprehensive/list",
    method: "get",
    params: query,
  })
}
export function getlistChart(query) {
  return request({
    url: "/comprehensive/dailyComprehensive/listChart",
    method: "get",
    params: query,
  })
}

// 导出综合报表
export function exportList(query) {
  return request({
    url: "/report/dailyReport/export",
    method: "get",
    params: query,
  })
}
