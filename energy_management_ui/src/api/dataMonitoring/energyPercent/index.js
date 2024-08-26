import request from "@/utils/request";

//列表接口
export function getElectricityShareAnalysis(query) {
  return request({
    url: "/energyShareAnalysis/getElectricityShareAnalysis",
    method: "get",
    params: query
  });
}
