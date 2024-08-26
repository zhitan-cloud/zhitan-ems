package com.dingzhuo.energy.project.electricity.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;
/**
 * electricityPrice对象 electricity_price
 *
 * @author sys
 * @date 2020-02-18
 */
public class ElectricityPrice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 生效日期 */
    @Excel(name = "生效日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date effectiveDate;

    /** 单价 */
    @Excel(name = "单价")
    private String priceId;

    /** 时段开始时间 */
    @Excel(name = "时段开始时间")
    private String beginDate;

    /** 时段结束时间 */
    @Excel(name = "时段结束时间")
    private String endDate;

    /** 时段名称 */
    @Excel(name = "时段名称")
    private String effectiveName;
    /** 单价 */
    @Excel(name = "单价")
    private String price;
    private String electricityName;

    @Excel(name = "单价")
    private Double prices;
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
    public void setPriceId(String priceId)
    {
        this.priceId = priceId;
    }

    public String getPriceId()
    {
        return priceId;
    }
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }

    public String getBeginDate()
    {
        return beginDate;
    }
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public String getEndDate()
    {
        return endDate;
    }
    public void setEffectiveName(String effectiveName)
    {
        this.effectiveName = effectiveName;
    }

    public String getEffectiveName()
    {
        return effectiveName;
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
                .append("priceId", getPriceId())
                .append("beginDate", getBeginDate())
                .append("endDate", getEndDate())
                .append("effectiveName", getEffectiveName())
                .append("price", getPrice())
                .toString();
    }
}