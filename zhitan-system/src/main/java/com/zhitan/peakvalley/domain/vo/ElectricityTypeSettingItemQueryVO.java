package com.zhitan.peakvalley.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 计费规则对象 rules
 *
 * @author ruoyi
 * @date 2024-06-19
 */
@Data
@ApiModel(value = "计费规则详情查询返回实体类")
public class ElectricityTypeSettingItemQueryVO {

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
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveDate;

    /**
     * 尖时段电费价格
     */
    @ApiModelProperty(value = "尖时段电费价格")
    private BigDecimal sharpFee;

    /**
     * 峰时段电费价格
     */
    @ApiModelProperty(value = "峰时段电费价格")
    private BigDecimal peakFee;

    /**
     * 平时段电费价格
     */
    @ApiModelProperty(value = "平时段电费价格")
    private BigDecimal flatFee;

    /**
     * 谷时段电费价格
     */
    @ApiModelProperty(value = "谷时段电费价格")
    private BigDecimal valleyFee;

    /**
     * 尖时段服务费价格
     */
    @ApiModelProperty(value = "尖时段服务费价格")
    private BigDecimal sharpServiceFee;

    /**
     * 峰时段服务费价格
     */
    @ApiModelProperty(value = "峰时段服务费价格")
    private BigDecimal peakServiceFee;

    /**
     * 平时段服务费价格
     */
    @ApiModelProperty(value = "平时段服务费价格")
    private BigDecimal flatServiceFee;

    /**
     * 谷时段服务费价格
     */
    @ApiModelProperty(value = "谷时段服务费价格")
    private BigDecimal valleyServiceFee;

    /**
     * 尖时段停车费价格
     */
    @ApiModelProperty(value = "尖时段停车费价格")
    private BigDecimal sharpParkingFee;

    /**
     * 峰时段停车费价格
     */
    @ApiModelProperty(value = "峰时段停车费价格")
    private BigDecimal peakParkingFee;

    /**
     * 平时段停车费价格
     */
    @ApiModelProperty(value = "平时段停车费价格")
    private BigDecimal flatParkingFee;

    /**
     * 谷时段停车费价格
     */
    @ApiModelProperty(value = "谷时段停车费价格")
    private BigDecimal valleyParkingFee;

    /**
     * 尖时段超时占用费价格
     */
    @ApiModelProperty(value = "尖时段超时占用费价格")
    private BigDecimal sharpOccupancyFee;

    /**
     * 峰时段超时占用费价格
     */
    @ApiModelProperty(value = "峰时段超时占用费价格")
    private BigDecimal peakOccupancyFee;

    /**
     * 平时段超时占用费价格
     */
    @ApiModelProperty(value = "平时段超时占用费价格")
    private BigDecimal flatOccupancyFee;

    /**
     * 谷时段超时占用费价格
     */
    @ApiModelProperty(value = "谷时段超时占用费价格")
    private BigDecimal valleyOccupancyFee;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 计费策略详情列表
     */
    @ApiModelProperty(value = "计费策略时间段详情列表")
    private List<ElectricityTypeSettingItemVO> ruleDetailList;
}
