package com.dingzhuo.energy.basic.data.facility.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备档案对象 facility_archives
 *
 * @author zhaowei
 * @date 2020-02-24
 */
@ApiModel(value = "设备档案")
public class FacilityArchives extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @ApiModelProperty(value = "主键id")
    private String id;

    /** 编码 */
    @Excel(name = "编码")
    @ApiModelProperty(value = "编码")
    private String code;

    /** 设备名称 */
    @Excel(name = "设备名称")
    @ApiModelProperty(value = "设备名称")
    private String facilityName;

    /** 设备类型 */
    @Excel(name = "设备类型")
    @ApiModelProperty(value = "设备类型")
    private String facilityType;

    /** 使用分厂 */
    @Excel(name = "使用分厂")
    @ApiModelProperty(value = "使用分厂")
    private String branchFactory;

    /** 物理位置 */
    @Excel(name = "物理位置")
    @ApiModelProperty(value = "物理位置")
    private String istallationLocationn;

    /** 能耗等级 */
    @Excel(name = "能耗等级")
    @ApiModelProperty(value = "能耗等级")
    private String facilityGrade;

    /** 额定功率 */
    @Excel(name = "额定功率")
    @ApiModelProperty(value = "额定功率")
    private String ratedPower;

    /** 生产日期 */
    @Excel(name = "生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "生产日期")
    private Date productionDate;

    /** 投运日期 */
    @Excel(name = "投运日期", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "投运日期")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date putrunDate;

    /** 生产厂家 */
    @Excel(name = "生产厂家")
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /** 负责人 */
    @Excel(name = "负责人")
    @ApiModelProperty(value = "负责人")
    private String personCharge;

    /** 技术参数 */
    @Excel(name = "技术参数")
    @ApiModelProperty(value = "技术参数")
    private String technicalData;

    /** 起始时间 */
    @Excel(name = "起始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "起始时间")
    private Date startTime;

    /** 检定周期 */
    @Excel(name = "检定周期")
    @ApiModelProperty(value = "检定周期")
    private Integer checkCycle;

    /** 提醒周期 */
    @Excel(name = "提醒周期")
    @ApiModelProperty(value = "提醒周期")
    private Integer reminderCycle;
    /** 是否重点设备 */
    @Excel(name = "是否重点设备")
    @ApiModelProperty(value = "是否重点设备")
    private String pointFlag;

    private boolean txflage;

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
    public void setFacilityName(String facilityName)
    {
        this.facilityName = facilityName;
    }

    public String getFacilityName()
    {
        return facilityName;
    }
    public void setFacilityType(String facilityType)
    {
        this.facilityType = facilityType;
    }

    public String getFacilityType()
    {
        return facilityType;
    }
    public void setBranchFactory(String branchFactory)
    {
        this.branchFactory = branchFactory;
    }

    public String getBranchFactory()
    {
        return branchFactory;
    }
    public void setIstallationLocationn(String istallationLocationn)
    {
        this.istallationLocationn = istallationLocationn;
    }

    public String getIstallationLocationn()
    {
        return istallationLocationn;
    }
    public void setFacilityGrade(String facilityGrade)
    {
        this.facilityGrade = facilityGrade;
    }

    public String getFacilityGrade()
    {
        return facilityGrade;
    }
    public void setRatedPower(String ratedPower)
    {
        this.ratedPower = ratedPower;
    }

    public String getRatedPower()
    {
        return ratedPower;
    }
    public void setProductionDate(Date productionDate)
    {
        this.productionDate = productionDate;
    }

    public Date getProductionDate()
    {
        return productionDate;
    }
    public void setPutrunDate(Date putrunDate)
    {
        this.putrunDate = putrunDate;
    }

    public Date getPutrunDate()
    {
        return putrunDate;
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
    public void setTechnicalData(String technicalData)
    {
        this.technicalData = technicalData;
    }

    public String getTechnicalData()
    {
        return technicalData;
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

    public String getPointFlag() {
        return pointFlag;
    }

    public void setPointFlag(String pointFlag) {
        this.pointFlag = pointFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("facilityName", getFacilityName())
            .append("facilityType", getFacilityType())
            .append("branchFactory", getBranchFactory())
            .append("istallationLocationn", getIstallationLocationn())
            .append("facilityGrade", getFacilityGrade())
            .append("ratedPower", getRatedPower())
            .append("productionDate", getProductionDate())
            .append("putrunDate", getPutrunDate())
            .append("manufacturer", getManufacturer())
            .append("personCharge", getPersonCharge())
            .append("technicalData", getTechnicalData())
            .append("startTime", getStartTime())
            .append("checkCycle", getCheckCycle())
            .append("reminderCycle", getReminderCycle())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
                .append("txflage", getTxflage())
                .append("pointFlag", getPointFlag())
            .toString();
    }
}
