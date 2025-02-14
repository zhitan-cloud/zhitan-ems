import request from "@/utils/request"

export function listEnergyCostTrend(query) {
  return request({
    url: "/energyTypeAnalysis/listEnergyCostTrend",
    method: "get",
    params: query,
  })
}
export function listEnergyCostTrendDetail(query) {
  return request({
    url: "/energyTypeAnalysis/listEnergyCostTrendDetail",
    method: "get",
    params: query,
  })
}
