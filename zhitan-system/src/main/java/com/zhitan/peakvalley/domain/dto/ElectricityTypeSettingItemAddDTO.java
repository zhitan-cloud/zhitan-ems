package com.zhitan.peakvalley.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 价格对象 rule_details
 *
 * @author ruoyi
 * @date 2024-06-14
 */
@Data
@ApiModel(value = "计费规则费用类型及时间段新增请求类")
public class ElectricityTypeSettingItemAddDTO {

    /**
     * 费用类型时间段
     */
    @ApiModelProperty(value = "费用类型时间段")
    @NotNull(message = "费用类型时间段不能为空!")
    @Min(value = 1, message = "费用类型时间段最小值为1")
    @Max(value = 48, message = "费用类型时间段最大值为48")
    private Integer timePeriod;

    /**
     * 时间段类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @ApiModelProperty(value = "时间段类型")
    @NotNull(message = "时间段类型不能为空!")
    private String type;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
