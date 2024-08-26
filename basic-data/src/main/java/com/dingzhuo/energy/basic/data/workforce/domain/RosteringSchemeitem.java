package com.dingzhuo.energy.basic.data.workforce.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 轮值方案对象 rostering_schemeItem
 *
 * @author sys
 * @date 2020-05-13
 */

/**
 * 【请填写功能名称】对象 rostering_schemeItem
 *
 * @author sys
 * @date 2020-05-13
 */
public class RosteringSchemeitem extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    private String id;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 轮值方案Id */
    @Excel(name = "轮值方案Id")
    private String schemeId;
    private String schemeName;
    /** 天次 */
    @Excel(name = "天次")
    private String dayofloopdays;

    /** 班次Id */
    @Excel(name = "班次Id")
    private String shiftId;
    private String shiftName;
    /** 值次Id */
    @Excel(name = "值次Id")
    private String dutyId;
    private String dutyName;
    /** 描述 */
    @Excel(name = "描述")
    private String description;

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
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setSchemeId(String schemeId)
    {
        this.schemeId = schemeId;
    }

    public String getSchemeId()
    {
        return schemeId;
    }
    public void setDayofloopdays(String dayofloopdays)
    {
        this.dayofloopdays = dayofloopdays;
    }

    public String getDayofloopdays()
    {
        return dayofloopdays;
    }
    public void setShiftId(String shiftId)
    {
        this.shiftId = shiftId;
    }

    public String getShiftId()
    {
        return shiftId;
    }
    public void setDutyId(String dutyId)
    {
        this.dutyId = dutyId;
    }

    public String getDutyId()
    {
        return dutyId;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("code", getCode())
                .append("name", getName())
                .append("schemeId", getSchemeId())
                .append("dayofloopdays", getDayofloopdays())
                .append("shiftId", getShiftId())
                .append("dutyId", getDutyId())
                .append("description", getDescription())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}