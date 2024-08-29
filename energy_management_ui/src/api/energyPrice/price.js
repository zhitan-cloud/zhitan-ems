import request from "@/utils/request";

export default {
  // 查询参数列表
  pageRule(query) {
    return request({
      url: "/rule/list",
      method: "get",
      params: query
    });
  },
  addRule(query) {
    return request({
      url: "/rule/addRule",
      method: "post",
      data: query
    });
  },
  editRule(query) {
    return request({
      url: "/rule/updateRule",
      method: "post",
      data: query
    });
  },
  delRule(query) {
    return request({
      url: "/rule/delRule/" + query.id,
      method: "delete",
      params: query
    });
  },
  getRuleDetail(query) {
    return request({
      url: "/rule/getRuleDetail",
      method: "get",
      params: query
    });
  }
};
