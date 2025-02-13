package com.zhitan.statisticalAnalysis.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 能耗实体类
 *
 * @Author: Zhujw
 * @Date: 2023/1/28
 */
@Data
public class EnergyConsumeVO implements Serializable {

    /**
     * 计量器具id
     */
    private String deviceId;

    /**
     * 能源类型
     */
    private String energyType;

    /**
     * 时间编码
     */
    private Date dataTime;

    /**
     * 费用
     */
    private BigDecimal costValue;

    /**
     * 累计量
     */
    private BigDecimal accumulationValue;
}
