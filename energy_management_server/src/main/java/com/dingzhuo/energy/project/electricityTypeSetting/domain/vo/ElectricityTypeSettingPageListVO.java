package com.dingzhuo.energy.project.electricityTypeSetting.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 计费规则对象 rules
 *
 * @author ruoyi
 * @date 2024-06-19
 */
@Data
@ApiModel(value = "计费规则分页查询返回实体类")
public class ElectricityTypeSettingPageListVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "计费规则id")
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String name;

    /**
     * 是否生效
     */
    @ApiModelProperty(value = "是否生效")
    private boolean isEffective;

    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveDate;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
