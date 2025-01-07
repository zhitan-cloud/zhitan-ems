package com.zhitan.costmanagement.domain.vo;

import com.zhitan.common.enums.TimeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * 尖峰平谷数据列表实体类 electricityDataItem
 *
 * @author ruoyi
 * @date 2024-06-19
 */
@Data

public class DeviationAnalysisDTO {

    /**
     * 模型编号
     */
    @ApiModelProperty(value = "模型编号")
    @NotBlank(message = "模型编号不能为空!")
    private String modelCode;

    /**
     * 节点id
     */
    @ApiModelProperty(value = "节点id")
    @NotBlank(message = "节点id不能为空!")
    private String nodeId;

    /**
     * 时间类型
     */
    @ApiModelProperty(value = "时间类型")
    @NotBlank(message = "时间类型不能为空!")
    private String timeType;


    private String date;



}
