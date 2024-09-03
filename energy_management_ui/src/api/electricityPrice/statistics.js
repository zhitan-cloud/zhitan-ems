import request from "@/utils/request";

// 查询electricity列表
export function getStatisticsList(query) {
  /*if(query.timeType=='MONTH'){
    query.timeType="DAY"
  }
  if(query.timeType=='YEAR'){
    query.timeType="MONTH"
  }*/
  query.timeType = "HOUR";
  return request({
    url: "/electricityPrice/statistics/getStatisticsList",
    method: "get",
    params: query
  });
}
// 查询electricity列表
export function getDataStatistics(query) {
  return request({
    url: "/electricityPrice/statistics/getDataStatistics",
    method: "get",
    params: query
  });
}

// 尖峰平谷 统计 查询
// 接口 ： electricityDataItem/getDataStatistics?modelCode=1&nodeId=1&timeType=DAY&queryTime=2024-08
export function getElectricityDataItemStatistics(query) {
  return request({
    url: "/electricityDataItem/getDataStatistics",
    method: "get",
    params: query
  });
}
