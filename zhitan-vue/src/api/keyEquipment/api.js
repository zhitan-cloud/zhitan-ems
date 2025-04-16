import request from "@/utils/request"
export default {
  //  查询所有设备列表
  getFacilityArchives(data) {
    return request({
      url: "/keyEquipment/dailyKeyEquipment/getFacilityArchives",
      method: "GET",
      params: data,
    })
  },
  // 查询重点设备列表
  getPointFacility(data) {
    return request({
      url: "/keyEquipment/dailyKeyEquipment/getPointFacility",
      method: "GET",
      params: data,
    })
  },
  // 重点设备能耗分析（日）图表
  dailyList(data) {
    return request({
      url: "/keyEquipment/dailyKeyEquipment/list",
      method: "GET",
      params: data,
    })
  },
  // 重点设备能耗分析（日）图表
  dailyChart(data) {
    return request({
      url: "/keyEquipment/dailyKeyEquipment/listChart",
      method: "GET",
      params: data,
    })
  },
  // 重点设备能耗统计（月）列表
  monthlyList(data) {
    return request({
      url: "/keyEquipment/MonthlyKeyEquipment/list",
      method: "GET",
      params: data,
    })
  },
  // 重点设备能耗统计（月）图表
  monthlyChart(data) {
    return request({
      url: "/keyEquipment/MonthlyKeyEquipment/listChart",
      method: "GET",
      params: data,
    })
  },
  // 重点设备能耗统计（年）列表
  yearList(data) {
    return request({
      url: "/keyEquipment/YearKeyEquipment/list",
      method: "GET",
      params: data,
    })
  },
  // 重点设备能耗统计（年）图表
  yearChart(data) {
    return request({
      url: "/keyEquipment/YearKeyEquipment/listChart",
      method: "GET",
      params: data,
    })
  },
}
