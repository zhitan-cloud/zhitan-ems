import request from '@/utils/request'

export function getDeviceStorage(nodeId,stateId) {
  return request({
    url: '/basicsetting/deviceStorage/'+nodeId+"/" + stateId,
    method: 'GET'
  })
}


export function saveDeviceStorage(nodeId,stateId, data) {
  return request({
    url: '/basicsetting/deviceStorage/' + nodeId+'/'+stateId,
    method: 'post',
    data: data
  });
}

export function getDeviceStateByNodeIds(query) {
  return request({
    url: '/basicsetting/deviceStorage/getDeviceStateByNodeIds',
    method: 'GET',
    params: query
  })
}

// 修改预报警设置
export function setIsEnable(nodeId,isEnbale,data) {
  return request({
    url: '/basicsetting/deviceStorage/setIsEnable/'+nodeId+'/'+isEnbale,
    method: 'post',
    data: data
  })
}

