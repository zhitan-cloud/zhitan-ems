import request from "@/utils/request";

// 获取全厂综合能耗
export function getEnergyConsumptionSummation(timeType) {
  return request({
    url: "/home/getHomeEnergyConsumptionSummation",
    method: "get",
    params: {
      timeType
    }
  });
}

// 综合能耗占比分析环形图数据
export function getEnergyConsumptionRatio(timeType) {
  return request({
    url: "/home/getHomeEnergyConsumptionRatio",
    method: "get",
    params: {
      timeType
    }
  });
}

// 能源设备能耗环形图数据
export function getHomeEnergyUnitConsumptionRatio(timeType) {
  return request({
    url: "/home/getHomeEnergyUnitConsumptionRatio",
    method: "get",
    params: {
      timeType
    }
  });
}

// 分类能源统计
export function getEnergyStatistic(timeType) {
  return request({
    url: "/home/getHomeEnergyStatistic",
    method: "get",
    params: {
      timeType
    }
  });
}

// 能源监测柱状图数据
export function getHomeEnergyMonitoring(timeType, energyType) {
  return request({
    url: "/home/getHomeEnergyMonitoring",
    method: "get",
    params: {
      timeType,
      energyType
    }
  });
}

// 获取能源类型
export function listEnergyType() {
  return request({
    url: "/home/listEnergyType",
    method: "get"
  });
}

// getHomeOutdoorTemperature
export function getHomeOutdoorTemperature() {
  return request({
    url: "/home/getHomeOutdoorTemperature",
    method: "get"
  });
}
