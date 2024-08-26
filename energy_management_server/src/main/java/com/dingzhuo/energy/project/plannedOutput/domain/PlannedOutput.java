package com.dingzhuo.energy.project.plannedOutput.domain;

import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * plannedOutput对象 planned_output
 * 
 * @author sys
 * @date 2020-12-16
 */
public class PlannedOutput extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 计划分类 */
    @Excel(name = "计划分类")
    private String palnType;

    /** 产品id */
    @Excel(name = "产品id")
    private Integer productId;

    /** 计划值 */
    @Excel(name = "计划值")
    private String planValue;
    /** 计划值 */
    @Excel(name = "计划电用量")
    private String value;
    /** 计划用水量 */
    @Excel(name = "计划水用量")
    private String waterValue;
    /** 计划值 */
    @Excel(name = "计划煤气用量")
    private String coalValue;
    /** 计划值 */
    @Excel(name = "计划蒸汽用量")
    private String steamValue;
    /** 日期 */
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dataTime;
    private String indexCode;
    private String productname;
    private String productsno;
    private String muid;
    private String timeCode;
    private TimeType timeType;
    public void setPalnType(String palnType)
    {
        this.palnType = palnType;
    }

    public String getPalnType()
    {
        return palnType;
    }
    public void setProductId(Integer productId)
    {
        this.productId = productId;
    }

    public Integer getProductId()
    {
        return productId;
    }
    public void setPlanValue(String planValue)
    {
        this.planValue = planValue;
    }

    public String getPlanValue()
    {
        return planValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getWaterValue() {
        return waterValue;
    }

    public void setWaterValue(String waterValue) {
        this.waterValue = waterValue;
    }

    public String getCoalValue() {
        return coalValue;
    }

    public void setCoalValue(String coalValue) {
        this.coalValue = coalValue;
    }

    public String getSteamValue() {
        return steamValue;
    }

    public void setSteamValue(String steamValue) {
        this.steamValue = steamValue;
    }

    public void setDataTime(Date dataTime)
    {
        this.dataTime = dataTime;
    }

    public Date getDataTime()
    {
        return dataTime;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductsno() {
        return productsno;
    }

    public void setProductsno(String productsno) {
        this.productsno = productsno;
    }

    public String getMuid() {
        return muid;
    }

    public void setMuid(String muid) {
        this.muid = muid;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("palnType", getPalnType())
                .append("productId", getProductId())
                .append("planValue", getPlanValue())
                .append("dataTime", getDataTime())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}