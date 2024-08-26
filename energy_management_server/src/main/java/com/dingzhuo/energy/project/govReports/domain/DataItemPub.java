package com.dingzhuo.energy.project.govReports.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * GovReports对象 data_item_pub
 *
 * @author zy
 * @date 2022-04-06
 */
@Data
public class DataItemPub {

    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private String indexId;

    /**
     * $column.columnComment
     */
    private String timeCode;

    /**
     * $column.columnComment
     */
    private Date beginTime;

    /**
     * $column.columnComment
     */
    private Date endTime;

    /**
     * 上报值
     */
    @Excel(name = "上报值")
    private Double value;

    /**
     * $column.columnComment
     */
    @Excel(name = "上报值")
    private String quality;

    /**
     * $column.columnComment
     */
    @Excel(name = "上报值")
    private String timeType;

    /**
     * $column.columnComment
     */
    @Excel(name = "上报值", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dataTime;

    /**
     * 发送状态 0 未发送 1 已发送 2发送失败
     */
    private Integer sendStatus = 0;

    /**
     * 发送时间
     */
    private Date sendDate;

    /**
     * 发送次数
     */
    private Integer sendTimes;

    /**
     * 设置的id
     */
    private String reportSetId;

    /**
     * 上限限值
     */
    private Double reportUpperLimit;

    /**
     * 上限替换值
     */
    private Double reportUpperReplace;

    /**
     * 下限限值
     */
    private Double reportLowerLimit;

    /**
     * 下限替换值
     */
    private Double reportLowerReplace;

    /**
     * 原始值
     */
    private Double originValue;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("indexId", getIndexId())
                .append("timeCode", getTimeCode())
                .append("beginTime", getBeginTime())
                .append("endTime", getEndTime())
                .append("value", getValue())
                .append("quality", getQuality())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("timeType", getTimeType())
                .append("dataTime", getDataTime())
                .append("createBy", getCreateBy())
                .append("sendStatus", getSendStatus())
                .append("sendDate", getSendDate())
                .append("sendTimes", getSendTimes())
                .append("reportSetId", getReportSetId())
                .append("reportUpperLimit", getReportUpperLimit())
                .append("reportUpperReplace", getReportUpperReplace())
                .append("reportLowerLimit", getReportLowerLimit())
                .append("reportLowerReplace", getReportLowerReplace())
                .append("originValue", getOriginValue())
                .toString();
    }
}
