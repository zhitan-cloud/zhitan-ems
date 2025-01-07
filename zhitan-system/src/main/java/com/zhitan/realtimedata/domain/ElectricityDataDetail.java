package com.zhitan.realtimedata.domain;

import com.zhitan.common.enums.TimeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 周期数据项.
 */
@ApiModel(value = "dataItem对象")
public class ElectricityDataDetail implements Serializable {
    private static final long serialVersionUID = -2777479013884125925L;

    @ApiModelProperty(value = "指标id")
    private String indexId;
    @ApiModelProperty(value = "时间类型编码")
    private String timeCode;
    @ApiModelProperty(value = "尖峰平谷的类型")
    private String electricityType;
    @ApiModelProperty(value = "指标code")
    private String indexCode;
    @ApiModelProperty(value = "日期")
    private Date dataTime;
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "尖峰平谷的类型")
    private double electricity;
    @ApiModelProperty(value = "值")
    private Double cost;
    @ApiModelProperty(value = "值")
    private Double price;
    @ApiModelProperty(value = "时间类型")
    private TimeType timeType;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }

    public String getElectricityType() {
        return electricityType;
    }

    public void setElectricityType(String electricityType) {
        this.electricityType = electricityType;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public void setIndexCode(String indexCode) {
        this.indexCode = indexCode;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
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

    public double getElectricity() {
        return electricity;
    }

    public void setElectricity(double electricity) {
        this.electricity = electricity;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
