import request from "@/utils/request"
export default {
  // 工序能耗分析（日）图表
  dailyList(data) {
    return request({
      url: "/processEnergy/dailyProcessEnergy/list",
      method: "GET",
      params: data,
    })
  },
  // 工序能耗分析（日）图表
  dailyChart(data) {
    return request({
      url: "/processEnergy/dailyProcessEnergy/listChart",
      method: "GET",
      params: data,
    })
  },
  // 工序能耗统计（月）列表
  monthlyList(data) {
    return request({
      url: "/processEnergy/monthlyProcessEnergy/list",
      method: "GET",
      params: data,
    })
  },
  // 工序能耗统计（月）图表
  monthlyChart(data) {
    return request({
      url: "/processEnergy/monthlyProcessEnergy/listChart",
      method: "GET",
      params: data,
    })
  },
  // 工序能耗统计（年）列表
  yearList(data) {
    return request({
      url: "/processEnergy/YearProcessEnergy/list",
      method: "GET",
      params: data,
    })
  },
  // 工序能耗统计（年）图表
  yearChart(data) {
    return request({
      url: "/processEnergy/YearProcessEnergy/listChart",
      method: "GET",
      params: data,
    })
  },
}
