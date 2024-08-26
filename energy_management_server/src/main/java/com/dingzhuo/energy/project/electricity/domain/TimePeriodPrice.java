package com.dingzhuo.energy.project.electricity.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * electricity对象 time_period_price
 * 
 * @author sys
 * @date 2020-02-19
 */
public class TimePeriodPrice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 生效日期 */
    private Date effectiveDate;

    /** 时段 */
    @Excel(name = "时段")
    private String timePeriod;

    /** 单价 */
    @Excel(name = "单价")
    private String price;
    private Double prices;
    private String electricityName;
    private String dictType;//字典类型
    private String dictValue;//字典值
    private String dictLabel;//字典名称

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setEffectiveDate(Date effectiveDate) 
    {
        this.effectiveDate = effectiveDate;
    }

    public Date getEffectiveDate() 
    {
        return effectiveDate;
    }
    public void setTimePeriod(String timePeriod) 
    {
        this.timePeriod = timePeriod;
    }

    public String getTimePeriod() 
    {
        return timePeriod;
    }
    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getPrice()
    {
        return price;
    }

    public Double getPrices() {
        return prices;
    }

    public void setPrices(Double prices) {
        this.prices = prices;
    }

    public String getDictType() {
        return dictType;
    }
    public void setDictType(String dictType) {
        this.dictType = dictType;
    }
    public String getDictValue() {
        return dictValue;
    }
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }
    public String getDictLabel() {
        return dictLabel;
    }
    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getElectricityName() {
        return electricityName;
    }

    public void setElectricityName(String electricityName) {
        this.electricityName = electricityName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("effectiveDate", getEffectiveDate())
            .append("timePeriod", getTimePeriod())
            .append("price", getPrice())
            .append("dictType", getDictType())
            .append("dictValue", getDictValue())
            .append("dictLabel", getDictLabel())
            .append("electricityName", getElectricityName())
            .toString();
    }
}
