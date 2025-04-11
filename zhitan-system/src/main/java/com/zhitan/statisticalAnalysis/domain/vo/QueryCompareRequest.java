package com.zhitan.statisticalAnalysis.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 数据查询条件实体
 * @author ZhiTan
 * @date: 2022年01月28日 14:49
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="QueryCompareRequest", description="获取数据的参数实体")
public class QueryCompareRequest {

    /**
     * 查询时间类型,对应常量类：TimeTypeConst
     */
    @NotBlank(message = "时间类型不能为空")
    @ApiModelProperty(value = "查询时间类型,对应常量：DAY/MONTH/SEASON/YEAR，默认MONTH")
    private String timeType;

    /**
     * 日期值，月份(202201-202212)、年份(2022-~)
     */
    @NotBlank(message = "时间编码不能为空")
    @ApiModelProperty(value = "日期值，月份(202201-202212)、年份(2022-~)")
    private String timeCode;

    /**
     * 节点Id
     */
    @NotBlank(message = "节点Id")
    @ApiModelProperty(value = "节点Id")
    private String nodeId;

    /**
     * 能源类型，通过/隔开,water/gas
     */
    @ApiModelProperty(value = "能源类型，通过/隔开,water/gas")
    private String energyType;

}