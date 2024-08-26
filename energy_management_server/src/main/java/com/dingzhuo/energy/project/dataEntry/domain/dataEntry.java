package com.dingzhuo.energy.project.dataEntry.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * stagseDataEntry对象 data_item
 * 
 * @author sys
 * @date 2020-03-25
 */
public class dataEntry extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "指标id")
    private String indexId;

    @Excel(name = "指标code")
    private String timeCode;

    @Excel(name = "值")
    private String value;

    @Excel(name = "开始时间")
    private Date beginTime;

    @Excel(name = "结束时间")
    private Date endTime;

    private String quality;

    @Excel(name = "时间类型")
    private String timeType;

    @Excel(name = "数据时间")
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
    public void setBeginTime(Date beginTime) 
    {
        this.beginTime = beginTime;
    }

    public String getValue() {return value;}

    public void setValue(String value) {this.value = value;}

    public void setQuality(String quality)
    {
        this.quality = quality;
    }

    public String getQuality() 
    {
        return quality;
    }
    public void setTimeType(String timeType) 
    {
        this.timeType = timeType;
    }

    public String getTimeType() 
    {
        return timeType;
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
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("indexId", getIndexId())
            .append("timeCode", getTimeCode())
            .append("beginTime", getBeginTime())
            .append("endTime", getEndTime())
            .append("quality", getQuality())
            .append("timeType", getTimeType())
            .append("value", getValue())
            .append("dataTime", getDataTime())
            .toString();
    }
}
