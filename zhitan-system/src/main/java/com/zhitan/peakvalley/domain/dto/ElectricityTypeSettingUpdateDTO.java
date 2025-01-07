package com.zhitan.peakvalley.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@ApiModel(value = "计费规则更新请求实体类")
public class ElectricityTypeSettingUpdateDTO {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空!")
    @ApiModelProperty(value = "计费规则id")
    private String id;

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空!")
    @ApiModelProperty(value = "规则名称")
    private String name;

    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    @NotNull(message = "生效时间不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date effectiveDate;

    /**
     * 尖时段电费价格
     */
    @ApiModelProperty(value = "尖时段电费价格")
    @NotNull(message = "尖时段电费价格不能为空!")
    @DecimalMin(value = "0", message = "尖时段电费价格最小为0")
    private BigDecimal sharpFee;

    /**
     * 峰时段电费价格
     */
    @ApiModelProperty(value = "峰时段电费价格")
    @NotNull(message = "峰时段电费价格不能为空!")
    @DecimalMin(value = "0", message = "峰时段电费价格最小为0")
    private BigDecimal peakFee;

    /**
     * 平时段电费价格
     */
    @ApiModelProperty(value = "平时段电费价格")
    @NotNull(message = "平时段电费价格不能为空!")
    @DecimalMin(value = "0", message = "平时段电费价格最小为0")
    private BigDecimal flatFee;

    /**
     * 谷时段电费价格
     */
    @ApiModelProperty(value = "谷时段电费价格")
    @NotNull(message = "谷时段电费价格不能为空!")
    @DecimalMin(value = "0", message = "谷时段电费价格最小为0")
    private BigDecimal valleyFee;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 计费策略详情列表
     */
    @ApiModelProperty(value = "计费策略详情列表")
    @NotNull(message = "尖峰平谷时间段不能为空!")
    private List<ElectricityTypeSettingItemUpdateDTO> ruleDetailList;
}
