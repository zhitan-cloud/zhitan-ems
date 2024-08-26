package com.dingzhuo.energy.project.energyShareAnalysis.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 能源数据分析 请求参数
 */
@Data
public class EnergyAnalysisDTO {


    /**
     * 统计开始时间
     */
    @NotNull(message = "请维护查询时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 统计开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "请维护查询时间")
    private Date endTime;

    /**
     * 时间类型
     */
    @NotBlank(message = "未找到时间类型")
    private String timeType;

    /**
     * 模型节点编号
     */
    @NotBlank(message = "未找到模型节点编号信息")
    private String modelCode;

    /**
     * 能源类型
     */
    @NotBlank(message = "请维护能源类型")
    private String energyType;
}