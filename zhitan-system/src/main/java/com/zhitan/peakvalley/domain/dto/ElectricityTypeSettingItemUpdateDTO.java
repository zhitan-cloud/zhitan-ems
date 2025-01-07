package com.zhitan.peakvalley.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 价格对象 rule_details
 *
 * @author ruoyi
 * @date 2024-06-14
 */
@Data
@ApiModel(value = "计费规则费用类型及时间段更新请求类")
public class ElectricityTypeSettingItemUpdateDTO {

    /**
     * 计费规则子表id
     */
    @ApiModelProperty(value = "计费规则子表id")
    @NotNull(message = "计费规则子表id不能为空!")
    private String id;

    /**
     * 电费类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @ApiModelProperty(value = "费用类型")
    @NotNull(message = "费用类型不能为空!")
    private String type;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
