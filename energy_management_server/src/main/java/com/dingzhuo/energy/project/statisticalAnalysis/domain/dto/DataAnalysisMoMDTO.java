package com.dingzhuo.energy.project.statisticalAnalysis.domain.dto;


import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 数据分析环比 请求参数
 *
 * @Author: Zhujw
 * @Date: 2023/3/3
 */
@Data
public class DataAnalysisMoMDTO {


    /**
     * 统计开始时间
     */
    @NotNull(message = "请维护查询时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报警开始时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 统计开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报警开始时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "请维护查询时间")
    private Date endTime;

    /**
     * 时间类型
     */
    @NotBlank(message = "未找到时间类型")
    private String timeType;

    /**
     * 模型节点id
     */
    @NotBlank(message = "未找到模型节点信息")
    private String nodeId;
}