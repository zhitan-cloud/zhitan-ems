package com.zhitan.basicdata.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 计量器具档案维护对象 meter_implement
 *
 * @author zhaowei
 * @date 2020-02-12
 */
@ApiModel(value = "计量器具")
public class MeterImplement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    private String id;

    /** 编码 */
    @Excel(name = "编码")
    @ApiModelProperty(value = "编码")
    private String code;

    /** 器具名称 */
    @Excel(name = "器具名称")
    @ApiModelProperty(value = "器具名称")
    private String meterName;

    /** 种类 */
    @Excel(name = "种类")
    @ApiModelProperty(value = "种类")
    private String meterType;

    /** 规格型号 */
    @Excel(name = "规格型号")
    @ApiModelProperty(value = "规格型号")
    private String modelNumber;

    /** 测量范围 */
    @Excel(name = "测量范围")
    @ApiModelProperty(value = "测量范围")
    private String measureRange;

    /** 生产厂商 */
    @Excel(name = "生产厂商")
    @ApiModelProperty(value = "生产厂商")
    private String manufacturer;

    /** 负责人 */
    @Excel(name = "负责人")
    @ApiModelProperty(value = "")
    private String personCharge;

    /** 安装位置 */
    @Excel(name = "安装位置")
    @ApiModelProperty(value = "安装位置")
    private String installactionLocation;

    /** 起始时间 */
    @Excel(name = "起始时间")
    @ApiModelProperty(value = "起始时间")
    private Date startTime;

    /** 投运时间 */
    @Excel(name = "投运时间")
    @ApiModelProperty(value = "投运时间")
    private Date putrunTime;

    /** 检定周期 */
    @Excel(name = "检定周期")
    @ApiModelProperty(value = "检定周期")
    private Integer checkCycle;

    /** 提醒周期 */
    @Excel(name = "提醒周期")
    @ApiModelProperty(value = "提醒周期")
    private Integer reminderCycle;

    /** 状态 */
    @Excel(name = "状态")
    @ApiModelProperty(value = "状态")
    private String meterStatus;

    /** 逻辑删除标志,Y已删除,N未删除 */
    @ApiModelProperty(value = "逻辑删除标志,Y已删除,N未删除")
    private String delFlage;
    /** 检定提醒标志  true 提醒 false不需要提醒 */
    @ApiModelProperty(value = "检定提醒标志  true 提醒 false不需要提醒")
    @TableField(exist = false)
    private boolean txflage;


    /** 状态 */
    @Excel(name = "检测能源类型")
    @ApiModelProperty(value = "检测能源类型")
    private String energyType;

    @Excel(name = "线径")
    private String wireDiameter;

    @ApiModelProperty(name = "网关主键")
    private String gatewayId;

    @Excel(name = "网关名称")
    private String gatewayName;

    @Excel(name = "允许最大功率")
    private String maxAllowablePower;

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public void setTxflage(boolean txflage)
    {
        this.txflage = txflage;
    }
    public boolean getTxflage()
    {
        return this.txflage;
    }
    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setMeterName(String meterName)
    {
        this.meterName = meterName;
    }

    public String getMeterName()
    {
        return meterName;
    }
    public void setMeterType(String meterType)
    {
        this.meterType = meterType;
    }

    public String getMeterType()
    {
        return meterType;
    }
    public void setModelNumber(String modelNumber)
    {
        this.modelNumber = modelNumber;
    }

    public String getModelNumber()
    {
        return modelNumber;
    }
    public void setMeasureRange(String measureRange)
    {
        this.measureRange = measureRange;
    }

    public String getMeasureRange()
    {
        return measureRange;
    }
    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }
    public void setPersonCharge(String personCharge)
    {
        this.personCharge = personCharge;
    }

    public String getPersonCharge()
    {
        return personCharge;
    }
    public void setInstallactionLocation(String installactionLocation)
    {
        this.installactionLocation = installactionLocation;
    }

    public String getInstallactionLocation()
    {
        return installactionLocation;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setCheckCycle(Integer checkCycle)
    {
        this.checkCycle = checkCycle;
    }

    public Integer getCheckCycle()
    {
        return checkCycle;
    }
    public void setReminderCycle(Integer reminderCycle)
    {
        this.reminderCycle = reminderCycle;
    }

    public Integer getReminderCycle()
    {
        return reminderCycle;
    }
    public void setMeterStatus(String meterStatus)
    {
        this.meterStatus = meterStatus;
    }

    public String getMeterStatus()
    {
        return meterStatus;
    }
    public void setDelFlage(String delFlage)
    {
        this.delFlage = delFlage;
    }

    public String getDelFlage()
    {
        return delFlage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("meterName", getMeterName())
            .append("meterType", getMeterType())
            .append("modelNumber", getModelNumber())
            .append("measureRange", getMeasureRange())
            .append("manufacturer", getManufacturer())
            .append("personCharge", getPersonCharge())
            .append("installactionLocation", getInstallactionLocation())
            .append("startTime", getStartTime())
            .append("checkCycle", getCheckCycle())
            .append("reminderCycle", getReminderCycle())
            .append("meterStatus", getMeterStatus())
            .append("delFlage", getDelFlage())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("txflage", getTxflage())
            .append("energyType", getEnergyType())
            .toString();
    }

    public Date getPutrunTime() {
        return putrunTime;
    }

    public void setPutrunTime(Date putrunTime) {
        this.putrunTime = putrunTime;
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType;
    }

    public String getWireDiameter() {
        return wireDiameter;
    }

    public void setWireDiameter(String wireDiameter) {
        this.wireDiameter = wireDiameter;
    }

    public String getMaxAllowablePower() {
        return maxAllowablePower;
    }

    public void setMaxAllowablePower(String maxAllowablePower) {
        this.maxAllowablePower = maxAllowablePower;
    }
}
