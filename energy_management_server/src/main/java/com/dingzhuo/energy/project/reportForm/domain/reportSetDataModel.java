package com.dingzhuo.energy.project.reportForm.domain;

import lombok.Data;

/**
 * @Description: 获取节点下的点位列表
 * @author: yxw
 * @date: 2022年04月01日 18:30
 */
@Data
public class reportSetDataModel {

    private String indexId;
    private String indexCode;
    private String indexName;
    private String meterId;
    private String meterName;
    /** 是否启用限值替换*/
    private boolean enableLimitValue;
}
