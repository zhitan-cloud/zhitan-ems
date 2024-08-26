package com.dingzhuo.energy.project.benchmarking.domain;

import com.dingzhuo.energy.common.utils.time.TimeType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 标杆管理对象 benchmarking_management
 * 
 * @author sys
 * @date 2020-12-21
 */
public class BenchmarkingManagement implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "指标ID")
    private String indexId;
    @Excel(name = "指标code")
    private String indexCode;
    @Excel(name = "指标名称")
    private String indexName;
    @Excel(name = "指标单位")
    private String unitId;

    /** $column.columnComment */
    @Excel(name = "日期类型")
    private TimeType timeType;

    /** $column.columnComment */
    @Excel(name = "日期")
    private Date dateTime;

    /** $column.columnComment */
    @Excel(name = "标杆值")
    private String value;
    @Excel(name = "实际值")
    private String actualValue;
    /** $column.columnComment */
    @Excel(name = "${comment}")
    private String timeCode;
    @Excel(name = "标杆范围")
    private String  benchmarkingRange;
    @Excel(name = "标杆类型")
    private String  benchmarkingType;
    private Date beginTime;
    private Date endTime;
    private Date createTime;
    private Date updateTime;
    public String getIndexId()
    {
        return indexId;
    }
    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }
    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public void setTimeType(TimeType timeType)
    {
        this.timeType = timeType;
    }

    public TimeType getTimeType()
    {
        return timeType;
    }
    public void setDateTime(Date dateTime) 
    {
        this.dateTime = dateTime;
    }

    public Date getDateTime() 
    {
        return dateTime;
    }
    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public void setTimeCode(String timeCode)
    {
        this.timeCode = timeCode;
    }

    public String getTimeCode() 
    {
        return timeCode;
    }

    public String getBenchmarkingRange() {
        return benchmarkingRange;
    }

    public void setBenchmarkingRange(String benchmarkingRange) {
        this.benchmarkingRange = benchmarkingRange;
    }

    public String getBenchmarkingType() {
        return benchmarkingType;
    }

    public void setBenchmarkingType(String benchmarkingType) {
        this.benchmarkingType = benchmarkingType;
    }



    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("indexId", getIndexId())
            .append("indexName", getIndexName())
            .append("unitId", getUnitId())
            .append("timeType", getTimeType())
            .append("dateTime", getDateTime())
            .append("value", getValue())
            .append("actualValue", getActualValue())
            .append("timeCode", getTimeCode())
            .append("benchmarkingRange", getBenchmarkingRange())
            .append("benchmarkingType", getBenchmarkingType())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
