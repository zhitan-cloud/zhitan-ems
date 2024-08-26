package com.dingzhuo.energy.project.reportForm.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

/**
 * @Description: 获取上报节点配置列表请求参数实体
 * @author: yxw
 * @date: 2022年04月01日 11:04
 */
@Data
public class reportSetRequestModel {
    /**
     * nodeid
     */
    @Excel(name = "节点ID")
    private String nodeId;
    /**
     * 指标id
     */
    private String indexId;
}
