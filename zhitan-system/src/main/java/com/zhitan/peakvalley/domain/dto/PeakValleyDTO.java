package com.zhitan.peakvalley.domain.dto;

import com.zhitan.common.enums.TimeType;
import com.zhitan.common.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * description 
 *
 * @author hmj
 * @date 2024-09-24 16:49
 */
@Data
public class PeakValleyDTO {

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

    /**
     * 查询时间
     */
    @ApiModelProperty(value = "查询时间")
    @NotNull(message = "查询时间不能为空!")
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date queryTime;


    public String getTimeType() {
        String result = TimeType.DAY.name();
        switch (TimeType.valueOf(this.timeType)) {
            case HOUR:
            case DAY:
                result= TimeType.HOUR.name();
                break;
            case YEAR:
                result= TimeType.MONTH.name();
                break;
            case MONTH:
            default:
                break;
        }
        return result;
    }
}
