import request from '@/utils/request'
//获取模型下的指标
export function getSettingIndex(nodeId) {
  return request({
    url: '/basicsetting/modelnode/energyIndex/' + nodeId,
    method: 'get',
  })
}
//获取模型下的能源品种
export function getSettingEnergy(nodeId) {
  return request({
    url: '/basicsetting/modelnode/energy/' + nodeId,
    method: 'get'
  })
}
//获取模型下的设备
export function getSettingDevice(nodeId) {
  return request({
    url: '/basicsetting/modelnode/device/' + nodeId,
    method: 'get'
  })
}
// 能耗指标趋势分析
export function getEnergyStatisticsTrend(query) {
  return request({
    url: '/energyStatistics/getEnergyStatisticsTrend',
    method: 'get',
    params: query,
  })
}
// 能耗指标趋势分析
export function getEnergyCurveTrend(query) {
  return request({
    url: '/energyCurve/getEnergyCurveTrend',
    method: 'get',
    params: query,
  })
}
// 能耗指标趋势分析导出
export function exportEnergyindex(query) {
  return request({
    url: '/energyStatistics/export',
    method: 'get',
    params: query
  })
}
// 能耗指标趋势分析导出
export function exportEnergyCurveindex(query) {
  return request({
    url: '/energyCurve/export',
    method: 'get',
    params: query
  })
}
//能源消费成本构成
export function getEnergyConstitute(query) {
  return request({
    url: 'energyStatistics/getEnergyConstitute',
    method: 'get',
    params: query,
  })
}
//能源消费成本分析
export function getEnergyConsumption(query) {
  return request({
    url: 'energyStatistics/getEnergyConsumption',
    method: 'get',
    params: query,
  })
}
// 能源成本导出
export function energyConsumptionExport(query) {
  return request({
    url: '/energyStatistics/energyConsumptionExport',
    method: 'get',
    params: query
  })
}
//能源消费成本分时分析报表
export function listEnergyConsumption(query) {
  return request({
    url: '/energyStatistics/listEnergyConsumption',
    method: 'get',
    params: query,
  })
}
//用能单元能耗分析
export function getEnergyUnit(query) {
  return request({
    url: '/energyStatistics/getEnergyUnit',
    method: 'get',
    params: query
  })
}
// 实时检测组态图功能的多sheet页报表
export function reportFormsvg(query) {
  return request({
    url: '/energyStatistics/reportFormsvg',
    method: 'get',
    params: query
  })
}
//实时检测组态图功能的多sheet页报表导出
export function reportFormsvgExport(query) {
  return request({
    url: '/energyStatistics/reportFormsvgExport',
    method: 'get',
    params: query
  })
}

//能耗统计
export function getEnergyAnalysis(query) {
  return request ({
    url: '/energyAnalysis/getEnergyAnalysis',
    method: 'get',
    params: query
  })
}
//导出能耗统计
export function exportDemo(query) {
  return request({
    url: '/energyAnalysis/export',
    method: 'get',
    params: query
  })
}
