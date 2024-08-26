package com.dingzhuo.energy.project.energyConsumeInput.domain;

import com.dingzhuo.energy.common.utils.time.TimeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 能耗手动录入列表实体
 * @author: yxw
 * @date: 2022年03月16日 18:05
 */
@Data
@ApiModel(value = "阶段录入")
public class EnergyConsumeInput {
    /** 指标id */
    @ApiModelProperty(value = "指标id")
    private String indexId;

    /**
     * 时间类型
     */
    @ApiModelProperty(value = "时间类型")
    private TimeType timeType;

    /** 指标名字*/
    @ApiModelProperty(value = "指标名字")
    private String name;

    /** 指标的code*/
    @ApiModelProperty(value = "指标的code")
    private String code;
    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;
    /**
     * 与数据库字段对应
     */
    @ApiModelProperty(value = "与数据库字段对应")
    private Date dataTime;
    /**
     * 页面显示时间字段
     */
    @ApiModelProperty(value = "页面显示时间字段")
    private String dataTimeStr;
    /**
     * 录入值
     */
    @ApiModelProperty(value = "录入值")
    private Double value;
    /**
     * 录入时间 与数据库字段对应
     */
    @ApiModelProperty(value = "录入时间")
    private Date createTime;
    /**
     * 录入时间 页面显示字段
     */
    @ApiModelProperty(value = "录入时间-页面")
    private String createTimeStr;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 指标类型名称
     */
    @ApiModelProperty(value = "指标类型名称")
    private String categoryName;
    /**
     * 能源类型名称
     */
    @ApiModelProperty(value = "能源类型名称")
    private String energyName;
}
