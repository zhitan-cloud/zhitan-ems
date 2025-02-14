import request from "@/utils/request"
// 获取用能单元下的某个电表的功率因数数据
export function powerFactorAnalysisDetail(params) {
  return request({
    url: "/powerFactorAnalysis/detail",
    method: "get",
    params,
  })
}
