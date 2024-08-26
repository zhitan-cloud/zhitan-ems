package com.dingzhuo.energy.project.plannedOutput.domain;

import com.dingzhuo.energy.common.utils.time.TimeType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 plan_energy
 * 
 * @author sys
 * @date 2020-12-17
 */
public class PlanEnergy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "产品ID")
    private Integer productId;
    @Excel(name = "产品名称")
    private String productname;
    @Excel(name = "单位")
    private String muid;
    @Excel(name = "计划值")
    private String planValue;
    @Excel(name = "实际值")
    private String actualValue;
    /** 电能耗量 */
    @Excel(name = "电能耗量")
    private String value;
    /** 电单位产品能耗量 */
    @Excel(name = "电单位产品能耗量")
    private String energyValue;

    /** 类型 */
    @Excel(name = "类型")
    private TimeType timeType;

    /** 蒸汽单位产品能耗量 */
    @Excel(name = "蒸汽单位产品能耗量")
    private String steamEnereyValue;

    /** 电单位产品能耗量 */
    @Excel(name = "水单位产品能耗量")
    private String waterValue;

    /** 水单位产品能耗量 */
    @Excel(name = "水单位产品能耗量")
    private String waterEnergyValue;

    /** 煤气能耗量 */
    @Excel(name = "煤气能耗量")
    private String coalValue;

    /** 煤气单位产品能耗量 */
    @Excel(name = "煤气单位产品能耗量")
    private String coalEnergyValue;

    /** 蒸汽能耗量 */
    @Excel(name = "蒸汽能耗量")
    private String steamValue;

    /** 日期 */
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dataTime;
    private String indexCode;
    private String timeCode;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setProductId(Integer productId)
    {
        this.productId = productId;
    }

    public Integer getProductId()
    {
        return productId;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getMuid() {
        return muid;
    }

    public void setMuid(String muid) {
        this.muid = muid;
    }

    public String getPlanValue() {
        return planValue;
    }

    public void setPlanValue(String planValue) {
        this.planValue = planValue;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }
    public void setEnergyValue(String energyValue) 
    {
        this.energyValue = energyValue;
    }

    public String getEnergyValue() 
    {
        return energyValue;
    }

    public String getSteamEnereyValue() {
        return steamEnereyValue;
    }

    public void setSteamEnereyValue(String steamEnereyValue) {
        this.steamEnereyValue = steamEnereyValue;
    }

    public String getWaterValue() {
        return waterValue;
    }

    public void setWaterValue(String waterValue) {
        this.waterValue = waterValue;
    }

    public String getWaterEnergyValue() {
        return waterEnergyValue;
    }

    public void setWaterEnergyValue(String waterEnergyValue) {
        this.waterEnergyValue = waterEnergyValue;
    }

    public String getCoalValue() {
        return coalValue;
    }

    public void setCoalValue(String coalValue) {
        this.coalValue = coalValue;
    }

    public String getCoalEnergyValue() {
        return coalEnergyValue;
    }

    public void setCoalEnergyValue(String coalEnergyValue) {
        this.coalEnergyValue = coalEnergyValue;
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
    public void setTimeType(TimeType timeType)
    {
        this.timeType = timeType;
    }

    public TimeType getTimeType()
    {
        return timeType;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("productId", getProductId())
            .append("productname", getProductname())
            .append("muid", getMuid())
            .append("planValue", getPlanValue())
            .append("value", getValue())
            .append("energyValue", getEnergyValue())
            .append("steamEnereyValue", getSteamEnereyValue())
            .append("waterValue", getWaterValue())
            .append("waterEnergyValue", getWaterEnergyValue())
            .append("coalValue", getCoalValue())
            .append("coalEnergyValue", getCoalEnergyValue())
            .append("steamValue", getSteamValue())
            .append("dataTime", getDataTime())
            .append("timeType", getTimeType())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
