import request from "@/utils/request"
export default {
  // 分项用能分析
  itemizedEnergyAnalysis(data) {
    return request({
      url: "/itemizedEnergyAnalysis/list",
      method: "GET",
      params: data,
    })
  },
  // 支路用能分析
  branchanalysis(data) {
    return request({
      url: "/branchanalysis/list",
      method: "GET",
      params: data,
    })
  },
}
