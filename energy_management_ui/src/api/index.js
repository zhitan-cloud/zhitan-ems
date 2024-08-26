import request from '@/utils/request'


// 首页IFrame加载地址
export function getIndexIframeUrl() {
  return request({
    url: '/HomePage/indexIframeUrl',
    method: 'get',
  })
}
