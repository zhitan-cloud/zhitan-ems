package com.zhitan.statisticalAnalysis.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * 费用分析-成本趋势分析统计图返回值信息
 *
 * @Author: Zhujw
 * @Date: 2023/2/14
*/
@Data
@ApiModel(value = "费用分析-成本趋势分析统计图返回值信息", description = "费用分析-成本趋势分析统计图返回值信息")
public class EnergyConsumeTrendDetailItem {

    /**
     *  能源类型
     */
    @ApiModelProperty(value = "能源类型")
    private String energyType;

    /**
     *  能源单位
     */
    @ApiModelProperty(value = "能源单位")
    private String energyUnit;

    /**
     *  累积量标签
     */
    @ApiModelProperty(value = "累积量标签")
    private String accumulationLabel;

    /**
     *  费用标签
     */
    @ApiModelProperty(value = "费用标签")
    private String costLabel;

    /**
     * 累积量key集合
     */
    @ApiModelProperty(value = "累积量key集合")
    private List<String> accumulationKeyList;

    /**
     * 累积量value集合
     */
    @ApiModelProperty(value = "累积量value集合")
    private List<BigDecimal> accumulationValueList;

    /**
     * 费用key集合
     */
    @ApiModelProperty(value = "费用key集合")
    private List<String> costKeyList;

    /**
     * 费用value集合
     */
    @ApiModelProperty(value = "费用value集合")
    private List<BigDecimal> costValueList;
}
