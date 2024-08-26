package com.dingzhuo.energy.data.monitoring.trend.history.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 历史监测数据请求 DTO
 *
 * @Author: Zhujw
 * @Date: 2023/3/7
 */
public class HistoricalDataDTO {

    /**
     * 点位id
     */
    @NotBlank(message = "未找到点位信息")
    private String indexId;

    /**
     * 时间类型
     */
    @NotBlank(message = "未找到时间类型")
    private String timeType;

    /**
     * 查询时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "未找到时间信息")
    private Date dataTime;

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

}