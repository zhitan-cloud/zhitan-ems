package com.dingzhuo.energy.project.energyConsumeInput.domain;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 计算引擎重算接口传递实体
 * @author: yxw
 * @date: 2022年03月18日 18:07
 */
@Data
public class ReCalcParamsModel {
    /** 指标id */
    private String indexId;
    /**
     * 时间类型
     */
    private String timeType;
    /**
     * 与数据库字段对应
     */
    private String dataTime;
}
