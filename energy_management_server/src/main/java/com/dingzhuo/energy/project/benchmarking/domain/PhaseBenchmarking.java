package com.dingzhuo.energy.project.benchmarking.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 对标对象 phase_benchmarking
 * 
 * @author sys
 * @date 2020-12-22
 */
public class PhaseBenchmarking extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String indexId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String timeCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String timeType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String value;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date dataTime;

    public void setIndexId(String indexId) 
    {
        this.indexId = indexId;
    }

    public String getIndexId() 
    {
        return indexId;
    }
    public void setTimeCode(String timeCode) 
    {
        this.timeCode = timeCode;
    }

    public String getTimeCode() 
    {
        return timeCode;
    }
    public void setTimeType(String timeType) 
    {
        this.timeType = timeType;
    }

    public String getTimeType() 
    {
        return timeType;
    }
    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }
    public void setDataTime(Date dataTime) 
    {
        this.dataTime = dataTime;
    }

    public Date getDataTime() 
    {
        return dataTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("indexId", getIndexId())
            .append("timeCode", getTimeCode())
            .append("timeType", getTimeType())
            .append("value", getValue())
            .append("dataTime", getDataTime())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
