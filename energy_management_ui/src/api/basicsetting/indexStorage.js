import request from '@/utils/request'

export function parseFormula(data) {
  return request({
    url: '/basicsetting/indexStorage/parseFormula',
    method: 'post',
    data: data
  })
}

export function getNodeIndex(nodeId) {
  return request({
    url: '/basicsetting/energyindex/includeChildrenNode/' + nodeId,
    method: 'GET'
  })
}

export function getCalcPeriod() {
  return request({
    url: '/basicsetting/indexStorage/calcPeriod/',
    method: 'GET'
  })
}

export function saveIndexStorage(indexId, data) {
  return request({
    url: '/basicsetting/indexStorage/' + indexId,
    method: 'post',
    data: data
  });
}

export function getIndexStorage(indexId) {
  return request({
    url: '/basicsetting/indexStorage/' + indexId,
    method: 'GET'
  })
}
