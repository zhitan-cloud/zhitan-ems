package com.dingzhuo.energy.basic.data.meter.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 计量器具统计查询对象 meter_implement
 * 
 * @author zhaowei
 * @date 2020-02-21
 */
public class MeterImplementCount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 器具名称 */
    @Excel(name = "器具名称")
    private String meterName;

    /** 种类 */
    @Excel(name = "种类")
    private String meterType;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String modelNumber;

    /** 测量范围 */
    @Excel(name = "测量范围")
    private String measureRange;

    /** 主键 */
    private String id;

    /** 生产厂商 */
    @Excel(name = "生产厂商")
    private String manufacturer;

    /** 负责人 */
    private String personCharge;

    /** 安装位置 */
    @Excel(name = "安装位置")
    private String installactionLocation;

    /** 起始时间 */
    private Date startTime;

    /** 投运时间 */
    @Excel(name = "投运时间")
    private Date putrunTime;


    /** 检定周期 */
    private Integer checkCycle;

    /** 提醒周期 */
    private Integer reminderCycle;

    /** 状态 */
    @Excel(name = "状态")
    private String meterStatus;

    /** 逻辑删除标志,Y已删除,N未删除 */
    private String delFlage;

    private boolean txflage;

    public void setTxflage(boolean txflage)
    {
        this.txflage = txflage;
    }
    public boolean getTxflage()
    {
        return this.txflage;
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
    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
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
            .append("code", getCode())
            .append("meterName", getMeterName())
            .append("meterType", getMeterType())
            .append("modelNumber", getModelNumber())
            .append("measureRange", getMeasureRange())
            .append("id", getId())
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
            .toString();
    }
    public Date getPutrunTime() {
        return putrunTime;
    }

    public void setPutrunTime(Date putrunTime) {
        this.putrunTime = putrunTime;
    }
}
