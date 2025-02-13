import request from "@/utils/request"

// 获取三相不平衡分析数据
export function threePhaseUnbalanceAnalysisDetail(params) {
  return request({
    url: "/threePhaseUnbalanceAnalysis/detail",
    method: "get",
    params,
  })
}
