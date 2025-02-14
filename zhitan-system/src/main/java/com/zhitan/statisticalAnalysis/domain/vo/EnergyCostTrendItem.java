package com.zhitan.statisticalAnalysis.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 成本趋势分析-表格信息
 *
 * @Author: Zhujw
 * @Date: 2023/2/14
 */
@Data
@ApiModel(value = "成本趋势分析-表格信息", description = "成本趋势分析-表格信息")
public class EnergyCostTrendItem {

    /**
     * 用能单元id
     */
    @ApiModelProperty(value = "用能单元id")
    private String energyUnitId;

    /**
     * 用能单元名称
     */
    @ApiModelProperty(value = "用能单元名称")
    private String energyUnitName;

    /**
     * 总费用
     */
    @ApiModelProperty(value = "总费用")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal total;

    /**
     * 时间
     */
    @ApiModelProperty(value = "时间")
    private String dateCode;

    /**
     * 能源类型
     */
    @ApiModelProperty(value = "能源类型")
    private List<CostTrendEnergyTypeItem> itemList;
}
