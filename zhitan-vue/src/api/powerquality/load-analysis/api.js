import request from "@/utils/request"

// 获取用能单元下的电表列表
export function listElectricityMeter(params) {
  return request({
    url: "/meter/listElectricityMeter",
    method: "get",
    params,
  })
}

// 获取用能单元下的某个电表的负荷分析数据
export function loadAnalysisDetail(params) {
  return request({
    url: "/loadAnalysis/detail",
    method: "get",
    params,
  })
}

// 获取用能单元下的电表列表-实体表
export function listElectricityDeviceMeter(params) {
  return request({
    url: "/loadAnalysis/listElectricMeter",
    method: "get",
    params,
  })
}
