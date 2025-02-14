package com.zhitan.statisticalAnalysis.domain.dto;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhitan.common.enums.TimeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 能流图查询
 */
@Data
public class FlowChartsDTO {

    /**
     * 能源类型名称
     */
    @EnumValue
    @ApiModelProperty(value = "时间类型")
    private TimeType timeType;

    /**
     * 查询时间
     */
    @NotNull(message = "查询时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "查询时间")
    private LocalDate queryTime;

    /**
     * 能源类型
     */
    @NotBlank(message = "能源类型不能为空")
    @ApiModelProperty(value = "能源类型")
    private String energyType;

    /**
     * 节点id
     */
    @ApiModelProperty(value = "节点id")
    private String nodeId;

    @NotBlank(message = "模型编码不能为空")
    @ApiModelProperty(value = "模型编码")
    private String modelCode;

    public TimeType getTimeType() {
        if (ObjectUtils.isEmpty(timeType)) {
            return TimeType.DAY;
        }
        return timeType;
    }
}
