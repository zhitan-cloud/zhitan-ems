import request from "@/utils/request"

// 能耗对比分析-区域能耗分析-能耗趋势/区域能耗统计分析表-列表
// 能耗对比分析-科室能耗分析-能耗趋势/能耗统计分析表-列表
// 能耗对比分析-设备能耗分析-能耗趋势/能耗统计分析表-列表
export function listRegion(query) {
  return request({
    url: "/consumptionanalysis/getByArea",
    method: "get",
    params: query,
  })
}
// 能耗对比分析-科室能耗分析-能耗排名-列表
// 能耗对比分析-设备能耗分析-能耗排名-列表
export function listDepartment(query) {
  return request({
    url: "/consumptionanalysis/getByDepartment",
    method: "get",
    params: query,
  })
}
// 能耗对比分析-能耗指标考核-能耗趋势/区域能耗统计分析表
export function listIndicatorassessment(query) {
  return request({
    url: "/consumptionanalysis/getPlanAndProdCount",
    method: "get",
    params: query,
  })
}
// 能耗对比分析-综合能耗分析-综合能耗趋势/各介质能耗占比/区域能耗统计分析表-列表
export function listComprehensive(query) {
  return request({
    url: "/consumptionanalysis/getComprehensiveEnergy",
    method: "get",
    params: query,
  })
}
// 能耗对比分析-综合能耗分析-能耗同比环比-列表
export function listYoY(query) {
  return request({
    url: "/consumptionanalysis/getYOY",
    method: "get",
    params: query,
  })
}
// 能耗对比分析-综合能耗分析-用能单元能耗排名-列表
export function listEnergyRanking(query) {
  return request({
    url: "/consumptionanalysis/getEnergyRanking",
    method: "get",
    params: query,
  })
}

// statisticsAnalysis/getFlowCharts
export function getFlowCharts(query) {
  return request({
    url: "/statisticsAnalysis/getFlowCharts",
    method: "get",
    params: query,
  })
}
// 对比分析同比
export function querySameCompareList(query) {
  return request({
    url: "/statisticsAnalysis/querySameCompareList",
    method: "get",
    params: query,
  })
}
// 对比分析环比
export function queryLoopCompareList(query) {
  return request({
    url: "/statisticsAnalysis/queryLoopCompareList",
    method: "get",
    params: query,
  })
}
