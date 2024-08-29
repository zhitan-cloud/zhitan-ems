package com.dingzhuo.energy.project.electricityTypeSetting.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 价格对象 rule_details
 *
 * @author ruoyi
 * @date 2024-06-14
 */
@Data
@ApiModel(value = "计费规则详情查询返回时间段实体类")
public class ElectricityTypeSettingItemVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "计费规则子表id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 计费规则id
     */
    @ApiModelProperty(value = "计费规则id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ruleId;

    /**
     * 费用类型时间段
     */
    @ApiModelProperty(value = "费用类型时间段")
    private Integer timePeriod;

    /**
     * 电费类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @ApiModelProperty(value = "费用类型 0:尖 1:峰 2:平 3:谷 4:深谷")
    private String type;

    /**
     * 电费类型 0:尖 1:峰 2:平 3:谷 4:深谷
     */
    @ApiModelProperty(value = "费用类型描述")
    private String typeDesc;

    /**
     * 组织架构id
     */
    @ApiModelProperty(value = "组织架构id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;

}
