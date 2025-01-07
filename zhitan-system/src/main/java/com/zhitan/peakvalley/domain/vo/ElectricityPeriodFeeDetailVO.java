package com.zhitan.peakvalley.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "计费规则各时段的费用返回实体")
public class ElectricityPeriodFeeDetailVO {

    /**
     * 费用类型时间段
     */
    @ApiModelProperty(value = "费用类型时间段")
    private Integer timePeriod;

    /**
     * 电费类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @ApiModelProperty(value = "费用类型 0:尖 1:峰 2:平 3:谷 4:深谷")
    private Integer type;

    /**
     * 电费类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @ApiModelProperty(value = "费用类型描述")
    private String typeDesc;

    /**
     * 电费价格
     */
    @ApiModelProperty(value = "电费价格")
    private BigDecimal electricityFee;

    /**
     * 服务费价格
     */
    @ApiModelProperty(value = "服务费价格")
    private BigDecimal serviceFee;

    /**
     * 停车费价格
     */
    @ApiModelProperty(value = "停车费价格")
    private BigDecimal parkingFee;

    /**
     * 超时占用费价格
     */
    @ApiModelProperty(value = "超时占用费价格")
    private BigDecimal occupancyFee;
}
