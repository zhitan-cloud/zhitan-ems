import request from "@/utils/request"

export function getHistoricalDataByIndexId(query) {
  return request({
    url: "/dataMonitoring/historyDataTrend/getHistoricalDataByIndexId",
    method: "get",
    params: query,
  })
}
export function getEnergyIndexByModelId(query) {
  return request({
    url: "/basicsetting/model/getEnergyIndexByModelId",
    method: "get",
    params: query,
  })
}
