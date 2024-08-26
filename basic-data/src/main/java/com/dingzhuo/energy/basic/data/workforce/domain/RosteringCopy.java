package com.dingzhuo.energy.basic.data.workforce.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RosteringCopy extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 编码 */
    @Excel(name = "编码")
    private String code;
    /** 名称 */
    @Excel(name = "名称")
    private String name;
    /** 轮值方案id */
    @Excel(name = "轮值方案id")
    private String schemeId;
    /** 班次名称 */
    @Excel(name = "班次名称")
    private String shiftName;
    /** 开始时间 */
    @Excel(name = "开始时间", width = 30)
    private String startTime;
    /** 结束时间 */
    @Excel(name = "结束时间", width = 30)
    private String enddTime;
    /** 是否跨天 */
    @Excel(name = "是否跨天")
    private String isCrossDay;
    /** 值次名称 */
    @Excel(name = "值次名称")
    private String dutyName;
    /** 班次id */
    @Excel(name = "班次id")
    private String shiftId;
    /** 值次id */
    @Excel(name = "值次id")
    private String dutyId;

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
    public String getSchemeId() { return schemeId; }

    public void setShiftName(String shiftName) { this.shiftName = shiftName; }
    public String getShiftName() { return shiftName; }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }
    public String getStartTime() { return startTime; }

    public void setEnddTime(String enddTime)
    {
        this.enddTime = enddTime;
    }
    public String getEnddTime() { return enddTime; }

    public void setIsCrossDay(String isCrossDay)
    {
        this.isCrossDay = isCrossDay;
    }
    public String getIsCrossDay()
    {
        return isCrossDay;
    }

    public void setDutyName(String dutyName) { this.dutyName = dutyName; }
    public String getDutyName() { return dutyName; }

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
    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("code",getCode())
                .append("name",getName())
                .append("schemeId",getSchemeId())
                .append("shiftName",getShiftName())
                .append("startTime",getStartTime())
                .append("enddTime",getEnddTime())
                .append("isCrossDay",getIsCrossDay())
                .append("dutyName",getDutyName())
                .append("shiftId", getShiftId())
                .append("dutyId", getDutyId())
                .toString();
    }

}
